package com.javarush.khasanov.entity;

import lombok.Data;

@Data
public class User implements Identifiable {
    private Long id;
    private String name;
    private String password;
}