package by.smertex.service;

import by.smertex.database.entity.Comment;
import by.smertex.database.entity.QComment;
import by.smertex.database.entity.enums.Role;
import by.smertex.database.repository.CommentRepository;
import by.smertex.database.repository.filter.QPredicateImpl;
import by.smertex.dto.filter.CommentFilter;
import by.smertex.dto.read.ReadCommentDto;
import by.smertex.mapper.CommentToReadCommentDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    public List<ReadCommentDto> findAllByFilter(UUID taskId, CommentFilter commentFilter, Pageable pageable){
        return commentRepository.findAll(QPredicateImpl.builder()
                        .add(commentFilter.createdBy().email(), QComment.comment.createdBy.email::containsIgnoreCase)
                        .add(commentFilter.createdBy().role(), QComment.comment.createdBy.role::eq)
                        .add(taskId, QComment.comment.task.id::eq)
                .buildAnd(), pageable)
                .filter(this::hasAccess)
                .map(commentToReadCommentDtoMapper::map)
                .toList();
    }

    private boolean hasAccess(Comment comment){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return comment.getTask().getPerformer().getEmail().equals(authentication.getPrincipal()) || authentication.getAuthorities().contains(Role.ADMIN);
    }

}
