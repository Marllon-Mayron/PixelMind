package com.pixelmind.pixelmind_api.dto;

import com.pixelmind.pixelmind_api.model.User;
import com.pixelmind.pixelmind_api.model.UserStats;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserStatsDTO {
    private Long id;
    private Long userId;
    private Integer ticket;
    private Integer gamesWon;
    private Integer gamesPlayed;
    private Double totalEarnings;
    private Integer offensiveDays;
    private Integer bestStreak;
    private LocalDateTime createdAt;

    public static UserStatsDTO fromEntity(UserStats entity) {
        UserStatsDTO dto = new UserStatsDTO();
        dto.setId(entity.getId());
        dto.setUserId(entity.getUser() != null ? entity.getUser().getId() : null);
        dto.setTicket(entity.getTicket());
        dto.setGamesWon(entity.getGamesWon());
        dto.setGamesPlayed(entity.getGamesPlayed());
        dto.setTotalEarnings(entity.getTotalEarnings());
        dto.setOffensiveDays(entity.getOffensiveDays());
        dto.setBestStreak(entity.getBestStreak());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }

    public UserStats toEntity(User user) {
        UserStats entity = new UserStats();
        entity.setId(this.id);
        entity.setUser(user);
        entity.setTicket(this.ticket != null ? this.ticket : 0);
        entity.setGamesWon(this.gamesWon != null ? this.gamesWon : 0);
        entity.setGamesPlayed(this.gamesPlayed != null ? this.gamesPlayed : 0);
        entity.setTotalEarnings(this.totalEarnings != null ? this.totalEarnings : 0.00);
        entity.setOffensiveDays(this.offensiveDays != null ? this.offensiveDays : 0);
        entity.setBestStreak(this.bestStreak != null ? this.bestStreak : 0);
        entity.setCreatedAt(this.createdAt != null ? this.createdAt : LocalDateTime.now());
        return entity;
    }
}
