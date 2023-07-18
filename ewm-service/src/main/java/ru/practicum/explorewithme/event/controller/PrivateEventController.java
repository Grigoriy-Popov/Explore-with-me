package ru.practicum.explorewithme.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.event.Event;
import ru.practicum.explorewithme.event.dto.EventMapper;
import ru.practicum.explorewithme.event.dto.EventMapperMapStruct;
import ru.practicum.explorewithme.event.dto.FullEventDto;
import ru.practicum.explorewithme.event.dto.NewEventDto;
import ru.practicum.explorewithme.event.dto.ShortEventDto;
import ru.practicum.explorewithme.event.dto.UpdateEventRequest;
import ru.practicum.explorewithme.event.service.PrivateEventService;
import ru.practicum.explorewithme.participation_request.ParticipationRequestService;
import ru.practicum.explorewithme.participation_request.dto.ParticipationRequestDto;
import ru.practicum.explorewithme.participation_request.dto.ParticipationRequestMapperMapStruct;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
@Slf4j
public class PrivateEventController {
    private final PrivateEventService privateEventService;
    private final ParticipationRequestService participationRequestService;
    private final EventMapperMapStruct eventMapper;
    private final ParticipationRequestMapperMapStruct requestMapper;

    @GetMapping
    public List<ShortEventDto> getAllInitiatorEvents(@PathVariable Long userId,
                                                     @PositiveOrZero @RequestParam(name = "from", required = false, defaultValue = "0") Integer from,
                                                     @Positive @RequestParam(name = "size", required = false, defaultValue = "10") Integer size) {
        log.trace("hit endpoint - getAllInitiatorEvents by user with id - {}", userId);
//        return EventMapper.toShortDto(eventService.getAllInitiatorEvents(userId, from, size));
        return eventMapper.toShortDto(privateEventService.getAllInitiatorEvents(userId, from, size));
    }

    @PatchMapping
    public FullEventDto editByUser(@PathVariable Long userId,
                                        @RequestBody @Valid UpdateEventRequest updateEventRequest) {
        log.trace("hit endpoint - editEventByUser by user with id - {}", userId);
//        return EventMapper.toFullDto(eventService.editEventByUser(userId, updateEventRequest));
        return eventMapper.toFullDto(privateEventService.editByUser(userId, updateEventRequest));
    }

    @PostMapping
    public FullEventDto create(@RequestBody @Valid NewEventDto newEventDto, @PathVariable Long userId) {
        log.trace("hit endpoint - createEvent - {}", newEventDto);
        Event event = EventMapper.toEntityFromNewEventDto(newEventDto);
//        return EventMapper.toFullDto(eventService.createEvent(event, userId, newEventDto.getCategory()));
        return eventMapper.toFullDto(privateEventService.create(event, userId, newEventDto.getCategory()));
    }

    @GetMapping("/{eventId}")
    public FullEventDto getByIdByInitiator(@PathVariable Long userId, @PathVariable Long eventId) {
        log.trace("hit endpoint - getEventByIdByInitiator");
//        return EventMapper.toFullDto(eventService.getEventByIdByInitiator(userId, eventId));
        return eventMapper.toFullDto(privateEventService.getByIdByInitiator(userId, eventId));
    }

    @PatchMapping("/{eventId}")
    public FullEventDto cancelByInitiator(@PathVariable Long userId, @PathVariable Long eventId) {
        log.trace("hit endpoint - cancelEventByInitiator");
//        return EventMapper.toFullDto(eventService.cancelEventByInitiator(userId, eventId));
        return eventMapper.toFullDto(privateEventService.cancelByInitiator(userId, eventId));
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getEventParticipationRequestsByInitiator(@PathVariable Long userId,
                                                                                  @PathVariable Long eventId) {
        log.trace("hit endpoint - getEventParticipationRequestsByInitiator");
//        return ParticipationRequestMapper.toDto(participationRequestService
//                .getEventParticipationRequestsByInitiator(userId, eventId));
        return requestMapper.toDto(participationRequestService
                .getEventParticipationRequestsByInitiator(userId, eventId));
    }

    @PatchMapping("/{eventId}/requests/{reqId}/confirm")
    public ParticipationRequestDto confirmRequestByInitiator(@PathVariable Long userId, @PathVariable Long eventId,
                                                             @PathVariable Long reqId) {
        log.trace("hit endpoint - confirmRequestByInitiator");
//        return ParticipationRequestMapper.toDto(participationRequestService.confirmRequestByInitiator(userId, eventId, reqId));
        return requestMapper.toDto(participationRequestService.confirmByInitiator(userId, eventId, reqId));
    }

    @PatchMapping("/{eventId}/requests/{reqId}/reject")
    public ParticipationRequestDto rejectRequestByInitiator(@PathVariable Long userId, @PathVariable Long eventId,
                                                            @PathVariable Long reqId) {
        log.trace("hit endpoint - rejectRequestByInitiator");
//        return ParticipationRequestMapper.toDto(participationRequestService.rejectRequestByInitiator(userId, eventId, reqId));
        return requestMapper.toDto(participationRequestService.rejectByInitiator(userId, eventId, reqId));
    }
}
