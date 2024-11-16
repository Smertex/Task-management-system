package by.smertex.dto.security;

import lombok.Builder;

@Builder
public record SecurityUserDto(String email,
                              boolean isAdmin) {
}
