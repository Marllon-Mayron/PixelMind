package com.pixelmind.pixelmind_api.service;

import com.pixelmind.pixelmind_api.dto.game.GameDTO;
import com.pixelmind.pixelmind_api.model.game.Clue;
import com.pixelmind.pixelmind_api.model.game.Game;
import com.pixelmind.pixelmind_api.model.store.NftItem;
import com.pixelmind.pixelmind_api.repository.GameRepository;
import com.pixelmind.pixelmind_api.repository.NftItemRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GameService {

    private final GameRepository gameRepository;
    private final NftItemRepository nftRepository;

    @Transactional
    public GameDTO createGame(GameDTO dto) {
        NftItem first = nftRepository.findById(dto.getFirstPlaceRewardId())
                .orElseThrow(() -> new EntityNotFoundException("NFT de primeiro lugar não encontrada"));

        NftItem top = nftRepository.findById(dto.getTopPlayersRewardId())
                .orElseThrow(() -> new EntityNotFoundException("NFT de top jogadores não encontrada"));

        Game game = dto.toEntity(first, top);
        Game saved = gameRepository.save(game);

        return GameDTO.fromEntity(saved);
    }


    @Transactional(readOnly = true)
    public List<GameDTO> listAll() {
        return gameRepository.findAll()
                .stream()
                .map(GameDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public GameDTO updateGame(Long id, GameDTO dto) {
        return gameRepository.findById(id).map(game -> {
            // Atualiza campos do game
            game.setTitle(dto.getTitle());
            game.setSummary(dto.getSummary());
            game.setValue(dto.getValue());
            game.setGlobalClue(dto.getGlobalClue());
            game.setAnswer(dto.getAnswer());
            game.setStartTime(dto.getStartTime());
            game.setEndTime(dto.getEndTime());
            game.setClueIntervalMinutes(dto.getClueIntervalMinutes());
            game.setDifficulty(dto.getDifficulty());
            game.setCurrentClue(dto.getCurrentClue());
            game.setStatus(dto.getStatus());
            game.setFirstPlaceReward(nftRepository.getReferenceById(dto.getFirstPlaceRewardId()));
            game.setTopPlayersReward(nftRepository.getReferenceById(dto.getTopPlayersRewardId()));
            game.setTopPlayersCount(dto.getTopPlayersCount());


            // Atualizar as clues:
            game.getClues().clear(); // remove as antigas

            if (dto.getClues() != null) {
                dto.getClues().forEach(clueDTO -> {
                    Clue clue = clueDTO.toEntity();
                    clue.setGame(game);
                    game.getClues().add(clue);
                });
            }

            Game updated = gameRepository.save(game);
            return GameDTO.fromEntity(updated);
        }).orElseThrow(() -> new RuntimeException("Game not found with id " + id));
    }

    @Transactional
    public void deleteGame(Long id) {
        if (!gameRepository.existsById(id)) {
            throw new RuntimeException("Game not found with id " + id);
        }
        gameRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public GameDTO getById(Long id) {
        return gameRepository.findById(id)
                .map(GameDTO::fromEntity)
                .orElseThrow(() -> new RuntimeException("Game not found with id " + id));
    }

    @Transactional(readOnly = true)
    public GameDTO getTodayGame() {
        LocalDateTime now = LocalDateTime.now();
        System.out.println("NOW: " + now);

        return gameRepository.findFirstByStartTimeBeforeAndEndTimeAfter(now, now)
                .map(GameDTO::fromEntity)
                .orElseThrow(() -> {
                    System.out.println("Nenhum jogo ativo encontrado!");
                    return new NoSuchElementException("Nenhum jogo ativo hoje.");
                });
    }
}
