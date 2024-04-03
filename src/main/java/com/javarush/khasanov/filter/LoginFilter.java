package com.javarush.khasanov.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import static com.javarush.khasanov.configuration.Configuration.*;
import static java.util.Objects.isNull;


@WebFilter(urlPatterns = {
        QUESTS_LIST_RESOURCE, LOGOUT_RESOURCE,
        QUEST_RESOURCE, RESTART_RESOURCE,
        CREATE_QUEST_RESOURCE, DELETE_QUEST_RESOURCE
})
public class LoginFilter extends HttpFilter {
    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException
    {
        HttpSession session = req.getSession();
        Long userId = (Long) session.getAttribute(ATTRIBUTE_USER_ID);
        if (isNull(userId)) {
            res.sendRedirect(LOGIN_RESOURCE);
        } else {
            chain.doFilter(req, res);
        }
    }
}