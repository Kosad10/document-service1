package ru.kosad10.documentservice.api.model;

import ru.kosad10.documentservice.enums.Action;

import java.time.LocalDate;

public record HistoryDTO(Long documentId, String author, LocalDate dateOfModify,
                         Action actionEnum, String comment) {
}
