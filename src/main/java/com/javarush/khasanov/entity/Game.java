package com.javarush.khasanov.entity;

import lombok.Data;

@Data
public class Game implements Identifiable {
    private Long id;
    private Quest quest;
    private Question currentQuestion;
}