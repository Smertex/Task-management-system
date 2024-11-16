package by.smertex.repository;

import by.smertex.annotation.IT;
import by.smertex.database.entity.Comment;
import by.smertex.database.entity.QComment;
import by.smertex.database.entity.Task;
import by.smertex.database.entity.User;
import by.smertex.database.repository.CommentRepository;
import by.smertex.database.repository.TaskRepository;
import by.smertex.database.repository.UserRepository;
import by.smertex.database.repository.filter.QPredicateImpl;
import by.smertex.dto.filter.CommentFilter;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@IT
@RequiredArgsConstructor
public class CommentRepositoryIT {

    private final UUID USER_TEST_ID = UUID.fromString("11d1b3a8-0def-4a8a-b00f-b51c43cd14e3");

    private final UUID TASK_TEST_ID = UUID.fromString("5f0288e7-301b-416b-af58-dd433667a607");

    private final CommentRepository commentRepository;

    private final UserRepository userRepository;

    private final TaskRepository taskRepository;

    /**
     * Поиск комментария через создателя
     */
    @Test
    @SuppressWarnings("all")
    void findByUser(){
        Optional<User> optionalUser = userRepository.findById(USER_TEST_ID);

        assertTrue(optionalUser.isPresent());

        User user = optionalUser.get();

        CommentFilter commentFilter = CommentFilter.builder()
                .createdBy(user)
                .build();

        List<Comment> comments = commentRepository.findAll(QPredicateImpl.builder()
                .add(commentFilter.createdBy(), QComment.comment.createdBy::eq)
                .buildAnd(), PageRequest.of(0, 2)).getContent();

        assertThat(comments).hasSize(2);

        comments.forEach(comment -> assertEquals(comment.getCreatedBy(), user));
    }

    /**
     * Поиск комментария через таск
     */
    @Test
    @SuppressWarnings("all")
    void findByTask(){
        Optional<Task> optionalTask = taskRepository.findById(TASK_TEST_ID);

        assertTrue(optionalTask.isPresent());

        Task task = optionalTask.get();

        CommentFilter commentFilter = CommentFilter.builder()
                .from(task)
                .build();

        List<Comment> comments = commentRepository.findAll(QPredicateImpl.builder()
                .add(commentFilter.from(), QComment.comment.task::eq)
                .buildAnd(), PageRequest.of(0, 2)).getContent();

        assertThat(comments).hasSize(2);

        comments.forEach(comment -> assertEquals(comment.getTask(), task));
    }
}
