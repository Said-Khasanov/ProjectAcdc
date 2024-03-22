package com.javarush.khasanov.service;

import com.javarush.khasanov.entity.Answer;
import com.javarush.khasanov.entity.Game;
import com.javarush.khasanov.entity.Quest;
import com.javarush.khasanov.entity.Question;
import com.javarush.khasanov.repository.GameRepository;
import com.javarush.khasanov.repository.QuestRepository;
import com.javarush.khasanov.repository.QuestionRepository;

import java.util.*;

public class GameService {
    private final GameRepository gameRepository;
    private final QuestRepository questRepository;
    private final QuestionRepository questionRepository;

    public GameService(
            GameRepository gameRepository,
            QuestRepository questRepository,
            QuestionRepository questionRepository
    ) {
        this.gameRepository = gameRepository;
        this.questRepository = questRepository;
        this.questionRepository = questionRepository;

        fillFirstQuest();
    }

    private void fillFirstQuest() {
        Question question1 = new Question();
        question1.setText("Вы готовы?");
        questionRepository.create(question1);

        Answer answer1 = new Answer();
        Answer answer2 = new Answer();
        answer1.setText("Да");
        answer2.setText("Нет");

        Quest quest = new Quest();
        Map<Question, List<Answer>> questions = quest.getQuestions();
        questions.put(question1, List.of(answer1, answer2));
        questRepository.create(quest);

        Game game = new Game();
        game.setCurrentQuestion(question1);
        game.setQuest(quest);

        gameRepository.create(game);
    }

    public Game getGame(Long id) {
        Optional<Game> game = gameRepository.get(id);
        return game.orElseGet(Game::new);
    }

    public List<Answer> getAnswers(Game game, Question question) {
        Quest quest = game.getQuest();
        Map<Question, List<Answer>> questions = quest.getQuestions();
        List<Answer> answers = questions.get(question);
        if (answers == null) {
            answers = Collections.emptyList();
        }

        return answers;
    }


}
