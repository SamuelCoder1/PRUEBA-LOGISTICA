package com.riwi.Logistica.application.dtos.requests;

import com.riwi.Logistica.application.dtos.responses.CargaOnlyWithId;
import com.riwi.Logistica.domain.enums.PalletStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PalletWithoutId {

    @NotNull(message = "La capacidad maxima es requerida")
    private Long capacidad_maxima;

    @NotBlank(message = "El peso es requerido")
    private String peso;

    @NotNull(message = "La ubicacion es requerida")
    private String ubicacion;

    @NotNull(message = "La carga es requerida")
    private Set<CargaOnlyWithId> cargas;

    @NotNull(message = "El estao del pallet es requerido")
    private PalletStatus estado;

}
