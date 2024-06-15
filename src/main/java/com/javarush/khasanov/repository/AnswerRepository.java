package com.javarush.khasanov.repository;

import com.javarush.khasanov.config.SessionFactoryCreator;
import com.javarush.khasanov.entity.Answer;

public class AnswerRepository extends AbstractRepository<Answer> {
    public AnswerRepository(SessionFactoryCreator sessionFactoryCreator) {
        super(Answer.class, sessionFactoryCreator);
    }

//    public Collection<Answer> getAnswers(Question question) {
//        try (Session session = sessionFactoryCreator.getSession()) {
////            Query<Answer> query = session.createQuery(
////                    "SELECT q.answers FROM Question q WHERE q = :question",
////                    Answer.class);
//            Question question1 = session.get(Question.class, question.getId());
//            query.setParameter("question", question);
//            return query.list();
//        }
//    }
}
