package com.pixelmind.pixelmind_api.repository;

import com.pixelmind.pixelmind_api.model.game.UserGameHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserGameHistoryRepository extends JpaRepository<UserGameHistory, Long> {
    List<UserGameHistory> findByUserId(Long userId);
    List<UserGameHistory> findByGameId(Long gameId);
    List<UserGameHistory> findByGameIdAndWonTrueOrderByPlayedAtAsc(Long gameId);
}