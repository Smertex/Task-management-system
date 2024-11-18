package by.smertex.service.realisation;

import by.smertex.dto.filter.TaskFilter;
import by.smertex.dto.security.SecurityUserDto;
import by.smertex.dto.update.CreateOrUpdateTaskDto;
import by.smertex.service.interfaces.AuthService;
import by.smertex.service.interfaces.TaskService;
import by.smertex.service.interfaces.UserService;
import by.smertex.mapper.realisation.CreateOrUpdateTaskDtoToTaskMapper;
import by.smertex.mapper.realisation.TaskToReadTaskDtoMapper;
import by.smertex.controller.exception.UserNotFoundInDatabaseException;
import by.smertex.database.entity.realisation.Task;
import by.smertex.database.repository.interfaces.TaskRepository;
import by.smertex.dto.read.ReadTaskDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    private final TaskToReadTaskDtoMapper taskToReadTaskDtoMapper;

    private final CreateOrUpdateTaskDtoToTaskMapper createOrUpdateTaskDtoToTaskMapper;

    private final MetainfoService metainfoService;

    private final AuthService authService;

    private final UserService userService;

    public Optional<Task> findById(UUID id){
        return taskRepository.findById(id);
    }

    @Override
    public Page<ReadTaskDto> findAllByFilter(TaskFilter filter, Pageable pageable){
        return taskRepository.findAllByFilter(filter, authService.takeUserFromContext().orElseThrow(), pageable)
                .map(taskToReadTaskDtoMapper::map);
    }

    @Override
    @Transactional
    public Optional<ReadTaskDto> save(CreateOrUpdateTaskDto dto){
        return Optional.of(dto)
                .map(element -> {
                    Task task = createOrUpdateTaskDtoToTaskMapper.map(element);
                    task.setMetainfo(metainfoService.save()
                            .orElseThrow());
                    task.setPerformer(userService.findByEmail(dto.performerEmail())
                            .orElseThrow(() -> new UserNotFoundInDatabaseException(dto.performerEmail())));
                    return task;
                })
                .map(taskRepository::save)
                .map(taskToReadTaskDtoMapper::map);
    }

    @Override
    @Transactional
    public Optional<ReadTaskDto> update(UUID id, CreateOrUpdateTaskDto dto) {
        return taskRepository.findById(id)
                .filter(this::hasAccess)
                .map(task -> {
                    Task update = createOrUpdateTaskDtoToTaskMapper.map(dto, task);
                    update.setPerformer(userService.findByEmail(dto.performerEmail())
                            .orElseThrow(() -> new UserNotFoundInDatabaseException(dto.performerEmail())));
                    return update;
                })
                .map(taskRepository::saveAndFlush)
                .map(taskToReadTaskDtoMapper::map);
    }

    @Override
    @Transactional
    public boolean delete(UUID id){
        if(!authService.takeUserFromContext().orElseThrow().isAdmin())
            return false;
        return taskRepository.findById(id)
                .map(task -> {
                    taskRepository.delete(task);
                    taskRepository.flush();
                    return true;
                })
                .orElse(false);
    }

    private boolean hasAccess(Task task){
        SecurityUserDto securityUserDto = authService.takeUserFromContext()
                .orElseThrow();
        return task.getPerformer().getEmail().equals(securityUserDto.email()) || securityUserDto.isAdmin();
    }
}
