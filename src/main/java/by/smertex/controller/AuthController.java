package by.smertex.controller;

import by.smertex.dto.JwtRequest;
import by.smertex.service.AuthService;
import by.smertex.util.ApiPath;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiPath.AUTH_PATH)
public class AuthController {

    private final AuthService authService;

    @PostMapping
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest){
        return authService.authentication(authRequest);
    }
}
