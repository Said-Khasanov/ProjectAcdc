package com.javarush.khasanov.repository;

import com.javarush.khasanov.config.SessionFactoryCreator;
import com.javarush.khasanov.entity.Game;
import com.javarush.khasanov.entity.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.Optional;

public class GameRepository extends AbstractRepository<Game> {
    public GameRepository(SessionFactoryCreator sessionFactoryCreator) {
        super(Game.class, sessionFactoryCreator);
    }

    public Optional<Game> getByUserAndQuestId(User user, Long questId) {
        try (Session session = sessionFactoryCreator.getSession()) {
            Query<Game> query = session.createQuery(
                    "SELECT g FROM Game g WHERE g.user = :user AND g.quest.id = :questId",
                    entityClass);
            query.setParameter("user", user);
            query.setParameter("questId", questId);
            return Optional.ofNullable(query.uniqueResult());
        }
    }

    public void deleteByQuestId(Long questId) {
        try (Session session = sessionFactoryCreator.getSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.createQuery("DELETE FROM Game g WHERE g.quest.id = :questId")
                        .setParameter("questId", questId)
                        .executeUpdate();
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
            }
        }
    }
}