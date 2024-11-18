package by.smertex.realisation.dto.read;

import by.smertex.realisation.database.entity.enums.Priority;
import by.smertex.realisation.database.entity.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.UUID;

@Schema(description = "Сущность задачи")
@Builder
public record ReadTaskDto(UUID id,
                          String name,
                          String description,
                          Status status,
                          Priority priority,
                          ReadUserDto performer) {
}
