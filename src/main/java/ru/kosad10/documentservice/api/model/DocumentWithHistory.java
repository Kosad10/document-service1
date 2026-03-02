package ru.kosad10.documentservice.api.model;

import lombok.Builder;
import ru.kosad10.documentservice.entity.History;
import ru.kosad10.documentservice.enums.Status;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Builder
public record DocumentWithHistory(
        Long id,
        UUID uuid,
        String author,
        String title,
        Status status,
        LocalDate createdAt,
        LocalDate updatedAt,
        List<History> historyList) {
}
