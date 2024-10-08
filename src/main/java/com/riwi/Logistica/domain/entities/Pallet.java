package com.riwi.Logistica.domain.entities;

import com.riwi.Logistica.domain.enums.PalletStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Entity(name = "pallets")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Pallet extends Auditable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long capacidad_maxima;

    @Column(nullable = false)
    private String peso;

    @Column(nullable = false)
    private String ubicacion;

    @OneToMany(mappedBy = "pallet", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Carga> cargas;

    @Enumerated(EnumType.STRING)
    private PalletStatus estado;

}
