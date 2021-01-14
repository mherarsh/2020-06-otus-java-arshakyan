package ru.mherarsh.model;

import ru.mherarsh.dto.UserDto;
import ru.mherarsh.dto.UserListDto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserBuilder {
    private UserBuilder() {
    }

    public static User buildUser(UserDto userDto) {
        var user = User.builder().name(userDto.getName()).build();
        user.setAddress(AddressDataSet.builder().street(userDto.getAddress()).build());
        user.setPhones(List.of(
                PhoneDataSet.builder().number(userDto.getPhone()).user(user).build()
        ));

        return user;
    }

    public static UserDto buildUserDto(User user) {
        var userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setAddress(user.getAddress().getStreet());
        user.getPhones().stream().findFirst().ifPresent(phoneDataSet -> userDto.setPhone(phoneDataSet.getNumber()));

        return userDto;
    }

    public static UserListDto buildUserListDto(List<User> users) {
        return new UserListDto(users.stream().map(UserBuilder::buildUserDto).collect(Collectors.toList()));
    }
}
