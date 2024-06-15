package com.javarush.khasanov.config;

import com.javarush.khasanov.entity.Answer;
import com.javarush.khasanov.entity.Quest;
import com.javarush.khasanov.entity.Question;
import com.javarush.khasanov.entity.User;
import com.javarush.khasanov.exception.ProjectException;
import com.javarush.khasanov.repository.AnswerRepository;
import com.javarush.khasanov.repository.QuestRepository;
import com.javarush.khasanov.repository.QuestionRepository;
import com.javarush.khasanov.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
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
@RequiredArgsConstructor
public class QuestParser {
    private final QuestRepository questRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final UserRepository userRepository;

    public void loadQuests() {
        long count = questRepository.getCount();
        if (count == 0) {
            loadQuestsFromDirectory();
        } else {
            log.info("Пропущена загрузка начальных квестов. В базе данны уже есть {} квестов", count);
        }
    }

    private void loadQuestsFromDirectory() {
        Path pathQuests = WEB_INF.resolve(PATH_TO_QUESTS);
        log.info("Загрузка начальных квестов");
        try (Stream<Path> list = Files.list(pathQuests)) {
            list.forEach(this::createQuestFromFile);
        } catch (NoSuchFileException ignored) {
            log.info("Пропущена загрузка начальных квестов. Нет файлов с квестами");
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

    public Quest parseQuest(Reader in) {
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
}
