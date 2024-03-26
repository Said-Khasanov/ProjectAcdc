package com.javarush.khasanov.service;

import com.javarush.khasanov.entity.Answer;
import com.javarush.khasanov.entity.Game;
import com.javarush.khasanov.entity.Quest;
import com.javarush.khasanov.entity.Question;
import com.javarush.khasanov.repository.AnswerRepository;
import com.javarush.khasanov.repository.GameRepository;
import com.javarush.khasanov.repository.QuestRepository;
import com.javarush.khasanov.repository.QuestionRepository;

import java.util.*;

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

    public Game getGame(Long id) {
        Optional<Game> optionalGame = gameRepository.get(id);
        return optionalGame.orElseGet(this::createGame);
    }

    public Game createGame() {
        Game game = new Game();
        Quest quest = questRepository.get(1L).orElseThrow();
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
