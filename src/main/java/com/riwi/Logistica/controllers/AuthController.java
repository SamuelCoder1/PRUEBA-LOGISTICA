package com.riwi.Logistica.controllers;

import com.riwi.Logistica.application.dtos.requests.LoginRequest;
import com.riwi.Logistica.application.dtos.responses.UserWithoutId;
import com.riwi.Logistica.application.services.impl.AuthService;
import com.riwi.Logistica.domain.entities.User;
import com.riwi.Logistica.domain.ports.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    IUserService userService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        try {
            String jwtToken = authService.login(loginRequest); // Usa el AuthService para iniciar sesión
            return ResponseEntity.ok(jwtToken); // Retorna el token
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body("Credenciales inválidas");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(500).body("Error en el servidor");
        }
    }

    @Operation(summary =  "Register")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid data")
    })
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody UserWithoutId userDTO) {
        User user = userService.register(userDTO);
        return ResponseEntity.status(201).body(user);
    }
}

