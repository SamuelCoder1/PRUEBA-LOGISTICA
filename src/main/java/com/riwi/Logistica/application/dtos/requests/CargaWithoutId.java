package com.riwi.Logistica.application.dtos.requests;

import com.riwi.Logistica.application.dtos.responses.PalletOnlyWithId;
import com.riwi.Logistica.application.dtos.responses.UserOnlyWithId;
import com.riwi.Logistica.domain.enums.CargaStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CargaWithoutId {

    private String dimensiones;

    @NotNull(message = "El peso es requerido")
    private Long peso;

    @NotNull(message = "El pallet es requerido")
    private PalletOnlyWithId pallet;

    @NotNull(message = "El estado es requerido")
    private CargaStatus estado;

    @NotNull(message = "El transportador es requerido")
    private UserOnlyWithId transportador;
}
