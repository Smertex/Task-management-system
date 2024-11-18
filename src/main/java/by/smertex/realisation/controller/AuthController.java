package by.smertex.realisation.controller;

import by.smertex.interfaces.service.AuthService;
import by.smertex.realisation.dto.security.JwtRequest;
import by.smertex.util.ApiPath;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "Контроллер аутентификации",
        description = "Возвращает JWT-токен, если в базе данных присутсвует пользователь, который проходит аутентификацию"
)
@RestController
@RequiredArgsConstructor
@RequestMapping(ApiPath.AUTH_PATH)
public class AuthController {

    private final AuthService authService;

    @Operation(
            summary = "Возвращает токен"
    )
    @PostMapping
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest){
        return authService.authentication(authRequest);
    }
}
