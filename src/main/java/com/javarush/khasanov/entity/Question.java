package com.javarush.khasanov.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Question implements Identifiable {
    private Long id;
    private String text;
    private boolean isEnding;
}