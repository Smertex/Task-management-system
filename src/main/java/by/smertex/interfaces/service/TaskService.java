package by.smertex.interfaces.service;

import by.smertex.realisation.database.entity.Task;
import by.smertex.realisation.dto.filter.TaskFilter;
import by.smertex.realisation.dto.read.ReadTaskDto;
import by.smertex.realisation.dto.update.CreateOrUpdateTaskDto;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskService {
    Optional<Task> findById(UUID id);

    List<ReadTaskDto> findAllByFilter(TaskFilter filter, Pageable pageable);

    Optional<ReadTaskDto> save(CreateOrUpdateTaskDto dto);

    Optional<ReadTaskDto> update(UUID id, CreateOrUpdateTaskDto dto);

    boolean delete(UUID id);
}
