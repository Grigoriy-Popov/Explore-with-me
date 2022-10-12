package ru.practicum.explorewithme.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public UserDto createUser(@Valid @RequestBody UserDto userDto) {
        log.info("createUser, name - {}, email - {}", userDto.getName(), userDto.getEmail());
        User user = UserMapper.fromDto(userDto);
        return UserMapper.toDto(userService.createUser(user));
    }

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable Long userId) {
        log.info("getUserById, id - {}", userId);
        return UserMapper.toDto(userService.getUserById(userId));
    }

    @GetMapping
    public List<UserDto> getAllUsers(@RequestParam(required = false) List<Long> ids,
            @PositiveOrZero @RequestParam(name = "from", required = false, defaultValue = "0") Integer from,
            @Positive @RequestParam(name = "size", required = false, defaultValue = "10") Integer size) {
        log.info("getAllUsers");
        return UserMapper.toDtoList(userService.getAllUsers(ids, from, size));
    }

    @PatchMapping("/{userId}")
    public UserDto editUser(@RequestBody UserDto userDto,
                         @PathVariable Long userId) {
        log.info("editUser, name - {}, email - {}", userDto.getName(), userDto.getEmail());
        User updateUser = UserMapper.fromDto(userDto);
        return UserMapper.toDto(userService.editUser(updateUser, userId));
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        log.info("deleteUser, id - {}", userId);
        userService.deleteUser(userId);
    }
}
