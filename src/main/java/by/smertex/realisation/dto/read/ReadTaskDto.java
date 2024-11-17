package by.smertex.realisation.dto.read;

import by.smertex.realisation.database.entity.enums.Priority;
import by.smertex.realisation.database.entity.enums.Status;
import lombok.Builder;

import java.util.UUID;

@Builder
public record ReadTaskDto(UUID id,
                          String name,
                          String description,
                          Status status,
                          Priority priority,
                          ReadUserDto performer) {
}
