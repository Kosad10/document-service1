package ru.kosad10.documentservice.api.model;

import ru.kosad10.documentservice.enums.Action;
import ru.kosad10.documentservice.enums.Statuses;

import java.time.LocalDate;
import java.util.UUID;

public record DocumentWithHistoryDTO(UUID uniqNumber, String author, String name,
                                     Statuses documentStatusEnum, LocalDate createdAddUpdatedAdd,
                                     String authorOfChanges, LocalDate dateOfModify, Action actionEnum, String comment) {
}
