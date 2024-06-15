package com.javarush.khasanov.service;

import com.javarush.khasanov.config.QuestParser;
import com.javarush.khasanov.entity.Quest;
import com.javarush.khasanov.entity.User;
import com.javarush.khasanov.exception.ProjectException;
import com.javarush.khasanov.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import static com.javarush.khasanov.config.Config.NOT_YOUR_OWN_QUEST_EXCEPTION;
import static java.util.Objects.isNull;

@Slf4j
@RequiredArgsConstructor
public class QuestService {

    private final QuestRepository questRepository;
    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final QuestParser questParser;

    public QuestService(
            QuestRepository questRepository,
            QuestionRepository questionRepository,
            AnswerRepository answerRepository,
            UserRepository userRepository,
            GameRepository gameRepository
    ) {
        this.questRepository = questRepository;
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
        questParser = new QuestParser(questRepository, questionRepository, answerRepository, userRepository);
        questParser.loadQuests();
    }

    public boolean createQuestFromText(String text, User author) {
        if (isNull(text) || isNull(author) || text.isBlank()) {
            return false;
        }
        try (StringReader stringReader = new StringReader(text)) {
            Quest quest = questParser.parseQuest(stringReader);
            quest.setAuthor(author);
            questRepository.update(quest);
        }
        log.info("Создан квест пользователем {}", author);
        return true;
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