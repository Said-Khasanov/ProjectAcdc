package com.javarush.khasanov.controller;

import com.javarush.khasanov.configuration.Components;
import com.javarush.khasanov.entity.User;
import com.javarush.khasanov.service.GameService;
import com.javarush.khasanov.service.UserService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import static com.javarush.khasanov.configuration.Configuration.QUEST_RESOURCE;
import static com.javarush.khasanov.configuration.Configuration.RESTART_RESOURCE;

@WebServlet(RESTART_RESOURCE)
public class RestartServlet extends HttpServlet {
    private final UserService userService = Components.get(UserService.class);
    private final GameService gameService = Components.get(GameService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        Long userId = (Long) session.getAttribute("userId");
        String username = (String) session.getAttribute("username");
        Long questId = (Long) session.getAttribute("questId");
        session.invalidate();

        User user = userService.getUser(userId);
        gameService.restartGame(user, questId);

        session = req.getSession();
        session.setAttribute("userId", userId);
        session.setAttribute("username", username);
        session.setAttribute("questId", questId);
        resp.sendRedirect(QUEST_RESOURCE);
    }
}