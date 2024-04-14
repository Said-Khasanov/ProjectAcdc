package com.javarush.khasanov.controller;

import com.javarush.khasanov.config.Components;
import com.javarush.khasanov.entity.Quest;
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

import static com.javarush.khasanov.config.Config.*;

@WebServlet(QUESTS_LIST_RESOURCE)
public class QuestsListServlet extends HttpServlet {

    private final QuestService questService = Components.get(QuestService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        List<Quest> questsList = questService.getAllQuests();
        session.setAttribute(ATTRIBUTE_QUESTS_LIST, questsList);
        RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher(QUESTS_LIST_PAGE);
        requestDispatcher.forward(req, resp);
    }
}