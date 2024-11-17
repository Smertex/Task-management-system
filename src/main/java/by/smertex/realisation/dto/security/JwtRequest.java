package by.smertex.realisation.dto.security;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record JwtRequest(@Email String username,
                         @Size(min = 1) String password) {
}
