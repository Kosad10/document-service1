package ru.kosad10.documentservice.api.model;

import ru.kosad10.documentservice.enums.Action;

import java.time.LocalDate;

public record HistoryWithoutDocument(Long id,
                                     String author,
                                     LocalDate dateOfModify,
                                     Action action,
                                     String comment) {
}
