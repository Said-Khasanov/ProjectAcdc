package com.javarush.khasanov.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements Identifiable {
    private Long id;
    private String name;
    private String password;
    private final Map<Long, Long> questGameMap = new HashMap<>();
}