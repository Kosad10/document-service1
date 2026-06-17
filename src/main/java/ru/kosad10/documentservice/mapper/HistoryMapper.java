package ru.kosad10.documentservice.mapper;

import org.mapstruct.Mapper;
import ru.kosad10.documentservice.api.model.HistoryWithoutDocument;
import ru.kosad10.documentservice.entity.Document;
import ru.kosad10.documentservice.entity.History;
import ru.kosad10.documentservice.enums.Action;

import java.time.LocalDate;
import java.util.List;

@Mapper(componentModel = "spring")
public interface HistoryMapper {

    HistoryWithoutDocument toDto (History history);

    History toEntity(HistoryWithoutDocument historyWithoutDocument);

    default History toDocumentHistory (Document document, Action action){
        return new History(null, document, document.getAuthor(), LocalDate.now(), action, "");
    }

    List<HistoryWithoutDocument> toDtoList (List<History> histories);

    List<History> toEntityList (List<HistoryWithoutDocument> historyWithoutDocuments);


}
