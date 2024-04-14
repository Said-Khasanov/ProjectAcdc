package com.javarush.khasanov.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Answer implements Identifiable {
    private Long id;
    private String text;
    private Long nextQuestionId;
    private boolean isDeadEnd;
}
