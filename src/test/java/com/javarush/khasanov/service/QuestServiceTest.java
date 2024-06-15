package com.javarush.khasanov.service;

import com.javarush.khasanov.entity.Quest;
import com.javarush.khasanov.entity.User;
import com.javarush.khasanov.exception.ProjectException;
import com.javarush.khasanov.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

class QuestServiceTest {
    private QuestRepository questRepository;
    private UserRepository userRepository;
    private QuestService questService;

    @BeforeEach
    void setUp() {
        questRepository = Mockito.mock(QuestRepository.class);
        QuestionRepository questionRepository = Mockito.mock(QuestionRepository.class);
        AnswerRepository answerRepository = Mockito.mock(AnswerRepository.class);
        userRepository = Mockito.mock(UserRepository.class);
        GameRepository gameRepository = Mockito.mock(GameRepository.class);
        questService = new QuestService(
                questRepository,
                questionRepository,
                answerRepository,
                userRepository,
                gameRepository
        );
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

    @Test
    void deleteQuest() {
        Long questId = 1L;
        Long userId = 1L;
        User user = User.builder().id(userId).build();
        Quest quest = Mockito.mock(Quest.class);
        Mockito.doReturn(user).when(quest).getAuthor();
        Mockito.doReturn(Optional.of(user)).when(userRepository).get(Mockito.anyLong());
        Mockito.doReturn(Optional.of(quest)).when(questRepository).get(Mockito.anyLong());
        questService.deleteQuest(questId, userId);
        Mockito.verify(questRepository).delete(Mockito.any(Quest.class));
    }

    @Test
    void whenDeleteNotYourOwnQuest_thenThrowsProjectException() {
        Long userId = 5L;
        Long questId = 3L;
        User user = User.builder().id(userId).build();
        User author = User.builder().build();
        Quest quest = new Quest();
        quest.setAuthor(author);
        quest.setId(questId);
        Mockito.doReturn(Optional.of(user)).when(userRepository).get(userId);
        Mockito.doReturn(Optional.of(quest)).when(questRepository).get(questId);
        assertThrows(ProjectException.class, ()-> questService.deleteQuest(questId, userId));
    }
}