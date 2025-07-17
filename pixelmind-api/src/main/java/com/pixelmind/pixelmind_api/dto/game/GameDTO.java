package com.pixelmind.pixelmind_api.dto.game;

import com.pixelmind.pixelmind_api.enums.GameStatus;
import com.pixelmind.pixelmind_api.model.game.Clue;
import com.pixelmind.pixelmind_api.model.game.Game;
import com.pixelmind.pixelmind_api.model.store.NftItem;
import com.pixelmind.pixelmind_api.service.NftItemService;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class GameDTO {
    private Long id;
    private String title;
    private String summary;
    private BigDecimal value;
    private String globalClue;
    private String answer;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer clueIntervalMinutes;
    private String difficulty;
    private Integer currentClue;
    private GameStatus status;
    private Long firstPlaceRewardId;
    private Long topPlayersRewardId;
    private Integer topPlayersCount;
    private List<ClueDTO> clues;

    // Converte Entity -> DTO
    public static GameDTO fromEntity(Game game) {
        GameDTO dto = new GameDTO();
        dto.setId(game.getId());
        dto.setTitle(game.getTitle());
        dto.setSummary(game.getSummary());
        dto.setValue(game.getValue());
        dto.setGlobalClue(game.getGlobalClue());
        dto.setAnswer(game.getAnswer());
        dto.setStartTime(game.getStartTime());
        dto.setEndTime(game.getEndTime());
        dto.setClueIntervalMinutes(game.getClueIntervalMinutes());
        dto.setDifficulty(game.getDifficulty());
        dto.setCurrentClue(game.getCurrentClue());
        dto.setStatus(game.getStatus());
        dto.setFirstPlaceRewardId(game.getFirstPlaceReward() != null ? game.getFirstPlaceReward().getId() : null);
        dto.setTopPlayersRewardId(game.getTopPlayersReward() != null ? game.getTopPlayersReward().getId() : null);
        dto.setTopPlayersCount(game.getTopPlayersCount());

        if (game.getClues() != null) {
            dto.setClues(game.getClues().stream()
                    .map(ClueDTO::fromEntity)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    // Converte DTO -> Entity (obs: não carrega os NFTs, isso deve ser feito no service com o repository)
    public Game toEntity(NftItem firstPlaceReward, NftItem topPlayersReward) {
        Game game = new Game();
        game.setId(this.id);
        game.setTitle(this.title);
        game.setSummary(this.summary);
        game.setValue(this.value);
        game.setGlobalClue(this.globalClue);
        game.setAnswer(this.answer);
        game.setStartTime(this.startTime);
        game.setEndTime(this.endTime);
        game.setClueIntervalMinutes(this.clueIntervalMinutes);
        game.setDifficulty(this.difficulty);
        game.setCurrentClue(this.currentClue);
        game.setStatus(this.status);
        game.setTopPlayersCount(this.topPlayersCount);

        game.setFirstPlaceReward(firstPlaceReward);
        game.setTopPlayersReward(topPlayersReward); // ← aqui estava o problema!

        if (this.clues != null) {
            List<Clue> clues = this.clues.stream()
                    .map(clueDTO -> {
                        Clue clue = clueDTO.toEntity();
                        clue.setGame(game);
                        return clue;
                    })
                    .collect(Collectors.toList());
            game.setClues(clues);
        }

        return game;
    }

}
