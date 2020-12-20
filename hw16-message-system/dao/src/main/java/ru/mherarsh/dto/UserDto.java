package ru.mherarsh.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.mherarsh.core.model.AddressDataSet;
import ru.mherarsh.core.model.PhoneDataSet;
import ru.mherarsh.core.model.User;
import ru.otus.messagesystem.client.ResultDataType;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto extends ResultDataType {
    private long id;
    private String name;
    private String address;
    private String phone;

    public UserDto(long id){
        this.id = id;
        this.name = "";
        this.address = "";
        this.phone = "";
    }

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
