package ru.kosad10.documentservice.api.resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ru.kosad10.documentservice.api.model.DocumentWithHistoryDTO;
import ru.kosad10.documentservice.api.model.DocumentDTO;
import ru.kosad10.documentservice.api.model.DocumentWithStatusAndDate;
import ru.kosad10.documentservice.api.model.DocumentsFilter;

import java.util.Collection;

public interface DocumentsResource {

    @GetMapping("/api/v1/document/{documentId}")
    DocumentWithHistoryDTO getDocumentsWithHistory(@PathVariable Long documentId);

    @GetMapping("/api/v1/documents/package")
    Page<DocumentWithStatusAndDate> getDocumentsPackageById(@RequestParam Collection<Long> documentsId,
                                                    Pageable pageable);

    @GetMapping("api/v1/documents")
    Page<DocumentWithStatusAndDate> findDocuments(@RequestBody DocumentsFilter userFilter,
                                    Pageable pageable);

    @PostMapping("api/v1/document/")
    DocumentDTO createDocument(@RequestBody DocumentDTO document);

    @PutMapping("api/v1/documents/{documentsId}")
    Page<DocumentWithStatusAndDate> submitDocuments(@PathVariable Collection<Long> documentsId,
                                                    Pageable pageable);

    @PutMapping("api/v1/documents/approval/{documentsId}")
    Page<DocumentWithStatusAndDate> approvalDocuments(@PathVariable Collection<Long> documentsId,
                                                      Pageable pageable);
}

