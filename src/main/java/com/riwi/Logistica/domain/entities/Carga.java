package com.riwi.Logistica.domain.entities;

import com.riwi.Logistica.domain.enums.CargaStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity(name = "cargas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Carga extends Auditable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String dimensiones;

    @Column(nullable = false)
    private Long peso;

    @ManyToOne
    @JoinColumn(name = "pallet_id")
    private Pallet pallet;

    @Enumerated(EnumType.STRING)
    private CargaStatus estado;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User transportador;
}
