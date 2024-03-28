package com.javarush.khasanov.config;

import com.javarush.khasanov.entity.Quest;

import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.regex.Pattern;

public final class Constants {
    private Constants(){}
    public static final String QUEST_NOT_EXISTS = "Quest not exists";
    public static final String PAGE_PREFIX = "/WEB-INF";
    public static final String PAGE_POSTFIX = ".jsp";
    public static final String HOME_RESOURCE = "/home";
    public static final String QUEST_RESOURCE = "/quest";
    public static final String QUESTS_LIST_RESOURCE = "/quests-list";
    public static final String SIGNUP_RESOURCE = "/signup";
    public static final String LOGIN_RESOURCE = "/login";
    public static final String RESTART_RESOURCE = "/restart";
    public static final String HOME_PAGE = PAGE_PREFIX + HOME_RESOURCE + PAGE_POSTFIX;
    public static final String QUEST_PAGE = PAGE_PREFIX + QUEST_RESOURCE + PAGE_POSTFIX;
    public static final String QUESTS_LIST_PAGE = PAGE_PREFIX + QUESTS_LIST_RESOURCE + PAGE_POSTFIX;
    public static final String SIGNUP_PAGE = PAGE_PREFIX + SIGNUP_RESOURCE + PAGE_POSTFIX;
    public static final String LOGIN_PAGE = PAGE_PREFIX + LOGIN_RESOURCE + PAGE_POSTFIX;
    public static final String PATH_TO_QUESTS = "../static/quests";
    public static final Long NON_EXISTENT_ID = 0L;
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
}