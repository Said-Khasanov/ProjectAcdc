package com.javarush.khasanov.controller;

import com.javarush.khasanov.configuration.Components;
import com.javarush.khasanov.entity.User;
import com.javarush.khasanov.service.QuestService;
import com.javarush.khasanov.service.UserService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import static com.javarush.khasanov.configuration.Configuration.*;

@WebServlet(CREATE_QUEST_RESOURCE)
public class CreateQuestServlet extends HttpServlet {
    private final QuestService questService = Components.get(QuestService.class);
    private final UserService userService = Components.get(UserService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher(CREATE_QUEST_PAGE);
        requestDispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Long userId = (Long) session.getAttribute("userId");
        User author = userService.getUser(userId);
        String questText = req.getParameter("questText");
        if (questService.createQuestFromText(questText, author)) {
            resp.sendRedirect(QUESTS_LIST_RESOURCE);
        } else {
            RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher(CREATE_QUEST_PAGE);
            requestDispatcher.forward(req, resp);
        }
    }
}