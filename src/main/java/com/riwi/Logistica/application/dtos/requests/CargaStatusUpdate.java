package com.riwi.Logistica.application.dtos.requests;

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
public class CargaStatusUpdate {

    @NotNull(message = "The status is required")
    private CargaStatus estado;
}
