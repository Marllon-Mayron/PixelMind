package com.pixelmind.pixelmind_api.dto;

import lombok.Data;

@Data
public class UserGameHistoryDTO {
    private Long userId;
    private Long gameId;
    private Integer score;
    private Boolean won;
    private Double prizeEarned;
    private Integer correctAnswers;
}
