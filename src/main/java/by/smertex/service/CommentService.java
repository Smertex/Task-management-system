package by.smertex.service;

import by.smertex.database.entity.Comment;
import by.smertex.database.repository.CommentRepository;
import by.smertex.dto.filter.CommentFilter;
import by.smertex.dto.read.ReadCommentDto;
import by.smertex.dto.security.SecurityUserDto;
import by.smertex.dto.update.CreateOrUpdateCommentDto;
import by.smertex.mapper.CommentToReadCommentDtoMapper;
import by.smertex.mapper.CreateOrUpdateCommentDtoToCommentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;

    private final CommentToReadCommentDtoMapper commentToReadCommentDtoMapper;

    private final CreateOrUpdateCommentDtoToCommentMapper createOrUpdateCommentDtoToCommentMapper;

    private final AuthService authService;

    private final UserService userService;

    private final TaskService taskService;

    public List<ReadCommentDto> findAllByFilter(UUID taskId, CommentFilter commentFilter, Pageable pageable){
        return commentRepository.findAllByFilter(taskId, commentFilter, authService.takeUserFromContext().orElseThrow(), pageable)
                .stream()
                .map(commentToReadCommentDtoMapper::map)
                .toList();
    }

    @Transactional
    public Optional<ReadCommentDto> add(UUID taskId, CreateOrUpdateCommentDto dto){
        return Optional.of(dto)
                .map(element -> {
                    Comment comment = createOrUpdateCommentDtoToCommentMapper.map(dto);
                    comment.setCreatedBy(userService.findByEmail(authService.takeUserFromContext()
                                    .map(SecurityUserDto::email)
                                    .orElseThrow())
                            .orElseThrow());
                    comment.setTask(taskService.findById(taskId)
                            .orElseThrow());
                    comment.setCreatedAt(LocalDateTime.now());
                    return comment;
                })
                .map(commentRepository::save)
                .map(commentToReadCommentDtoMapper::map);
    }

    @Transactional
    public Optional<ReadCommentDto> update(UUID commentId, CreateOrUpdateCommentDto dto){
        return commentRepository.findById(commentId)
                .map(comment -> createOrUpdateCommentDtoToCommentMapper.map(dto, comment))
                .map(commentRepository::saveAndFlush)
                .map(commentToReadCommentDtoMapper::map);
    }

}
