package ru.practicum.explorewithme.event;

public interface EventService {
//    EventFullDto getEventByIdPublic(Integer eventId, String ip, String uri);

    Event createEvent(NewEventDto newEventDto, Long userId);

    Event cancelEvent(Long eventId, Long userId);
}
