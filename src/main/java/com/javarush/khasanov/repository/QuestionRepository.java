package com.javarush.khasanov.repository;

import com.javarush.khasanov.config.SessionFactoryCreator;
import com.javarush.khasanov.entity.Question;

public class QuestionRepository extends AbstractRepository<Question> {
    public QuestionRepository(SessionFactoryCreator sessionFactoryCreator) {
        super(Question.class, sessionFactoryCreator);
    }
}
