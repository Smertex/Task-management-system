package by.smertex.dto.read;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record ReadMetainfoDto(UUID id,
                              LocalDateTime createAt,
                              LocalDateTime closedAt,
                              ReadUserDto createBy){
}
