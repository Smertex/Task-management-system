package by.smertex.service.interfaces;

import by.smertex.dto.filter.CommentFilter;
import by.smertex.dto.read.ReadCommentDto;
import by.smertex.dto.update.CreateOrUpdateCommentDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface CommentService {
    Page<ReadCommentDto> findAllByFilter(UUID taskId, CommentFilter commentFilter, Pageable pageable);

    Optional<ReadCommentDto> add(UUID taskId, CreateOrUpdateCommentDto dto);

    Optional<ReadCommentDto> update(UUID commentId, CreateOrUpdateCommentDto dto);
}
