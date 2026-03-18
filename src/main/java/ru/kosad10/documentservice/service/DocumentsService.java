package ru.kosad10.documentservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kosad10.documentservice.api.model.CreateDocumentRequest;
import ru.kosad10.documentservice.api.model.DocumentWithHistory;
import ru.kosad10.documentservice.api.model.DocumentWithResultStatus;
import ru.kosad10.documentservice.entity.Document;
import ru.kosad10.documentservice.enums.Status;
import ru.kosad10.documentservice.mapper.DocumentMapper;
import ru.kosad10.documentservice.repository.DocumentsRepository;

import java.util.*;


@Service
@RequiredArgsConstructor
public class DocumentsService {

    private final DocumentsRepository documentsRepository;
    private final DocumentMapper documentMapper;

    public DocumentWithHistory createDocument(CreateDocumentRequest createDocumentRequest) {
        Document document = new Document();
        document.setUuid(UUID.randomUUID());
        document.setAuthor(createDocumentRequest.author());
        document.setTitle(createDocumentRequest.title());
        document.setStatus(Status.DRAFT);
        return documentMapper.toDto(documentsRepository.save(document));
    }

    public DocumentWithHistory getDocumentWithHistory(Long documentId) {
        Document document = documentsRepository.findDocumentAndHistoryById(documentId);
        return documentMapper.toDto(document);
    }

    public Page<Document> findDocuments(Collection<Long> documentsId, Pageable pageable) {
        if (documentsId == null || documentsId.isEmpty()) {
            return Page.empty();
        }
        return documentsRepository.findByIdIn(documentsId, pageable);
    }

    @Transactional
    public List<DocumentWithResultStatus> submitDocuments(Collection<Long> documentsId) {
        List<Document> documents = documentsRepository.findAllByIdWithWriteLock(documentsId);
        List<Document> conflict = findConflict(documents);
        List<Long> notFoundIds = collectNotFoundIds(documents, documentsId);
        List<Document> success = submitDraft(documents);
        return documentMapper.toDocumentsWithResultStatus(success, conflict, notFoundIds);
    }

    private List<Document> submitDraft(List<Document> documents) {
        List<Document> successfullySubmit = new ArrayList<>();
        for (Document document : documents) {
            if ((document.getStatus()).equals(Status.DRAFT)) {
                document.setStatus(Status.SUBMITTED);
                successfullySubmit.add(document);
            }
        }
        return documentsRepository.saveAll(successfullySubmit);
    }

    private List<Document> findConflict(List<Document> documents) {
        List<Document> conflictDraft = new ArrayList<>();
        for (Document document : documents) {
            if (!(document.getStatus().equals(Status.DRAFT))) {
                conflictDraft.add(document);
            }
        }
        return conflictDraft;
    }

    private List<Long> collectNotFoundIds (List<Document> documents, Collection<Long> documentsId) {
        Set<Long> inputIds = new HashSet<>(documentsId);
        List<Long> foundIds = new ArrayList<>();
        for (Document document : documents) {
            foundIds.add(document.getId());
        }
        inputIds.removeAll(foundIds);
        return inputIds.stream().toList();
    }
}
