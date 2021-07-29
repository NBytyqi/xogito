package com.xogito.userprojectmanagement.mappers;

import com.xogito.userprojectmanagement.entities.dtos.UserDto;
import com.xogito.userprojectmanagement.entities.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(
        componentModel = "spring"
)
public interface UserToDtoMapper {

    User mapDtoToUser(UserDto userDto);
    List<User> mapDtosToUsers(List<UserDto> userDtos);

    UserDto mapUserToDto(User user);
    List<UserDto> mapUsersToUserDtos(List<User> users);
}