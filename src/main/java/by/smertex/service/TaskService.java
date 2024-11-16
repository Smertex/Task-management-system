package by.smertex.service;

import by.smertex.database.entity.QTask;
import by.smertex.database.entity.Task;
import by.smertex.database.entity.enums.Role;
import by.smertex.database.repository.TaskRepository;
import by.smertex.database.repository.filter.QPredicate;
import by.smertex.database.repository.filter.QPredicateImpl;
import by.smertex.dto.filter.TaskAdminFilter;
import by.smertex.dto.filter.TaskUserFilter;
import by.smertex.dto.filter.UserFilter;
import by.smertex.dto.read.ReadTaskDto;
import by.smertex.dto.update.CreateOrUpdateUserDto;
import by.smertex.mapper.CreateOrUpdateTaskDtoToTaskMapper;
import by.smertex.mapper.TaskToReadTaskDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    private final TaskRepository taskRepository;

    private final TaskToReadTaskDtoMapper taskToReadTaskDtoMapper;

    private final CreateOrUpdateTaskDtoToTaskMapper createOrUpdateTaskDtoToTaskMapper;

    public List<ReadTaskDto> findAllByFilter(TaskAdminFilter filter, Pageable pageable){
        QPredicate qPredicate = QPredicateImpl.builder()
                .add(filter.createdAt(), QTask.task.metaInfo.createdAt::eq)
                .add(filter.closedAt(), QTask.task.metaInfo.closeAt::eq)
                .add(filter.status(), QTask.task.status::eq)
                .add(filter.priority(), QTask.task.priority::eq);
        userFilterToTaskPredicate(qPredicate, filter.createdBy());
        userFilterToTaskPredicate(qPredicate, filter.performer());
        return taskRepository.findAll(qPredicate.buildAnd(), pageable)
                .map(taskToReadTaskDtoMapper::map).toList();
    }

    public List<ReadTaskDto> findAllByTokenAndFilter(TaskUserFilter filter, Pageable pageable){
        QPredicate qPredicate = QPredicateImpl.builder()
                .add(filter.createdAt(), QTask.task.metaInfo.createdAt::eq)
                .add(filter.closedAt(), QTask.task.metaInfo.closeAt::eq)
                .add(filter.status(), QTask.task.status::eq)
                .add(filter.priority(), QTask.task.priority::eq);
        userFilterToTaskPredicate(qPredicate, filter.createdBy());
        return taskRepository.findAll(qPredicate.buildAnd(), pageable)
                .filter(this::hasAccess)
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
                    task.setMetaInfo(metainfoService.save()
                            .orElseThrow());
                    return task;
                })
                .map(taskRepository::save)
                .map(taskToReadTaskDtoMapper::map);
    }

    private boolean hasAccess(Task task){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return task.getPerformer().getEmail().equals(authentication.getPrincipal()) || authentication.getAuthorities().contains(Role.ADMIN);
    }

    private void userFilterToTaskPredicate(QPredicate predicate, UserFilter filter){
        predicate
               .add(filter.email(), QTask.task.metaInfo.createdBy.email::containsIgnoreCase)
               .add(filter.role(), QTask.task.metaInfo.createdBy.role::eq);
    }
}
