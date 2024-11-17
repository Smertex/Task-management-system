package by.smertex.realisation.controller.exception;

import by.smertex.realisation.dto.exception.ResponseException;
import jakarta.annotation.Nullable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@RestControllerAdvice(basePackages = "by.smertex.realisation.controller")
public class ExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  @Nullable HttpHeaders headers,
                                                                  @Nullable HttpStatusCode status,
                                                                  @Nullable WebRequest request) {
        return ResponseEntity.badRequest()
                .body(new ResponseException(ex.getMessage(), status, LocalDateTime.now()));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ResponseException> repositoryUniqueException(DataIntegrityViolationException exception){
        return ResponseEntity.badRequest()
                .body(new ResponseException(exception.getMessage(), HttpStatus.CONFLICT, LocalDateTime.now()));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ResponseException> incorrectData(NoSuchElementException exception){
        return ResponseEntity.badRequest()
                .body(new ResponseException(exception.getMessage(), HttpStatus.BAD_REQUEST, LocalDateTime.now()));
    }
}
