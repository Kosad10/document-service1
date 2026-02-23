package ru.kosad10.documentservice.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RestController;
import ru.kosad10.documentservice.api.model.DocumentDTO;
import ru.kosad10.documentservice.api.model.DocumentWithHistoryDTO;
import ru.kosad10.documentservice.api.model.DocumentWithStatusAndDate;
import ru.kosad10.documentservice.api.model.DocumentsFilter;
import ru.kosad10.documentservice.api.resource.DocumentsResource;
import ru.kosad10.documentservice.api.service.DocumentsService;

import java.util.Collection;

@RestController
@AllArgsConstructor
public class DocumentsController implements DocumentsResource {

    private final DocumentsService documentsService;

    @Override
    public DocumentWithHistoryDTO getDocumentsWithHistory(Long documentId) {
        return null;
    }

    @Override
    public Page<DocumentWithStatusAndDate> getDocumentsPackageById(Collection<Long> documentsId, Pageable pageable) {
        return null;
    }

    @Override
    public Page<DocumentWithStatusAndDate> findDocuments(DocumentsFilter userFilter, Pageable pageable) {
        return null;
    }

    @Override
    public DocumentDTO createDocument(DocumentDTO document) {
        return null;
    }

    @Override
    public Page<DocumentWithStatusAndDate> submitDocuments(Collection<Long> documentsId, Pageable pageable) {
        return null;
    }

    @Override
    public Page<DocumentWithStatusAndDate> approvalDocuments(Collection<Long> documentsId, Pageable pageable) {
        return null;
    }
}
