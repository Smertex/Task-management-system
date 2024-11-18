package by.smertex.realisation.controller;

import by.smertex.interfaces.service.CommentService;
import by.smertex.interfaces.service.TaskService;
import by.smertex.realisation.dto.exception.ApplicationResponse;
import by.smertex.realisation.dto.filter.CommentFilter;
import by.smertex.realisation.dto.filter.TaskFilter;
import by.smertex.realisation.dto.read.ReadCommentDto;
import by.smertex.realisation.dto.read.ReadTaskDto;
import by.smertex.realisation.dto.update.CreateOrUpdateCommentDto;
import by.smertex.realisation.dto.update.CreateOrUpdateTaskDto;
import by.smertex.util.ApiPath;
import by.smertex.util.ResponseMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Tag(
        name = "Контроллер задач",
        description = "Позволяет взаимодействовать с задачами и их комментариями"
)
@RestController
@RequiredArgsConstructor
@RequestMapping(ApiPath.TASK_PATH)
public class TaskController {

    private final TaskService taskService;

    private final CommentService commentService;

    @Operation(
            summary = "Возвращает список всех задач",
            description = """
                          Поиск происходит по средством фильтрации и пагинации. Если пользователь попытается получить задачи, где он не является исполнителем,
                          то будет получен пустой список. ADMIN игнорирует данное ограничение и может получать любые задачи.
                          """
    )
    @GetMapping
    public List<ReadTaskDto> findAll(@RequestBody @Validated @Parameter(name = "Тело фильтрации") TaskFilter filter,
                                     @Parameter(name = "Тело пагинации") Pageable pageable){
        return taskService.findAllByFilter(filter, pageable);
    }

    @Operation(
            summary = "Создает задачу",
            description = """
                          При вводе неккоректных или пустых данных будет выдана ошибка. Как обычные пользователи, так и администраторы, могут создавать
                          для других пользователей задачи. Создать задачу и делегировать ее на несуществующего пользователя невозможно
                          """
    )
    @PostMapping
    public ReadTaskDto create(@Validated @RequestBody @Parameter(name = "Тело сохранения") CreateOrUpdateTaskDto dto){
        return taskService.save(dto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, ResponseMessage.CREATE_TASK_EXCEPTION));
    }

    @Operation(
            summary = "Обновляет задачу",
            description = """
                          При вводе неккоректных или пустых данных будет выдана ошибка. Как обычные пользователи, так и ADMIN, могут делегировать
                          права на задачу другому пользователю. В случае с обычным пользователем, он теряет возможность взаимодействовать с задачей в тот момент, 
                          как перестает быть ее исполнителем. Делегировать задачу на несуществующего пользователя невозможно
                          """
    )
    @PutMapping(ApiPath.ID_TASK_PATH)
    public ReadTaskDto updateTask(@PathVariable @Parameter(description = "Идентификатор задачи") UUID id,
                                  @Validated @RequestBody @Parameter(name = "Тело обновления") CreateOrUpdateTaskDto dto){
        return taskService.update(id, dto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ResponseMessage.UPDATE_TASK_NOT_FOUND));
    }

    @Operation(
            summary = "Удаляет задачу",
            description = """
                          Метод, доступный только тем, кто авторизован как ADMIN. Позволяет удалять задачи, как следствие комментарии к ним тоже удаляются.
                          При успешном удалении возвращается ответ "Task deleted" с кодом "200". При ошибке "The task was not deleted" с кодом "400"
                          """
    )
    @DeleteMapping(ApiPath.ID_TASK_PATH)
    public ResponseEntity<ApplicationResponse> deleteTask(@PathVariable @Parameter(description = "Идентификатор задачи") UUID id){
        return taskService.delete(id) ? ResponseEntity.ok(new ApplicationResponse(ResponseMessage.DELETE_TASK_SUCCESSFULLY, HttpStatus.OK, LocalDateTime.now())) :
                ResponseEntity.badRequest().body(new ApplicationResponse(ResponseMessage.DELETE_TASK_FAILED, HttpStatus.BAD_REQUEST, LocalDateTime.now()));
    }

    @Operation(
            summary = "Поиск комментариев по задаче",
            description = """
                          Поиск комментариев по id задачи, а также фильтру и пагинации. Если пользователь попытается получить комментарии, где он не является исполнителем,
                          будет возвращен пустой список. ADMIN игнорирует данное ограничнние
                          """
    )
    @GetMapping(ApiPath.COMMENT_IN_TASK_PATH)
    public List<ReadCommentDto> findAllComment(@PathVariable @Parameter(description = "Идентификатор задачи") UUID id,
                                               @Validated @RequestBody @Parameter(name = "Тело фильтрации") CommentFilter filter,
                                               @Parameter(name = "Тело пагинации") Pageable pageable){
        return commentService.findAllByFilter(id, filter, pageable);
    }

    @Operation(
            summary = "Добавление комментария к задаче",
            description = """
                          Обычные пользователи могут добавлять комментарии только к тем задачам, в которых являются исполнителями. ADMIN игнорирует данное ограничение
                          """
    )
    @PostMapping(ApiPath.COMMENT_IN_TASK_PATH)
    public ReadCommentDto addComment(@PathVariable @Parameter(description = "Идентификатор задачи") UUID id,
                                     @Validated @RequestBody @Parameter(name = "Тело сохранения") CreateOrUpdateCommentDto dto){
        return commentService.add(id, dto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, ResponseMessage.ADD_COMMENT_EXCEPTION));
    }

    @Operation(
            summary = "Обновление комментария",
            description = """
                          Обновление содержание комментария, доступно только тому пользователю, что сам оставил комментарий. Данное ограничение накладывается и на ADMIN
                          """
    )
    @PutMapping(ApiPath.COMMENT_UPDATE_PATH)
    public ReadCommentDto updateComment(@PathVariable @Parameter(description = "Идентификатор комментария") UUID id,
                                        @Validated @RequestBody @Parameter(name = "Тело обновления") CreateOrUpdateCommentDto dto){
        return commentService.update(id, dto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, ResponseMessage.UPDATE_COMMENT_EXCEPTION));
    }

}
