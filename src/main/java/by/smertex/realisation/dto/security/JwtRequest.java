package by.smertex.realisation.dto.security;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Schema(description = "Данные аутентификации")
@Builder
public record JwtRequest(@Email String username,
                         @Size(min = 1) String password) {
}
