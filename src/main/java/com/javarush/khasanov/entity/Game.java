package com.javarush.khasanov.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Game implements Identifiable {
    private Long id;
    private Quest quest;
    private Question currentQuestion;
    @Builder.Default
    private GameState state = GameState.PLAYING;
}