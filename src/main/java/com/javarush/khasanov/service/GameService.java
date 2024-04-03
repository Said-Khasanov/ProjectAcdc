package com.javarush.khasanov.service;

import com.javarush.khasanov.entity.*;
import com.javarush.khasanov.exception.ProjectException;
import com.javarush.khasanov.repository.AnswerRepository;
import com.javarush.khasanov.repository.GameRepository;
import com.javarush.khasanov.repository.QuestRepository;
import com.javarush.khasanov.repository.QuestionRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

import static com.javarush.khasanov.configuration.Configuration.QUEST_NOT_EXISTS_EXCEPTION;
import static java.util.Objects.isNull;

@Slf4j
public class GameService {
    private final GameRepository gameRepository;
    private final QuestRepository questRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    public GameService(
            GameRepository gameRepository,
            QuestRepository questRepository,
            QuestionRepository questionRepository,
            AnswerRepository answerRepository
    ) {
        this.gameRepository = gameRepository;
        this.questRepository = questRepository;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
    }

    public Game getUserGame(User user, Long questId) {
        Map<Long, Long> questGameMap = user.getQuestGameMap();
        Long gameId = questGameMap.get(questId);
        Optional<Game> optionalGame = gameRepository.get(gameId);
        if (optionalGame.isEmpty()) {
            Game game = createGame(questId);
            questGameMap.put(questId, game.getId());
            gameRepository.update(game);
            return game;
        }
        return optionalGame.get();
    }

    public void restartGame(User user, Long questId) {
        Map<Long, Long> questGameMap = user.getQuestGameMap();
        questGameMap.remove(questId);
    }

    private Quest getQuest(Long questId) {
        Optional<Quest> optionalQuest = questRepository.get(questId);
        if (optionalQuest.isEmpty()) {
            log.error("Не найден квест по id={}", questId);
            throw new ProjectException(QUEST_NOT_EXISTS_EXCEPTION);
        }
        return optionalQuest.get();
    }

    private Game createGame(Long questId) {
        Quest quest = getQuest(questId);
        Question firstQuestion = quest.getFirstQuestion();
        Game game = Game.builder()
                .quest(quest)
                .currentQuestion(firstQuestion)
                .build();
        gameRepository.create(game);
        return game;
    }

    public List<Answer> getAnswers(Game game, Question question) {
        if (isNull(game) || isNull(question)) {
            return Collections.emptyList();
        }
        Quest quest = game.getQuest();
        Map<Question, List<Answer>> questions = quest.getQuestions();
        List<Answer> answers = questions.get(question);
        return Objects.requireNonNullElse(answers, Collections.emptyList());
    }

    public void sendAnswer(Game game, Long answerId) {
        Optional<Answer> optionalAnswer = answerRepository.get(answerId);
        Answer answer = optionalAnswer.orElseThrow();
        Long idNextQuestion = answer.getNextQuestionId();

        Optional<Question> optionalQuestion = questionRepository.get(idNextQuestion);
        Question nextQuestion = optionalQuestion.orElseThrow();

        game.setCurrentQuestion(nextQuestion);

        if (answer.isDeadEnd()) {
            finishGame(game);
        }
    }

    public void finishGame(Game game) {
        game.setState(GameState.FINISHED);
    }

}
