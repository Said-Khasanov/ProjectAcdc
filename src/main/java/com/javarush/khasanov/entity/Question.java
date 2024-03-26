package com.javarush.khasanov.entity;

import lombok.Data;

@Data
public class Question implements Identifiable {
    private Long id;
    private String text;
    private boolean isEnding;
}