package com.pixelmind.pixelmind_api.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "user_stats")
public class UserStats {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @Column(name = "ticket")
    private Integer ticket = 0;

    @Column(name = "games_won")
    private Integer gamesWon = 0;

    @Column(name = "games_played")
    private Integer gamesPlayed = 0;

    @Column(name = "total_earnings")
    private Double totalEarnings = 0.00;

    @Column(name = "offensive_days")
    private Integer offensiveDays = 0;

    @Column(name = "best_streak")
    private Integer bestStreak = 0;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}
