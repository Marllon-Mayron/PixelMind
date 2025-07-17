package com.pixelmind.pixelmind_api.service;

import com.pixelmind.pixelmind_api.dto.UserGameHistoryDTO;
import com.pixelmind.pixelmind_api.model.User;
import com.pixelmind.pixelmind_api.model.game.Game;
import com.pixelmind.pixelmind_api.model.game.UserGameHistory;
import com.pixelmind.pixelmind_api.model.store.NftItem;
import com.pixelmind.pixelmind_api.model.store.UserNft;
import com.pixelmind.pixelmind_api.repository.GameRepository;
import com.pixelmind.pixelmind_api.repository.UserGameHistoryRepository;
import com.pixelmind.pixelmind_api.repository.UserNftRepository;
import com.pixelmind.pixelmind_api.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserGameHistoryService {

    private final UserGameHistoryRepository historyRepository;
    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final UserNftRepository userNftRepository;

    public UserGameHistoryService(
            UserGameHistoryRepository historyRepository,
            UserRepository userRepository,
            UserNftRepository userNftRepository,
            GameRepository gameRepository) {
        this.historyRepository = historyRepository;
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
        this.userNftRepository = userNftRepository;
    }

    public void saveHistory(UserGameHistoryDTO dto, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        Game game = gameRepository.findById(dto.getGameId())
                .orElseThrow(() -> new EntityNotFoundException("Game não encontrado"));

        UserGameHistory history = new UserGameHistory();
        history.setUser(user);
        history.setGame(game);
        history.setScore(dto.getScore());
        history.setWon(dto.getWon());
        history.setPrizeEarned(dto.getPrizeEarned());
        history.setCorrectAnswers(dto.getCorrectAnswers());

        historyRepository.save(history);

        // Se ganhou, verificar e aplicar recompensa
        if (dto.getWon()) {
            int position = getUserPosition(game.getId(), userEmail);

            if (position == 1 && game.getFirstPlaceReward() != null) {
                giveUserNftIfNotExists(user, game.getFirstPlaceReward());
            } else if (position > 1 && position <= (1 + (game.getTopPlayersCount() != null ? game.getTopPlayersCount() : 0))) {
                if (game.getTopPlayersReward() != null) {
                    giveUserNftIfNotExists(user, game.getTopPlayersReward());
                }
            }
        }
    }

    private void giveUserNftIfNotExists(User user, NftItem nftItem) {
        boolean alreadyHas = userNftRepository.existsByUserAndNftItem(user, nftItem);
        if (!alreadyHas) {
            UserNft userNft = new UserNft();
            userNft.setUser(user);
            userNft.setNftItem(nftItem);
            userNftRepository.save(userNft);
        }
    }


    public List<UserGameHistory> findAll() {
        return historyRepository.findAll();
    }

    public List<UserGameHistory> findByUserId(Long userId) {
        return historyRepository.findByUserId(userId);
    }

    public List<UserGameHistory> findByGameId(Long gameId) {
        return historyRepository.findByGameId(gameId);
    }
    public int getUserPosition(Long gameId, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        // Buscar todos os vencedores do jogo, ordenados por `playedAt`
        List<UserGameHistory> winners = historyRepository
                .findByGameIdAndWonTrueOrderByPlayedAtAsc(gameId);

        for (int i = 0; i < winners.size(); i++) {
            if (winners.get(i).getUser().getId().equals(user.getId())) {
                return i + 1; // posição começa em 1
            }
        }

        return -1; // não encontrado
    }

}
