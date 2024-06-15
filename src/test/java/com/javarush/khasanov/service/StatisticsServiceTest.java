package com.javarush.khasanov.service;

import com.javarush.khasanov.repository.GameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class StatisticsServiceTest {
    private GameRepository gameRepository;
    private StatisticsService statisticsService;

    @BeforeEach
    void setUp() {
        gameRepository = Mockito.mock(GameRepository.class);
        statisticsService = new StatisticsService(gameRepository);
    }

    @Test
    void whenCalculateStats_thenCallsGetAllOnce() {
        statisticsService.calculate();
        Mockito.verify(gameRepository, Mockito.only()).getAll();
    }

}