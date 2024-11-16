package by.smertex.service;

import by.smertex.database.repository.CommentRepository;
import by.smertex.dto.filter.CommentFilter;
import by.smertex.dto.read.ReadCommentDto;
import by.smertex.mapper.CommentToReadCommentDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;

    private final CommentToReadCommentDtoMapper commentToReadCommentDtoMapper;

    private final AuthService authService;

    public List<ReadCommentDto> findAllByFilter(UUID taskId, CommentFilter commentFilter, Pageable pageable){
        return commentRepository.findAllByFilter(taskId, commentFilter, authService.takeUserFromContext(), pageable)
                .stream()
                .map(commentToReadCommentDtoMapper::map)
                .toList();
    }

}
