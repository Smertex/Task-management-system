package by.smertex.dto.read;

import by.smertex.database.entity.enums.Role;
import lombok.Builder;

import java.util.UUID;

@Builder
public record ReadUserDto(UUID id,
                          String email,
                          Role role) {
}
