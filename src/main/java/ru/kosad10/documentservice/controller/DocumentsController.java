package ru.kosad10.documentservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RestController;
import ru.kosad10.documentservice.api.model.CreateDocumentRequest;
import ru.kosad10.documentservice.api.model.DocumentWithHistory;
import ru.kosad10.documentservice.api.model.DocumentWithResultStatus;
import ru.kosad10.documentservice.api.model.DocumentsFilter;
import ru.kosad10.documentservice.api.resource.DocumentsResource;
import ru.kosad10.documentservice.mapper.DocumentMapper;
import ru.kosad10.documentservice.service.DocumentService;

import java.util.Collection;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class DocumentsController implements DocumentsResource {

    private final DocumentService documentsService;
    private final DocumentMapper documentMapper;

    @Override
    public DocumentWithHistory getDocumentsWithHistory(Long documentId) {
        return documentsService.getDocumentWithHistory(documentId);
    }

    @Override
    public Page<DocumentWithHistory> getDocumentsPackageById(Collection<Long> documentsId, Pageable pageable) {
        return documentsService.findDocuments(documentsId, pageable).map(documentMapper::toDto);
    }

    @Override
    public Page<DocumentWithResultStatus> findDocuments(DocumentsFilter userFilter, Pageable pageable) {
        return null;
    }

    @Override
    public DocumentWithHistory createDocument(CreateDocumentRequest document) {
        return documentsService.createDocument(document);
    }

    @Override
    public List<DocumentWithResultStatus> submitDocuments(Collection<Long> documentsId) {
        return documentsService.submitDocuments(documentsId);
    }

    @Override
    public List<DocumentWithResultStatus> approvalDocuments(Collection<Long> documentsId) {
        return documentsService.approveDocuments(documentsId);
    }
}
