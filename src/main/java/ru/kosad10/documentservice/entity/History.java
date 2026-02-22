package ru.kosad10.documentservice.entity;

import jakarta.persistence.*;
import ru.kosad10.documentservice.enums.Action;

import java.time.LocalDate;

@Entity
public class History {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "history_seq"
    )
    @SequenceGenerator(
            name = "history_seq",
            sequenceName = "history_id_seq",
            allocationSize = 1
    )
    @Column
    private Long id;
    @Column
    private Long documentId;
    @Column
    private String author;
    @Column
    private LocalDate dateOfModify;
    @Column
    private Action actionEnum;
    @Column
    private String comment;
}
