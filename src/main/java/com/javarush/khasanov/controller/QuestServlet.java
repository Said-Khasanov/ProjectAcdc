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
    public static final String QUEST_JSP = "/WEB-INF/quest.jsp";
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
        Game game = getUserGame(session);

        Question question = game.getCurrentQuestion();
        List<Answer> answers = gameService.getAnswers(game, question);

        session.setAttribute("question", question);
        session.setAttribute("answers", answers);

        RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher(QUEST_JSP);
        requestDispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        Game userGame = getUserGame(session);

        sendAnswer(req, userGame);
        resp.sendRedirect("/quest");
    }

    private void sendAnswer(HttpServletRequest req, Game game) {
        String stringAnswerId = req.getHeader("answerId");

        if (Objects.nonNull(stringAnswerId)) {
            Long answerId = Long.parseLong(stringAnswerId);
            gameService.sendAnswer(game, answerId);
        }
    }

    private Game getUserGame(HttpSession session) {
        Long gameId = (Long) session.getAttribute("gameId");
        Game game;
        if (Objects.isNull(gameId)) {
            game = gameService.createGame();
            session.setAttribute("gameId", game.getId());
        } else {
            game = gameService.getGame(gameId);
        }
        return game;
    }
}