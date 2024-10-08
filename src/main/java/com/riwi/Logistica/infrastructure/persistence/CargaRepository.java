package com.riwi.Logistica.infrastructure.persistence;

import com.riwi.Logistica.domain.entities.Carga;
import com.riwi.Logistica.domain.entities.Pallet;
import com.riwi.Logistica.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CargaRepository extends JpaRepository<Carga, Long> {
    List<Carga> findByTransportador(User transportador);
    List<Carga> findByPallet(Pallet pallet);
}
