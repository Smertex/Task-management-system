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

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiPath.TASK_PATH)
public class TaskController {

    private final TaskService taskService;

    private final CommentService commentService;

    @Operation(
            summary = "Возвращает список всех пользователей по фильтрации"
    )
    @GetMapping
    public List<ReadTaskDto> findAll(@RequestBody @Validated TaskFilter filter,
                                     Pageable pageable){
        return taskService.findAllByFilter(filter, pageable);
    }

    @Operation(
            summary = "Создает задачу"
    )
    @PostMapping
    public ReadTaskDto create(@Validated @RequestBody CreateOrUpdateTaskDto dto){
        return taskService.save(dto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, ResponseMessage.CREATE_TASK_EXCEPTION));
    }

    @Operation(
            summary = "Обновляет задачу"
    )
    @PutMapping(ApiPath.ID_TASK_PATH)
    public ReadTaskDto updateTask(@PathVariable @Parameter(description = "Идентификатор задачи") UUID id,
                                  @Validated @RequestBody CreateOrUpdateTaskDto dto){
        return taskService.update(id, dto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ResponseMessage.UPDATE_TASK_NOT_FOUND));
    }

    @Operation(
            summary = "Удаляет задачу"
    )
    @DeleteMapping(ApiPath.ID_TASK_PATH)
    public ResponseEntity<ApplicationResponse> deleteTask(@PathVariable @Parameter(description = "Идентификатор задачи") UUID id){
        return taskService.delete(id) ? ResponseEntity.ok(new ApplicationResponse(ResponseMessage.DELETE_TASK_SUCCESSFULLY, HttpStatus.OK, LocalDateTime.now())) :
                ResponseEntity.badRequest().body(new ApplicationResponse(ResponseMessage.DELETE_TASK_FAILED, HttpStatus.BAD_REQUEST, LocalDateTime.now()));
    }

    @Operation(
            summary = "Поиск комментариев по задаче"
    )
    @GetMapping(ApiPath.COMMENT_IN_TASK_PATH)
    public List<ReadCommentDto> findAllComment(@PathVariable @Parameter(description = "Идентификатор задачи") UUID id,
                                               @Validated @RequestBody CommentFilter filter,
                                               Pageable pageable){
        return commentService.findAllByFilter(id, filter, pageable);
    }

    @Operation(
            summary = "Добавление комментария к задаче"
    )
    @PostMapping(ApiPath.COMMENT_IN_TASK_PATH)
    public ReadCommentDto addComment(@PathVariable @Parameter(description = "Идентификатор задачи") UUID id,
                                     @Validated @RequestBody CreateOrUpdateCommentDto dto){
        return commentService.add(id, dto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, ResponseMessage.ADD_COMMENT_EXCEPTION));
    }

    @Operation(
            summary = "Обновление комментария"
    )
    @PutMapping(ApiPath.COMMENT_UPDATE_PATH)
    public ReadCommentDto updateComment(@PathVariable @Parameter(description = "Идентификатор комментария") UUID id,
                                        @Validated @RequestBody CreateOrUpdateCommentDto dto){
        return commentService.update(id, dto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, ResponseMessage.UPDATE_COMMENT_EXCEPTION));
    }

}
