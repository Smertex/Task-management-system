package by.smertex.service.realisation;

import by.smertex.database.repository.interfaces.CommentRepository;
import by.smertex.dto.read.ReadCommentDto;
import by.smertex.dto.security.SecurityUserDto;
import by.smertex.dto.update.CreateOrUpdateCommentDto;
import by.smertex.service.interfaces.AuthService;
import by.smertex.service.interfaces.CommentService;
import by.smertex.service.interfaces.TaskService;
import by.smertex.service.interfaces.UserService;
import by.smertex.mapper.realisation.CommentToReadCommentDtoMapper;
import by.smertex.mapper.realisation.CreateOrUpdateCommentDtoToCommentMapper;
import by.smertex.controller.exception.UserNotFoundInDatabaseException;
import by.smertex.database.entity.realisation.Comment;
import by.smertex.dto.filter.CommentFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
    public Page<ReadCommentDto> findAllByFilter(UUID taskId, CommentFilter commentFilter, Pageable pageable){
        return commentRepository.findAllByFilter(taskId, commentFilter, authService.takeUserFromContext().orElseThrow(), pageable)
                .map(commentToReadCommentDtoMapper::map);
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
