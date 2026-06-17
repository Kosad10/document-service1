package ru.kosad10.documentservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.orm.hibernate5.HibernateJdbcException;
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
        List<Document> conflict = findConflict(documents, invalidStatuses);
        List<Long> notFoundIds = collectNotFoundIds(documents, documentsId);
        List<Document> success = submitDraft(documents);

        return documentMapper.toDocumentsWithResultStatus(success, conflict, Collections.emptyList(), notFoundIds);
    }

    private List<Document> submitDraft(List<Document> documents) {
        List<Document> successfullySubmit = new ArrayList<>();

        for (Document document : documents) {

            if ((document.getStatus()).equals(Status.DRAFT)) {
                document.setStatus(Status.SUBMITTED);
                successfullySubmit.add(document);
                historyRepository.save(historyMapper.toDocumentHistory(document, Action.SUBMIT));
            } else {
                return Collections.emptyList();
            }

        }
        return documentsRepository.saveAll(successfullySubmit);
    }

    @Transactional
    public List<DocumentWithResultStatus> approveDocuments(Collection<Long> ids) {
        List<Document> documents = documentsRepository.findAllByIdWithWriteLock(ids);
        Set<Status> invalidStatuses = Set.of(Status.APPROVED);
        List<Document> conflict = findConflict(documents, invalidStatuses);
        List<Long> notFoundIds = collectNotFoundIds(documents, ids);
        List<Document> error = errorApprove(documents);
        List<Document> approve = approveSubmittedDocuments(documents);
        return documentMapper.toDocumentsWithResultStatus(approve, conflict, error, notFoundIds);
    }


    private List<Document> findConflict(List<Document> documents, Set<Status> invalidStatuses) {
        return documents.stream()
                .filter(i -> invalidStatuses.contains(i.getStatus()))
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

    private List<Document> errorApprove(List<Document> documents) {
        return documents.stream()
                .filter(i -> i.getStatus().equals(Status.DRAFT))
                .toList();
    }

    private List<Document> approveSubmittedDocuments(List<Document> documents) {
        List<Document> approvedDocuments = new ArrayList<>();
        for (Document document : documents) {
            if (document.getStatus().equals(Status.SUBMITTED) && makeRegistryEntry(document)) {
                document.setStatus(Status.APPROVED);
                approvedDocuments.add(document);
                historyRepository.save(historyMapper.toDocumentHistory(document, Action.APPROVE));
            }
        }
        return documentsRepository.saveAll(approvedDocuments);
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
