package com.riwi.Logistica.controllers;

import com.riwi.Logistica.application.dtos.exception.GenericNotFoundExceptions;
import com.riwi.Logistica.application.dtos.exception.UnauthorizedAccessException;
import com.riwi.Logistica.application.dtos.requests.PalletWithoutId;
import com.riwi.Logistica.application.services.impl.PalletService;
import com.riwi.Logistica.domain.entities.Carga;
import com.riwi.Logistica.domain.entities.Pallet;
import com.riwi.Logistica.domain.ports.service.IPalletService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pallets")
@CrossOrigin("*")
public class PalletController {

    @Autowired
    PalletService palletService;

    @Operation(summary =  "Create a new pallet")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pallet created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid data"),
            @ApiResponse(responseCode = "403", description = "Access denied: Only an admin can create pallets.")

    })
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/create")
    public ResponseEntity<Pallet> create(@RequestBody PalletWithoutId palletDTO) {
        Pallet pallet = palletService.create(palletDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(pallet);
    }

    @Operation(summary = "Get all pallets")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pallets retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied: Only an admin can read pallets.")
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping
    public ResponseEntity<List<Pallet>> readAll() {
        List<Pallet> pallets = palletService.readAll();
        return ResponseEntity.ok(pallets);
    }

    @Operation(summary = "Get pallet by ID", description = "Retrieves a pallet by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pallet retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Pallet not found"),
            @ApiResponse(responseCode = "403", description = "Access denied: Only an admin can create pallets.")
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/readById/{id}")
    public ResponseEntity<?> readById(@PathVariable Long id) {
        try{
            Pallet pallet = palletService.readById(id)
                    .orElseThrow(() -> new GenericNotFoundExceptions("Pallet not found with id: " + id));
            return ResponseEntity.ok(pallet);
        }catch (UnauthorizedAccessException error){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("dsvdsf");
        }
    }

    @Operation(summary = "Update an existing pallet")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pallet updated successfully"),
            @ApiResponse(responseCode = "404", description = "Pallet not found"),
            @ApiResponse(responseCode = "400", description = "Invalid data"),
            @ApiResponse(responseCode = "403", description = "Access denied: Only an admin can update pallets.")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/update/{id}")
    public ResponseEntity<Pallet> update(@PathVariable Long id, @RequestBody PalletWithoutId palletWithoutId) {
        Pallet updatedPallet = palletService.update(id, palletWithoutId);
        return ResponseEntity.ok(updatedPallet);
    }

    @Operation(summary = "Delete a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "403", description = "Access denied: Only an admin can delete users.")
    })
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        palletService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get all cargas assigned to a specific pallet")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cargas found"),
            @ApiResponse(responseCode = "404", description = "Pallet not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/{id}/loads")
    public ResponseEntity<List<Carga>> getCargasByPallet(@PathVariable Long id) {
        List<Carga> cargas = palletService.findCargasByPallet(id);
        return ResponseEntity.ok(cargas);
    }
}
