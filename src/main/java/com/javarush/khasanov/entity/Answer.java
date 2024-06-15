package com.javarush.khasanov.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "answer")
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;

    @ManyToOne(cascade = CascadeType.ALL)
    @ToString.Exclude
    private Question question;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "next_question_id")
    @ToString.Exclude
    private Question nextQuestion;

    @Column(name = "dead_end")
    @Builder.Default
    private boolean isDeadEnd = false;
}
