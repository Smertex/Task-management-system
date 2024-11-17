package by.smertex.repository;

import by.smertex.annotation.IT;
import by.smertex.realisation.database.entity.Comment;
import by.smertex.realisation.database.entity.Task;
import by.smertex.realisation.database.repository.CommentRepository;
import by.smertex.realisation.database.repository.TaskRepository;
import by.smertex.realisation.dto.filter.CommentFilter;
import by.smertex.realisation.dto.filter.UserFilter;
import by.smertex.realisation.dto.security.SecurityUserDto;
import by.smertex.realisation.service.AuthServiceImpl;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@IT
@RequiredArgsConstructor
@ExtendWith(MockitoExtension.class)
public class CommentRepositoryIT {

    private static final String USER_EMAIL_TEST = "evgenii@gmail.com";

    private static final String ADMIN_EMAIL_TEST = "smertexx@gmail.com";

    private static final UUID TASK_ID_TEST = UUID.fromString("a9099b32-e5b2-41aa-9ab6-d4d461549c70");

    private static final Integer PAGE_NUMBER = 0;

    private static final Integer PAGE_SIZE = 2;

    private final CommentRepository commentRepository;

    private final TaskRepository taskRepository;

    @Mock
    private final AuthServiceImpl authServiceImpl;

    /**
     * Проверка фильтрации и пагинации у администратора,а
     * также ограничения на получения комментариев из заданий, где он не является исполнителем
     */
    @Test
    void findAllByFilterForAdmin(){
        Mockito.doReturn(Optional.of(new SecurityUserDto(ADMIN_EMAIL_TEST, true)))
                .when(authServiceImpl)
                .takeUserFromContext();

        CommentFilter filter = CommentFilter.builder()
                .createdBy(UserFilter.builder()
                        .email(USER_EMAIL_TEST)
                        .build())
                .build();

        Pageable pageable = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);

        List<Comment> comments = commentRepository.findAllByFilter(TASK_ID_TEST, filter, authServiceImpl.takeUserFromContext().orElseThrow(), pageable);

        assert comments.size() <= PAGE_SIZE;

        Task task = taskRepository.findById(TASK_ID_TEST)
                .orElseThrow();

        comments.stream()
                .peek(comment -> assertNotEquals(task.getPerformer().getEmail(), authServiceImpl.takeUserFromContext()
                        .orElseThrow().email()))
                .forEach(comment -> assertEquals(filter.createdBy().email(), comment.getCreatedBy().getEmail()));
    }

    /**
     * Проверка фильтрации и пагинации у пользователя,а
     * также ограничения на получения комментариев из заданий, где он не является исполнителем
     */
    @Test
    void findAllByFilterForUser(){
        Mockito.doReturn(Optional.of(new SecurityUserDto(USER_EMAIL_TEST, false)))
                .when(authServiceImpl)
                .takeUserFromContext();

        CommentFilter filter = CommentFilter.builder()
                .createdBy(UserFilter.builder()
                        .email(USER_EMAIL_TEST)
                        .build())
                .build();

        Pageable pageable = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);

        List<Comment> comments = commentRepository.findAllByFilter(TASK_ID_TEST, filter, authServiceImpl.takeUserFromContext().orElseThrow(), pageable);
        Task task = taskRepository.findById(TASK_ID_TEST)
                .orElseThrow();

        assert comments.size() <= PAGE_SIZE;

        comments.stream()
                .peek(comment -> assertEquals(task.getPerformer().getEmail(), authServiceImpl.takeUserFromContext()
                        .orElseThrow().email()))
                .forEach(comment -> assertEquals(filter.createdBy().email(), comment.getCreatedBy().getEmail()));
    }


}
