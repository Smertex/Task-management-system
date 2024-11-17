package by.smertex.service;

import by.smertex.annotation.IT;
import by.smertex.database.entity.Task;
import by.smertex.database.entity.enums.Priority;
import by.smertex.database.entity.enums.Role;
import by.smertex.database.entity.enums.Status;
import by.smertex.dto.filter.TaskFilter;
import by.smertex.dto.filter.UserFilter;
import by.smertex.dto.read.ReadTaskDto;
import by.smertex.dto.security.SecurityUserDto;
import by.smertex.dto.update.CreateOrUpdateTaskDto;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@IT
@RequiredArgsConstructor
@ExtendWith(MockitoExtension.class)
public class TaskServiceIT {

    private static final String USER_EMAIL_TEST = "evgenii@gmail.com";

    private static final String ADMIN_EMAIL_TEST = "smertexx@gmail.com";

    private static final UUID TASK_ID_WHERE_PERFORMER_USER_TEST = UUID.fromString("a9099b32-e5b2-41aa-9ab6-d4d461549c70");

    private static final UUID TASK_ID_WHERE_PERFORMER_ADMIN_TEST = UUID.fromString("5f0288e7-301b-416b-af58-dd433667a607");

    private static final Integer PAGE_NUMBER = 0;

    private static final Integer PAGE_SIZE = 2;

    @InjectMocks
    private final TaskService taskService;

    @MockBean
    private final AuthService authService;

    @Test
    void findByFilterWhereUserPerformer(){
        Mockito.doReturn(Optional.of(new SecurityUserDto(USER_EMAIL_TEST, false)))
                .when(authService)
                .takeUserFromContext();
        TaskFilter filter = TaskFilter.builder()
                .createdBy(new UserFilter(null, null))
                .performer(new UserFilter(null, null))
                .build();
        Pageable pageable = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);
        List<ReadTaskDto> tasks = taskService.findAllByFilter(filter, pageable);

