package com.pixelmind.pixelmind_api.model.game;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "clues")
public class Clue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_number")
    private Integer orderNumber;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String answer;

    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;
}
