package com.dperez.CarRegistry.service;

import com.dperez.CarRegistry.repository.entity.UserEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UserService {
    UserEntity save(UserEntity newUser);
    void addUserImage(Integer id, MultipartFile file) throws IOException;
}
