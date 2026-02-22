package ru.kosad10.documentservice.api.resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kosad10.documentservice.api.model.DocumentWithHistory;
import ru.kosad10.documentservice.api.model.DocumentDTO;

import java.util.Collection;

public interface DocumentsResource {

    @GetMapping("/api/v1/document/{documentId}")
    DocumentWithHistory getDocumentsWithHistory(@PathVariable Long documentId);

    @GetMapping("/api/v1/documents")
    Page <DocumentDTO> getDocumentsPackageById (@RequestParam Collection<Long> documentsId,
                                                    Pageable pageable);


}
