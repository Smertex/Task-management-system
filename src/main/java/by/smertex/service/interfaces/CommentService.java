package by.smertex.service.interfaces;

import by.smertex.dto.filter.CommentFilter;
import by.smertex.dto.read.ReadCommentDto;
import by.smertex.dto.update.CreateOrUpdateCommentDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * Сервис для работы с комментариями
 */
public interface CommentService {

    /**
     * Передает в репозиторий фильтр, условия пагинации. В случае, если
     * пользователь попытается получить комментарии из задачи, где он не является исполнителем, будет выведен пустой список.
     * ADMIN игнорирует данное ограничение
     */
    Page<ReadCommentDto> findAllByFilter(UUID taskId, CommentFilter commentFilter, Pageable pageable);

    /**
     * Сохранение комментария под существующей задачей. Пользователь может добавить комментарий только в том случае, если задача существует и он
     * является исполнителем. Условие с исполнителем ADMIN может игнорировать
     */
    Optional<ReadCommentDto> add(UUID taskId, CreateOrUpdateCommentDto dto);

    /**
     * Обновление комментария. Данная опция доступна только создателю комментария. ADMIN, включительно, не может проигнорировать данное ограничение
     */
    Optional<ReadCommentDto> update(UUID commentId, CreateOrUpdateCommentDto dto);
}
