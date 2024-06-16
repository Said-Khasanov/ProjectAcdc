package com.javarush.khasanov.service;

import com.javarush.khasanov.config.Config;
import com.javarush.khasanov.entity.User;
import com.javarush.khasanov.exception.ProjectException;
import com.javarush.khasanov.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserServiceTest {

    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        userService = new UserService(userRepository);
    }

    @Test
    void whenRegisterUserAndNameIsNull_thenReturnsNonExistentId() {
        String password = "password";
        Long expected = Config.NON_EXISTENT_ID;
        Long actual = userService.registerUser(null, password);
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "\n", "\t"})
    void whenRegisterUserAndNameIsBlank_thenReturnsNonExistentId(String name) {
        String password = "password";
        Long expected = Config.NON_EXISTENT_ID;
        Long actual = userService.registerUser(name, password);
        assertEquals(expected, actual);
    }

    @Test
    void whenRegisterUserAndPasswordIsNull_thenReturnsNonExistentId() {
        String name = "user";
        Long expected = Config.NON_EXISTENT_ID;
        Long actual = userService.registerUser(name, null);
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "\n", "\t"})
    void whenRegisterUserAndPasswordIsBlank_thenReturnsNonExistentId(String password) {
        String name = "user";
        Long expected = Config.NON_EXISTENT_ID;
        Long actual = userService.registerUser(name, password);
        assertEquals(expected, actual);
    }

    @Test
    void whenSuccessRegister_thenCreateUser() {
        String name = "user";
        String password = "password";

        userService.registerUser(name, password);
        Mockito.verify(userRepository).create(Mockito.any(User.class));
    }

    @Test
    void whenLoginNonExistentUser_thenReturnsNonExistentId() {
        String name = "";
        String password = "password";
        Long expected = Config.NON_EXISTENT_ID;
        Long actual = userService.loginUser(name, password);
        assertEquals(expected, actual);
    }

    @Test
    void whenLoginWithWrongPassword_thenReturnsNonExistentId() {
        String name = "user";
        String password = "password";
        User correctUser = User.builder()
                .name(name)
                .password(password)
                .build();

        Mockito.doReturn(Optional.of(correctUser)).when(userRepository).get(name);
        userRepository.get(name);
        Mockito.verify(userRepository).get(name);

        Long expected = Config.NON_EXISTENT_ID;
        Long actual = userService.loginUser(name, "");
        assertEquals(expected, actual);
    }

    @Test
    void whenSuccessLogin_thenReturnsUserId() {
        String name = "user";
        String password = "password";
        Long expected = 2L;
        User correctUser = User.builder()
                .name(name)
                .password(password)
                .id(expected)
                .build();

        Mockito.doReturn(Optional.of(correctUser)).when(userRepository).get(name);
        Long actual = userService.loginUser(name, password);

        assertEquals(expected, actual);
    }

    @Test
    void whenGetUserAndIdNotExists_thenThrowsProjectException() {
        assertThrows(ProjectException.class, () -> userService.getUser(-1L));
    }

    @Test
    void whenGetUserById_thenReturnsCorrectUser() {
        String name = "user";
        String password = "password";
        Long id = 2L;
        User expected = User.builder()
                .name(name)
                .password(password)
                .id(id)
                .build();

        Mockito.doReturn(Optional.of(expected)).when(userRepository).get(id);
        User actual = userService.getUser(id);
        assertEquals(expected, actual);
    }

}