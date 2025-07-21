package com.pixelmind.pixelmind_api.controller;

import com.pixelmind.pixelmind_api.dto.UserDTO;
import com.pixelmind.pixelmind_api.model.User;
import com.pixelmind.pixelmind_api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getLoggedUser(Principal principal) {

        UserDTO user = UserDTO.fromEntity(userService.getUserByPrincipal(principal));


        System.out.println(user);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/me")
    public ResponseEntity<UserDTO> updateLoggedUser(@RequestBody UserDTO dto, Principal principal) {
        UserDTO updatedUser = userService.updateUser(principal, dto);
        return ResponseEntity.ok(updatedUser);
    }
}
