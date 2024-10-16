package com.riwi.Logistica.domain.entities;

import com.riwi.Logistica.domain.enums.PalletStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity(name = "pallets")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Pallet extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long capacidad_maxima;

    @Column(nullable = false)
    private String ubicacion;

    @Enumerated(EnumType.STRING)
    private PalletStatus estado;
}
