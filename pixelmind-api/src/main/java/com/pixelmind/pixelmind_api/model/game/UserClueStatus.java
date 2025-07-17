package com.pixelmind.pixelmind_api.model.game;

import com.pixelmind.pixelmind_api.model.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "user_clue_status")
public class UserClueStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "clue_id", nullable = false)
    private Clue clue;

    private LocalDateTime answeredAt = LocalDateTime.now();

    private Boolean correct;

    private String status = "pending"; // "pending" ou "resolved"
}
