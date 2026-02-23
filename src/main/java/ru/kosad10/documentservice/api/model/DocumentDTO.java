package ru.kosad10.documentservice.api.model;

import ru.kosad10.documentservice.enums.Statuses;

import java.time.LocalDate;
import java.util.UUID;

public record DocumentDTO(UUID uniqNumber, String author, String name) {
}
