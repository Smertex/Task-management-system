package by.smertex.service.interfaces;

import by.smertex.database.entity.realisation.Task;
import by.smertex.dto.filter.TaskFilter;
import by.smertex.dto.read.ReadTaskDto;
import by.smertex.dto.update.CreateOrUpdateTaskDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface TaskService {
    Optional<Task> findById(UUID id);

    Page<ReadTaskDto> findAllByFilter(TaskFilter filter, Pageable pageable);

    Optional<ReadTaskDto> save(CreateOrUpdateTaskDto dto);

    Optional<ReadTaskDto> update(UUID id, CreateOrUpdateTaskDto dto);

    boolean delete(UUID id);
}
