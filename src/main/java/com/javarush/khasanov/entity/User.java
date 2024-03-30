package com.javarush.khasanov.entity;

import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
public class User implements Identifiable {
    private Long id;
    private String name;
    private String password;
    private final Map<Long, Long> questGameMap = new HashMap<>();
}