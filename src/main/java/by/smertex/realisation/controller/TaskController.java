package by.smertex.realisation.controller;

import by.smertex.interfaces.service.CommentService;
import by.smertex.interfaces.service.TaskService;
import by.smertex.realisation.dto.filter.CommentFilter;
import by.smertex.realisation.dto.filter.TaskFilter;
import by.smertex.realisation.dto.read.ReadCommentDto;
import by.smertex.realisation.dto.read.ReadTaskDto;
import by.smertex.realisation.dto.security.AppResponse;
import by.smertex.realisation.dto.update.CreateOrUpdateCommentDto;
import by.smertex.realisation.dto.update.CreateOrUpdateTaskDto;
import by.smertex.util.ApiPath;
import by.smertex.util.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiPath.TASK_PATH)
public class TaskController {

    private final TaskService taskService;

    private final CommentService commentService;

    @GetMapping
    public List<ReadTaskDto> findAll(@RequestBody @Validated TaskFilter filter,
                                     Pageable pageable){
        return taskService.findAllByFilter(filter, pageable);
    }

    @PostMapping
    public ReadTaskDto create(@Validated @RequestBody CreateOrUpdateTaskDto dto){
        return taskService.save(dto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, ResponseMessage.CREATE_TASK_EXCEPTION));
    }

    @PutMapping(ApiPath.ID_TASK_PATH)
    public ReadTaskDto updateTask(@PathVariable UUID id,
                                  @Validated @RequestBody CreateOrUpdateTaskDto dto){
        return taskService.update(id, dto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ResponseMessage.UPDATE_TASK_NOT_FOUND));
    }

    @DeleteMapping(ApiPath.ID_TASK_PATH)
    public ResponseEntity<AppResponse> deleteTask(@PathVariable UUID id){
        return taskService.delete(id) ? ResponseEntity.ok(new AppResponse(HttpStatus.OK.value(), ResponseMessage.DELETE_TASK_SUCCESSFULLY)) :
                ResponseEntity.badRequest().body(new AppResponse(HttpStatus.BAD_REQUEST.value(), ResponseMessage.DELETE_TASK_FAILED));
    }

    @GetMapping(ApiPath.COMMENT_IN_TASK_PATH)
    public List<ReadCommentDto> findAllComment(@PathVariable UUID id,
                                               @Validated @RequestBody CommentFilter filter,
                                               Pageable pageable){
        return commentService.findAllByFilter(id, filter, pageable);
    }

    @PostMapping(ApiPath.COMMENT_IN_TASK_PATH)
    public ReadCommentDto addComment(@PathVariable UUID id,
                                     @Validated @RequestBody CreateOrUpdateCommentDto dto){
        return commentService.add(id, dto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, ResponseMessage.ADD_COMMENT_EXCEPTION));
    }

    @PutMapping(ApiPath.COMMENT_UPDATE_PATH)
    public ReadCommentDto updateComment(@PathVariable UUID id,
                                        @Validated @RequestBody CreateOrUpdateCommentDto dto){
        return commentService.update(id, dto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, ResponseMessage.UPDATE_COMMENT_EXCEPTION));
    }

}
