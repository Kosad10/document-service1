package ru.kosad10.documentservice.api.model;

import ru.kosad10.documentservice.enums.Status;

import java.time.LocalDate;

public record DocumentsFilter(String author,
                              Status documentStatusEnum, LocalDate createdFrom, LocalDate createdTo) {
}
