package by.smertex.service.interfaces;

import by.smertex.database.entity.realisation.Task;
import by.smertex.dto.filter.TaskFilter;
import by.smertex.dto.read.ReadTaskDto;
import by.smertex.dto.update.CreateOrUpdateTaskDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * Сервис для работы с задачами
 */
public interface TaskService {
    Optional<Task> findById(UUID id);

    /**
     * Передает в репозиторий фильтр и условия пагинации. В случае, если
     * пользователь попытается получить задачи, где он не является исполнителем, будет выведен пустой список.
     * ADMIN игнорирует данное ограничение
     */
    Page<ReadTaskDto> findAllByFilter(TaskFilter filter, Pageable pageable);

    /**
     * Метод для сохранения задачи на основе DTO. При возникновении ошибки, указанной явно, будет откат транзакции
     */
    Optional<ReadTaskDto> save(CreateOrUpdateTaskDto dto);

    /**
     * Метод для обновлении задачи на основе DTO. Обновление способен соврешить либо исполнитель задачи, либо ADMIN
     */
    Optional<ReadTaskDto> update(UUID id, CreateOrUpdateTaskDto dto);

    /**
     * Метод для удаления задачи, который доступен только пользователям с ролью ADMIN
     */
    boolean delete(UUID id);
}
