package ru.kosad10.documentservice.api.model;

import ru.kosad10.documentservice.entity.Document;
import ru.kosad10.documentservice.enums.Action;

import java.time.LocalDate;

public record HistoryDTO(Document document,
                         String author, LocalDate updatedAt,
                         Action action, String comment) {
}
