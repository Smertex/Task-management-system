package by.smertex.mapper;

import by.smertex.database.entity.Task;
import by.smertex.database.repository.UserRepository;
import by.smertex.dto.update.CreateOrUpdateUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateOrUpdateTaskDtoToTaskMapper implements Mapper<CreateOrUpdateUserDto, Task>{

    private final UserRepository userRepository;

    @Override
    public Task map(CreateOrUpdateUserDto from) {
        Task task = copy(from, new Task());
        userRepository.findByEmail(from.performerEmail())
                .ifPresent(task::setPerformer);
        return copy(from, new Task());
    }

    @Override
    public Task map(CreateOrUpdateUserDto from, Task to){
        return copy(from, to);
    }

    private Task copy(CreateOrUpdateUserDto from, Task to){
        to.setName(from.name());
        to.setDescription(from.description());
        to.setStatus(from.status());
        to.setPriority(from.priority());
        userRepository.findByEmail(from.performerEmail())
                .ifPresent(to::setPerformer);
        return to;
    }
}
