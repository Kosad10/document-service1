package ru.kosad10.documentservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kosad10.documentservice.api.model.CreateDocumentRequest;
import ru.kosad10.documentservice.api.model.DocumentWithHistory;
import ru.kosad10.documentservice.api.model.DocumentWithResultStatus;
import ru.kosad10.documentservice.entity.Document;
import ru.kosad10.documentservice.entity.History;
import ru.kosad10.documentservice.enums.Action;
import ru.kosad10.documentservice.enums.Status;
import ru.kosad10.documentservice.mapper.DocumentMapper;
import ru.kosad10.documentservice.repository.DocumentsRepository;
import ru.kosad10.documentservice.repository.HistoryRepository;

import javax.print.Doc;
import java.time.LocalDate;
import java.util.*;


@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentsRepository documentsRepository;
    private final DocumentMapper documentMapper;
    private final HistoryRepository historyRepository;

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
        List<Document> conflict = findConflict(documents, Status.SUBMITTED, Status.APPROVED );
        List<Long> notFoundIds = collectNotFoundIds(documents, documentsId);
        List<Document> success = submitDraft(documents);
        return documentMapper.toDocumentsWithResultStatus(success, Collections.emptyList(), conflict, notFoundIds);
    }

    private List<Document> submitDraft(List<Document> documents) {
        List<Document> successfullySubmit = new ArrayList<>();
        for (Document document : documents) {
            if ((document.getStatus()).equals(Status.DRAFT)) {
                document.setStatus(Status.SUBMITTED);
                successfullySubmit.add(document);
                History history = new History(null, document, document.getAuthor(), LocalDate.now(), Action.SUBMIT, "");
                historyRepository.save(history);
            } else {
                return Collections.emptyList();
            }
        }
        return documentsRepository.saveAll(successfullySubmit);
    }

    @Transactional
    public List<DocumentWithResultStatus> approveDocuments (Collection<Long> ids){
        List<Document> documents = documentsRepository.findAllByIdWithWriteLock(ids);
        List<Document> conflict = findConflict(documents, Status.APPROVED, Status.APPROVED);
        List<Long> notFoundIds = collectNotFoundIds(documents, ids);
        List<Document> error = errorApprove(documents);
        List<Document> approve = approveSubmittedDocuments(documents);
        return documentMapper.toDocumentsWithResultStatus(approve, conflict, error, notFoundIds);
    }

    private List<Document> findConflict(List<Document> documents, Status status, Status secondStatus) {
        List<Document> conflictDraft = new ArrayList<>();
        for (Document document : documents) {
            if ((document.getStatus().equals(status)) || (document.getStatus().equals(secondStatus))) {
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

    private List<Document> errorApprove (List<Document> documents){
        List<Document> errorApprove = new ArrayList<>();
        for(Document document : documents){
            if(document.getStatus().equals(Status.DRAFT)){
                errorApprove.add(document);
            }
        }
        return errorApprove;
    }

    private List<Document> approveSubmittedDocuments(List<Document> documents){
        List<Document> approvedDocuments = new ArrayList<>();
        for(Document document : documents) {
            if(document.getStatus().equals(Status.SUBMITTED)){
                document.setStatus(Status.APPROVED);
                approvedDocuments.add(document);
                History history = new History(null, document,
                        document.getAuthor(),
                        LocalDate.now(),
                        Action.APPROVE, "");
                historyRepository.save(history);
            }
        }
        return documentsRepository.saveAll(approvedDocuments);
        // в случае согласования у нас конфликт, если документ уже согласован, тоесть если статус САБМИТ и АППРУВ
        // в случае подтверждения, у нас конфликт только если документ уже аппрув
    }
}
