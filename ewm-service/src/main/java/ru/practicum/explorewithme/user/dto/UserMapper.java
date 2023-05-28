package ru.practicum.explorewithme.user.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.explorewithme.user.User;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class UserMapper {

    public static UserDto toDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }

    public static ShortUserDto toShortDto(User user) {
        return new ShortUserDto(user.getId(), user.getName());
    }

    public static List<UserDto> toDto(Collection<User> users) {
        return users.stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }

    public static User toEntity(UserDto userDto) {
        return new User(userDto.getId(), userDto.getName(), userDto.getEmail());
    }

}
