package by.smertex.dto.read;

import by.smertex.database.entity.enums.Priority;
import by.smertex.database.entity.enums.Status;
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
