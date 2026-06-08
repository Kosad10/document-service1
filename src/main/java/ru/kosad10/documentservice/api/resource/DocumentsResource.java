package ru.kosad10.documentservice.api.resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.web.bind.annotation.*;
import ru.kosad10.documentservice.api.model.*;

import java.util.Collection;
import java.util.List;

public interface DocumentsResource {

    @GetMapping("/api/v1/document/{documentId}")
    DocumentWithHistory getDocumentsWithHistory(@PathVariable Long documentId);

    @GetMapping("/api/v1/documents/package")
    Page<DocumentWithoutHistory> getDocumentsPackageById(@RequestParam Collection<Long> documentsId,
                                                    Pageable pageable);

    @GetMapping("api/v1/documents")
    PagedModel<DocumentWithoutHistory> findDocuments(@RequestBody DocumentsFilter documentsFilter,
                                                     @PageableDefault(size = 5, page = 2, sort = "id") Pageable pageable);

    @PostMapping("api/v1/document/")
    DocumentWithHistory createDocument(@RequestBody CreateDocumentRequest document);

    // добавить возможность оставить комментарии при отправлении на согласование документов
    @PutMapping("api/v1/documents/{documentsId}")
    List<DocumentWithResultStatus> submitDocuments(@PathVariable Collection<Long> documentsId);

    // добавить возможность оставлять комментарии при утверждении документа
    @PutMapping("api/v1/documents/approval/{documentsId}")
    List<DocumentWithResultStatus> approvalDocuments(@PathVariable Collection<Long> documentsId);
}

