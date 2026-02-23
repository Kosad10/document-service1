package ru.kosad10.documentservice.api.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kosad10.documentservice.api.model.DocumentDTO;
import ru.kosad10.documentservice.api.model.DocumentWithHistory;
import ru.kosad10.documentservice.api.model.DocumentWithStatusAndDate;
import ru.kosad10.documentservice.api.model.DocumentsFilter;
import ru.kosad10.documentservice.api.resource.DocumentsResource;

import java.util.Collection;

@RestController
public class DocumentsController implements DocumentsResource {

    @Override
    public DocumentWithHistory getDocumentsWithHistory(Long documentId) {
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
