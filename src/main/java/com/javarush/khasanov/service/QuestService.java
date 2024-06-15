package com.javarush.khasanov.service;

import com.javarush.khasanov.entity.Answer;
import com.javarush.khasanov.entity.Quest;
import com.javarush.khasanov.entity.Question;
import com.javarush.khasanov.entity.User;
import com.javarush.khasanov.exception.ProjectException;
import com.javarush.khasanov.repository.*;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.stream.Stream;

import static com.javarush.khasanov.config.Config.*;
import static java.util.Objects.*;

@Slf4j
public class QuestService {

    private final QuestRepository questRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final UserRepository userRepository;
    private final GameRepository gameRepository;

    public QuestService(
            QuestRepository questRepository,
            QuestionRepository questionRepository,
            AnswerRepository answerRepository,
            UserRepository userRepository,
            GameRepository gameRepository
    ) {
        this.questRepository = questRepository;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
        //loadQuestsFromDirectory();
    }

    private void loadQuestsFromDirectory() {
        Path pathQuests = WEB_INF.resolve(PATH_TO_QUESTS);
        log.info("Загрузка начальных квестов");
        try (Stream<Path> list = Files.list(pathQuests)) {
            list.forEach(this::createQuestFromFile);
        } catch (NoSuchFileException ignored) {
            log.info("Пропущена загрузка начальных квестов");
        } catch (IOException e) {
            log.error("Ошибка при загрузке начальных квестов");
            throw new ProjectException(e);
        }
        log.info("Завершена загрузка начальных квестов");
    }

    private void createQuestFromFile(Path path) {
        if (path.toFile().isFile()) {
            try (BufferedReader bufferedReader = Files.newBufferedReader(path)) {
                Quest quest = parseQuest(bufferedReader);
                User admin = userRepository.getAdmin();
                quest.setAuthor(admin);
                questRepository.update(quest);
            } catch (IOException e) {
                log.error("Ошибка при загрузке начальных квестов. Файл {}", path.getFileName());
                throw new ProjectException(e);
            }
        }
    }

    public boolean createQuestFromText(String text, User author) {
        if (isNull(text) || isNull(author) || text.isBlank()) {
            return false;
        }
        try (StringReader stringReader = new StringReader(text)) {
            Quest quest = parseQuest(stringReader);
            quest.setAuthor(author);
            questRepository.update(quest);
        }
        log.info("Создан квест пользователем {}", author);
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
                        quest.getQuestions().add(currentQuestion);
                        answerList.clear();
                    }
                    currentQuestion = parseQuestion(questionMatcher, line, tagQuestionMap);
                    currentQuestion.setQuest(quest);
                    questionRepository.create(currentQuestion);
                    if (isNull(quest.getFirstQuestion())) {
                        quest.setFirstQuestion(currentQuestion);
                    }
                } else if (answerMatcher.find()) {
                    Answer answer = parseAnswer(answerMatcher, line, tagAnswerMap);
                    answer.setQuestion(currentQuestion);
                    answerList.add(answer);
                }
            }
        } catch (IOException e) {
            throw new ProjectException(e);
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
                answer.setNextQuestion(nextQuestion);
                answerRepository.create(answer);
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
        if (tag.startsWith(ENDING_PREFIX)) {
            question.setEnding(true);
        }

        questions.put(tag, question);
        return question;
    }

    private void parseTitle(Matcher titleMatcher, String line, Quest quest) {
        int startIndex = titleMatcher.end();
        String titleText = line.substring(startIndex);
        quest.setTitle(titleText);
        questRepository.create(quest);
    }

    public List<Quest> getAllQuests() {
        return new ArrayList<>(questRepository.getAll());
    }

    public void deleteQuest(Long questId, Long userId) {
        User user = userRepository.get(userId).orElseThrow();
        Quest quest = questRepository.get(questId).orElseThrow();
        Long authorId = quest.getAuthor().getId();
        if (!userId.equals(authorId)) {
            log.error("Попытка удалить чужой квест {} пользователем {}", quest, user);
            throw new ProjectException(NOT_YOUR_OWN_QUEST_EXCEPTION);
        }
        gameRepository.deleteByQuestId(questId);
        questRepository.delete(quest);
        log.info("Удалён квест {} пользователем {}", quest, user);
    }
}