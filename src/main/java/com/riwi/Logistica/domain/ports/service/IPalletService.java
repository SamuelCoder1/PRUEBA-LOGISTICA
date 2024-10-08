package com.riwi.Logistica.domain.ports.service;

import com.riwi.Logistica.application.dtos.requests.PalletWithoutId;
import com.riwi.Logistica.application.services.generic.*;
import com.riwi.Logistica.domain.entities.Pallet;

public interface IPalletService extends
        Create<Pallet, PalletWithoutId>,
        Update<Long, Pallet, PalletWithoutId>,
        Delete<Long>,
        ReadAll<Pallet>,
        ReadById<Pallet, Long> {
}
