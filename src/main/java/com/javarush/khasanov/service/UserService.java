package com.javarush.khasanov.service;

import com.javarush.khasanov.entity.User;
import com.javarush.khasanov.repository.UserRepository;

import java.util.Optional;

import static com.javarush.khasanov.config.Constants.NON_EXISTENT_ID;
import static java.util.Objects.isNull;

public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean registerUser(String name, String password) {
        if (isNull(name) || isNull(password)) {
            return false;
        }
        User user = new User();
        user.setName(name);
        user.setPassword(password);
        userRepository.create(user);
        return true;
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

}
