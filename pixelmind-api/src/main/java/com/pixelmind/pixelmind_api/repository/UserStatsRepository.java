package com.pixelmind.pixelmind_api.repository;

import com.pixelmind.pixelmind_api.model.UserStats;
import com.pixelmind.pixelmind_api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserStatsRepository extends JpaRepository<UserStats, Long> {
    Optional<UserStats> findByUser(User user);
}
