package com.riwi.Logistica.application.services.impl;

import com.riwi.Logistica.application.dtos.responses.UserWithoutId;
import com.riwi.Logistica.domain.entities.User;
import com.riwi.Logistica.domain.enums.Role;
import com.riwi.Logistica.domain.ports.service.IUserService;
import com.riwi.Logistica.infrastructure.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService {

    @Autowired
    UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public User register(UserWithoutId userDTO) {

        User user = User.builder()
                .username(userDTO.getUsername())
                .password(encryptPassword(userDTO.getPassword()))
                .role(userDTO.getRole())
                .build();

        userRepository.save(user);
        System.out.println("Usuario saved: " + user);
        return user;
    }

    private String encryptPassword(String password) {
        return passwordEncoder.encode(password); // Encripta la contraseña
    }

    private boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(Role.ADMINISTRADOR.name()));
    }
}
