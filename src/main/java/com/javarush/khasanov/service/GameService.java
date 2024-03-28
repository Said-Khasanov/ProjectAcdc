package com.javarush.khasanov.service;

import com.javarush.khasanov.entity.Answer;
import com.javarush.khasanov.entity.Game;
import com.javarush.khasanov.entity.Quest;
import com.javarush.khasanov.entity.Question;
import com.javarush.khasanov.exception.GameException;
import com.javarush.khasanov.repository.AnswerRepository;
import com.javarush.khasanov.repository.GameRepository;
import com.javarush.khasanov.repository.QuestRepository;
import com.javarush.khasanov.repository.QuestionRepository;

import java.util.*;

import static com.javarush.khasanov.config.Constants.QUEST_NOT_EXISTS;

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

    public Game getSessionGame(Long id, Long questId) {
        Optional<Game> optionalGame = gameRepository.get(id);
        if (optionalGame.isEmpty()) {
            return createGame(questId);
        }

        Game game = optionalGame.get();
        Quest quest = getQuest(questId);
        if (game.getQuest().equals(quest)) {
            return game;
        }

        return createGame(questId);
    }

    private Quest getQuest(Long questId) {
        Optional<Quest> optionalQuest = questRepository.get(questId);
        if (optionalQuest.isEmpty()) {
            throw new GameException(QUEST_NOT_EXISTS);
        }
        return optionalQuest.get();
    }

    private Game createGame(Long questId) {
        Quest quest = getQuest(questId);
        Game game = new Game();
        game.setQuest(quest);
        game.setCurrentQuestion(quest.getFirstQuestion());
        gameRepository.create(game);
        return game;
    }

    public List<Answer> getAnswers(Game game, Question question) {
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
    }
}
