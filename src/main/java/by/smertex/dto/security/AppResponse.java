package by.smertex.dto.security;

import lombok.Data;
import lombok.Value;

import java.util.Date;

@Data
@Value
public class AppResponse {
    int status;

    String message;

    Date timestamp;

    public AppResponse(int status, String message){
        this.status = status;
        this.message = message;
        this.timestamp = new Date();
    }
}
