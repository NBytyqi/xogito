package com.xogito.userprojectmanagement.services;

import com.xogito.userprojectmanagement.entities.dtos.UserDto;
import com.xogito.userprojectmanagement.exceptions.UserNotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    UserDto saveUser(UserDto userDto);
    void deleteUser(Long userId) throws UserNotFoundException;
    UserDto updateUser(UserDto userDto, Long userId) throws UserNotFoundException;
    List<UserDto> getAllUsers(Pageable pageable);
    List<UserDto> getUsersByNameOrEmail(String query, Pageable pageable);
}