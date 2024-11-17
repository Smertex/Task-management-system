package by.smertex.realisation.dto.read;

import by.smertex.realisation.database.entity.enums.Role;
import lombok.Builder;

import java.util.UUID;

@Builder
public record ReadUserDto(UUID id,
                          String email,
                          Role role) {
}
