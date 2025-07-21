package com.pixelmind.pixelmind_api.service;

import com.pixelmind.pixelmind_api.dto.UserStatsDTO;
import com.pixelmind.pixelmind_api.model.User;
import com.pixelmind.pixelmind_api.model.UserStats;
import com.pixelmind.pixelmind_api.repository.UserStatsRepository;
import com.pixelmind.pixelmind_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserStatsService {

    private final UserStatsRepository userStatsRepository;
    private final UserRepository userRepository;

    public Optional<UserStatsDTO> findByUserId(Long userId) {
        return userRepository.findById(userId)
                .flatMap(userStatsRepository::findByUser)
                .map(UserStatsDTO::fromEntity);
    }

    public UserStatsDTO saveOrUpdate(UserStatsDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        UserStats entity = dto.toEntity(user);

        UserStats saved = userStatsRepository.save(entity);
        return UserStatsDTO.fromEntity(saved);
    }

    public void deleteById(Long id) {
        userStatsRepository.deleteById(id);
    }

    public Optional<UserStatsDTO> findById(Long id) {
        return userStatsRepository.findById(id).map(UserStatsDTO::fromEntity);
    }
}
