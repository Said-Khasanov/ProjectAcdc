package com.javarush.khasanov.controller;

import com.javarush.khasanov.entity.Quest;
import com.javarush.khasanov.repository.AnswerRepository;
import com.javarush.khasanov.repository.QuestRepository;
import com.javarush.khasanov.repository.QuestionRepository;
import com.javarush.khasanov.service.QuestService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("")
public class QuestsListServlet extends HttpServlet {
    private final QuestRepository questRepository = new QuestRepository();
    private final QuestionRepository questionRepository = new QuestionRepository();
    private final AnswerRepository answerRepository = new AnswerRepository();
    private final QuestService questService = new QuestService(
            questRepository,
            questionRepository,
            answerRepository
    );

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        List<Quest> questsList = new ArrayList<>(questRepository.getAll());
        session.setAttribute("questsList", questsList);

        RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher("/WEB-INF/quests-list.jsp");
        requestDispatcher.forward(req, resp);
    }

}
