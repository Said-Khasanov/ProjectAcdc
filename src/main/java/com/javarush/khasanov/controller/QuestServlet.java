package com.javarush.khasanov.controller;

import com.javarush.khasanov.entity.Answer;
import com.javarush.khasanov.entity.Game;
import com.javarush.khasanov.entity.Question;
import com.javarush.khasanov.repository.AnswerRepository;
import com.javarush.khasanov.repository.GameRepository;
import com.javarush.khasanov.repository.QuestRepository;
import com.javarush.khasanov.repository.QuestionRepository;
import com.javarush.khasanov.service.GameService;
import com.javarush.khasanov.service.QuestService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@WebServlet("/quest")
public class QuestServlet extends HttpServlet {
    public static final String QUEST_PAGE = "/WEB-INF/quest.jsp";
    private final GameRepository gameRepository = new GameRepository();
    private final QuestRepository questRepository = new QuestRepository();
    private final QuestionRepository questionRepository = new QuestionRepository();
    private final AnswerRepository answerRepository = new AnswerRepository();
    private final GameService gameService = new GameService(
            gameRepository,
            questRepository,
            questionRepository,
            answerRepository
    );
    private final QuestService questService = new QuestService(
            questRepository,
            questionRepository,
            answerRepository
    );

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();

        Long questId = getQuestId(req);
        Game game = getSessionGame(session, questId);

        Question question = game.getCurrentQuestion();
        List<Answer> answers = gameService.getAnswers(game, question);

        session.setAttribute("question", question);
        session.setAttribute("answers", answers);

        RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher(QUEST_PAGE);
        requestDispatcher.forward(req, resp);
    }

    private static Long getQuestId(HttpServletRequest req) {
        String stringId = req.getParameter("id");
        HttpSession session = req.getSession();
        return Objects.isNull(stringId)
                ? (Long) session.getAttribute("questId")
                : Long.parseLong(stringId);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        Long questId = getQuestId(req);
        Game game = getSessionGame(session, questId);

        sendAnswer(req, game);
        resp.sendRedirect("/quest");
    }

    private void sendAnswer(HttpServletRequest req, Game game) {
        String stringAnswerId = req.getHeader("answerId");

        if (Objects.nonNull(stringAnswerId)) {
            Long answerId = Long.parseLong(stringAnswerId);
            gameService.sendAnswer(game, answerId);
        }
    }

    private Game getSessionGame(HttpSession session, Long questId) {
        Long gameId = (Long) session.getAttribute("gameId");
        Game game = Objects.isNull(gameId)
                ? gameService.createGame(questId)
                : gameService.getGame(gameId, questId);

        session.setAttribute("gameId", game.getId());
        session.setAttribute("questId", questId);
        return game;
    }
}