        assertFalse(tasks.isEmpty());
        tasks.forEach(task -> assertEquals(task.performer().email(), USER_EMAIL_TEST));
    }

    @Test
    void findByFilterWhereUserNotPerformer(){
        Mockito.doReturn(Optional.of(new SecurityUserDto(USER_EMAIL_TEST, false)))
                .when(authService)
                .takeUserFromContext();
        TaskFilter filter = TaskFilter.builder()
                .createdBy(new UserFilter(null, null))
                .performer(new UserFilter(ADMIN_EMAIL_TEST, null))
                .build();
        Pageable pageable = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);
        List<ReadTaskDto> tasks = taskService.findAllByFilter(filter, pageable);
        assertTrue(tasks.isEmpty());

        Mockito.doReturn(Optional.of(new SecurityUserDto(ADMIN_EMAIL_TEST, false)))
                .when(authService)
                .takeUserFromContext();
        List<ReadTaskDto> tasksWhereAdminPerformer = taskService.findAllByFilter(filter, pageable);
        assertFalse(tasksWhereAdminPerformer.isEmpty());
        tasksWhereAdminPerformer
                .forEach(task -> assertNotEquals(task.performer().email(), USER_EMAIL_TEST));
    }

    @Test
    void findByFilterWhereAdminNotPerformer(){
        Mockito.doReturn(Optional.of(new SecurityUserDto(ADMIN_EMAIL_TEST, true)))
                .when(authService)
                .takeUserFromContext();
        TaskFilter filter = TaskFilter.builder()
                .createdBy(new UserFilter(null, null))
                .performer(new UserFilter(null, Role.USER))
                .build();
        Pageable pageable = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);

        List<ReadTaskDto> tasks = taskService.findAllByFilter(filter, pageable);

        assertFalse(tasks.isEmpty());
        tasks.forEach(task -> assertNotEquals(task.performer().email(), ADMIN_EMAIL_TEST));
    }

    @Test
    void saveTestForNotExistencePerformer(){
        Mockito.doReturn(Optional.of(new SecurityUserDto(USER_EMAIL_TEST, false)))
                .when(authService)
                .takeUserFromContext();
        CreateOrUpdateTaskDto createOrUpdateTaskDto = CreateOrUpdateTaskDto.builder()
                .status(Status.IN_PROGRESS)
                .priority(Priority.HIGHEST)
                .description("Test save")
                .name("Test save task")
                .performerEmail(USER_EMAIL_TEST + ".")
                .build();
        assertThrows(NoSuchElementException.class,
                () -> taskService.save(createOrUpdateTaskDto));
    }

    @Test
    void saveForExistencePerformer(){
        Mockito.doReturn(Optional.of(new SecurityUserDto(USER_EMAIL_TEST, false)))
                .when(authService)
                .takeUserFromContext();
        CreateOrUpdateTaskDto createOrUpdateTaskDto = CreateOrUpdateTaskDto.builder()
                .status(Status.IN_PROGRESS)
                .priority(Priority.HIGHEST)
                .description("Test save")
                .name("Test save task")
                .performerEmail(USER_EMAIL_TEST)
                .build();

        Optional<ReadTaskDto> readTaskDto = taskService.save(createOrUpdateTaskDto);
        assertTrue(readTaskDto.isPresent());
        readTaskDto.ifPresent(task -> assertEquals(task.performer().email(), USER_EMAIL_TEST));
        readTaskDto.ifPresent(task -> assertEquals(task.status(), Status.IN_PROGRESS));
        readTaskDto.ifPresent(task -> assertEquals(task.priority(), Priority.HIGHEST));
    }

    @Test
    void updateWhereUserPerformer(){
        String testDescription = "updateWhereUserPerformer";
        Mockito.doReturn(Optional.of(new SecurityUserDto(USER_EMAIL_TEST, false)))
                .when(authService)
                .takeUserFromContext();
        CreateOrUpdateTaskDto createOrUpdateTaskDto = CreateOrUpdateTaskDto.builder()
                .status(Status.IN_PROGRESS)
                .priority(Priority.HIGHEST)
                .description(testDescription)
                .name("Test save task")
                .performerEmail(USER_EMAIL_TEST)
                .build();
        Optional<ReadTaskDto> readTaskDto = taskService.update(TASK_ID_WHERE_PERFORMER_USER_TEST, createOrUpdateTaskDto);
        assertTrue(readTaskDto.isPresent());
        readTaskDto.ifPresent(task -> assertEquals(task.status(), Status.IN_PROGRESS));
        readTaskDto.ifPresent(task -> assertEquals(task.priority(), Priority.HIGHEST));
        readTaskDto.ifPresent(task -> assertEquals(task.description(), testDescription));
    }

    @Test
    void updateWhereUserNotPerformer(){
        Mockito.doReturn(Optional.of(new SecurityUserDto(USER_EMAIL_TEST, false)))
                .when(authService)
                .takeUserFromContext();
        String nameTask = "57fd59ce-39d7-4ec9-82d4-20b54ed5dd1e";
        String testDescription = "updateWhereUserPerformer";

        TaskFilter taskFilter = TaskFilter.builder()
                .performer(new UserFilter(null, null))
                .createdBy(new UserFilter(null, null))
                .name(nameTask)
                .build();

        Pageable pageable = PageRequest.of(PAGE_NUMBER, 1);

        List<ReadTaskDto> readTaskDto = taskService.findAllByFilter(taskFilter, pageable);
        assertTrue(readTaskDto.isEmpty());

        CreateOrUpdateTaskDto createOrUpdateTaskDto = CreateOrUpdateTaskDto.builder()
                .status(Status.IN_PROGRESS)
                .priority(Priority.HIGHEST)
                .description(testDescription)
                .name(nameTask)
                .performerEmail(USER_EMAIL_TEST)
                .build();
        Optional<ReadTaskDto> readTaskDtoAfterUpdate = taskService.update(TASK_ID_WHERE_PERFORMER_ADMIN_TEST, createOrUpdateTaskDto);
        assertTrue(readTaskDtoAfterUpdate.isEmpty());

        Mockito.doReturn(Optional.of(new SecurityUserDto(ADMIN_EMAIL_TEST, false)))
                .when(authService)
                .takeUserFromContext();

        Optional<ReadTaskDto> readTaskDtoWhereUserPerformerMakingUpdate = taskService.update(TASK_ID_WHERE_PERFORMER_ADMIN_TEST, createOrUpdateTaskDto);

        readTaskDtoWhereUserPerformerMakingUpdate.ifPresent(task -> assertEquals(task.performer().email(), USER_EMAIL_TEST));
        readTaskDtoWhereUserPerformerMakingUpdate.ifPresent(task -> assertEquals(task.status(), Status.IN_PROGRESS));
        readTaskDtoWhereUserPerformerMakingUpdate.ifPresent(task -> assertEquals(task.priority(), Priority.HIGHEST));
        readTaskDtoWhereUserPerformerMakingUpdate.ifPresent(task -> assertEquals(task.description(), testDescription));

        Mockito.doReturn(Optional.of(new SecurityUserDto(USER_EMAIL_TEST, false)))
                .when(authService)
                .takeUserFromContext();

        readTaskDto = taskService.findAllByFilter(taskFilter, pageable);
        assertFalse(readTaskDto.isEmpty());
        assertEquals(readTaskDto.getFirst().performer().email(), USER_EMAIL_TEST);
    }

    @Test
    void updateWhereAdminNotPerformer(){
        Mockito.doReturn(Optional.of(new SecurityUserDto(ADMIN_EMAIL_TEST, true)))
                .when(authService)
                .takeUserFromContext();

        Optional<Task> task = taskService.findById(TASK_ID_WHERE_PERFORMER_USER_TEST);
        assertTrue(task.isPresent());
        assertEquals(task.orElseThrow().getPerformer().getEmail(), USER_EMAIL_TEST);

        String nameTask = "57fd59ce-39d7-4ec9-82d4-20b54ed5dd1e";
        String testDescription = "updateWhereUserPerformer";

        CreateOrUpdateTaskDto createOrUpdateTaskDto = CreateOrUpdateTaskDto.builder()
                .status(Status.IN_PROGRESS)
                .priority(Priority.HIGHEST)
                .description(testDescription)
                .name(nameTask)
                .performerEmail(ADMIN_EMAIL_TEST)
                .build();

        Optional<ReadTaskDto> readTaskDto = taskService.update(TASK_ID_WHERE_PERFORMER_USER_TEST, createOrUpdateTaskDto);
        readTaskDto.ifPresent(dto -> assertEquals(dto.performer().email(), ADMIN_EMAIL_TEST));

        Mockito.doReturn(Optional.of(new SecurityUserDto(USER_EMAIL_TEST, false)))
                .when(authService)
                .takeUserFromContext();
        TaskFilter taskFilter = TaskFilter.builder()
                .performer(new UserFilter(null, null))
                .createdBy(new UserFilter(null, null))
                .name(nameTask)
                .build();
        Pageable pageable = PageRequest.of(PAGE_NUMBER, 1);
        List<ReadTaskDto> readTaskDtoList = taskService.findAllByFilter(taskFilter, pageable);
        assertTrue(readTaskDtoList.isEmpty());
    }

    @Test
    void deleteTryByUser(){
        Mockito.doReturn(Optional.of(new SecurityUserDto(ADMIN_EMAIL_TEST, false)))
                .when(authService)
                .takeUserFromContext();
        assertFalse(taskService.delete(TASK_ID_WHERE_PERFORMER_USER_TEST));
    }

    @Test
    void deleteTryByPerformer(){
        Mockito.doReturn(Optional.of(new SecurityUserDto(USER_EMAIL_TEST, false)))
                .when(authService)
                .takeUserFromContext();
        assertFalse(taskService.delete(TASK_ID_WHERE_PERFORMER_USER_TEST));
    }

    @Test
    void deleteTryByAdmin(){
        Mockito.doReturn(Optional.of(new SecurityUserDto(ADMIN_EMAIL_TEST, true)))
                .when(authService)
                .takeUserFromContext();
        assertTrue(taskService.delete(TASK_ID_WHERE_PERFORMER_USER_TEST));
    }
}
