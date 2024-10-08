package com.riwi.Logistica.domain.ports.service;

import com.riwi.Logistica.application.dtos.requests.CargaWithoutId;
import com.riwi.Logistica.application.dtos.requests.PalletWithoutId;
import com.riwi.Logistica.application.services.generic.*;
import com.riwi.Logistica.domain.entities.Carga;
import com.riwi.Logistica.domain.entities.Pallet;

public interface ICargaService extends
        Create<Carga, CargaWithoutId>,
        Update<Long, Carga, CargaWithoutId>,
        Delete<Long>,
        ReadAll<Carga>,
        ReadById<Carga, Long> {
}
