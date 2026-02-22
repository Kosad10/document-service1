package ru.kosad10.documentservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.kosad10.documentservice.enums.Action;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
