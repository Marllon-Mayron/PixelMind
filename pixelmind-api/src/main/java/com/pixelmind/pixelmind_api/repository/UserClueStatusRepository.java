package com.pixelmind.pixelmind_api.repository;

import com.pixelmind.pixelmind_api.model.game.UserClueStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface UserClueStatusRepository extends JpaRepository<UserClueStatus, Long> {

    @Query("""
    SELECT ucs FROM UserClueStatus ucs
    JOIN ucs.clue c
    JOIN c.game g
    WHERE ucs.user.id = :userId
    AND g.startTime BETWEEN :startOfDay AND :endOfDay
    """)
    List<UserClueStatus> findStatusesByUserAndGameDate(
            @Param("userId") Long userId,
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay
    );
}
