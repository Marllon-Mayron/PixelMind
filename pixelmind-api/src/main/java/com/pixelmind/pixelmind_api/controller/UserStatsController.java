package com.pixelmind.pixelmind_api.controller;

import com.pixelmind.pixelmind_api.dto.UserStatsDTO;
import com.pixelmind.pixelmind_api.model.User;
import com.pixelmind.pixelmind_api.service.UserService;
import com.pixelmind.pixelmind_api.service.UserStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/api/user-stats")
@RequiredArgsConstructor
public class UserStatsController {

    private final UserStatsService userStatsService;
    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserStatsDTO> getById(@PathVariable Long id) {
        Optional<UserStatsDTO> dto = userStatsService.findById(id);
        return dto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/me")
    public ResponseEntity<UserStatsDTO> getMe(Principal principal) {
        User user = userService.getUserByPrincipal(principal);
        Optional<UserStatsDTO> dto = userStatsService.findByUserId(user.getId());
        return dto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UserStatsDTO> createOrUpdate(@RequestBody UserStatsDTO dto) {
        try {
            UserStatsDTO saved = userStatsService.saveOrUpdate(dto);
            return ResponseEntity.ok(saved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userStatsService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
