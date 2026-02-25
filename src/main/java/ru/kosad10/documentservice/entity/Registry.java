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
public class Registry { //Registry

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "registry_seq"
    )
    @SequenceGenerator(
            name = "registry_seq",
            sequenceName = "registry_id_seq",
            allocationSize = 1
    )
    @Column
    private Long id;
    @OneToOne
    @JoinColumn
    private Document document;

}
