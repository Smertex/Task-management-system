package by.smertex.realisation.service;

import by.smertex.interfaces.service.AuthService;
import by.smertex.interfaces.service.CommentService;
import by.smertex.interfaces.service.TaskService;
import by.smertex.interfaces.service.UserService;
import by.smertex.realisation.controller.exception.UserNotFoundInDatabaseException;
import by.smertex.realisation.database.entity.Comment;
import by.smertex.realisation.database.repository.CommentRepository;
import by.smertex.realisation.dto.filter.CommentFilter;
import by.smertex.realisation.dto.read.ReadCommentDto;
import by.smertex.realisation.dto.security.SecurityUserDto;
import by.smertex.realisation.dto.update.CreateOrUpdateCommentDto;
import by.smertex.realisation.mapper.CommentToReadCommentDtoMapper;
import by.smertex.realisation.mapper.CreateOrUpdateCommentDtoToCommentMapper;
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
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final CommentToReadCommentDtoMapper commentToReadCommentDtoMapper;

    private final CreateOrUpdateCommentDtoToCommentMapper createOrUpdateCommentDtoToCommentMapper;

    private final AuthService authService;

    private final UserService userService;

    private final TaskService taskService;

    @Override
    public List<ReadCommentDto> findAllByFilter(UUID taskId, CommentFilter commentFilter, Pageable pageable){
        return commentRepository.findAllByFilter(taskId, commentFilter, authService.takeUserFromContext().orElseThrow(), pageable)
                .stream()
                .map(commentToReadCommentDtoMapper::map)
                .toList();
    }

    @Override
    @Transactional
    public Optional<ReadCommentDto> add(UUID taskId, CreateOrUpdateCommentDto dto){
        SecurityUserDto user = authService.takeUserFromContext().orElseThrow();
        return taskService.findById(taskId)
                .filter(task -> user.isAdmin() || task.getPerformer().getEmail().equals(user.email()))
                .map(task -> {
                    Comment comment = createOrUpdateCommentDtoToCommentMapper.map(dto);
                    comment.setCreatedBy(userService.findByEmail(user.email())
                            .orElseThrow(() -> new UserNotFoundInDatabaseException(user.email())));
                    comment.setTask(taskService.findById(taskId)
                            .orElseThrow());
                    comment.setCreatedAt(LocalDateTime.now());
                    return comment;
                })
                .map(commentRepository::save)
                .map(commentToReadCommentDtoMapper::map);
    }

    @Override
    @Transactional
    public Optional<ReadCommentDto> update(UUID commentId, CreateOrUpdateCommentDto dto){
        return commentRepository.findById(commentId)
                .filter(comment -> authService.takeUserFromContext().orElseThrow().email().equals(comment.getCreatedBy().getEmail()))
                .map(comment -> createOrUpdateCommentDtoToCommentMapper.map(dto, comment))
                .map(commentRepository::saveAndFlush)
                .map(commentToReadCommentDtoMapper::map);
    }

}
