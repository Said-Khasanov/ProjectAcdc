package com.javarush.khasanov.repository;

import com.javarush.khasanov.entity.Answer;
import com.javarush.khasanov.entity.Quest;
import com.javarush.khasanov.entity.Question;

public class QuestRepository extends AbstractRepository<Quest> {

    public QuestRepository() {
        Question question1 = new Question();
        question1.setText("Вы готовы?");
        Answer answer1 = new Answer();
        Answer answer2 = new Answer();
        answer1.setText("Да");
        answer2.setText("Нет");
        Quest quest = new Quest();
    }
}
