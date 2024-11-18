package by.smertex.controller.realisation;

import by.smertex.controller.interfaces.AuthController;
import by.smertex.service.interfaces.AuthService;
import by.smertex.dto.security.JwtRequest;
import by.smertex.util.ApiPath;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(ApiPath.AUTH_PATH)
public class AuthControllerImpl implements AuthController {

    private final AuthService authService;

    @PostMapping
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest){
        return authService.authentication(authRequest);
    }
}
