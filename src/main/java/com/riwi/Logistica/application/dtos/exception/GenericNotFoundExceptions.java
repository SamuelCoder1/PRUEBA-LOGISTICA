package com.riwi.Logistica.application.dtos.exception;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GenericNotFoundExceptions extends RuntimeException {

    public GenericNotFoundExceptions(String message) {
        super(message);
    }

}
