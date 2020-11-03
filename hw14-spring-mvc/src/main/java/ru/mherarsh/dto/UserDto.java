package ru.mherarsh.dto;

import lombok.Getter;
import lombok.Setter;
import ru.mherarsh.core.model.AddressDataSet;
import ru.mherarsh.core.model.PhoneDataSet;
import ru.mherarsh.core.model.User;

import java.util.List;

@Getter
@Setter
public class UserDto {
    private long id;
    private String name;
    private String address;
    private String phone;

    public UserDto(User user) {
        id = user.getId();
        name = user.getName();
        address = user.getAddress().getStreet();

        user.getPhones().stream().findFirst().ifPresent(phoneDataSet -> phone = phoneDataSet.getNumber());
    }

    public User toUser(){
        var user = User.builder().name(name).build();
        user.setAddress(AddressDataSet.builder().street(address).build());
        user.setPhones(List.of(
                PhoneDataSet.builder().number(phone).user(user).build()
        ));

        return user;
    }
}
