package ru.kosad10.documentservice.mapper;

import org.mapstruct.Mapper;
import ru.kosad10.documentservice.api.model.DocumentWithHistory;
import ru.kosad10.documentservice.api.model.DocumentWithResultStatus;
import ru.kosad10.documentservice.entity.Document;
import ru.kosad10.documentservice.enums.ResultStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring")
public interface DocumentMapper {

    DocumentWithHistory toDto(Document document);

    Document toEntity(DocumentWithHistory documentWithHistory);

    default List<DocumentWithResultStatus> toDocumentsWithResultStatus(List<Document> success,
                                                                       List<Document> conflict,
                                                                       List<Document> error,
                                                                       List<Long> notFound) {
        List<DocumentWithResultStatus> resultList = new ArrayList<>();

        resultList.addAll(mapDocumentsToList(success, ResultStatus.SUCCESSFULLY));
        resultList.addAll(mapDocumentsToList(conflict, ResultStatus.CONFLICT));
        resultList.addAll(mapDocumentsToList(error, ResultStatus.REGISTRATIONERROR));
        resultList.addAll(mapIdsToList(notFound));
        return resultList;
    }

    private List<DocumentWithResultStatus> mapDocumentsToList(List<Document> documents, ResultStatus resultStatus) {
        List<DocumentWithResultStatus> resultList = new ArrayList<>();
        if (!(documents == null)|| !(documents.isEmpty())) {
            for (Document document : documents) {
                DocumentWithResultStatus idWithResult = new DocumentWithResultStatus(document.getId(), resultStatus);
                resultList.add(idWithResult);
            }
        } else {
            return Collections.emptyList();
        }
        return  resultList;
    }

    private List<DocumentWithResultStatus> mapIdsToList(List<Long> ids) {
        List<DocumentWithResultStatus> resultList = new ArrayList<>();
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }
        for (Long id : ids) {
            DocumentWithResultStatus idWithResult = new DocumentWithResultStatus(id, ResultStatus.NOTFOUND);
            resultList.add(idWithResult);
        }
        return resultList;
    }
}
