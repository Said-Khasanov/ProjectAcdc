package com.javarush.khasanov.repository;

import com.javarush.khasanov.entity.User;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

import static com.javarush.khasanov.config.Config.ADMIN_ID;
import static com.javarush.khasanov.config.Config.ADMIN_USERNAME;

@Slf4j
public class UserRepository extends AbstractRepository<User> {

    public User getAdmin() {
        Optional<User> optionalUser = get(ADMIN_ID);
        if (optionalUser.isEmpty()) {
            log.info("Регистрация админа");
            User admin = User.builder()
                    .name(ADMIN_USERNAME)
                    .password(ADMIN_USERNAME)
                    .build();
            create(admin);
            return admin;
        }
        return optionalUser.get();
    }

    public Optional<User> get(String username) {
        return map.values()
                .stream()
                .filter(user -> user.getName().equalsIgnoreCase(username))
                .findFirst();
    }
}