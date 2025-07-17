package com.pixelmind.pixelmind_api.dto.game;

import com.pixelmind.pixelmind_api.model.game.Clue;
import lombok.Data;

@Data
public class ClueDTO {
    private Long id;
    private Integer orderNumber;
    private String content;
    private String answer;

    public Clue toEntity() {
        Clue clue = new Clue();
        clue.setId(id);
        clue.setOrderNumber(orderNumber);
        clue.setContent(content);
        clue.setAnswer(answer);
        return clue;
    }

    public static ClueDTO fromEntity(Clue clue) {
        ClueDTO dto = new ClueDTO();
        dto.setId(clue.getId());
        dto.setOrderNumber(clue.getOrderNumber());
        dto.setContent(clue.getContent());
        dto.setAnswer(clue.getAnswer());
        return dto;
    }
}
