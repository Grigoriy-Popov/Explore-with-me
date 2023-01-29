package ru.practicum.explorewithme.event;

import ru.practicum.explorewithme.event.dto.UpdateEventRequest;

import java.util.List;

public interface PrivateEventService {
    List<Event> getAllInitiatorEvents(Long userId, Integer from, Integer size);

    Event editEventByUser(Long userId, UpdateEventRequest updateEventRequest);

    Event createEvent(Event event, Long userId, Long categoryId);

    Event getEventByIdByInitiator(Long userId, Long eventId);

    Event cancelEventByInitiator(Long userId, Long eventId);
}
