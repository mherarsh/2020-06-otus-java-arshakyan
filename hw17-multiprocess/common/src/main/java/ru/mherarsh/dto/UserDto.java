package ru.mherarsh.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.otus.messagesystem.client.ResultDataType;

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
}
