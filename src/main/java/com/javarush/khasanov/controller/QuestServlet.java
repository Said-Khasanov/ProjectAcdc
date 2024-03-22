package com.javarush.khasanov.controller;

import com.javarush.khasanov.entity.Answer;
import com.javarush.khasanov.entity.Game;
import com.javarush.khasanov.entity.Question;
import com.javarush.khasanov.repository.GameRepository;
import com.javarush.khasanov.repository.QuestRepository;
import com.javarush.khasanov.repository.QuestionRepository;
import com.javarush.khasanov.service.GameService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/")
public class QuestServlet extends HttpServlet {

    public static final String INDEX_PAGE = "/WEB-INF/index.jsp";
    private final GameRepository gameRepository = new GameRepository();
    private final QuestRepository questRepository = new QuestRepository();
    private final QuestionRepository questionRepository = new QuestionRepository();
    private final GameService gameService = new GameService(gameRepository, questRepository, questionRepository);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Game userGame = getUserGame(session);
        Question question = userGame.getCurrentQuestion();
        List<Answer> answers = gameService.getAnswers(userGame, question);
        session.setAttribute("question", question);
        session.setAttribute("answers", answers);
        RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher(INDEX_PAGE);
        requestDispatcher.forward(req, resp);
    }

    private Game getUserGame(HttpSession session) {
        Long gameId = (Long) session.getAttribute("gameId");
        Game game;
        if (gameId == null) {
            long newGameId = 1L;
            game = gameService.getGame(newGameId);
            session.setAttribute("gameId", newGameId);
        } else {
            game = gameService.getGame(gameId);
        }
        return game;
    }


}
