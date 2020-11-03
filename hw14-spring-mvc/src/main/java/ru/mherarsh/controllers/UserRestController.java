package ru.mherarsh.controllers;

import org.springframework.web.bind.annotation.*;
import ru.mherarsh.core.service.DBServiceUser;
import ru.mherarsh.dto.UserDto;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class UserRestController {
    private final DBServiceUser usersService;

    public UserRestController(DBServiceUser usersService) {
        this.usersService = usersService;
    }

    @GetMapping("/api/users")
    public List<UserDto> getAllUsers() {
        return usersService.findAll().stream()
                .map(UserDto::new).collect(Collectors.toList());
    }

    @PostMapping("/api/users")
    public UserDto saveUser(@RequestBody UserDto userDto) {
        if (userDto.getName() == null || userDto.getName().isBlank()) {
            throw new IllegalArgumentException("name is empty");
        }

        var userId = usersService.saveUser(userDto.toUser());
        userDto.setId(userId);

        return userDto;
    }
}

