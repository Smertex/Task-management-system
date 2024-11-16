package by.smertex.mapper;

import by.smertex.database.entity.Task;
import by.smertex.database.repository.UserRepository;
import by.smertex.dto.update.CreateOrUpdateTaskUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateOrUpdateTaskDtoToTaskMapper implements Mapper<CreateOrUpdateTaskUserDto, Task>{

    private final UserRepository userRepository;

    @Override
    public Task map(CreateOrUpdateTaskUserDto from) {
        Task task = copy(from, new Task());
        userRepository.findByEmail(from.performerEmail())
                .ifPresent(task::setPerformer);
        return copy(from, new Task());
    }

    @Override
    public Task map(CreateOrUpdateTaskUserDto from, Task to){
        return copy(from, to);
    }

    private Task copy(CreateOrUpdateTaskUserDto from, Task to){
        to.setName(from.name());
        to.setDescription(from.description());
        to.setStatus(from.status());
        to.setPriority(from.priority());
        userRepository.findByEmail(from.performerEmail())
                .ifPresent(to::setPerformer);
        return to;
    }
}
