package com.dperez.CarRegistry.service.impl;

import com.dperez.CarRegistry.repository.UserRepository;
import com.dperez.CarRegistry.repository.entity.UserEntity;
import com.dperez.CarRegistry.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;

    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                return userRepository.findByMail(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User " + username + " not found"));
            }
        };
    }

    @Override
    public UserEntity save(UserEntity newUser) {
        return userRepository.save(newUser);
    }

    @Override
    public void addUserImage(Integer id, MultipartFile file) throws IOException {


        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found. Unknown Id: " + id));

        log.info("Saving user image {}", file.getName());
        userEntity.setImage(Base64.getEncoder().encodeToString(file.getBytes()));
        userRepository.save(userEntity);
    }

    @Override
    public byte[] getUserImage(Integer id) throws IOException {

        UserEntity userEntity = userRepository
                .findById(id).orElseThrow(() -> new RuntimeException("User not found. Unknown Id: " + id));

        return Base64.getDecoder().decode(userEntity.getImage());
    }


}
