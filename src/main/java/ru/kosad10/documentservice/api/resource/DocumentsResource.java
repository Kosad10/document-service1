package ru.kosad10.documentservice.api.resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import ru.kosad10.documentservice.api.model.CreateDocumentRequest;
import ru.kosad10.documentservice.api.model.DocumentWithHistory;
import ru.kosad10.documentservice.api.model.DocumentWithStatusAndDate;
import ru.kosad10.documentservice.api.model.DocumentsFilter;

import java.util.Collection;

public interface DocumentsResource {

    @GetMapping("/api/v1/document/{documentId}")
    DocumentWithHistory getDocumentsWithHistory(@PathVariable Long documentId);

    @GetMapping("/api/v1/documents/package")
    Page<DocumentWithHistory> getDocumentsPackageById(@RequestParam Collection<Long> documentsId,
                                                    Pageable pageable);

    @GetMapping("api/v1/documents")
    Page<DocumentWithStatusAndDate> findDocuments(@RequestBody DocumentsFilter userFilter,
                                    @PageableDefault(size = 5, page = 2, sort = "id") Pageable pageable);

    @PostMapping("api/v1/document/")
    DocumentWithHistory createDocument(@RequestBody CreateDocumentRequest document);

    @PutMapping("api/v1/documents/{documentsId}")
    Page<DocumentWithStatusAndDate> submitDocuments(@PathVariable Collection<Long> documentsId,
                                                    Pageable pageable);

    @PutMapping("api/v1/documents/approval/{documentsId}")
    Page<DocumentWithStatusAndDate> approvalDocuments(@PathVariable Collection<Long> documentsId,
                                                      Pageable pageable);
}

