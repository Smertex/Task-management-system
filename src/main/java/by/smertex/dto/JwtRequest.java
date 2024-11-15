package by.smertex.dto;

import lombok.Builder;

@Builder
public record JwtRequest(String username,
                         String password) {
}
