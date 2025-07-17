// UserClueStatusDTO.java
package com.pixelmind.pixelmind_api.dto.game;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserClueStatusDTO {
    private Long userId;
    private Long clueId;
    private Boolean correct;
    private String status;
    private LocalDateTime answeredAt;
}
