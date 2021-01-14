package ru.mherarsh.controllers;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import ru.mherarsh.config.AppProperties;
import ru.mherarsh.dto.UserDto;
import ru.mherarsh.service.AsyncUserService;

@Controller
public class UserController {
    private final AsyncUserService asyncUserService;

    public UserController(AsyncUserService asyncUserService, AppProperties appProperties) {
        this.asyncUserService = asyncUserService;
    }

    @MessageMapping("/putUser.{sessionId}")
    public void putUser(@DestinationVariable String sessionId, UserDto userDto) {
        if (userDto.getName() == null || userDto.getName().isBlank()) {
            throw new IllegalArgumentException("name is empty");
        }

        asyncUserService.saveUser(userDto, sessionId);
    }

    @MessageMapping("/getUsers.{sessionId}")
    public void getUsers(@DestinationVariable String sessionId) {
        asyncUserService.findAll(sessionId);
    }
}
