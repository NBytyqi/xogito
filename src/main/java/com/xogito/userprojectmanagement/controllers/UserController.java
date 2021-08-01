package com.xogito.userprojectmanagement.controllers;

import com.xogito.userprojectmanagement.entities.dtos.UserDto;
import com.xogito.userprojectmanagement.exceptions.UserNotFoundException;
import com.xogito.userprojectmanagement.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers(@PageableDefault(page = 0, size = 10) Pageable pageable) {
        return new ResponseEntity(userService.getAllUsers(pageable), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        return new ResponseEntity(userService.saveUser(userDto), HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId) throws UserNotFoundException {
        userService.deleteUser(userId);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long userId, @RequestBody UserDto userDto) throws UserNotFoundException {
        return new ResponseEntity(userService.updateUser(userDto, userId), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserDto>> getUsersByNameOrEmail(@RequestParam(required = true) String query, @PageableDefault(page = 0, size = 10) Pageable pageable) {
        return new ResponseEntity(userService.getUsersByNameOrEmail(query, pageable), HttpStatus.OK);
    }
}
