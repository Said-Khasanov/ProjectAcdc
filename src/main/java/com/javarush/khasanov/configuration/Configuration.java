package com.javarush.khasanov.configuration;

import com.javarush.khasanov.entity.Quest;

import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.regex.Pattern;

public final class Configuration {
    private Configuration(){}
    public static final String PAGE_PREFIX = "/WEB-INF";
    public static final String PAGE_POSTFIX = ".jsp";
    public static final String HOME_RESOURCE = "/home";
    public static final String QUEST_RESOURCE = "/quest";
    public static final String QUESTS_LIST_RESOURCE = "/quests-list";
    public static final String SIGNUP_RESOURCE = "/signup";
    public static final String LOGIN_RESOURCE = "/login";
    public static final String LOGOUT_RESOURCE = "/logout";
    public static final String RESTART_RESOURCE = "/restart";
    public static final String STATISTICS_RESOURCE = "/statistics";
    public static final String CREATE_QUEST_RESOURCE = "/create-quest";
    public static final String DELETE_QUEST_RESOURCE = "/delete";
    public static final String HOME_PAGE = PAGE_PREFIX + HOME_RESOURCE + PAGE_POSTFIX;
    public static final String QUEST_PAGE = PAGE_PREFIX + QUEST_RESOURCE + PAGE_POSTFIX;
    public static final String QUESTS_LIST_PAGE = PAGE_PREFIX + QUESTS_LIST_RESOURCE + PAGE_POSTFIX;
    public static final String SIGNUP_PAGE = PAGE_PREFIX + SIGNUP_RESOURCE + PAGE_POSTFIX;
    public static final String LOGIN_PAGE = PAGE_PREFIX + LOGIN_RESOURCE + PAGE_POSTFIX;
    public static final String STATISTICS_PAGE = PAGE_PREFIX + STATISTICS_RESOURCE + PAGE_POSTFIX;
    public static final String CREATE_QUEST_PAGE = PAGE_PREFIX + CREATE_QUEST_RESOURCE + PAGE_POSTFIX;
    public static final String PATH_TO_QUESTS = "../static/quests";
    public static final Long NON_EXISTENT_ID = 0L;
    public static final Long ADMIN_ID = 1L;
    public static final String ADMIN_USERNAME = "admin";
    public static final String ENDING_PREFIX = "e";
    public static final String NEXT_QUESTION_SIGN = ">";
    public static final Pattern titlePattern = Pattern.compile("^[Tt]: *\t*");
    public static final Pattern questionPattern = Pattern.compile("^[QqEe][0-9]+: *\t*");
    public static final Pattern answerPattern = Pattern.compile(
            "^[Aa]" + NEXT_QUESTION_SIGN + "[QqEe][0-9]+: *\t*"
    );
    public static final Path WEB_INF = Paths.get(
            URI.create(
                    Objects.requireNonNull(Quest.class.getResource("/")).toString()
            )
    ).getParent();
    public static final String QUEST_NOT_EXISTS_EXCEPTION = "Quest not exists";
    public static final String COMPONENT_CREATION_EXCEPTION = "The component could not be created";
    public static final String NOT_YOUR_OWN_QUEST_EXCEPTION = "You can't delete a quest that is not your own";
    public static final String USER_NOT_FOUND_EXCEPTION = "Пользователь не найден";
    public static final String PARAMETER_USERNAME = "username";
    public static final String PARAMETER_PASSWORD = "password";
    public static final String PARAMETER_QUEST_ID = "id";
    public static final String PARAMETER_QUEST_TEXT = "questText";
    public static final String ATTRIBUTE_USER_ID = "userId";
    public static final String ATTRIBUTE_QUEST_ID = "questId";
    public static final String ATTRIBUTE_QUESTION = "question";
    public static final String ATTRIBUTE_ANSWERS = "answers";
    public static final String ATTRIBUTE_QUESTS_LIST = "questsList";
    public static final String ATTRIBUTE_USERNAME = "username";
    public static final String ATTRIBUTE_STATISTICS_MAP = "statsMap";
    public static final String REQUEST_HEADER_ANSWER_ID = "answerId";
}