package com.fosm.lysaai_core_api.application.service;

import com.fosm.lysaai_core_api.application.dto.user.UserRequestDto;
import com.fosm.lysaai_core_api.application.dto.user.UserResponseDto;
import com.fosm.lysaai_core_api.domain.model.User;
import com.fosm.lysaai_core_api.domain.port.in.UserService;
import com.fosm.lysaai_core_api.domain.port.out.UserRepository;
import com.fosm.lysaai_core_api.infrastructure.config.exceptions.AlreadyExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.nevmock.digivise.utils.UtilsKt.hashPasswordBcrypt;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserResponseDto createUser(UserRequestDto user) {
        Timestamp now = new Timestamp(System.currentTimeMillis());

        User newUser = new User();

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new AlreadyExistException(
                    "User already exists with email: " + user.getEmail() + ". Please choose a different email."
            );
        }

        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new AlreadyExistException(
                    "User already exists with username: " + user.getUsername() + ". Please choose a different username."
            );
        }

        newUser.setId(UUID.randomUUID());
        newUser.setName(user.getName());
        newUser.setUsername(user.getUsername());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(hashPasswordBcrypt(user.getPassword()));
        newUser.setCreatedAt(now);
        newUser.setUpdatedAt(now);

        userRepository.save(newUser);

        return toDto(newUser);
    }

    @Override
    public UserResponseDto getUserById(UUID userId) {
        User newUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        return toDto(newUser);
    }

    @Override
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponseDto updateUser(UUID userId, UserRequestDto updatedUser) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        existingUser.setName(updatedUser.getName());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setPassword(updatedUser.getPassword());
        existingUser.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        userRepository.save(existingUser);

        return toDto(existingUser);
    }

    @Override
    public void deleteUser(UUID userId) {
        userRepository.deleteById(userId);
    }

    private UserResponseDto toDto(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .createdAt(Timestamp.valueOf(user.getCreatedAt().toString()))
                .updatedAt(Timestamp.valueOf(user.getUpdatedAt().toString()))
                .build();
    }
}
