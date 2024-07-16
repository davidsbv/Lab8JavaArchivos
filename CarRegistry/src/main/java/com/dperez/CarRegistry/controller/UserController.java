package com.dperez.CarRegistry.controller;

import com.dperez.CarRegistry.controller.dtos.LoginRequest;
import com.dperez.CarRegistry.controller.dtos.SignUpRequest;
import com.dperez.CarRegistry.service.impl.AuthenticationServiceImpl;
import com.dperez.CarRegistry.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userService;
    private final AuthenticationServiceImpl authenticationServiceImpl;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignUpRequest request) {
        try {
            return ResponseEntity.ok(authenticationServiceImpl.signup(request));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            return ResponseEntity.ok(authenticationServiceImpl.login(request));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping("/image/{id}/add")
    public ResponseEntity<String> uploadImage(@PathVariable Integer id,

                                              @RequestParam(value = "imageFile") MultipartFile imageFile){
       try {
           userService.addUserImage(id, imageFile);
           return ResponseEntity.ok("Image saved successfully");
       }
       catch (RuntimeException e){
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
       }
       catch (Exception e){
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
       }
    }

    @GetMapping(value = "/download-image")
    public ResponseEntity<?> downloadFile() throws IOException {

        MediaType contentType = MediaType.IMAGE_PNG;
        ClassPathResource imageFile = new ClassPathResource("/images/user.png");

        InputStream inputStream = imageFile.getInputStream();

        return ResponseEntity.ok()
                .contentType(contentType)
                .body(new InputStreamResource(inputStream));
    }

}
