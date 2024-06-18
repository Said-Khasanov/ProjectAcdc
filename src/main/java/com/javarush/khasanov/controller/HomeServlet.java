package com.javarush.khasanov.controller;

import com.javarush.khasanov.config.ApplicationProperties;
import com.javarush.khasanov.util.LiquibaseInit;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static com.javarush.khasanov.config.Config.HOME_PAGE;
import static com.javarush.khasanov.config.Config.HOME_RESOURCE;

@WebServlet(urlPatterns = {"", HOME_RESOURCE}, loadOnStartup = 1)
public class HomeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher(HOME_PAGE);
        requestDispatcher.forward(req, resp);
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        try {
            LiquibaseInit.runWithProperties(new ApplicationProperties());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        super.init(config);
    }
}