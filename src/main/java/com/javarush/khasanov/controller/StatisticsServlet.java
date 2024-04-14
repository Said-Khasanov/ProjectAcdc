package com.javarush.khasanov.controller;

import com.javarush.khasanov.config.Components;
import com.javarush.khasanov.entity.GameState;
import com.javarush.khasanov.service.StatisticsService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Map;

import static com.javarush.khasanov.config.Config.*;

@WebServlet(STATISTICS_RESOURCE)
public class StatisticsServlet extends HttpServlet {

    private final StatisticsService statisticsService = Components.get(StatisticsService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Map<String, Map<GameState, Long>> statsMap = statisticsService.calculate();
        session.setAttribute(ATTRIBUTE_STATISTICS_MAP, statsMap);
        RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher(STATISTICS_PAGE);
        requestDispatcher.forward(req, resp);
    }
}
