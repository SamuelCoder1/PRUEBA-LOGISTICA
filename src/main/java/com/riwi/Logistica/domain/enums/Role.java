package com.riwi.Logistica.domain.enums;

import java.util.Collections;
import java.util.Set;

public enum Role {

    ADMINISTRADOR(Set.of("MANAGE_ALL")),
    TRANSPORTADOR(Set.of("VIEW_CARRIERS"));

    private final Set<String> permissions;

    Role(Set<String> permissions) {
        this.permissions = permissions;
    }

    public Set<String> getPermissions() {
        return Collections.unmodifiableSet(permissions);
    }
}