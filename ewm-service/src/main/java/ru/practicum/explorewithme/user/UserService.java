package ru.practicum.explorewithme.user;

import java.util.List;

public interface UserService {

    User create(User user);

    User getById(Long userId);

    void checkExistenceById(Long userId);

    List<User> getAll(List<Long> usersIdList, Integer from, Integer size);

    User edit(User user, Long userId);

    void delete(Long userId);

}
