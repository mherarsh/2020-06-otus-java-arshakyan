package ru.mherarsh.controllers;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import ru.mherarsh.dto.ErrorDto;
import ru.mherarsh.dto.UserDto;
import ru.mherarsh.service.AsyncUserService;

@Controller
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final SimpMessagingTemplate template;
    private final Gson gson;
    private final AsyncUserService asyncUserService;

    public UserController(SimpMessagingTemplate template, Gson gson, AsyncUserService asyncUserService) {
        this.template = template;
        this.gson = gson;
        this.asyncUserService = asyncUserService;
    }

    @MessageMapping("/putUser.{sessionId}")
    public void putUser(@DestinationVariable String sessionId, UserDto userDto) {
        try {
            if (userDto.getName() == null || userDto.getName().isBlank()) {
                throw new IllegalArgumentException("name is empty");
            }

            asyncUserService.saveUser(userDto, data -> usersChanges());
        } catch (Exception e) {
            errorHandler(sessionId, e);
        }
    }

    @MessageMapping("/getUsers.{sessionId}")
    public void getUsers(@DestinationVariable String sessionId) {
        asyncUserService.findAll(data -> sentMessage("/topic/getUsersResponse." + sessionId, data.getUsers()));
    }

    private void usersChanges() {
        asyncUserService.findAll(data -> sentMessage("/topic/usersChanges", data.getUsers()));
    }

    private void errorHandler(String sessionId, Exception e) {
        logger.error(sessionId, e);

        var errorDto = new ErrorDto(e.getMessage());

        sentMessage("/topic/sessionErrors." + sessionId, errorDto);
    }

    private void sentMessage(String description, Object data){
        template.convertAndSend(description, gson.toJson(data));
    }
}
