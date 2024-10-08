package com.riwi.Logistica.infrastructure.persistence;

import com.riwi.Logistica.domain.entities.Pallet;
import com.riwi.Logistica.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PalletRepository extends JpaRepository<Pallet, Long> {
}
