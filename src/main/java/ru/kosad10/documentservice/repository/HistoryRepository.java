package ru.kosad10.documentservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kosad10.documentservice.entity.History;

import java.util.Optional;

public interface HistoryRepository extends JpaRepository<History, Long> {

    Optional<History> findHistoriesByDocumentId(Long documentId);
}
