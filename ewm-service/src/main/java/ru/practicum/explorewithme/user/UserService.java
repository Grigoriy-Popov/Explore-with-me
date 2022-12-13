package ru.practicum.explorewithme.user;

import java.util.List;

public interface UserService {

    User createUser(User user);

    User getUserById(Long userId);

    void checkExistenceById(Long userId);

    List<User> getAllUsers(List<Long> usersIdList, Integer from, Integer size);

    User editUser(User user, Long userId);

    void deleteUser(Long userId);

}
