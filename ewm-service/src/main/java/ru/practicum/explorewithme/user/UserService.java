package ru.practicum.explorewithme.user;

import ru.practicum.explorewithme.common_dto.PageInfo;

import java.util.List;

public interface UserService {

    User create(User user);

    User getById(Long userId);

    void checkExistenceById(Long userId);

    List<User> getAll(List<Long> usersId, PageInfo pageInfo);

    User edit(User user, Long userId);

    void delete(Long userId);

}
