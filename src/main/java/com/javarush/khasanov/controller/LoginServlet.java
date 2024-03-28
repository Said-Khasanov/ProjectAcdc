package com.javarush.khasanov.controller;

import com.javarush.khasanov.config.Components;
import com.javarush.khasanov.service.UserService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import static com.javarush.khasanov.config.Constants.*;

@WebServlet(LOGIN_RESOURCE)
public class LoginServlet extends HttpServlet {
    private final UserService userService = Components.get(UserService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher(LOGIN_PAGE);
        requestDispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        Long userId = userService.loginUser(username, password);
        if (NON_EXISTENT_ID.equals(userId)) {
            doGet(req, resp);
        } else {
            HttpSession session = req.getSession();
            session.setAttribute("userId", userId);
            resp.sendRedirect(HOME_RESOURCE);
        }
    }
}