package ru.practicum.explorewithme.user;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapperMapStruct {

    ShortUserDto toShortUserDto(User user);

    UserDto toUserDto(User user);

    User toUser(UserDto userDto);

    List<UserDto> toDtoList(List<User> users);
}
