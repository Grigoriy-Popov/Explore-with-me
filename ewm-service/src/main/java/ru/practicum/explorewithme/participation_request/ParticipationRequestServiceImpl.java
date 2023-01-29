package ru.practicum.explorewithme.participation_request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.event.Event;
import ru.practicum.explorewithme.event.PublicEventService;
import ru.practicum.explorewithme.event.State;
import ru.practicum.explorewithme.exceptions.*;
import ru.practicum.explorewithme.user.User;
import ru.practicum.explorewithme.user.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ParticipationRequestServiceImpl implements ParticipationRequestService {
    private final ParticipationRequestRepository participationRequestRepository;
    private final PublicEventService eventService;
    private final UserService userService;

    public ParticipationRequest getRequestById(Long requestId) {
        return participationRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException(String.format("Request with id %d not found", requestId)));
    }

    @Override
    public ParticipationRequest createRequest(Long userId, Long eventId) {
        Event event = eventService.getEventById(eventId);
        User user = userService.getUserById(userId);
        if (participationRequestRepository.existsByRequesterIdAndEventId(userId, eventId)) {
            throw new AlreadyExistsException(String.format("Request from user with name %s to event with title %s" +
                    "is already exists", user.getName(), event.getTitle()));
        }
        if (userId.equals(event.getInitiator().getId())) {
            throw new AccessDeniedException("Initiator of the event can't create request to his event");
        }
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new IncorrectStateException("You can create request only to published events");
        }
        if (!event.getParticipantLimit().equals(0) && event.getParticipantLimit() != null) {
            Integer confirmedRequestsAmount = getAmountOfConfirmedRequestsByEvent(event);
            if (confirmedRequestsAmount.equals(event.getParticipantLimit())) {
                throw new AccessDeniedException("The limit on the number of participants has been reached");
            }
        }
        ParticipationRequest request = ParticipationRequest.builder()
                .event(event)
                .requester(user)
                .status(event.isRequestModeration() ? RequestStatus.PENDING : RequestStatus.CONFIRMED)
                .status(RequestStatus.PENDING)
                .build();
        return participationRequestRepository.save(request);
    }

    @Override
    public List<ParticipationRequest> getUserRequestsByRequester(Long userId) {
        userService.checkExistenceById(userId);
        return participationRequestRepository.findAllByRequesterId(userId);
    }

    @Override
    public ParticipationRequest cancelRequestByRequester(Long userId, Long requestId) {
        userService.checkExistenceById(userId);
        ParticipationRequest request = getRequestById(requestId);
        if (!request.getRequester().getId().equals(userId)) {
            throw new AccessDeniedException("Only requester can cancel his request");
        }
        request.setStatus(RequestStatus.CANCELED);
        return participationRequestRepository.save(request);
    }

    @Override
    public Integer getAmountOfConfirmedRequestsByEvent(Event event) {
        return participationRequestRepository.countByEventIdAndStatusIs(event.getId(), RequestStatus.CONFIRMED);
    }

    @Override
    public List<ParticipationRequest> getEventParticipationRequestsByInitiator(Long userId, Long eventId) {
        userService.checkExistenceById(userId);
        Event event = eventService.getEventById(eventId);
        if (!userId.equals(event.getInitiator().getId())) {
            throw new AccessDeniedException("Only initiator can view this information");
        }
        return participationRequestRepository.findAllByEventId(eventId);
    }

    @Override
    public ParticipationRequest confirmRequestByInitiator(Long userId, Long eventId, Long reqId) {
        userService.checkExistenceById(userId);
        Event event = eventService.getEventById(eventId);
        ParticipationRequest request = getRequestById(reqId);
        if (!userId.equals(event.getInitiator().getId())) {
            throw new AccessDeniedException("Only initiator can confirm the request");
        }
        if (!request.getStatus().equals(RequestStatus.PENDING)) {
            throw new IncorrectStateException("You can confirm only pending requests");
        }
        Integer confirmedRequestsAmount = getAmountOfConfirmedRequestsByEvent(event);
        if (confirmedRequestsAmount.equals(event.getParticipantLimit())) {
            List<ParticipationRequest> pendingEventRequests = participationRequestRepository
                    .findAllByEventAndStatusIs(event, RequestStatus.PENDING);
            for (ParticipationRequest participationRequest : pendingEventRequests) {
                participationRequest.setStatus(RequestStatus.REJECTED);
                participationRequestRepository.save(request);
            }
            throw new AccessDeniedException("The limit on the number of participants has been reached");
        }
        request.setStatus(RequestStatus.CONFIRMED);
        return participationRequestRepository.save(request);
    }

    @Override
    public ParticipationRequest rejectRequestByInitiator(Long userId, Long eventId, Long reqId) {
        userService.checkExistenceById(userId);
        Event event = eventService.getEventById(eventId);
        ParticipationRequest request = getRequestById(reqId);
        if (!userId.equals(event.getInitiator().getId())) {
            throw new AccessDeniedException("Only initiator can reject the request");
        }
        if (!request.getStatus().equals(RequestStatus.PENDING)) {
            throw new IncorrectStateException("You can reject only pending requests");
        }
        request.setStatus(RequestStatus.REJECTED);
        return participationRequestRepository.save(request);
    }
}
