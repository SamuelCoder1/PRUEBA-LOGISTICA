package com.riwi.Logistica.controllers;

import com.riwi.Logistica.application.dtos.exception.GenericNotFoundExceptions;
import com.riwi.Logistica.application.dtos.exception.UnauthorizedAccessException;
import com.riwi.Logistica.application.dtos.requests.CargaStatusUpdate;
import com.riwi.Logistica.application.dtos.requests.CargaWithoutId;
import com.riwi.Logistica.application.dtos.requests.ReportRequest;
import com.riwi.Logistica.application.services.impl.CargaService;
import com.riwi.Logistica.domain.entities.Carga;
import com.riwi.Logistica.domain.entities.User;
import com.riwi.Logistica.domain.enums.Role;
import com.riwi.Logistica.domain.ports.service.ICargaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/loads")
@CrossOrigin("*")
public class CargaController {

    @Autowired
    CargaService cargaService;

    @Operation(summary =  "Create a new Carga")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Carga created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid data"),
            @ApiResponse(responseCode = "403", description = "Access denied: Only an admin can create cargas.")

    })
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/create")
    public ResponseEntity<Carga> create(@RequestBody CargaWithoutId CargaDTO) {
        Carga Carga = cargaService.create(CargaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(Carga);
    }

    @Operation(summary = "Get all cargas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cargas retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied: Only an admin can read cargas.")
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping
    public ResponseEntity<List<Carga>> readAll() {
        List<Carga> Cargas = cargaService.readAll();
        return ResponseEntity.ok(Cargas);
    }

    @Operation(summary = "Get carga by ID", description = "Retrieves a carga by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Carga retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Carga not found"),
            @ApiResponse(responseCode = "403", description = "Access denied: Only an admin can create cargas.")
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/readById/{id}")
    public ResponseEntity<?> readById(@PathVariable Long id) {
        try{
            Carga Carga = cargaService.readById(id)
                    .orElseThrow(() -> new GenericNotFoundExceptions("Carga not found with id: " + id));
            return ResponseEntity.ok(Carga);
        }catch (UnauthorizedAccessException error){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Access denied");
        }
    }

    @Operation(summary = "Update an existing carga")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Carga updated successfully"),
            @ApiResponse(responseCode = "404", description = "Carga not found"),
            @ApiResponse(responseCode = "400", description = "Invalid data"),
            @ApiResponse(responseCode = "403", description = "Access denied: Only an admin can update cargas.")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/update/{id}")
    public ResponseEntity<Carga> update(@PathVariable Long id, @RequestBody CargaWithoutId CargaWithoutId) {
        Carga updatedCarga = cargaService.update(id, CargaWithoutId);
        return ResponseEntity.ok(updatedCarga);
    }

    @Operation(summary = "Delete a carga")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Carga deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Carga not found"),
            @ApiResponse(responseCode = "403", description = "Access denied: Only an admin can delete cargas.")
    })
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        cargaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Update the status of a carga")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Carga status updated successfully"),
            @ApiResponse(responseCode = "404", description = "Carga not found"),
            @ApiResponse(responseCode = "403", description = "Access denied: Only an admin or a transporter can update carga status.")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PatchMapping("/{id}/status")
    public ResponseEntity<Carga> updateCargaStatus(@PathVariable Long id, @RequestBody CargaStatusUpdate statusUpdateDTO) {
        Carga updatedCarga = cargaService.updateCargaStatus(id, statusUpdateDTO);
        return ResponseEntity.ok(updatedCarga);
    }

    @Operation(summary = "Report damage to a carga")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Damage reported successfully"),
            @ApiResponse(responseCode = "404", description = "Carga not found"),
            @ApiResponse(responseCode = "403", description = "Access denied: Only an admin or a transporter can report damage.")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/{id}/damage")
    public ResponseEntity<Void> reportDamage(@PathVariable Long id, @RequestBody ReportRequest request) {
        request.setId_carga(id);
        cargaService.reportDamage(request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get cargas assigned to the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cargas retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied: Only an admin or a transporter can access this resource."),
            @ApiResponse(responseCode = "404", description = "No cargas found for the user.")
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/carriers")
    public ResponseEntity<List<Carga>> findCargaByTransportador(Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        List<Carga> cargas;
        if (user.getRole() == Role.ADMINISTRADOR) {
            cargas = cargaService.readAll();
        } else if (user.getRole() == Role.TRANSPORTADOR) {
            cargas = cargaService.findCargaByTransportador(user);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(cargas);
    }

}
