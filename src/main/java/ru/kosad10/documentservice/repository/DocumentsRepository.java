package ru.kosad10.documentservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kosad10.documentservice.entity.Document;

@Repository
public interface DocumentsRepository extends JpaRepository <Document, Long> {
}
