package by.smertex.mapper;

import by.smertex.database.entity.Task;
import by.smertex.dto.read.ReadTaskDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskToReadTaskDtoMapper implements Mapper<Task, ReadTaskDto> {

    private final UserToReadUserDtoMapper userToReadUserDtoMapper;

    @Override
    public ReadTaskDto map(Task from) {
        return ReadTaskDto.builder()
                .id(from.getId())
                .name(from.getName())
                .description(from.getDescription())
                .status(from.getStatus())
                .priority(from.getPriority())
                .performer(userToReadUserDtoMapper.map(from.getPerformer()))
                .build();
    }
}
