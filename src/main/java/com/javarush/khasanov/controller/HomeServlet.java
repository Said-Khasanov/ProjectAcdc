package com.javarush.khasanov.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static com.javarush.khasanov.config.Constants.HOME_PAGE;
import static com.javarush.khasanov.config.Constants.HOME_RESOURCE;

@WebServlet(urlPatterns = {"", HOME_RESOURCE})
public class HomeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher(HOME_PAGE);
        requestDispatcher.forward(req, resp);
    }
}
