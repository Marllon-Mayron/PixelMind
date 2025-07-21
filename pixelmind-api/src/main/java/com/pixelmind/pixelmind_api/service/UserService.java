package com.pixelmind.pixelmind_api.service;

import com.pixelmind.pixelmind_api.dto.UserDTO;
import com.pixelmind.pixelmind_api.model.User;
import com.pixelmind.pixelmind_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getUserByPrincipal(Principal principal) {
        return userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found."));
    }

    public UserDTO getUserData(Principal principal) {
        User user = getUserByPrincipal(principal);
        return convertToDTO(user);
    }

    public UserDTO updateUser(Principal principal, UserDTO updatedData) {
        User user = getUserByPrincipal(principal);

        user.setName(updatedData.getName());
        user.setUserName(updatedData.getUserName());
        user.setPhone(updatedData.getPhone());
        user.setUf(updatedData.getUf());
        user.setAge(updatedData.getAge());
        userRepository.save(user);

        return convertToDTO(user);
    }

    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setName(user.getName());
        dto.setUserName(user.getUserName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setUf(user.getUf());
        dto.setAge(user.getAge());
        dto.setPrivileges(user.getPrivileges());
        return dto;
    }
}
