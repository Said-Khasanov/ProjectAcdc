package com.javarush.khasanov.service;

import com.javarush.khasanov.entity.Game;
import com.javarush.khasanov.entity.GameState;
import com.javarush.khasanov.entity.User;
import com.javarush.khasanov.repository.GameRepository;
import com.javarush.khasanov.repository.UserRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class StatisticsService {

    private final UserRepository userRepository;
    private final GameRepository gameRepository;

    public StatisticsService(UserRepository userRepository, GameRepository gameRepository) {
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
    }

    public Map<String, Map<GameState, Long>> calculate() {
        ArrayList<User> users = new ArrayList<>(userRepository.getAll());
        Map<String, Map<GameState, Long>> statsMap = new HashMap<>(users.size());
        for (User user : users) {
            Map<Long, Long> questGameMap = user.getQuestGameMap();
            Map<GameState, Long> countMap = new HashMap<>();

            countMap.put(GameState.PLAYING, 0L);
            countMap.put(GameState.FINISHED, 0L);

            for (var entry : questGameMap.entrySet()) {
                Long gameId = entry.getValue();

                Optional<Game> optionalGame = gameRepository.get(gameId);
                if (optionalGame.isPresent()) {
                    Game game = optionalGame.get();
                    GameState state = game.getState();
                    Long count = countMap.get(state);
                    countMap.put(state, ++count);
                }
            }
            statsMap.put(user.getName(), countMap);
        }
        return  statsMap;
    }


}