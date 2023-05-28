package ru.practicum.explorewithme.user.dto;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.practicum.explorewithme.user.User;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapperMapStruct {

    UserDto toDto(User user);

    ShortUserDto toShortUserDto(User user);

    List<UserDto> toDto(Collection<User> users);

    User toEntity(UserDto userDto);

}
