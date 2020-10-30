package ru.mherarsh.controllers;

import org.springframework.web.bind.annotation.*;
import ru.mherarsh.core.model.AddressDataSet;
import ru.mherarsh.core.model.PhoneDataSet;
import ru.mherarsh.core.model.User;
import ru.mherarsh.core.service.DBServiceUser;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class UserRestController {
    private final DBServiceUser usersService;

    public UserRestController(DBServiceUser usersService) {
        this.usersService = usersService;
    }

    @GetMapping("/api/users")
    public List<UserViewModel> getAllUsers() {
        return usersService.findAll().stream()
                .map(UserViewModel::new).collect(Collectors.toList());
    }

    @PostMapping("/api/users")
    public UserViewModel saveUser(@RequestBody UserViewModel userViewModel) {
        userViewModel.id = saveUserToDb(userViewModel);

        return userViewModel;
    }

    private long saveUserToDb(UserViewModel userViewModel) {
        if (userViewModel.name == null || userViewModel.name.isBlank()) {
            throw new IllegalArgumentException("name is empty");
        }

        var user = User.builder().name(userViewModel.name).build();
        user.setAddress(AddressDataSet.builder().street(userViewModel.address).build());
        user.setPhones(List.of(
                PhoneDataSet.builder().number(userViewModel.phone).user(user).build()
        ));

        return usersService.saveUser(user);
    }
}

class UserViewModel {
    long id = 0;
    String name = "";
    String address = "";
    String phone = "";

    public UserViewModel(User user) {
        id = user.getId();
        name = user.getName();
        address = user.getAddress().getStreet();

        user.getPhones().stream().findFirst().ifPresent(phoneDataSet -> phone = phoneDataSet.getNumber());
    }
}