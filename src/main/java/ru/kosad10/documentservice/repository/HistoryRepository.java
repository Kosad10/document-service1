package ru.kosad10.documentservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kosad10.documentservice.entity.History;

import java.util.Optional;

@Repository
public interface HistoryRepository extends JpaRepository<History, Long> {

    Optional<History> findHistoriesByDocumentId(Long documentId);
}
