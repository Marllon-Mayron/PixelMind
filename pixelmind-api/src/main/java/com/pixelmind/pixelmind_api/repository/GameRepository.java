package com.pixelmind.pixelmind_api.repository;

import com.pixelmind.pixelmind_api.model.game.Game;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface GameRepository extends JpaRepository<Game, Long> {
    Optional<Game> findFirstByStartTimeBeforeAndEndTimeAfter(LocalDateTime now1, LocalDateTime now2);
}

