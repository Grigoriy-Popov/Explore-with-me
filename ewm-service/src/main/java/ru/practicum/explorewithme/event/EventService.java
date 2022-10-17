package ru.practicum.explorewithme.event;

import ru.practicum.explorewithme.event.dto.AdminUpdateEventRequest;
import ru.practicum.explorewithme.event.dto.UpdateEventRequest;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {

    //UTIL
    Event getEventById(Long eventId);

    // PUBLIC
    List<Event> getAllEventsByPublicUser(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
            LocalDateTime rangeEnd, Boolean onlyAvailable, String sort, Integer from, Integer size,
            String ip, String uri);

    Event getEventByIdByPublicUser(Long eventId, String ip, String uri);

    // PRIVATE
    List<Event> getAllInitiatorEvents(Long userId, Integer from, Integer size);

    Event editEventByUser(Long userId, UpdateEventRequest updateEventRequest);

    Event createEvent(Event event, Long userId, Long categoryId);

    Event getEventByIdByInitiator(Long userId, Long eventId);

    Event cancelEventByInitiator(Long userId, Long eventId);

    // ADMIN
    List<Event> getEventsByAdmin(List<Long> users, List<String> states, List<Long> categories,
                                 LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size);

    Event editEventByAdmin(Long eventId, AdminUpdateEventRequest adminUpdateEventRequest);

    Event publishEventByAdmin(Long eventId);

    Event rejectEventByAdmin(Long eventId);

}
