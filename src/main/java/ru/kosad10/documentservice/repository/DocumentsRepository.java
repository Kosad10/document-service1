package ru.kosad10.documentservice.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.kosad10.documentservice.entity.Document;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentsRepository extends JpaRepository <Document, Long>, JpaSpecificationExecutor<Document> {

    @Query("""
            SELECT d
            FROM Document d
            JOIN FETCH d.history h
            where d.id = :id""")
    Optional<Document> findDocumentAndHistoryById(Long id);

    Page<Document> findByIdIn(Collection<Long> ids, Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT d FROM Document d WHERE d.id IN :ids")
    List<Document> findAllByIdWithWriteLock(@Param("ids") Collection<Long> ids);
}
