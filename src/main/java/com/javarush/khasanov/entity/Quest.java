package com.javarush.khasanov.entity;

import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class Quest implements Identifiable {
    private Long id;
    private final Map<Question, List<Answer>> questions = new HashMap<>();
}
