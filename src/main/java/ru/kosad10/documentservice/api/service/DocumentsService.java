package ru.kosad10.documentservice.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kosad10.documentservice.api.model.DocumentWithHistoryDTO;

@Service
@RequiredArgsConstructor
public class DocumentsService {

    public DocumentWithHistoryDTO getDocumentWithHistory(Long documentId){
        return null;
    }
}
