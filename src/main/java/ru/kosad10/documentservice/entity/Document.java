package ru.kosad10.documentservice.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CollectionId;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import ru.kosad10.documentservice.enums.Statuses;

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

    private Statuses status;

    @CreationTimestamp
    private LocalDate createdAt;

    @UpdateTimestamp
    private LocalDate updatedAt;
    @JoinColumn
    @OneToMany
    private List<History> history;
    @JoinColumn
    @OneToOne
    private Registry registry;
}
