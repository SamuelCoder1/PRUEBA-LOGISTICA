package com.riwi.Logistica.application.dtos.responses;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PalletOnlyWithId {

    @NotNull(message = "El id es requerido")
    private Long id;
}
