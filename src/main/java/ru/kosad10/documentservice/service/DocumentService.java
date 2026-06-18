package ru.kosad10.documentservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kosad10.documentservice.api.model.*;
import ru.kosad10.documentservice.entity.Document;
import ru.kosad10.documentservice.entity.Registry;
import ru.kosad10.documentservice.enums.Action;
import ru.kosad10.documentservice.enums.Status;
import ru.kosad10.documentservice.exceptions.NotFoundException;
import ru.kosad10.documentservice.mapper.DocumentMapper;
import ru.kosad10.documentservice.mapper.HistoryMapper;
import ru.kosad10.documentservice.repository.DocumentsRepository;
import ru.kosad10.documentservice.repository.HistoryRepository;
import ru.kosad10.documentservice.repository.RegistryRepository;
import ru.kosad10.documentservice.repository.specification.DocumentSpecification;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentsRepository documentsRepository;
    private final DocumentMapper documentMapper;
    private final HistoryMapper historyMapper;
    private final HistoryRepository historyRepository;
    private final RegistryRepository registryRepository;

    public DocumentWithHistory createDocument(CreateDocumentRequest createDocumentRequest) {
        Document document = documentMapper.entityForSave(createDocumentRequest);
        return documentMapper.toDtoDocument(documentsRepository.save(document));
    }


    public DocumentWithHistory getDocumentWithHistory(Long documentId) {
        Document document = documentsRepository.findDocumentAndHistoryById(documentId)
                .orElseThrow(() -> new NotFoundException("Документ с id: " + documentId + " не найден."));
        return documentMapper.toDtoDocument(document);
    }


    public Page<DocumentWithoutHistory> getDocumentPackageById(Collection<Long> documentsId, Pageable pageable) {
        if (documentsId == null || documentsId.isEmpty()) {
            return Page.empty();
        }
        Page<Document> page = documentsRepository.findByIdIn(documentsId, pageable);

        return page.map(documentMapper::toDtoWithoutHistory);
    }


    @Transactional(readOnly = true)
    public Page<DocumentWithoutHistory> findDocuments(DocumentsFilter documentsFilter, Pageable pageable) {
        Specification<Document> spec = DocumentSpecification.withFilters(documentsFilter);
        return documentsRepository.findAll(spec, pageable)
                .map(documentMapper::toDtoWithoutHistory);
    }


    @Transactional
    public List<DocumentWithResultStatus> submitDocuments(Collection<Long> documentsId) {
        List<Document> documents = documentsRepository.findAllByIdWithWriteLock(documentsId);
        Set<Status> invalidStatuses = Set.of(Status.SUBMITTED, Status.APPROVED);
        List<Long> conflict = findConflict(documents, invalidStatuses);
        List<Long> notFoundIds = collectNotFoundIds(documents, documentsId);
        List<Long> success = submitDraft(documents);

        return documentMapper.toDocumentsWithResultStatus(success, conflict, Collections.emptyList(), notFoundIds);
    }

    private List<Long> submitDraft(List<Document> documents) {
        List<Document> documentsForSave = documents.stream()
                .filter(i -> Status.DRAFT.equals(i.getStatus()))
                .peek(document -> document.setStatus(Status.SUBMITTED))
                .peek(document -> {
                    historyRepository.save(historyMapper.toDocumentHistory(document, Action.SUBMIT));
                })
                .toList();

        return documentsRepository.saveAll(documentsForSave).stream().map(Document::getId).toList();
    }

    @Transactional
    public List<DocumentWithResultStatus> approveDocuments(Collection<Long> ids) {
        List<Document> documents = documentsRepository.findAllByIdWithWriteLock(ids);
        Set<Status> invalidStatuses = Set.of(Status.APPROVED);
        List<Long> conflict = findConflict(documents, invalidStatuses);
        List<Long> notFoundIds = collectNotFoundIds(documents, ids);
        List<Long> error = errorApprove(documents);
        List<Long> approve = approveSubmittedDocuments(documents);
        return documentMapper.toDocumentsWithResultStatus(approve, conflict, error, notFoundIds);
    }


    private List<Long> findConflict(List<Document> documents, Set<Status> invalidStatuses) {
        return documents.stream()
                .filter(i -> invalidStatuses.contains(i.getStatus()))
                .map(Document::getId)
                .toList();
    }

    private List<Long> collectNotFoundIds(List<Document> documents, Collection<Long> requestedIds) {
        Set<Long> foundIds = documents.stream()
                .map(Document::getId)
                .collect(Collectors.toSet());

        return requestedIds.stream()
                .filter(i -> !foundIds.contains(i))
                .toList();
    }

    private List<Long> errorApprove(List<Document> documents) {
        return documents.stream()
                .filter(i -> i.getStatus().equals(Status.DRAFT))
                .map(Document::getId)
                .toList();
    }

    private List<Long> approveSubmittedDocuments(List<Document> documents) {

        List<Document> approvedDocuments = documents.stream()
                .filter(i -> Status.SUBMITTED.equals(i.getStatus()))
                .peek(i -> i.setStatus(Status.APPROVED))
                .peek(i -> {
                    historyRepository.save(historyMapper.toDocumentHistory(i, Action.APPROVE));
                })
                .toList();

        return documentsRepository.saveAll(approvedDocuments).stream().map(Document::getId).toList();
    }

    private boolean makeRegistryEntry(Document document) {
        if (registryRepository.existsRegistryByDocumentId(document.getId())) {
            return false;
        }

        Registry registry = new Registry();
        registry.setDocumentId((document.getId()));
        registryRepository.save(registry);
        return true;
    }
}
