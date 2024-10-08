package com.riwi.Logistica.infrastructure.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/*Esta clase en utils la puedo utilizar para crear una contrasea encriptada y quemar los datos en la db, con esta contraseña*/

public class PasswordGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "samuel2006";
        String encodedPassword = encoder.encode(rawPassword);
        System.out.println(encodedPassword);
    }
}
