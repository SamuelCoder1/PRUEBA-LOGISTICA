package com.riwi.Logistica.application.services.impl;

import com.riwi.Logistica.application.dtos.exception.GenericNotFoundExceptions;
import com.riwi.Logistica.application.dtos.exception.UnauthorizedAccessException;
import com.riwi.Logistica.application.dtos.requests.PalletWithoutId;
import com.riwi.Logistica.application.dtos.responses.CargaOnlyWithId;
import com.riwi.Logistica.domain.entities.AuditRecord;
import com.riwi.Logistica.domain.entities.Carga;
import com.riwi.Logistica.domain.entities.Pallet;
import com.riwi.Logistica.domain.enums.Role;
import com.riwi.Logistica.domain.ports.service.IPalletService;
import com.riwi.Logistica.infrastructure.persistence.AuditRepository;
import com.riwi.Logistica.infrastructure.persistence.CargaRepository;
import com.riwi.Logistica.infrastructure.persistence.PalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PalletService implements IPalletService {

    @Autowired
    PalletRepository palletRepository;

    @Autowired
    CargaRepository cargaRepository;

    @Autowired
    AuditRepository auditRepository;

    @Override
    public Pallet create(PalletWithoutId palletDTO) {
        if (!isAdmin()) {
            throw new UnauthorizedAccessException("Access denied: Only an admin can create pallets.");
        }

        Set<Carga> cargas = new HashSet<>();
        for (CargaOnlyWithId cargaOnlyWithId : palletDTO.getCargas()) {
            Carga carga = cargaRepository.findById(cargaOnlyWithId.getId())
                    .orElseThrow(() -> new GenericNotFoundExceptions("Carga no encontrada con ID: " + cargaOnlyWithId.getId()));
            cargas.add(carga);
        }

        Pallet pallet = new Pallet();
        pallet.setCapacidad_maxima(palletDTO.getCapacidad_maxima());
        pallet.setPeso(palletDTO.getPeso());
        pallet.setUbicacion(palletDTO.getUbicacion());
        pallet.setCargas(cargas);
        pallet.setEstado(palletDTO.getEstado());

        Pallet savedPallet = palletRepository.save(pallet);
        logAudit("Pallet", savedPallet.getId(), "CREATE", getCurrentUser(), "Created pallet with ID: " + savedPallet.getId());

        return savedPallet;
    }

    @Override
    public Pallet update(Long palletId, PalletWithoutId palletDTO) {
        if (!isAdmin()) {
            throw new UnauthorizedAccessException("Access denied: Only an admin can update pallets.");
        }

        Pallet pallet = palletRepository.findById(palletId)
                .orElseThrow(() -> new GenericNotFoundExceptions("Pallet not found"));

        pallet.setCapacidad_maxima(palletDTO.getCapacidad_maxima());
        pallet.setPeso(palletDTO.getPeso());
        pallet.setUbicacion(palletDTO.getUbicacion());
        pallet.setEstado(palletDTO.getEstado());

        Set<Carga> cargas = new HashSet<>();
        for (CargaOnlyWithId cargaOnlyWithId : palletDTO.getCargas()) {
            Carga carga = cargaRepository.findById(cargaOnlyWithId.getId())
                    .orElseThrow(() -> new GenericNotFoundExceptions("Carga not found with ID: " + cargaOnlyWithId.getId()));
            cargas.add(carga);
        }

        pallet.setCargas(cargas);
        Pallet updatedPallet = palletRepository.save(pallet);
        logAudit("Pallet", updatedPallet.getId(), "UPDATE", getCurrentUser(), "Updated pallet with ID: " + updatedPallet.getId());

        return updatedPallet;
    }

    @Override
    public void delete(Long id) {
        if (!isAdmin()) {
            throw new UnauthorizedAccessException("Access denied: Only an admin can delete pallets.");
        }
        Pallet pallet = palletRepository.findById(id)
                .orElseThrow(() -> new GenericNotFoundExceptions("Pallet not found with id:" + id));

        palletRepository.delete(pallet);
        logAudit("Pallet", pallet.getId(), "DELETE", getCurrentUser(), "Deleted pallet with ID: " + pallet.getId());
    }

    @Override
    public List<Pallet> readAll() {
        if (!isAdmin()) {
            throw new UnauthorizedAccessException("Access denied: Only an admin can read pallets.");
        }
        return palletRepository.findAll();
    }

    @Override
    public Optional<Pallet> readById(Long id) {
        if (!isAdmin()) {
            throw new UnauthorizedAccessException("Access denied: Only an admin can read pallets.");
        }

        return Optional.ofNullable(palletRepository.findById(id)
                .orElseThrow(() -> new GenericNotFoundExceptions("Pallet not found with ID: " + id)));
    }

    public List<Carga> findCargasByPallet(Long palletId) {
        if (!isAdmin() && !isTransportador()) {
            throw new UnauthorizedAccessException("Access denied: Only an admin or a transporter can view carga assignments.");
        }

        Pallet pallet = palletRepository.findById(palletId)
                .orElseThrow(() -> new GenericNotFoundExceptions("Pallet not found with ID: " + palletId));

        List<Carga> cargas = cargaRepository.findByPallet(pallet);

        return cargas;
    }

    private void logAudit(String entityName, Long entityId, String action, String user, String details) {
        AuditRecord auditRecord = new AuditRecord();
        auditRecord.setEntityName(entityName);
        auditRecord.setEntityId(entityId);
        auditRecord.setAction(action);
        auditRecord.setUser(user);
        auditRecord.setTimestamp(new Date());
        auditRecord.setDetails(details);

        auditRepository.save(auditRecord);
    }

    private String getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null ? authentication.getName() : "Unknown User";
    }

    private boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(Role.ADMINISTRADOR.name()));
    }

    private boolean isTransportador() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(Role.TRANSPORTADOR.name()));
    }
}
