package by.smertex.service;

import by.smertex.database.entity.Task;
import by.smertex.database.repository.TaskRepository;
import by.smertex.dto.filter.TaskFilter;
import by.smertex.dto.read.ReadTaskDto;
import by.smertex.dto.security.SecurityUserDto;
import by.smertex.dto.update.CreateOrUpdateTaskDto;
import by.smertex.mapper.CreateOrUpdateTaskDtoToTaskMapper;
import by.smertex.mapper.TaskToReadTaskDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaskService {

    private final MetainfoService metainfoService;

    private final AuthService authService;

    private final TaskRepository taskRepository;

    private final UserService userService;

    private final TaskToReadTaskDtoMapper taskToReadTaskDtoMapper;

    private final CreateOrUpdateTaskDtoToTaskMapper createOrUpdateTaskDtoToTaskMapper;

    public Optional<Task> findById(UUID id){
        return taskRepository.findById(id);
    }

    public List<ReadTaskDto> findAllByFilter(TaskFilter filter, Pageable pageable){
        return taskRepository.findAllByFilter(filter, authService.takeUserFromContext().orElseThrow(), pageable).stream()
                .map(taskToReadTaskDtoMapper::map)
                .toList();
    }

    @Transactional
    public Optional<ReadTaskDto> save(CreateOrUpdateTaskDto dto){
        return Optional.of(dto)
                .map(element -> {
                    Task task = createOrUpdateTaskDtoToTaskMapper.map(element);
                    task.setMetainfo(metainfoService.save()
                            .orElseThrow());
                    task.setPerformer(userService.findByEmail(dto.performerEmail())
                            .orElseThrow());
                    return task;
                })
                .map(taskRepository::save)
                .map(taskToReadTaskDtoMapper::map);
    }

    @Transactional
    public Optional<ReadTaskDto> update(UUID id, CreateOrUpdateTaskDto dto) {
        return taskRepository.findById(id)
                .filter(this::hasAccess)
                .map(task -> {
                    Task update = createOrUpdateTaskDtoToTaskMapper.map(dto, task);
                    update.setPerformer(userService.findByEmail(dto.performerEmail())
                            .orElseThrow());
                    return update;
                })
                .map(taskRepository::saveAndFlush)
                .map(taskToReadTaskDtoMapper::map);
    }

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
