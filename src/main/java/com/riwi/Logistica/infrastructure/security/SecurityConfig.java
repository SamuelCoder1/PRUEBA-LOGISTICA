package com.riwi.Logistica.infrastructure.security;

import com.riwi.Logistica.domain.enums.Role;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity // Habilita la configuración de seguridad
@AllArgsConstructor // Genera un constructor
@Configuration // Indica que esta clase es una clase de configuración
public class SecurityConfig {

    private final JwtFilter jwtFilter; // Filtro para manejar la autenticación JWT

    private final AuthenticationProvider authenticationProvider; // Proveedor de autenticación para manejar las credenciales

    // Definición de los endpoints públicos
    private final String[] PUBLIC_ENDPOINTS = {
            "/auth/login",
            "/auth/register",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/api/audit/logs"
    };

    // Definición de los endpoints que solo pueden ser accedidos por ADMINISTRADOR
    private final String[] ADMIN_ENDPOINTS = {
            "/api/pallets/create",
            "/api/pallets",
            "/api/pallets/readById/**",
            "/api/pallets/update/**",
            "/api/pallets/delete/**",
            "/api/pallets/*/loads",
            "/api/loads/create",
            "/api/loads",
            "/api/loads/readById/**",
            "/api/loads/update/**",
            "/api/loads/delete/**",
            "/api/loads/*/status",
            "/api/loads/*/damage",
            "/api/loads/carriers"
    };


    private final String[] TRANSPORTADOR_ENDPOINTS = {
            "/api/loads/*/status",
            "/api/loads/*/damage",
            "/api/pallets/*/loads",
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable()) // Desactiva CSRF ya que se usará JWT
                .authorizeHttpRequests(authRequest -> authRequest
                        .requestMatchers(ADMIN_ENDPOINTS).hasAuthority(Role.ADMINISTRADOR.name()) // Permite solo a ADMIN acceder a los endpoints de ADMI
                        .requestMatchers(TRANSPORTADOR_ENDPOINTS).hasAuthority(Role.TRANSPORTADOR.name())// Permite solo a TRANSPORTADOR acceder a los endpoints de TRANSPORTADOR
                        .requestMatchers(PUBLIC_ENDPOINTS).permitAll() // Permite acceso a todos a los endpoints públicos
                        .anyRequest().authenticated()) // Cualquier otra solicitud requiere autenticación
                .authenticationProvider(authenticationProvider) // Establece el proveedor de autenticación
                .sessionManagement(sessionManager -> sessionManager.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Configura la sesión para que sea sin estado
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class) // Agrega el filtro JWT antes del filtro de autenticación por nombre de usuario y contraseña
                .build(); // Construye la cadena de seguridad
    }
}