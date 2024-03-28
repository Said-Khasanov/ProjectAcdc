package com.javarush.khasanov.controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import static com.javarush.khasanov.config.Constants.QUEST_RESOURCE;
import static com.javarush.khasanov.config.Constants.RESTART_RESOURCE;

@WebServlet(RESTART_RESOURCE)
public class RestartServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        Long questId = (Long) session.getAttribute("questId");
        session.invalidate();
        session = req.getSession();
        session.setAttribute("questId", questId);
        resp.sendRedirect(QUEST_RESOURCE);
    }
}
