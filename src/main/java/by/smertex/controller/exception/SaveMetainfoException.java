package by.smertex.controller.exception;

import lombok.Getter;

@Getter
public class SaveMetainfoException extends RuntimeException{

    private final String message;

    public SaveMetainfoException(String message){
        super(new RuntimeException());
        this.message = message;
    }

}
