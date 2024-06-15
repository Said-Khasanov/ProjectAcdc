package com.javarush.khasanov.repository;

import com.javarush.khasanov.config.SessionFactoryCreator;
import com.javarush.khasanov.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.Optional;

import static com.javarush.khasanov.config.Config.ADMIN_USERNAME;

@Slf4j
public class UserRepository extends AbstractRepository<User> {

    public UserRepository(SessionFactoryCreator sessionFactoryCreator) {
        super(User.class, sessionFactoryCreator);
    }

    public User getAdmin() {
        Optional<User> optionalUser = get(ADMIN_USERNAME);
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
        try (Session session = sessionFactoryCreator.getSession()) {
            Query<User> query = session.createQuery(
                    "SELECT u FROM User u WHERE u.name = :username",
                    entityClass);
            query.setParameter("username", username);
            query.setMaxResults(1);
            return Optional.ofNullable(query.uniqueResult());
        }
    }
}