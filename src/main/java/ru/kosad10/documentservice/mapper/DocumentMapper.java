package ru.kosad10.documentservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.kosad10.documentservice.api.model.*;
import ru.kosad10.documentservice.entity.Document;
import ru.kosad10.documentservice.entity.History;
import ru.kosad10.documentservice.enums.ResultStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring")
public interface DocumentMapper {

    DocumentWithHistory toDtoDocument(Document document);

    HistoryWithoutDocument toDtoHistory(History history);

    DocumentWithoutHistory toDtoWithoutHistory(Document document);

    @Mapping(target = "uuid", expression = "java(java.util.UUID.randomUUID())")
    @Mapping(target = "status", constant = "DRAFT")
    Document entityForSave(CreateDocumentRequest createDocumentRequest);


    Document toEntity(DocumentWithHistory documentWithHistory);

    default List<DocumentWithResultStatus> toDocumentsWithResultStatus(List<Long> success,
                                                                       List<Long> conflict,
                                                                       List<Long> error,
                                                                       List<Long> notFound) {
        List<DocumentWithResultStatus> resultList = new ArrayList<>();

        resultList.addAll(mapDocumentsToList(success, ResultStatus.SUCCESSFULLY));
        resultList.addAll(mapDocumentsToList(conflict, ResultStatus.CONFLICT));
        resultList.addAll(mapDocumentsToList(error, ResultStatus.REGISTRATION_ERROR));
        resultList.addAll(mapDocumentsToList(notFound, ResultStatus.NOTFOUND));
        return resultList;
    }

    private List<DocumentWithResultStatus> mapDocumentsToList(List<Long> documentsIds, ResultStatus resultStatus) {

        return documentsIds.stream()
                .map(doc -> {
                    return new DocumentWithResultStatus(doc, resultStatus);
                })
                .toList();
    }
}
