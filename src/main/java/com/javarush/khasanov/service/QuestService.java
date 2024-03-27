package com.javarush.khasanov.service;

import com.javarush.khasanov.entity.Answer;
import com.javarush.khasanov.entity.Quest;
import com.javarush.khasanov.entity.Question;
import com.javarush.khasanov.repository.AnswerRepository;
import com.javarush.khasanov.repository.QuestRepository;
import com.javarush.khasanov.repository.QuestionRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class QuestService {
    private static final Pattern titlePattern = Pattern.compile("^[Tt]: *\t*");
    private static final Pattern questionPattern = Pattern.compile("^[QqEe][0-9]+: *\t*");
    public static final String NEXT_QUESTION_SIGN = ">";
    private static final Pattern answerPattern = Pattern.compile(
            "^[Aa]" + NEXT_QUESTION_SIGN + "[QqEe][0-9]+: *\t*"
    );
    private static final Path WEB_INF = Paths.get(
            URI.create(
                    Objects.requireNonNull(Quest.class.getResource("/")).toString()
            )
    ).getParent();

    private final QuestRepository questRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    public QuestService(
            QuestRepository questRepository,
            QuestionRepository questionRepository,
            AnswerRepository answerRepository
    ) {
        this.questRepository = questRepository;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        loadQuests();
    }

    private void loadQuests() {
        Path pathQuests = WEB_INF.resolve("../static/quests");
        try (Stream<Path> list = Files.list(pathQuests)) {
            list.forEach(this::loadQuest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadQuest(Path path) {
        System.out.println(path);
        try (BufferedReader bufferedReader = Files.newBufferedReader(path)) {
            Quest quest = parseQuest(bufferedReader);
            questRepository.create(quest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Quest parseQuest(Reader in) {
        Quest quest = new Quest();
        Map<String, Question> tagQuestionMap = new HashMap<>();
        Map<String, Answer> tagAnswerMap = new HashMap<>();
        Question currentQuestion = new Question();
        List<Answer> answerList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(in)) {
            while (reader.ready()) {
                String line = reader.readLine();
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
                    if (quest.getFirstQuestion() == null) {
                        quest.setFirstQuestion(currentQuestion);
                    }
                } else if (answerMatcher.find()) {
                    Answer answer = parseAnswer(answerMatcher, line, tagAnswerMap);
                    answerList.add(answer);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        configureAnswers(tagAnswerMap, tagQuestionMap);
        return quest;
    }

    private void configureAnswers(Map<String, Answer> tagAnswerMap, Map<String, Question> tagQuestionMap) {
        for (var entry : tagAnswerMap.entrySet()) {
            String tag = entry.getKey();
            Answer answer = entry.getValue();
            Question nextQuestion = tagQuestionMap.get(tag);
            if (nextQuestion.isEnding()) {
                answer.setDeadEnd(true);
            }
            answer.setNextQuestionId(nextQuestion.getId());
            answerRepository.create(answer);
        }
    }

    private Answer parseAnswer(Matcher answerMatcher, String line, Map<String, Answer> answers) {
        int startIndex = answerMatcher.end();
        String tag = answerMatcher.group().toLowerCase().trim();
        String titleText = line.substring(startIndex);

        Answer answer = new Answer();
        answer.setText(titleText);

        String nextQuestionTag = tag.split(NEXT_QUESTION_SIGN)[1];
        answers.put(nextQuestionTag, answer);
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
}
