package ru.practicum.explorewithme.event.service;

import ru.practicum.explorewithme.common_dto.PageInfo;
import ru.practicum.explorewithme.event.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface PublicEventService {
    Event getById(Long eventId);

    List<Event> getAllByPublicUser(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                   LocalDateTime rangeEnd, Boolean onlyAvailable, PageInfo pageInfo,
                                   String ip, String uri);

    Event getByIdByPublicUser(Long eventId, String ip, String uri);
}
