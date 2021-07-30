package com.xogito.userprojectmanagement.services.impl;

import com.xogito.userprojectmanagement.entities.User;
import com.xogito.userprojectmanagement.entities.dtos.UserDto;
import com.xogito.userprojectmanagement.exceptions.UserNotFoundException;
import com.xogito.userprojectmanagement.mappers.UserToDtoMapper;
import com.xogito.userprojectmanagement.repositories.UserRepository;
import com.xogito.userprojectmanagement.services.UserService;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserToDtoMapper userMapper = Mappers.getMapper(UserToDtoMapper.class);

    @Override
    public UserDto saveUser(UserDto userDto) {
        User savedUser = userRepository.save(userMapper.mapDtoToUser(userDto));
        return userMapper.mapUserToDto(savedUser);
    }

    @Override
    public void deleteUser(Long userId) throws UserNotFoundException {
        User existingUser = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        userRepository.delete(existingUser);
    }

    @Override
    public UserDto updateUser(UserDto userDto, Long userId) throws UserNotFoundException {
        User existingUser = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        if(Optional.ofNullable(userDto).isPresent()) {
            if(Optional.ofNullable(userDto.getName()).isPresent()) {
                existingUser.setName(userDto.getName());
            }
            if(Optional.ofNullable(userDto.getEmail()).isPresent()) {
                existingUser.setEmail(userDto.getEmail());
            }
        }
        return userMapper.mapUserToDto(userRepository.save(existingUser));
    }

    @Override
    public List<UserDto> getAllUsers(Pageable pageable) {
        return userMapper.mapUsersToUserDtos(userRepository.findAll(pageable).getContent());
    }

    @Override
    public List<UserDto> getUsersByNameOrEmail(String query, Pageable pageable) {
        return userMapper.mapUsersToUserDtos(userRepository.findByNameContainingOrEmailContaining(query, query, pageable));
    }
}