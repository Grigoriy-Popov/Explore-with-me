package ru.practicum.explorewithme.participation_request;

import ru.practicum.explorewithme.event.Event;

import java.util.List;

public interface ParticipationRequestService {
    ParticipationRequest create(Long userId, Long eventId);

    List<ParticipationRequest> getUserRequestsByRequester(Long userId);

    ParticipationRequest cancelByRequester(Long userId, Long requestId);

    Integer getAmountOfConfirmedRequestsByEvent(Event event);

    List<ParticipationRequest> getEventParticipationRequestsByInitiator(Long userId, Long eventId);

    ParticipationRequest confirmByInitiator(Long userId, Long eventId, Long reqId);

    ParticipationRequest rejectByInitiator(Long userId, Long eventId, Long reqId);

}
