package com.javarush.khasanov.repository;

import com.javarush.khasanov.config.SessionFactoryCreator;
import com.javarush.khasanov.entity.Quest;
import org.hibernate.Session;

public class QuestRepository extends AbstractRepository<Quest> {

    public QuestRepository(SessionFactoryCreator sessionFactoryCreator) {
        super(Quest.class, sessionFactoryCreator);
    }

    public long getCount() {
        try (Session session = sessionFactoryCreator.getSession()) {
            return session.createQuery("select count(q.id) from Quest q", Long.class).uniqueResult();
        }
    }
}
