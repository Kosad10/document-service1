package ru.kosad10.documentservice.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.generator.EventType;
import org.hibernate.type.SqlTypes;
import ru.kosad10.documentservice.enums.Status;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "documents")
@NoArgsConstructor

public class Document {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "documents_seq"
    )
    @SequenceGenerator(
            name = "documents_seq",
            sequenceName = "documents_id_seq",
            allocationSize = 1
    )
    private Long id;

    private UUID uuid;

    private String author;

    private String title;
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(columnDefinition = "status")
    private Status status;

    @CreationTimestamp
    private LocalDate createdAt;

    @UpdateTimestamp
    private LocalDate updatedAt;
    @JoinColumn
    @OneToMany
    private List<History> history;
    @JoinColumn
    @OneToOne(mappedBy = "document")
    private Registry registry;
}
