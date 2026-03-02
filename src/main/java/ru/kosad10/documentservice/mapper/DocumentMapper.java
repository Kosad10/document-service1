package ru.kosad10.documentservice.mapper;

import org.mapstruct.Mapper;
import ru.kosad10.documentservice.api.model.DocumentWithHistory;
import ru.kosad10.documentservice.entity.Document;

@Mapper(componentModel = "spring")
public interface DocumentMapper {

    DocumentWithHistory toDto (Document document);

    Document toEntity (DocumentWithHistory documentWithHistory);
}
