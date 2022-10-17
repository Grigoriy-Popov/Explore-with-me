package ru.practicum.explorewithme.event;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.category.Category;
import ru.practicum.explorewithme.category.CategoryService;
import ru.practicum.explorewithme.client.EndpointHit;
import ru.practicum.explorewithme.client.StatClient;
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
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final UserService userService;
    private final CategoryService categoryService;
    private final ParticipationRequestRepository participationRequestRepository;
    private final LocationRepository locationRepository;
    private final StatClient statClient;

    @Override
    public Event getEventById(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format("Event with id %d not found", eventId)));
    }

    //PUBLIC
    @Override
    public List<Event> getAllEventsByPublicUser(String text, List<Long> categories, Boolean paid,
                                                LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable,
                                                String sort, Integer from, Integer size, String ip, String uri) {
        postEndpointHit(uri, ip);
        PageRequest pageRequest = PageRequest.of(from / size, size);
        LocalDateTime start = rangeStart == null ? LocalDateTime.now() : rangeStart;
        LocalDateTime end = rangeEnd == null ? LocalDateTime.MAX : rangeEnd;
        // Список приходит отсортированным по дате, если нужна сортировка по просмотрам, она происходит дальше
        List<Event> events = eventRepository.getAllEventsByPublicUser(text, categories, paid, start, end, pageRequest);
        events.forEach(this::setConfirmedRequestsAndViews);
        if (onlyAvailable) {
            events = events.stream()
                    .filter(e -> e.getParticipantLimit() >= e.getConfirmedRequests())
                    .collect(Collectors.toList());
        }
        if (sort.equals("VIEWS")) {
            events.sort(Comparator.comparing(Event::getViews));
        }
        return events;
    }

    @Override
    public Event getEventByIdByPublicUser(Long eventId, String ip, String uri) {
        Event event = getEventById(eventId);
        if (event.getState() != State.PUBLISHED) {
            throw new IncorrectStateException("You can view only published events");
        }
        postEndpointHit(uri, ip);
        setConfirmedRequestsAndViews(event);
        return event;
    }

    //PRIVATE
    @Override
    public Event createEvent(Event event, Long userId, Long categoryId) {
        if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new IncorrectDateException("Event cannot begin earlier than 2 hours later");
        }
        User initiator = userService.getUserById(userId);
        Category category = categoryService.getCategoryById(categoryId);
        Location location = locationRepository.save(event.getLocation());
        event.setLocation(location);
        event.setCategory(category);
        event.setInitiator(initiator);
        return eventRepository.save(event);
    }

    @Override
    public List<Event> getAllInitiatorEvents(Long userId, Integer from, Integer size) {
        User user = userService.getUserById(userId);
        Pageable page = PageRequest.of(from / size, size);
        List<Event> initiatorEvents = eventRepository.findAllByInitiator(user, page);
        initiatorEvents.forEach(this::setConfirmedRequestsAndViews);
        return initiatorEvents;
    }

    @Override
    public Event editEventByUser(Long userId, UpdateEventRequest updateEventRequest) {
        userService.checkExistenceById(userId);
        Event event = getEventById(updateEventRequest.getEventId());
        Category category = categoryService.getCategoryById(updateEventRequest.getCategory());
        if (event.getState() != State.CANCELED && event.getState() != State.PENDING) {
            throw new IncorrectStateException("You can edit only canceled or pending events");
        }
        if (!userId.equals(event.getInitiator().getId())) {
            throw new AccessDeniedException("Only initiator can edit the event");
        }
        if (updateEventRequest.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new IncorrectDateException("Event cannot begin earlier than 2 hours later");
        }
        if (event.getState() == State.CANCELED) {
            event.setState(State.PENDING);
        }
        event.setParticipantLimit(updateEventRequest.getParticipantLimit());
        Integer confirmedRequestsAmount = participationRequestRepository.countByEventIdAndStatusIs(event.getId(),
                RequestStatus.CONFIRMED);
        if (event.getParticipantLimit() < confirmedRequestsAmount) {
            throw new AccessDeniedException("New value of participant limit is less then amount of " +
                    "confirmed requests");
        }
        event.setAnnotation(updateEventRequest.getAnnotation());
        event.setCategory(category);
        event.setDescription(updateEventRequest.getDescription());
        event.setEventDate(updateEventRequest.getEventDate());
        event.setPaid(updateEventRequest.getPaid());
        event.setTitle(updateEventRequest.getTitle());
        setConfirmedRequestsAndViews(event);
        return eventRepository.save(event);
    }

    @Override
    public Event getEventByIdByInitiator(Long userId, Long eventId) {
        userService.checkExistenceById(userId);
        Event event = getEventById(eventId);
        if (!userId.equals(event.getInitiator().getId())) {
            throw new AccessDeniedException("Only initiator can view this information");
        }
        setConfirmedRequestsAndViews(event);
        return event;
    }

    @Override
    public Event cancelEventByInitiator(Long userId, Long eventId) {
        userService.checkExistenceById(userId);
        Event event = getEventById(eventId);
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
                                        LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size) {
        List<State> stateList = new ArrayList<>();
        for (String s : states) {
            State state = State.valueOf(s);
            stateList.add(state);
        }
        Pageable page = PageRequest.of(from / size, size);
        List<Event> events = eventRepository
                .getEventsByAdmin(users, stateList, categories, rangeStart, rangeEnd, page);
        events.forEach(this::setConfirmedRequestsAndViews);
        return events;
    }

    @Override
    public Event editEventByAdmin(Long eventId, AdminUpdateEventRequest adminUpdateEventRequest) {
        Event event = getEventById(eventId);
        Category category = categoryService.getCategoryById(adminUpdateEventRequest.getCategory());
        if (adminUpdateEventRequest.getAnnotation() != null) {
            event.setAnnotation(adminUpdateEventRequest.getAnnotation());
        }
        event.setCategory(category);
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
        return event;
    }

    @Override
    public Event publishEventByAdmin(Long eventId) {
        Event event = getEventById(eventId);
        if (event.getState() != State.PENDING) {
            throw new IncorrectStateException("Only pending events can be published");
        }
        LocalDateTime publishTime = LocalDateTime.now();
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
    public Event rejectEventByAdmin(Long eventId) {
        Event event = getEventById(eventId);
        if (event.getState() != State.PENDING) {
            throw new IncorrectDateException("Only pending events can be canceled");
        }
        event.setState(State.CANCELED);
        setConfirmedRequestsAndViews(event);
        return event;
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
                eventRepository.getMinCreationDate(),
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
