package com.pixelmind.pixelmind_api.model.game;

import com.pixelmind.pixelmind_api.model.User;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "user_game_history")
public class UserGameHistory {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    @Column(name = "played_at")
    private LocalDateTime playedAt = LocalDateTime.now();

    private Integer score;
    private Boolean won;

    @Column(name = "prize_earned")
    private Double prizeEarned = 0.00;

    @Column(name = "correct_answers")
    private Integer correctAnswers = 0;
}
