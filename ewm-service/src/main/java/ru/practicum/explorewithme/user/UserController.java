package ru.practicum.explorewithme.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.user.dto.UserDto;
import ru.practicum.explorewithme.user.dto.UserMapper;
import ru.practicum.explorewithme.user.dto.UserMapperMapStruct;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "admin/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    private final UserMapperMapStruct mapper;

    @PostMapping
    public UserDto create(@Valid @RequestBody UserDto userDto) {
        log.trace("hit endpoint - createUser, name - {}, email - {}", userDto.getName(), userDto.getEmail());
        User user = UserMapper.toEntity(userDto);
//        return UserMapper.toDto(userService.createUser(user)); второй вариант маппинга - вручную
        return mapper.toDto(userService.create(user));
    }

    @GetMapping("/{userId}")
    public UserDto getById(@PathVariable Long userId) {
        log.trace("hit endpoint - getUserById, id - {}", userId);
//        return UserMapper.toDto(userService.getUserById(userId));
        return mapper.toDto(userService.getById(userId));
    }

    @GetMapping
    public List<UserDto> getAll(@RequestParam(required = false) List<Long> ids,
            @PositiveOrZero @RequestParam(name = "from", required = false, defaultValue = "0") Integer from,
            @Positive @RequestParam(name = "size", required = false, defaultValue = "10") Integer size) {
        log.trace("hit endpoint - getAllUsers");
//        return UserMapper.toDto(userService.getAllUsers(ids, from, size));
        return mapper.toDto(userService.getAll(ids, from, size));
    }

    @PatchMapping("/{userId}")
    public UserDto edit(@RequestBody UserDto userDto,
                         @PathVariable Long userId) {
        log.trace("hit endpoint - editUser, name - {}, email - {}", userDto.getName(), userDto.getEmail());
//        User updateUser = UserMapper.fromDto(userDto);
        User updateUser = mapper.toEntity(userDto);
//        return UserMapper.toDto(userService.editUser(updateUser, userId));
        return mapper.toDto(userService.edit(updateUser, userId));
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable Long userId) {
        log.trace("hit endpoint - deleteUser, id - {}", userId);
        userService.delete(userId);
    }
}
