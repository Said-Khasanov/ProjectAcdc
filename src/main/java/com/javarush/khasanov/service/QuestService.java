package com.javarush.khasanov.service;

import com.javarush.khasanov.entity.Answer;
import com.javarush.khasanov.entity.Quest;
import com.javarush.khasanov.entity.Question;
import com.javarush.khasanov.entity.User;
import com.javarush.khasanov.repository.AnswerRepository;
import com.javarush.khasanov.repository.QuestRepository;
import com.javarush.khasanov.repository.QuestionRepository;
import com.javarush.khasanov.repository.UserRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.stream.Stream;

import static com.javarush.khasanov.configuration.Configuration.*;
import static java.util.Objects.*;

public class QuestService {
    private final QuestRepository questRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final UserRepository userRepository;

    public QuestService(
            QuestRepository questRepository,
            QuestionRepository questionRepository,
            AnswerRepository answerRepository,
            UserRepository userRepository
    ) {
        this.questRepository = questRepository;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.userRepository = userRepository;
        loadQuestsFromDirectory();
    }

    private void loadQuestsFromDirectory() {
        Path pathQuests = WEB_INF.resolve(PATH_TO_QUESTS);
        try (Stream<Path> list = Files.list(pathQuests)) {
            list.forEach(this::createQuestFromFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void createQuestFromFile(Path path) {
        if (path.toFile().isFile()) {
            try (BufferedReader bufferedReader = Files.newBufferedReader(path)) {
                Quest quest = parseQuest(bufferedReader);
                quest.setAuthor(userRepository.getAdmin());
                questRepository.create(quest);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public boolean createQuestFromText(String text, User author) {
        if (isNull(text) || isNull(author) || text.isBlank()){
            return false;
        }
        try (StringReader stringReader = new StringReader(text)) {
            Quest quest = parseQuest(stringReader);
            quest.setAuthor(author);
            questRepository.create(quest);
        }
        return true;
    }

    private Quest parseQuest(Reader in) {
        Quest quest = new Quest();
        Map<String, Question> tagQuestionMap = new HashMap<>();
        Map<String, List<Answer>> tagAnswerMap = new HashMap<>();
        Question currentQuestion = new Question();
        List<Answer> answerList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(in)) {
            String line;
            while (nonNull(line = reader.readLine())) {
                Matcher titleMatcher = titlePattern.matcher(line);
                Matcher questionMatcher = questionPattern.matcher(line);
                Matcher answerMatcher = answerPattern.matcher(line);

                if (titleMatcher.find()) {
                    parseTitle(titleMatcher, line, quest);
                } else if (questionMatcher.find()) {
                    if (!answerList.isEmpty() || currentQuestion.isEnding()) {
                        quest.getQuestions().put(currentQuestion, new ArrayList<>(answerList));
                        answerList.clear();
                    }
                    currentQuestion = parseQuestion(questionMatcher, line, tagQuestionMap);
                    if (isNull(quest.getFirstQuestion())) {
                        quest.setFirstQuestion(currentQuestion);
                    }
                } else if (answerMatcher.find()) {
                    Answer answer = parseAnswer(answerMatcher, line, tagAnswerMap);
                    answerRepository.create(answer);
                    answerList.add(answer);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        configureAnswers(tagAnswerMap, tagQuestionMap);
        return quest;
    }

    private void configureAnswers(Map<String, List<Answer>> tagAnswerMap, Map<String, Question> tagQuestionMap) {
        for (var entry : tagAnswerMap.entrySet()) {
            String tag = entry.getKey();
            for (Answer answer : entry.getValue()) {
                Question nextQuestion = tagQuestionMap.get(tag);
                if (nextQuestion.isEnding()) {
                    answer.setDeadEnd(true);
                }
                answer.setNextQuestionId(nextQuestion.getId());
                answerRepository.update(answer);
            }
        }
    }

    private Answer parseAnswer(Matcher answerMatcher, String line, Map<String, List<Answer>> answers) {
        int startIndex = answerMatcher.end();
        String tag = answerMatcher.group().toLowerCase().trim();
        String titleText = line.substring(startIndex);

        Answer answer = Answer.builder()
                .text(titleText)
                .build();

        String nextQuestionTag = tag.split(NEXT_QUESTION_SIGN)[1];
        List<Answer> answerList = requireNonNullElse(answers.get(nextQuestionTag), new ArrayList<>());
        answerList.add(answer);
        answers.put(nextQuestionTag, answerList);
        return answer;
    }

    private Question parseQuestion(Matcher questionMatcher, String line, Map<String, Question> questions) {
        int startIndex = questionMatcher.end();
        String tag = questionMatcher.group().toLowerCase().trim();
        String questionText = line.substring(startIndex);

        Question question = new Question();
        question.setText(questionText);
        if (tag.startsWith("e")) {
            question.setEnding(true);
        }

        questionRepository.create(question);
        questions.put(tag, question);
        return question;
    }

    private void parseTitle(Matcher titleMatcher, String line, Quest quest) {
        int startIndex = titleMatcher.end();
        String titleText = line.substring(startIndex);
        quest.setTitle(titleText);
    }

    public List<Quest> getAllQuests() {
        return new ArrayList<>(questRepository.getAll());
    }

}
