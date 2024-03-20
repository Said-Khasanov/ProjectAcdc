package com.javarush.khasanov.entity;

import lombok.Data;

@Data
public class Answer implements Identifiable {
    private Long id;
    private String text;
    private Long idNextQuestion;
    private boolean isDeadEnd;
}
