package com.javarush.khasanov.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Answer implements Identifiable {
    private Long id;
    private String text;
    private Long nextQuestionId;
    private boolean isDeadEnd;
}
