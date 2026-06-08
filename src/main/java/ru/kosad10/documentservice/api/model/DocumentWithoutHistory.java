package ru.kosad10.documentservice.api.model;

import ru.kosad10.documentservice.enums.Status;

import java.time.LocalDate;
import java.util.UUID;

public record DocumentWithoutHistory(Long id,
                                     UUID uuid,
                                     String author,
                                     String title,
                                     Status status,
                                     LocalDate createdAt,
                                     LocalDate updatedAt) {
}
