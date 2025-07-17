package com.pixelmind.pixelmind_api.model.game;

import com.pixelmind.pixelmind_api.enums.GameStatus;
import com.pixelmind.pixelmind_api.model.store.NftItem;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "games")
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String summary;

    @Column(precision = 10, scale = 2)
    private BigDecimal value;

    @Column(name = "global_clue", columnDefinition = "TEXT")
    private String globalClue;

    private String answer;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Integer clueIntervalMinutes;

    private String difficulty;

    private Integer currentClue = 0;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private GameStatus status = GameStatus.not_resolved;

    @OneToOne
    private NftItem firstPlaceReward; // NFT exclusiva para o 1º lugar

    @OneToOne
    private NftItem topPlayersReward; // NFT para os demais ganhadores

    private Integer topPlayersCount; // Quantos jogadores (além do 1º) ganham a recompensa

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Clue> clues;
}
