package com.javarush.khasanov.service;

import com.javarush.khasanov.entity.User;
import com.javarush.khasanov.exception.ProjectException;
import com.javarush.khasanov.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

import static com.javarush.khasanov.config.Config.NON_EXISTENT_ID;
import static com.javarush.khasanov.config.Config.USER_NOT_FOUND_EXCEPTION;
import static java.util.Objects.isNull;

@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Long registerUser(String name, String password) {
        if (isNull(name) || name.isBlank() || isNull(password) || password.isBlank()) {
            return NON_EXISTENT_ID;
        }
        User user = User.builder()
                .name(name)
                .password(password)
                .build();
        userRepository.create(user);
        log.info("Пользователь {} зарегистрирован", name);
        return user.getId();
    }

    public Long loginUser(String username, String password) {
        Optional<User> optionalUser = userRepository.get(username);
        if (optionalUser.isEmpty()) {
            return NON_EXISTENT_ID;
        }
        User user = optionalUser.get();
        if (user.getPassword().equals(password)) {
            return user.getId();
        }
        return NON_EXISTENT_ID;
    }

    public User getUser(Long id) {
        Optional<User> optionalUser = userRepository.get(id);
        if (optionalUser.isEmpty()) {
            log.error("Не найден пользователь по id={}", id);
            throw new ProjectException(USER_NOT_FOUND_EXCEPTION);
        }
        return optionalUser.get();
    }

    public void registerAdmin() {
        userRepository.getAdmin();
    }
}
