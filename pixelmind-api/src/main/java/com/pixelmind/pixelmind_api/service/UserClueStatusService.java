// UserClueStatusService.java
package com.pixelmind.pixelmind_api.service;

import com.pixelmind.pixelmind_api.dto.game.UserClueStatusDTO;
import com.pixelmind.pixelmind_api.model.game.Clue;
import com.pixelmind.pixelmind_api.model.User;
import com.pixelmind.pixelmind_api.model.game.UserClueStatus;
import com.pixelmind.pixelmind_api.repository.ClueRepository;
import com.pixelmind.pixelmind_api.repository.UserClueStatusRepository;
import com.pixelmind.pixelmind_api.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserClueStatusService {

    private final UserRepository userRepository;
    private final ClueRepository clueRepository;
    private final UserClueStatusRepository statusRepository;

    public UserClueStatusService(UserRepository userRepository,
                                 ClueRepository clueRepository,
                                 UserClueStatusRepository statusRepository) {
        this.userRepository = userRepository;
        this.clueRepository = clueRepository;
        this.statusRepository = statusRepository;
    }

    public UserClueStatus saveOrUpdate(UserClueStatusDTO dto, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Clue clue = clueRepository.findById(dto.getClueId())
                .orElseThrow(() -> new RuntimeException("Clue not found"));

        UserClueStatus status = new UserClueStatus();
        status.setUser(user);
        status.setClue(clue);
        status.setCorrect(dto.getCorrect());
        status.setStatus(dto.getStatus());
        status.setAnsweredAt(LocalDateTime.now());

        return statusRepository.save(status);
    }

    public List<UserClueStatus> findStatusesByUserAndTodayGame(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.plusDays(1).atStartOfDay().minusNanos(1);

        return statusRepository.findStatusesByUserAndGameDate(user.getId(), startOfDay, endOfDay);
    }


}
