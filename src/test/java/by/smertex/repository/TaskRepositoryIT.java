package by.smertex.repository;

import by.smertex.annotation.IT;
import by.smertex.database.entity.realisation.Task;
import by.smertex.database.entity.realisation.enums.Priority;
import by.smertex.database.entity.realisation.enums.Role;
import by.smertex.database.entity.realisation.enums.Status;
import by.smertex.database.repository.interfaces.TaskRepository;
import by.smertex.dto.filter.TaskFilter;
import by.smertex.dto.filter.UserFilter;
import by.smertex.dto.security.SecurityUserDto;
import by.smertex.service.realisation.AuthServiceImpl;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@IT
@RequiredArgsConstructor
@ExtendWith(MockitoExtension.class)
public class TaskRepositoryIT {

    private static final String USER_EMAIL_TEST = "evgenii@gmail.com";

    private static final String ADMIN_EMAIL_TEST = "smertexx@gmail.com";

    private static final Integer PAGE_NUMBER = 0;

    private static final Integer PAGE_SIZE = 5;

    private final TaskRepository taskRepository;

    @Mock
    private final AuthServiceImpl authServiceImpl;

    /**
     * Проверка фильтрации у пользователя, а также ограничения на получения заданий по исполнителю
     */
    @Test
    void findAllByFilterUser(){
        Mockito.doReturn(Optional.of(new SecurityUserDto(USER_EMAIL_TEST, false)))
                .when(authServiceImpl)
                .takeUserFromContext();
        TaskFilter filter = TaskFilter.builder()
                .createdBy(new UserFilter(null, null))
                .status(Status.WAITING)
                .performer(new UserFilter(null, null))
                .priority(Priority.LOWEST)
                .build();
        Pageable pageable = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);

        Page<Task> tasks = taskRepository.findAllByFilter(filter, authServiceImpl.takeUserFromContext().orElseThrow(), pageable);

        tasks.stream()
                .peek(task -> assertEquals(task.getPerformer().getEmail(), USER_EMAIL_TEST))
                .peek(task -> assertEquals(task.getStatus(), filter.status()))
                .forEach(task -> assertEquals(task.getPriority(), filter.priority()));

    }

    /**
     * Проверка фильтрации у администратора, а также ограничения на получения заданий по исполнителю
     */
    @Test
    void findAllByFilterAdmin(){
        Mockito.doReturn(Optional.of(new SecurityUserDto(ADMIN_EMAIL_TEST, true)))
                .when(authServiceImpl)
                .takeUserFromContext();

        TaskFilter filter = TaskFilter.builder()
                .createdBy(new UserFilter(null, null))
                .status(Status.WAITING)
                .performer(new UserFilter(null, Role.USER))
                .build();

        Pageable pageable = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);

        Page<Task> tasks = taskRepository.findAllByFilter(filter, authServiceImpl.takeUserFromContext().orElseThrow(), pageable);

        assertFalse(tasks.isEmpty());

        tasks.stream()
                .peek(task -> assertNotEquals(task.getPerformer().getEmail(), ADMIN_EMAIL_TEST))
                .peek(task -> assertEquals(task.getPerformer().getRole(), Role.USER))
                .forEach(task -> assertEquals(task.getStatus(), Status.WAITING));
    }
}
