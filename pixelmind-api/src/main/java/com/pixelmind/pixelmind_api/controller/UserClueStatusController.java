// UserClueStatusController.java
package com.pixelmind.pixelmind_api.controller;

import com.pixelmind.pixelmind_api.dto.game.UserClueStatusDTO;
import com.pixelmind.pixelmind_api.model.game.UserClueStatus;
import com.pixelmind.pixelmind_api.service.UserClueStatusService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/user-clue-status")
public class UserClueStatusController {

    private final UserClueStatusService service;

    public UserClueStatusController(UserClueStatusService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> saveStatus(@RequestBody UserClueStatusDTO dto, Principal principal) {
        String email = principal.getName(); // email do usuário do token JWT
        UserClueStatus saved = service.saveOrUpdate(dto, email);
        return ResponseEntity.ok("");
    }

    @GetMapping("/today")
    public ResponseEntity<List<UserClueStatusDTO>> getTodayStatuses(Principal principal) {
        String email = principal.getName();
        List<UserClueStatus> statuses = service.findStatusesByUserAndTodayGame(email);

        List<UserClueStatusDTO> dtos = statuses.stream()
                .map(status -> {
                    UserClueStatusDTO dto = new UserClueStatusDTO();
                    dto.setUserId(status.getUser().getId());
                    dto.setClueId(status.getClue().getId());
                    dto.setCorrect(status.getCorrect());
                    dto.setStatus(status.getStatus());
                    dto.setAnsweredAt(status.getAnsweredAt());
                    return dto;
                })
                .toList();

        return ResponseEntity.ok(dtos);
    }


}
