package ru.practicum.explorewithme.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.common_dto.PageInfo;
import ru.practicum.explorewithme.event.dto.AdminUpdateEventRequest;
import ru.practicum.explorewithme.event.dto.EventMapperMapStruct;
import ru.practicum.explorewithme.event.dto.FullEventDto;
import ru.practicum.explorewithme.event.service.AdminEventService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.explorewithme.Constants.DATE_TIME_PATTERN;

@RestController
@RequestMapping(path = "/admin/events")
@RequiredArgsConstructor
@Validated
@Slf4j
public class AdminEventController {
    private final AdminEventService adminEventService;
    private final EventMapperMapStruct eventMapper;

    @GetMapping
    public List<FullEventDto> getAll(@RequestParam(required = false) List<Long> users,
            @RequestParam(required = false) List<String> states,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) @DateTimeFormat(pattern = DATE_TIME_PATTERN) LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = DATE_TIME_PATTERN) LocalDateTime rangeEnd,
            @PositiveOrZero @RequestParam(required = false, defaultValue = "0") int from,
            @Positive @RequestParam(required = false, defaultValue = "10") int size) {
        log.trace("hit endpoint - getEventsByAdmin, states - {}", states);
//        return EventMapper.toFullDto(eventService
//                .getEventsByAdmin(users, states, categories, rangeStart, rangeEnd, from, size));
        PageInfo pageInfo = PageInfo.builder().from(from).size(size).build();
        return eventMapper.toFullDto(adminEventService
                .getEventsByAdmin(users, states, categories, rangeStart, rangeEnd, pageInfo));
    }

    @PutMapping("/{eventId}")
    public FullEventDto edit(@PathVariable Long eventId,
                             @RequestBody AdminUpdateEventRequest adminUpdateEventRequest) {
        log.trace("hit endpoint - editEventByAdmin");
//        return EventMapper.toFullDto(eventService.editEventByAdmin(eventId, adminUpdateEventRequest));
        return eventMapper.toFullDto(adminEventService.editByAdmin(eventId, adminUpdateEventRequest));
    }

    @PatchMapping("/{eventId}/publish")
    public FullEventDto publish(@PathVariable Long eventId) {
        log.trace("hit endpoint - publishEventByAdmin");
//        return EventMapper.toFullDto(eventService.publishEventByAdmin(eventId));
        return eventMapper.toFullDto(adminEventService.publishByAdmin(eventId));
    }

    @PatchMapping("/{eventId}/reject")
    public FullEventDto reject(@PathVariable Long eventId) {
        log.trace("hit endpoint - rejectEventByAdmin");
//        return EventMapper.toFullDto(eventService.rejectEventByAdmin(eventId));
        return eventMapper.toFullDto(adminEventService.rejectByAdmin(eventId));
    }
}
