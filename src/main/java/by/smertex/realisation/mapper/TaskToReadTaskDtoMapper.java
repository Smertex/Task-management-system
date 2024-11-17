package by.smertex.realisation.mapper;

import by.smertex.interfaces.mapper.Mapper;
import by.smertex.realisation.database.entity.Task;
import by.smertex.realisation.dto.read.ReadTaskDto;
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
