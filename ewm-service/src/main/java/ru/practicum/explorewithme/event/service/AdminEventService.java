package ru.practicum.explorewithme.event.service;

import ru.practicum.explorewithme.event.Event;
import ru.practicum.explorewithme.event.dto.AdminUpdateEventRequest;

import java.time.LocalDateTime;
import java.util.List;

public interface AdminEventService {
    List<Event> getEventsByAdmin(List<Long> users, List<String> states, List<Long> categories,
                                 LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size);

    Event editEventByAdmin(Long eventId, AdminUpdateEventRequest adminUpdateEventRequest);

    Event publishEventByAdmin(Long eventId);

    Event rejectEventByAdmin(Long eventId);
}
