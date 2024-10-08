package com.riwi.Logistica.application.dtos.responses;

import com.riwi.Logistica.domain.enums.Role;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserOnlyWithId {

    @NotNull(message = "El id es requerido")
    private Long id;

}
