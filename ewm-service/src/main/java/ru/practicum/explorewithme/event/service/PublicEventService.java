package ru.practicum.explorewithme.event.service;

import ru.practicum.explorewithme.event.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface PublicEventService {
    Event getEventById(Long eventId);

    List<Event> getAllEventsByPublicUser(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                         LocalDateTime rangeEnd, Boolean onlyAvailable, String sort, int from, int size,
                                         String ip, String uri);

    Event getEventByIdByPublicUser(Long eventId, String ip, String uri);
}
