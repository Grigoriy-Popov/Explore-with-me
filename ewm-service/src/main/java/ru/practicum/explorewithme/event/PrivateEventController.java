package ru.practicum.explorewithme.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.event.dto.*;
import ru.practicum.explorewithme.participation_request.ParticipationRequestDto;
import ru.practicum.explorewithme.participation_request.ParticipationRequestMapper;
import ru.practicum.explorewithme.participation_request.ParticipationRequestService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
@Slf4j
public class PrivateEventController {
    private final EventService eventService;
    private final ParticipationRequestService participationRequestService;

    @GetMapping
    public List<ShortEventDto> getAllInitiatorEvents(@PathVariable Long userId,
            @PositiveOrZero @RequestParam(name = "from", required = false, defaultValue = "0") Integer from,
            @Positive @RequestParam(name = "size", required = false, defaultValue = "10") Integer size) {
        return EventMapper.toShortDtoList(eventService.getAllInitiatorEvents(userId, from, size));
    }

    @PatchMapping
    public FullEventDto editEventByUser(@PathVariable Long userId,
                                        @RequestBody @Valid UpdateEventRequest updateEventRequest) {
        return EventMapper.toFullDto(eventService.editEventByUser(userId, updateEventRequest));
    }

    @PostMapping
    public FullEventDto createEvent(@RequestBody @Valid NewEventDto newEventDto, @PathVariable Long userId) {
        log.info("createEvent - {}", newEventDto);
        Event event = EventMapper.fromNewEventDto(newEventDto);
        return EventMapper.toFullDto(eventService.createEvent(event, userId, newEventDto.getCategory()));
    }

    @GetMapping("/{eventId}")
    public FullEventDto getEventByIdByInitiator(@PathVariable Long userId, @PathVariable Long eventId) {
        log.info("getEventByIdByInitiator");
        return EventMapper.toFullDto(eventService.getEventByIdByInitiator(userId, eventId));
    }

    @PatchMapping("/{eventId}")
    public FullEventDto cancelEventByInitiator(@PathVariable Long userId, @PathVariable Long eventId) {
        log.info("cancelEventByInitiator");
        return EventMapper.toFullDto(eventService.cancelEventByInitiator(userId, eventId));
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getEventParticipationRequestsByInitiator(@PathVariable Long userId,
                                                                                  @PathVariable Long eventId) {
        log.info("getEventParticipationRequestsByInitiator");
        return ParticipationRequestMapper.toDtoList(participationRequestService
                .getEventParticipationRequestsByInitiator(userId, eventId));
    }

    @PatchMapping("/{eventId}/requests/{reqId}/confirm")
    public ParticipationRequestDto confirmRequestByInitiator(@PathVariable Long userId, @PathVariable Long eventId,
                                                             @PathVariable Long reqId) {
        log.info("confirmRequestByInitiator");
        return ParticipationRequestMapper.toDto(participationRequestService.confirmRequestByInitiator(userId, eventId, reqId));
    }

    @PatchMapping("/{eventId}/requests/{reqId}/reject")
    public ParticipationRequestDto rejectRequestByInitiator(@PathVariable Long userId, @PathVariable Long eventId,
                                                             @PathVariable Long reqId) {
        log.info("rejectRequestByInitiator");
        return ParticipationRequestMapper.toDto(participationRequestService.rejectRequestByInitiator(userId, eventId, reqId));
    }
}
