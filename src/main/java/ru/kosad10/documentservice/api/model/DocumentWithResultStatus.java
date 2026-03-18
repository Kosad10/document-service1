package ru.kosad10.documentservice.api.model;

import ru.kosad10.documentservice.enums.ResultStatus;
import ru.kosad10.documentservice.enums.Status;

import java.time.LocalDate;
import java.util.UUID;

public record DocumentWithResultStatus(Long id, ResultStatus resultStatus) {
}
