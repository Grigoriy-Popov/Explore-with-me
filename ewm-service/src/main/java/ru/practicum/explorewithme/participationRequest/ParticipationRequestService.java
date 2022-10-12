package ru.practicum.explorewithme.participationRequest;

import ru.practicum.explorewithme.event.Event;

import java.util.List;

public interface ParticipationRequestService {
    ParticipationRequest createRequest(Long userId, Long eventId);

    List<ParticipationRequest> getUserRequestsByRequester(Long userId);

    ParticipationRequest cancelRequestByRequester(Long userId, Long requestId);

    Integer getAmountOfConfirmedRequestsByEvent(Event event);

    List<ParticipationRequest> getEventParticipationRequestsByInitiator(Long userId, Long eventId);

    ParticipationRequest confirmRequestByInitiator(Long userId, Long eventId, Long reqId);

    ParticipationRequest rejectRequestByInitiator(Long userId, Long eventId, Long reqId);

}
