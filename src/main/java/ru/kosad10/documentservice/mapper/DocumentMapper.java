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
import java.util.Map;

@Mapper(componentModel = "spring")
public interface DocumentMapper {

    DocumentWithHistory toDtoDocument(Document document);

    HistoryWithoutDocument toDtoHistory(History history);

    DocumentWithoutHistory toDtoWithoutHistory(Document document);

    @Mapping(target = "uuid", expression = "java(java.util.UUID.randomUUID())")
    @Mapping(target = "status", constant = "DRAFT")
    Document entityForSave(CreateDocumentRequest createDocumentRequest);


    Document toEntity(DocumentWithHistory documentWithHistory);

    default List<DocumentWithResultStatus> toDocumentsWithResultStatus(
            Map<ResultStatus, List<Long>> idsWithResultStatus) {

        List<DocumentWithResultStatus> resultList = new ArrayList<>();

        for (Map.Entry<ResultStatus, List<Long>> entry : idsWithResultStatus.entrySet()) {

            ResultStatus resultStatus = entry.getKey();
            List<Long> ids = entry.getValue();

            for (Long id : ids) {
                resultList.add(new DocumentWithResultStatus(id, resultStatus));
            }

        }
        return resultList;
    }
}
