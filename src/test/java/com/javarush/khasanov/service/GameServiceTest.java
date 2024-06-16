package com.javarush.khasanov.service;

import com.javarush.khasanov.entity.*;
import com.javarush.khasanov.exception.ProjectException;
import com.javarush.khasanov.repository.AnswerRepository;
import com.javarush.khasanov.repository.GameRepository;
import com.javarush.khasanov.repository.QuestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GameServiceTest {

    private GameRepository gameRepository;
    private QuestRepository questRepository;
    private AnswerRepository answerRepository;
    private GameService gameService;

    @BeforeEach
    void setUp() {
        gameRepository = Mockito.mock(GameRepository.class);
        questRepository = Mockito.mock(QuestRepository.class);
        answerRepository = Mockito.mock(AnswerRepository.class);
        gameService = new GameService(gameRepository, questRepository, answerRepository);
    }


    @Test
    void getUserGame() {
        User user = Mockito.mock(User.class);
        Long questId = 1L;
        Game expected = Game.builder().build();
        Mockito.doReturn(Optional.of(expected)).when(gameRepository).getByUserAndQuestId(user, questId);
        Game actual = gameService.getUserGame(user, questId);
        assertEquals(expected, actual);
    }

    @Test
    void whenGetUserGameThatNotExist_thenCreateNewGame() {
        User user = User.builder().build();
        Long questId = 1L;
        Quest quest = new Quest();
        quest.setId(questId);
        Mockito.doReturn(Optional.of(quest)).when(questRepository).get(questId);
        gameService.getUserGame(user, questId);
        Mockito.verify(gameRepository).create(Mockito.any(Game.class));
    }

    @Test
    void whenGetUserGameAndQuestIdNotExist_thenThrowsProjectException() {
        User user = User.builder().build();
        Long questId = 0L;
        assertThrows(ProjectException.class, () -> gameService.getUserGame(user, questId));
    }

    @Test
    void restartGame() {
        User user = Mockito.mock(User.class);
        Long questId = 1L;
        Game game = Game.builder().build();
        Mockito.doReturn(Optional.of(game)).when(gameRepository).getByUserAndQuestId(user, questId);
        gameService.restartGame(user, questId);
        Mockito.verify(gameRepository).delete(Mockito.any(Game.class));
    }

    @Test
    void whenGetAnswersWithNullGame_thenReturnsEmptyList() {
        Question question = new Question();
        List<Answer> actual = gameService.getAnswers(question);
        assertEquals(Collections.emptyList(), actual);
    }

    @Test
    void whenGetAnswersWithNullQuestion_thenReturnsEmptyList() {
        List<Answer> actual = gameService.getAnswers(null);
        assertEquals(Collections.emptyList(), actual);
    }

    @Test
    void sendAnswer() {
        Game game = Game.builder().build();
        Long answerId = 1L;
        Question expected = new Question();

        Answer answer = Answer.builder()
                .id(answerId)
                .nextQuestion(expected)
                .build();
        Mockito.doReturn(Optional.of(answer)).when(answerRepository).get(answerId);

        gameService.sendAnswer(game, answerId);
        Question actual = game.getCurrentQuestion();
        assertEquals(expected, actual);
    }

    @Test
    void finishGame() {
        Game game = Game.builder().build();
        gameService.finishGame(game);
        GameState actual = game.getState();
        assertEquals(GameState.FINISHED, actual);
    }
}