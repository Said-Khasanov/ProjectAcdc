package com.javarush.khasanov.repository;

import com.javarush.khasanov.config.ApplicationProperties;
import com.javarush.khasanov.config.SessionFactoryCreator;
import com.javarush.khasanov.entity.User;
import com.javarush.khasanov.util.LiquibaseInit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.Optional;

import static com.javarush.khasanov.config.Config.*;

class UserRepositoryTest {

    private static final JdbcDatabaseContainer<?> CONTAINER;

    public static final ApplicationProperties applicationProperties = new ApplicationProperties();

    private UserRepository userRepository;

    static {
        CONTAINER = new PostgreSQLContainer<>(DOCKER_IMAGE_NAME);
        CONTAINER.start();
        applicationProperties.setProperty(HIBERNATE_CONNECTION_DRIVER_CLASS, CONTAINER.getDriverClassName());
        applicationProperties.setProperty(HIBERNATE_CONNECTION_URL, CONTAINER.getJdbcUrl());
        applicationProperties.setProperty(HIBERNATE_CONNECTION_USERNAME, CONTAINER.getUsername());
        applicationProperties.setProperty(HIBERNATE_CONNECTION_PASSWORD, CONTAINER.getPassword());
        try {
            LiquibaseInit.runWithProperties(applicationProperties);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    void setUp() {
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