package com.javarush.khasanov.repository;

import com.javarush.khasanov.config.SessionFactoryCreator;
import com.javarush.khasanov.entity.Quest;

public class QuestRepository extends AbstractRepository<Quest> {

    public QuestRepository(SessionFactoryCreator sessionFactoryCreator) {
        super(Quest.class, sessionFactoryCreator);
    }

}
