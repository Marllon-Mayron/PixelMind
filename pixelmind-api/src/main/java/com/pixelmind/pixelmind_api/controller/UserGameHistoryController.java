package com.pixelmind.pixelmind_api.controller;

import com.pixelmind.pixelmind_api.dto.UserGameHistoryDTO;
import com.pixelmind.pixelmind_api.model.game.UserGameHistory;
import com.pixelmind.pixelmind_api.service.UserGameHistoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user-game-history")
@CrossOrigin(origins = "*")
public class UserGameHistoryController {

    private final UserGameHistoryService service;

    public UserGameHistoryController(UserGameHistoryService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Void> saveUserHistory(@RequestBody UserGameHistoryDTO dto, Principal principal
    ) {
        String email = principal.getName();
        service.saveHistory(dto, email);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public List<UserGameHistory> getAll() {
        return service.findAll();
    }

    @GetMapping("/user/{userId}")
    public List<UserGameHistory> getByUser(@PathVariable Long userId) {
        return service.findByUserId(userId);
    }

    @GetMapping("/game/{gameId}")
    public List<UserGameHistory> getByGame(@PathVariable Long gameId) {
        return service.findByGameId(gameId);
    }

    @GetMapping("/position/{gameId}")
    public ResponseEntity<Map<String, Object>> getUserPosition(
            @PathVariable Long gameId,
            Principal principal  // aqui pega o usuário autenticado direto
    ) {
        int position = service.getUserPosition(gameId, principal.getName());
        Map<String, Object> response = new HashMap<>();
        response.put("position", position);
        return ResponseEntity.ok(response);
    }


}
