package by.smertex.controller.exception;

import by.smertex.util.ResponseMessage;
import lombok.Getter;

@Getter
public class UserNotFoundInDatabaseException extends RuntimeException{

    private final String message;

    public UserNotFoundInDatabaseException(Throwable e, String user){
        super(e);
        this.message = ResponseMessage.USER_NOT_FOUND_EXCEPTION + " " + user;
    }

    public UserNotFoundInDatabaseException(String user){
        super(new RuntimeException());
        this.message = ResponseMessage.USER_NOT_FOUND_EXCEPTION + " " + user;
    }
}
