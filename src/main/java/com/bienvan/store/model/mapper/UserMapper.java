package com.bienvan.store.model.mapper;

import com.bienvan.store.dto.UserDto;
import com.bienvan.store.model.User;

public class UserMapper {
    public static UserDto toUserDto(User user){
        UserDto tmp = new UserDto();
        tmp.setId(user.getId());
        tmp.setEmail(user.getEmail());
        tmp.setName(user.getName());
        tmp.setGender(user.getGender());
        return tmp;
    }
}
