package com.riwi.Logistica.domain.ports.service;

import com.riwi.Logistica.application.dtos.responses.UserWithoutId;
import com.riwi.Logistica.application.services.generic.Register;
import com.riwi.Logistica.domain.entities.User;

public interface IUserService extends
        Register<User, UserWithoutId> {
}
