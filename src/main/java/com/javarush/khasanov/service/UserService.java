package com.javarush.khasanov.service;

import com.javarush.khasanov.entity.User;
import com.javarush.khasanov.exception.ProjectException;
import com.javarush.khasanov.repository.UserRepository;

import java.util.Optional;

import static com.javarush.khasanov.configuration.Configuration.NON_EXISTENT_ID;
import static java.util.Objects.isNull;

public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Long registerUser(String name, String password) {
        if (isNull(name) || name.isBlank() || isNull(password) || password.isBlank()) {
            return NON_EXISTENT_ID;
        }
        User user = User.builder()
                .name(name)
                .password(password)
                .build();
        userRepository.create(user);
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
            throw new ProjectException("Пользователь не найден");
        }
        return optionalUser.get();
    }

}
