package ru.kosad10.documentservice.mapper;

import ru.kosad10.documentservice.api.model.HistoryWithoutDocument;
import ru.kosad10.documentservice.entity.History;

import java.util.List;

public interface HistoryMapper {

    HistoryWithoutDocument toDto (History history);

    History tiEntity (HistoryWithoutDocument historyWithoutDocument);

    List<HistoryWithoutDocument> toDtoList (List<History> histories);

    List<History> toEntityList (List<HistoryWithoutDocument> historyWithoutDocuments);
}
