package ru.kosad10.documentservice.api.model;

import ru.kosad10.documentservice.enums.Status;

import java.time.LocalDate;
import java.util.UUID;

public record DocumentsFilter(UUID uniqNumber,
                              String author,
                              String name,
                              Status documentStatusEnum, LocalDate createdAddUpdatedAdd) {
}
