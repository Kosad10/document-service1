package ru.kosad10.documentservice.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.kosad10.documentservice.enums.Statuses;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "documents")
@NoArgsConstructor
@AllArgsConstructor
public class Documents {

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
    @Column
    private Long id;
    @Column
    private UUID uniqNumber;
    @Column
    private String author;
    @Column
    private String name;
    @Column
    private Statuses documentStatusEnum;
    @Column
    private LocalDate createdAddUpdatedAdd;
}
