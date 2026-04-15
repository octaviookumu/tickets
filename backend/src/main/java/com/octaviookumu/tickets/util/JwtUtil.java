package com.octaviookumu.tickets.util;

import org.springframework.security.oauth2.jwt.Jwt;

import java.util.UUID;

public final class JwtUtil {
    // private because this class is not meant to be instantiated
    // it’s just a container for static methods.
    private JwtUtil() {
    }

    public static UUID parseUserId(Jwt jwt) {
        return UUID.fromString(jwt.getSubject());
    }
}
