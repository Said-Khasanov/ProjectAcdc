package com.javarush.khasanov.repository;

import com.javarush.khasanov.config.ApplicationProperties;
import com.javarush.khasanov.config.SessionFactoryCreator;
import com.javarush.khasanov.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

class UserRepositoryTest {

    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        ApplicationProperties applicationProperties = new ApplicationProperties();
        SessionFactoryCreator sessionFactoryCreator = new SessionFactoryCreator(applicationProperties);
        userRepository = Mockito.spy(new UserRepository(sessionFactoryCreator));
    }

    @Test
    void whenAdminNotExists_thenCreateAdmin() {
        Optional<User> optionalAdmin = userRepository.get("admin");
        optionalAdmin.ifPresent(user -> userRepository.delete(user));
        userRepository.getAdmin();
        Mockito.verify(userRepository).create(Mockito.any(User.class));
    }
}