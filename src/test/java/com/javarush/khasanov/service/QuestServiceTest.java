package com.javarush.khasanov.service;

import com.javarush.khasanov.entity.Quest;
import com.javarush.khasanov.entity.User;
import com.javarush.khasanov.repository.AnswerRepository;
import com.javarush.khasanov.repository.QuestRepository;
import com.javarush.khasanov.repository.QuestionRepository;
import com.javarush.khasanov.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertFalse;

class QuestServiceTest {
    private QuestRepository questRepository;
    private QuestService questService;

    @BeforeEach
    void setUp() {
        questRepository = Mockito.mock(QuestRepository.class);
        QuestionRepository questionRepository = Mockito.mock(QuestionRepository.class);
        AnswerRepository answerRepository = Mockito.mock(AnswerRepository.class);
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        questService = new QuestService(questRepository, questionRepository, answerRepository, userRepository);
    }

    @Test
    void createQuestFromText() {
        questService.createQuestFromText("quest text", User.builder().build());
        Mockito.verify(questRepository).create(Mockito.any(Quest.class));
    }


    @Test
    void whenCreateQuestFromNullText_thenReturnsFalse() {
        boolean actual = questService.createQuestFromText(null, User.builder().build());
        assertFalse(actual);
    }

    @Test
    void whenCreateQuestFromBlankText_thenReturnsFalse() {
        boolean actual = questService.createQuestFromText("", User.builder().build());
        assertFalse(actual);
    }

    @Test
    void whenCreateQuestWithNullAuthor_thenReturnsFalse() {
        boolean actual = questService.createQuestFromText("quest text", null);
        assertFalse(actual);
    }

    @Test
    void getAllQuests() {
        questService.getAllQuests();
        Mockito.verify(questRepository).getAll();
    }

}