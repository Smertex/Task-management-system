package by.smertex.service;

import by.smertex.database.entity.Task;
import by.smertex.database.repository.TaskRepository;
import by.smertex.dto.filter.TaskFilter;
import by.smertex.dto.read.ReadTaskDto;
import by.smertex.dto.security.SecurityUserDto;
import by.smertex.dto.update.CreateOrUpdateUserDto;
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

    private final TaskToReadTaskDtoMapper taskToReadTaskDtoMapper;

    private final CreateOrUpdateTaskDtoToTaskMapper createOrUpdateTaskDtoToTaskMapper;

    public List<ReadTaskDto> findAllByFilter(TaskFilter filter, Pageable pageable){
        return taskRepository.findAllByFilter(filter, authService.takeUserFromContext(), pageable).stream()
                .map(taskToReadTaskDtoMapper::map)
                .toList();
    }

    @Transactional
    public Optional<ReadTaskDto> update(UUID id, CreateOrUpdateUserDto dto) {
        return taskRepository.findById(id)
                .filter(this::hasAccess)
                .map(task -> createOrUpdateTaskDtoToTaskMapper.map(dto, task))
                .map(taskRepository::saveAndFlush)
                .map(taskToReadTaskDtoMapper::map);
    }

    @Transactional
    public Optional<ReadTaskDto> save(CreateOrUpdateUserDto dto){
        return Optional.of(dto)
                .map(element -> {
                    Task task = createOrUpdateTaskDtoToTaskMapper.map(element);
                    task.setMetainfo(metainfoService.save()
                            .orElseThrow());
                    return task;
                })
                .map(taskRepository::save)
                .map(taskToReadTaskDtoMapper::map);
    }

    private boolean hasAccess(Task task){
        SecurityUserDto securityUserDto = authService.takeUserFromContext();
        return task.getPerformer().getEmail().equals(securityUserDto.email()) || securityUserDto.isAdmin();
    }
}
