package by.smertex.controller;

import by.smertex.util.ApiPath;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiPath.ADMIN_PATH)
public class AdminController {
    @GetMapping
    public ResponseEntity<?> get(){
        return ResponseEntity.ok("admin");
    }
}
