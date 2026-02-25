package ru.kosad10.documentservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kosad10.documentservice.api.model.HistoryDTO;
import ru.kosad10.documentservice.repository.DocumentsRepository;
import ru.kosad10.documentservice.repository.HistoryRepository;


@Service
@RequiredArgsConstructor
public class DocumentsService {

    private final DocumentsRepository documentsRepository;
    private final HistoryRepository historyRepository;

    public HistoryDTO getDocumentWithHistory(Long documentId) {
        return null;
    }
}
