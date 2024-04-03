package com.javarush.khasanov.controller;

import com.javarush.khasanov.configuration.Components;
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

@WebServlet(SIGNUP_RESOURCE)
public class SignupServlet extends HttpServlet {
    private final UserService userService = Components.get(UserService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher(SIGNUP_PAGE);
        requestDispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter(PARAMETER_USERNAME);
        String password = req.getParameter(PARAMETER_PASSWORD);
        Long userId = userService.registerUser(username, password);
        if (!NON_EXISTENT_ID.equals(userId)) {
            HttpSession session = req.getSession();
            session.setAttribute(ATTRIBUTE_USER_ID, userId);
            session.setAttribute(ATTRIBUTE_USERNAME, username);
            RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher(HOME_PAGE);
            requestDispatcher.forward(req, resp);
        }
    }
}