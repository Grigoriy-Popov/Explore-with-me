package ru.practicum.explorewithme.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.category.Category;
import ru.practicum.explorewithme.category.CategoryService;
import ru.practicum.explorewithme.client.EndpointHit;
import ru.practicum.explorewithme.client.StatClient;
import ru.practicum.explorewithme.event.Event;
import ru.practicum.explorewithme.event.EventRepository;
import ru.practicum.explorewithme.event.State;
import ru.practicum.explorewithme.event.dto.AdminUpdateEventRequest;
import ru.practicum.explorewithme.event.dto.UpdateEventRequest;
import ru.practicum.explorewithme.event.location.Location;
import ru.practicum.explorewithme.event.location.LocationRepository;
import ru.practicum.explorewithme.exceptions.AccessDeniedException;
import ru.practicum.explorewithme.exceptions.IncorrectDateException;
import ru.practicum.explorewithme.exceptions.IncorrectStateException;
import ru.practicum.explorewithme.exceptions.NotFoundException;
import ru.practicum.explorewithme.participation_request.ParticipationRequestRepository;
import ru.practicum.explorewithme.participation_request.RequestStatus;
import ru.practicum.explorewithme.user.User;
import ru.practicum.explorewithme.user.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements PrivateEventService, PublicEventService, AdminEventService {
    private final EventRepository eventRepository;
    private final UserService userService;
    private final CategoryService categoryService;
    private final ParticipationRequestRepository participationRequestRepository;
    private final LocationRepository locationRepository;
    private final StatClient statClient;

    @Override
    public Event getById(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format("Event with id %d not found", eventId)));
    }

    //PUBLIC
    @Override
    public List<Event> getAllByPublicUser(String text, List<Long> categories, Boolean paid,
                                          LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable,
                                          String sort, int from, int size, String ip, String uri) {
        postEndpointHit(uri, ip);
        PageRequest pageRequest = PageRequest.of(from / size, size);
        LocalDateTime start = rangeStart == null ? LocalDateTime.now() : rangeStart;
        LocalDateTime end = rangeEnd == null ? LocalDateTime.MAX : rangeEnd;
        // Список приходит отсортированным по дате, если нужна сортировка по просмотрам, она происходит дальше
        List<Event> events = eventRepository.findAllByPublicUser(text, categories, paid, start, end, pageRequest);
        if (onlyAvailable) {
            events = events.stream()
                    .filter(event -> event.getParticipantLimit() >= event.getConfirmedRequests())
                    .collect(Collectors.toList());
        }
        if (sort.equals("VIEWS")) {
            events.sort(Comparator.comparing(Event::getViews));
        }
        events.forEach(this::setConfirmedRequestsAndViews);
        return events;
    }

    @Override
    public Event getByIdByPublicUser(Long eventId, String ip, String uri) {
        Event event = getById(eventId);
        if (event.getState() != State.PUBLISHED) {
            throw new IncorrectStateException("You can view only published events");
        }
        postEndpointHit(uri, ip);
        setConfirmedRequestsAndViews(event);
        return event;
    }

    //PRIVATE
    @Override
    public Event create(Event event, Long userId, Long categoryId) {
        if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new IncorrectDateException("Event cannot begin earlier than 2 hours later");
        }
        User initiator = userService.getById(userId);
        Category category = categoryService.getById(categoryId);
        Location location = locationRepository.save(event.getLocation());
        event.setInitiator(initiator);
        event.setCategory(category);
        event.setLocation(location);
        event.setState(State.PENDING);
        return eventRepository.save(event);
    }

    @Override
    public List<Event> getAllInitiatorEvents(Long userId, Integer from, Integer size) {
        User user = userService.getById(userId);
        Pageable page = PageRequest.of(from / size, size);
        List<Event> initiatorEvents = eventRepository.findAllByInitiator(user, page);
        initiatorEvents.forEach(this::setConfirmedRequestsAndViews);
        return initiatorEvents;
    }

    @Override
    public Event editByUser(Long userId, UpdateEventRequest updateEventRequest) {
        userService.checkExistenceById(userId);
        Event event = getById(updateEventRequest.getEventId());
        if (event.getState() != State.CANCELED && event.getState() != State.PENDING) {
            throw new IncorrectStateException("You can edit only canceled or pending events");
        }
        if (!userId.equals(event.getInitiator().getId())) {
            throw new AccessDeniedException("Only initiator can edit the event");
        }
        if (updateEventRequest.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new IncorrectDateException("Event cannot begin earlier than 2 hours later");
        }
        Integer confirmedRequestsAmount = participationRequestRepository.countByEventIdAndStatusIs(event.getId(),
                RequestStatus.CONFIRMED);
        int participantLimit = updateEventRequest.getParticipantLimit();
        if (participantLimit < confirmedRequestsAmount) {
            throw new AccessDeniedException("New value of participant limit is less then amount of " +
                    "confirmed requests");
        }
        event.setParticipantLimit(participantLimit);
        if (event.getState() == State.CANCELED) {
            event.setState(State.PENDING);
        }
        event.setAnnotation(updateEventRequest.getAnnotation());
        Category category = categoryService.getById(updateEventRequest.getCategory());
        event.setCategory(category);
        event.setDescription(updateEventRequest.getDescription());
        event.setEventDate(updateEventRequest.getEventDate());
        event.setPaid(updateEventRequest.getPaid());
        event.setTitle(updateEventRequest.getTitle());
        setConfirmedRequestsAndViews(event);
        return eventRepository.save(event);
    }

    @Override
    public Event getByIdByInitiator(Long userId, Long eventId) {
        userService.checkExistenceById(userId);
        Event event = getById(eventId);
        if (!userId.equals(event.getInitiator().getId())) {
            throw new AccessDeniedException("Only initiator can view this information");
        }
        setConfirmedRequestsAndViews(event);
        return event;
    }

    @Override
    public Event cancelByInitiator(Long userId, Long eventId) {
        userService.checkExistenceById(userId);
        Event event = getById(eventId);
        if (!userId.equals(event.getInitiator().getId())) {
            throw new AccessDeniedException("Only initiator can cancel the event");
        }
        if (event.getState() != State.PENDING) {
            throw new IncorrectStateException("You can cancel only pending events");
        }
        setConfirmedRequestsAndViews(event);
        event.setState(State.CANCELED);
        return eventRepository.save(event);
    }

    //ADMIN
    @Override
    public List<Event> getEventsByAdmin(List<Long> users, List<String> states, List<Long> categories,
                                        LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size) {
        List<State> stateList = new ArrayList<>();
        if (states != null) {
            states.forEach(state -> stateList.add(State.valueOf(state)));
        }
        if (users == null) {
            users = new ArrayList<>();
        }
        if (categories == null) {
            categories = new ArrayList<>();
        }
        LocalDateTime start = rangeStart == null ? LocalDateTime.now() : rangeStart;
        LocalDateTime end = rangeEnd == null ? LocalDateTime.MAX : rangeEnd;
        Pageable page = PageRequest.of(from / size, size);
        List<Event> events = eventRepository
                .findByAdmin(users, stateList, categories, start, end, page);
        events.forEach(this::setConfirmedRequestsAndViews);
        return events;
    }

    @Override
    public Event editByAdmin(Long eventId, AdminUpdateEventRequest adminUpdateEventRequest) {
        Event event = getById(eventId);
        Category category = categoryService.getById(adminUpdateEventRequest.getCategory());
        event.setCategory(category);
        if (adminUpdateEventRequest.getAnnotation() != null) {
            event.setAnnotation(adminUpdateEventRequest.getAnnotation());
        }
        if (adminUpdateEventRequest.getDescription() != null) {
            event.setDescription(adminUpdateEventRequest.getDescription());
        }
        if (adminUpdateEventRequest.getEventDate() != null) {
            event.setEventDate(adminUpdateEventRequest.getEventDate());
        }
        if (adminUpdateEventRequest.getLocation() != null) {
            event.setLocation(adminUpdateEventRequest.getLocation());
        }
        if (adminUpdateEventRequest.getPaid() != null) {
            event.setPaid(adminUpdateEventRequest.getPaid());
        }
        if (adminUpdateEventRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(adminUpdateEventRequest.getParticipantLimit());
        }
        if (adminUpdateEventRequest.getRequestModeration() != null) {
            event.setRequestModeration(adminUpdateEventRequest.getRequestModeration());
        }
        if (adminUpdateEventRequest.getTitle() != null) {
            event.setTitle(adminUpdateEventRequest.getTitle());
        }
        setConfirmedRequestsAndViews(event);
        return eventRepository.save(event);
    }

    @Override
    public Event publishByAdmin(Long eventId) {
        Event event = getById(eventId);
        if (event.getState() != State.PENDING) {
            throw new IncorrectStateException("Only pending events can be published");
        }
        var publishTime = LocalDateTime.now();
        if (publishTime.isAfter(event.getEventDate().minusHours(1))) {
            throw new IncorrectDateException("The start date of the event must be no earlier than one hour " +
                    "from the date of publication");
        }
        event.setPublishedOn(publishTime);
        event.setState(State.PUBLISHED);
        setConfirmedRequestsAndViews(event);
        return eventRepository.save(event);
    }

    @Override
    public Event rejectByAdmin(Long eventId) {
        Event event = getById(eventId);
        if (event.getState() != State.PENDING) {
            throw new IncorrectDateException("Only pending events can be canceled");
        }
        event.setState(State.CANCELED);
        setConfirmedRequestsAndViews(event);
        return eventRepository.save(event);
    }

    private void postEndpointHit(String uri, String ip) {
        statClient.postEndpointHit(EndpointHit.builder()
                .app("ewm-main-service")
                .uri("[" + uri + "]")
                .ip(ip)
                .timestamp(LocalDateTime.now())
                .build());
    }

    private void setConfirmedRequestsAndViews(Event event) {
        event.setConfirmedRequests(getConfirmedRequests(event));
        event.setViews(getViews(event));
    }

    private Integer getConfirmedRequests(Event event) {
        return participationRequestRepository.countByEventIdAndStatusIs(event.getId(), RequestStatus.CONFIRMED);
    }

    private Integer getViews(Event event) {
        ResponseEntity<Object> responseEntity = statClient.getVewStats(
                eventRepository.findMinCreationDate(),
                LocalDateTime.now(),
                List.of(String.format("/events/%d", event.getId())),
                false);
        Integer views = 0;
        if (!Objects.equals(responseEntity.getBody(), "")) {
            views = (Integer) (((List<LinkedHashMap>) Objects.requireNonNull(responseEntity.getBody()))
                    .get(0).get("hits"));
        }
        return views;
    }
}
