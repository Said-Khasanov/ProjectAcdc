package com.javarush.khasanov.service;

import com.javarush.khasanov.entity.*;
import com.javarush.khasanov.exception.ProjectException;
import com.javarush.khasanov.repository.AnswerRepository;
import com.javarush.khasanov.repository.GameRepository;
import com.javarush.khasanov.repository.QuestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

import static com.javarush.khasanov.config.Config.QUEST_NOT_EXISTS_EXCEPTION;

@Slf4j
@RequiredArgsConstructor
public class GameService {
    private final GameRepository gameRepository;
    private final QuestRepository questRepository;
    private final AnswerRepository answerRepository;

    public Game getUserGame(User user, Long questId) {
        Optional<Game> optionalGame = gameRepository.getByUserAndQuestId(user, questId);
        return optionalGame.orElseGet(() -> createGame(user, questId));
    }

    public void restartGame(User user, Long questId) {
        gameRepository.getByUserAndQuestId(user, questId).ifPresent(gameRepository::delete);
    }

    private Quest getQuest(Long questId) {
        Optional<Quest> optionalQuest = questRepository.get(questId);
        if (optionalQuest.isEmpty()) {
            log.error("Не найден квест по id={}", questId);
            throw new ProjectException(QUEST_NOT_EXISTS_EXCEPTION);
        }
        return optionalQuest.get();
    }

    private Game createGame(User user, Long questId) {
        Quest quest = getQuest(questId);
        Question firstQuestion = quest.getFirstQuestion();
        Game game = Game.builder().quest(quest).currentQuestion(firstQuestion).build();
        game.setUser(user);
        gameRepository.create(game);
        return game;
    }

    public List<Answer> getAnswers(Question question) {
        return question.getAnswers();
    }

    public void sendAnswer(Game game, Long answerId) {
        Answer answer = answerRepository.get(answerId).orElseThrow();
        Question nextQuestion = answer.getNextQuestion();
        game.setCurrentQuestion(nextQuestion);
        gameRepository.update(game);
        if (answer.isDeadEnd()) {
            finishGame(game);
        }
    }

    public void finishGame(Game game) {
        game.setState(GameState.FINISHED);
        gameRepository.update(game);
    }

}
