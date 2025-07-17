package com.pixelmind.pixelmind_api.repository;

import com.pixelmind.pixelmind_api.model.game.Clue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClueRepository extends JpaRepository<Clue, Long> {
}
