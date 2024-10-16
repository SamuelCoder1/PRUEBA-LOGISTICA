package com.riwi.Logistica.application.services.impl;

import com.riwi.Logistica.application.dtos.exception.CapacityExceededException;
import com.riwi.Logistica.application.dtos.exception.GenericNotFoundExceptions;
import com.riwi.Logistica.application.dtos.exception.UnauthorizedAccessException;
import com.riwi.Logistica.application.dtos.requests.CargaStatusUpdate;
import com.riwi.Logistica.application.dtos.requests.CargaWithoutId;
import com.riwi.Logistica.application.dtos.requests.ReportRequest;
import com.riwi.Logistica.domain.entities.AuditRecord;
import com.riwi.Logistica.domain.entities.Carga;
import com.riwi.Logistica.domain.entities.Pallet;
import com.riwi.Logistica.domain.entities.User;
import com.riwi.Logistica.domain.enums.CargaStatus;
import com.riwi.Logistica.domain.enums.Role;
import com.riwi.Logistica.domain.ports.service.ICargaService;
import com.riwi.Logistica.infrastructure.persistence.AuditRepository;
import com.riwi.Logistica.infrastructure.persistence.CargaRepository;
import com.riwi.Logistica.infrastructure.persistence.PalletRepository;
import com.riwi.Logistica.infrastructure.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CargaService implements ICargaService {

    @Autowired
    CargaRepository cargaRepository;

    @Autowired
    PalletRepository palletRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MailService emailService;

    @Autowired
    AuditRepository auditRepository;

    @Override
    public Carga create(CargaWithoutId cargaDTO) {
        if (!isAdmin()) {
            throw new UnauthorizedAccessException("Access denied: Only an admin can create cargas.");
        }

        Pallet pallet = palletRepository.findById(cargaDTO.getPallet().getId())
                .orElseThrow(() -> new GenericNotFoundExceptions("Pallet not found with ID: " + cargaDTO.getPallet().getId()));

        User transportador = userRepository.findById(cargaDTO.getTransportador().getId())
                .orElseThrow(() -> new GenericNotFoundExceptions("Transportador not found with ID: " + cargaDTO.getTransportador().getId()));

        Long pesoTotalCargas = cargaRepository.findByPallet(pallet).stream()
                .mapToLong(Carga::getPeso)
                .sum();

        if (pesoTotalCargas + cargaDTO.getPeso() > pallet.getCapacidad_maxima()) {
            throw new CapacityExceededException("Cannot add carga. Exceeds maximum capacity of the pallet.");
        }

        Carga carga = Carga.builder()
                .dimensiones(cargaDTO.getDimensiones())
                .peso(cargaDTO.getPeso())
                .estado(cargaDTO.getEstado())
                .transportador(transportador)
                .build();

        cargaRepository.save(carga);
        logAudit("Carga", carga.getId(), "CREATE", getCurrentUser(), "Created carga with ID: " + carga.getId());

        return carga;
    }


    @Override
    public Carga update(Long id, CargaWithoutId cargaDTO) {
        if (!isAdmin()) {
            throw new UnauthorizedAccessException("Access denied: Only an admin can update cargas.");
        }

        Carga existingCarga = cargaRepository.findById(id)
                .orElseThrow(() -> new GenericNotFoundExceptions("Carga not found with ID: " + id));


        Pallet newPallet = palletRepository.findById(cargaDTO.getPallet().getId())
                .orElseThrow(() -> new GenericNotFoundExceptions("Pallet not found with ID: " + cargaDTO.getPallet().getId()));

        Long totalPesoCargas = cargaRepository.findByPallet(newPallet).stream()
                .filter(c -> !c.getId().equals(existingCarga.getId()))
                .mapToLong(Carga::getPeso)
                .sum();

        if (totalPesoCargas + cargaDTO.getPeso() > newPallet.getCapacidad_maxima()) {
            throw new CapacityExceededException("Cannot update carga. Exceeds maximum capacity of the new pallet.");
        }

        existingCarga.setDimensiones(cargaDTO.getDimensiones());
        existingCarga.setPeso(cargaDTO.getPeso());
        existingCarga.setEstado(cargaDTO.getEstado());
        existingCarga.setTransportador(userRepository.findById(cargaDTO.getTransportador().getId())
                .orElseThrow(() -> new GenericNotFoundExceptions("Transportador not found with ID: " + cargaDTO.getTransportador().getId())));

        Carga updatedCarga = cargaRepository.save(existingCarga);
        logAudit("Carga", updatedCarga.getId(), "UPDATE", getCurrentUser(), "Updated carga with ID: " + updatedCarga.getId());

        return updatedCarga;
    }


    @Override
    public void delete(Long id) {
        if (!isAdmin()) {
            throw new UnauthorizedAccessException("Access denied: Only an admin can delete cargas.");
        }

        Carga carga = cargaRepository.findById(id)
                .orElseThrow(() -> new GenericNotFoundExceptions("Carga not found with ID: " + id));

        cargaRepository.delete(carga);
        logAudit("Carga", carga.getId(), "DELETE", getCurrentUser(), "Deleted carga with ID: " + carga.getId());
    }

    @Override
    public List<Carga> readAll() {
        if (!isAdmin()) {
            throw new UnauthorizedAccessException("Access denied: Only an admin can read cargas.");
        }
        return cargaRepository.findAll();
    }

    @Override
    public Optional<Carga> readById(Long id) {
        if (!isAdmin()) {
            throw new UnauthorizedAccessException("Access denied: Only an admin can read cargas.");
        }

        Carga carga = cargaRepository.findById(id)
                .orElseThrow(() -> new GenericNotFoundExceptions("Carga not found with ID: " + id));

        return Optional.ofNullable(carga);
    }

    public Carga updateCargaStatus(Long id, CargaStatusUpdate updateStatus) {
        if (!isAdmin() && !isTransportador()) {
            throw new UnauthorizedAccessException("Access denied: Only an admin or a transporter can update the carga status");
        }

        Carga carga = cargaRepository.findById(id)
                .orElseThrow(() -> new GenericNotFoundExceptions("Carga not found with ID: " + id));

        carga.setEstado(updateStatus.getEstado());
        Carga updatedCarga = cargaRepository.save(carga);
        logAudit("Carga", updatedCarga.getId(), "UPDATE_STATUS", getCurrentUser(), "Updated carga status to: " + updateStatus.getEstado());

        return updatedCarga;
    }

    public void reportDamage(ReportRequest request) {
        if (!isAdmin()) {
            throw new UnauthorizedAccessException("Access denied: Only an admin or a transporter can report damage");
        }

        Carga carga = cargaRepository.findById(request.getId_carga())
                .orElseThrow(() -> new GenericNotFoundExceptions("Carga not found"));

        carga.setEstado(CargaStatus.DAÑADA);
        cargaRepository.save(carga);

        logAudit("Carga", carga.getId(), "REPORT_DAMAGE", getCurrentUser(), "Reported damage for carga with ID: " + carga.getId());

        // Enviar el reporte por correo
        emailService.sendReport(carga, request);
    }

    public List<Carga> findCargaByTransportador(User transportador) {
        if (!isTransportador()) {
            throw new UnauthorizedAccessException("Access denied: Only a transporter can see their assigned loads");
        }
        return cargaRepository.findByTransportador(transportador);
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
