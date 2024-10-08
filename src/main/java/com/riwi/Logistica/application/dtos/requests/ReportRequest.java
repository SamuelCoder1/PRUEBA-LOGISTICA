package com.riwi.Logistica.application.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportRequest {

    @NotNull(message = "El id de la carga es requerida")
    private Long id_carga;

    @NotBlank(message = "Los detalles del reporte son requeridos")
    private String detalles;
}
