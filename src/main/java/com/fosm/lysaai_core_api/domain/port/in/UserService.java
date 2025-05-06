package com.fosm.lysaai_core_api.domain.port.in;


import com.fosm.lysaai_core_api.application.dto.user.UserRequestDto;
import com.fosm.lysaai_core_api.application.dto.user.UserResponseDto;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserResponseDto createUser(UserRequestDto user);
    UserResponseDto getUserById(UUID userId);
    List<UserResponseDto> getAllUsers();
    UserResponseDto updateUser(UUID userId, UserRequestDto user);
    void deleteUser(UUID userId);
}