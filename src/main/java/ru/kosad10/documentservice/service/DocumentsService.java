package ru.kosad10.documentservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kosad10.documentservice.api.model.CreateDocumentRequest;
import ru.kosad10.documentservice.api.model.DocumentWithHistory;
import ru.kosad10.documentservice.entity.Document;
import ru.kosad10.documentservice.enums.Status;
import ru.kosad10.documentservice.mapper.DocumentMapper;
import ru.kosad10.documentservice.repository.DocumentsRepository;
import ru.kosad10.documentservice.repository.HistoryRepository;

import java.util.UUID;


@Service
@RequiredArgsConstructor
public class DocumentsService {

    private final DocumentsRepository documentsRepository;
    private final HistoryRepository historyRepository;
    private final DocumentMapper documentMapper;

    public DocumentWithHistory createDocument (CreateDocumentRequest createDocumentRequest){
        Document document = new Document();
        document.setUuid(UUID.randomUUID());
        document.setAuthor(createDocumentRequest.author());
        document.setTitle(createDocumentRequest.title());
        document.setStatus(Status.DRAFT);
        return documentMapper.toDto(documentsRepository.save(document));
    }

    public DocumentWithHistory getDocumentWithHistory(Long documentId) {
        return null;
    }
}
