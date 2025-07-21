package com.pixelmind.pixelmind_api.controller;

import com.pixelmind.pixelmind_api.config.JwtUtil;
import com.pixelmind.pixelmind_api.dto.UserCreateDTO;
import com.pixelmind.pixelmind_api.model.User;
import com.pixelmind.pixelmind_api.model.UserStats;
import com.pixelmind.pixelmind_api.repository.UserRepository;
import com.pixelmind.pixelmind_api.repository.UserStatsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserStatsRepository userStatsRepository;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserCreateDTO dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email already registered.");
        }

        User user = new User();
        user.setName(dto.getName());
        user.setUserName(dto.getUserName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setUf(dto.getUf());
        user.setAge(dto.getAge());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setPrivileges("user");

        userRepository.save(user);

        UserStats stats = new UserStats();
        stats.setUser(user); // associa o user
        userStatsRepository.save(stats);

        String token = jwtUtil.generateToken(user.getEmail(), user.getPrivileges());

        return ResponseEntity.ok(Map.of(
                "message", "User registered successfully!",
                "token", token
        ));
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User login) {
        var optUser = userRepository.findByEmail(login.getEmail());

        if (optUser.isPresent() && passwordEncoder.matches(login.getPassword(), optUser.get().getPassword())) {
            String token = jwtUtil.generateToken(optUser.get().getEmail(), optUser.get().getPrivileges());
            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "privileges", optUser.get().getPrivileges()
            ));
        } else {
            return ResponseEntity.status(401).body("Invalid email or password.");
        }
    }

}
