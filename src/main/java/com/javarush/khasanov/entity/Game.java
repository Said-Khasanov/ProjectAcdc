package com.javarush.khasanov.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Game implements Identifiable {
    private Long id;
    private Quest quest;
    private Question currentQuestion;
    @Builder.Default
    private GameState state = GameState.PLAYING;
}