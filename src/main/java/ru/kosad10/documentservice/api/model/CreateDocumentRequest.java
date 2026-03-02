package ru.kosad10.documentservice.api.model;


import java.util.UUID;

public record CreateDocumentRequest(
                                    String author,
                                    String title) {
}
