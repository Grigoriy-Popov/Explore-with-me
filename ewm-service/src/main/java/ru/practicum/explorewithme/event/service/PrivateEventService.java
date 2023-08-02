package ru.practicum.explorewithme.event.service;

import ru.practicum.explorewithme.event.Event;
import ru.practicum.explorewithme.event.dto.UpdateEventRequest;

import java.util.List;

public interface PrivateEventService {
    List<Event> getAllInitiatorEvents(Long userId, int from, int size);

    Event editByUser(Long userId, UpdateEventRequest updateEventRequest);

    Event create(Event event, Long userId, Long categoryId);

    Event getByIdByInitiator(Long userId, Long eventId);

    Event cancelByInitiator(Long userId, Long eventId);
}
