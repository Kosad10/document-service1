package ru.kosad10.documentservice.api.model;

import ru.kosad10.documentservice.enums.Statuses;

import java.time.LocalDate;
import java.util.UUID;

public record DocumentsFilter(UUID uniqNumber, String author, String name,
                              Statuses documentStatusEnum, LocalDate createdAddUpdatedAdd) {
}
