package ru.kosad10.documentservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StatementRegister {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "statement_register_seq"
    )
    @SequenceGenerator(
            name = "statement_register_seq",
            sequenceName = "statement_register_id_seq",
            allocationSize = 1
    )
    @Column
    private Long id;
    @Column
    private Long document_id;

}
