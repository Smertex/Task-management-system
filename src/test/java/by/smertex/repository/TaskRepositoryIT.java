package by.smertex.repository;

import by.smertex.annotation.IT;
import by.smertex.database.entity.Task;
import by.smertex.database.entity.enums.Priority;
import by.smertex.database.entity.enums.Status;
import by.smertex.database.repository.TaskRepository;
import by.smertex.dto.filter.TaskFilter;
import by.smertex.dto.filter.UserFilter;
import by.smertex.dto.security.SecurityUserDto;
import by.smertex.service.AuthService;
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

@IT
@RequiredArgsConstructor
@ExtendWith(MockitoExtension.class)
public class TaskRepositoryIT {

    private static final String USER_TEST_EMAIL = "evgenii@gmail.com";

    private static final String ADMIN_TEST_EMAIL = "smertexx@gmail.com";

    private static final UUID TEST_TASK_ID = UUID.fromString("a9099b32-e5b2-41aa-9ab6-d4d461549c70");

    private static final Integer PAGE_NUMBER = 0;

    private static final Integer PAGE_SIZE = 2;

    private final TaskRepository taskRepository;

    @Mock
    private final AuthService authService;


    @Test
    void findAllByFilterUser(){
        Mockito.doReturn(Optional.of(new SecurityUserDto(USER_TEST_EMAIL, false)))
                .when(authService)
                .takeUserFromContext();
        TaskFilter filter = TaskFilter.builder()
                .createdBy(new UserFilter(null, null))
                .status(Status.WAITING)
                .performer(new UserFilter(null, null))
                .priority(Priority.LOWEST)
                .build();
        Pageable pageable = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);

        List<Task> tasks = taskRepository.findAllByFilter(filter, authService.takeUserFromContext().orElseThrow(), pageable);

        assert tasks.size() <= PAGE_SIZE;

        tasks.stream()
                .peek(task -> assertEquals(task.getPerformer().getEmail(), USER_TEST_EMAIL))
                .peek(task -> assertEquals(task.getStatus(), filter.status()))
                .forEach(task -> assertEquals(task.getPriority(), filter.priority()));

    }

    @Test
    void findAllByFilterAdmin(){
        Mockito.doReturn(Optional.of(new SecurityUserDto(USER_TEST_EMAIL, false)))
                .when(authService)
                .takeUserFromContext();

        System.out.println(authService.takeUserFromContext().orElseThrow());

    }
}
