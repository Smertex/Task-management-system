package by.smertex.realisation.dto.security;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "JWT-токен")
public record JwtResponse(String token) {
}
