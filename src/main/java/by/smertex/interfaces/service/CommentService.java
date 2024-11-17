package by.smertex.interfaces.service;

import by.smertex.realisation.dto.filter.CommentFilter;
import by.smertex.realisation.dto.read.ReadCommentDto;
import by.smertex.realisation.dto.update.CreateOrUpdateCommentDto;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CommentService {
    List<ReadCommentDto> findAllByFilter(UUID taskId, CommentFilter commentFilter, Pageable pageable);

    Optional<ReadCommentDto> add(UUID taskId, CreateOrUpdateCommentDto dto);

    Optional<ReadCommentDto> update(UUID commentId, CreateOrUpdateCommentDto dto);
}
