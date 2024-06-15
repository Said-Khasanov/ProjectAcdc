package com.javarush.khasanov.service;

import com.javarush.khasanov.entity.Game;
import com.javarush.khasanov.entity.GameState;
import com.javarush.khasanov.entity.User;
import com.javarush.khasanov.repository.GameRepository;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


@RequiredArgsConstructor
public class StatisticsService {

    private final GameRepository gameRepository;

    public Map<String, Map<GameState, Long>> calculate() {
        ArrayList<Game> games = new ArrayList<>(gameRepository.getAll());
        Map<String, Map<GameState, Long>> statsMap = new HashMap<>();

        for (Game game : games) {
            User user = game.getUser();
            Map<GameState, Long> countMap = statsMap.get(user.getName());

            if (countMap == null) {
                countMap = new HashMap<>();
                for (GameState state : GameState.values()) {
                    countMap.put(state, 0L);
                }
            }

            GameState state = game.getState();
            Long count = countMap.get(state);
            countMap.put(state, ++count);
            statsMap.put(user.getName(), countMap);
        }

        return statsMap;
    }
}