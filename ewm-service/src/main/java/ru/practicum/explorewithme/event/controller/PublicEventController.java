package ru.practicum.explorewithme.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.common_dto.PageInfo;
import ru.practicum.explorewithme.event.dto.EventMapperMapStruct;
import ru.practicum.explorewithme.event.dto.FullEventDto;
import ru.practicum.explorewithme.event.dto.ShortEventDto;
import ru.practicum.explorewithme.event.service.PublicEventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.explorewithme.Constants.DATE_TIME_PATTERN;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
@Validated
@Slf4j
public class PublicEventController {
    private final PublicEventService eventService;
    private final EventMapperMapStruct eventMapper;

    @GetMapping
    public List<ShortEventDto> getAllByPublicUser(@RequestParam(required = false, defaultValue = "") String text,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false) @DateTimeFormat(pattern = DATE_TIME_PATTERN) LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = DATE_TIME_PATTERN) LocalDateTime rangeEnd,
            @RequestParam(required = false, defaultValue = "false") Boolean onlyAvailable,
            @RequestParam(required = false, defaultValue = "EVENT_DATE") String sort,
            @PositiveOrZero @RequestParam(required = false, defaultValue = "0") int from,
            @Positive @RequestParam(required = false, defaultValue = "10") int size, HttpServletRequest request) {
        log.trace("hit endpoint - getAllEventsByPublicUser");
//        return EventMapper.toShortDto(eventService.getAllEventsByPublicUser(text, categories, paid, rangeStart,
//                rangeEnd, onlyAvailable, sort, from, size, request.getRemoteAddr(), request.getRequestURI()));
        PageInfo pageInfo = PageInfo.builder().from(from).size(size).sort(sort).build();
        return eventMapper.toShortDto(eventService.getAllByPublicUser(text, categories, paid, rangeStart,
                rangeEnd, onlyAvailable, pageInfo, request.getRemoteAddr(), request.getRequestURI()));
    }

    @GetMapping(path = "/{eventId}")
    public FullEventDto getByIdByPublicUser(@PathVariable Long eventId, HttpServletRequest request) {
        log.trace("hit endpoint - getEventByIdByPublicUser, eventId - {}", eventId);
//        return EventMapper.toFullDto(eventService.getEventByIdByPublicUser(eventId, request.getRemoteAddr(),
//                request.getRequestURI()));
        return eventMapper.toFullDto(eventService.getByIdByPublicUser(eventId, request.getRemoteAddr(),
                request.getRequestURI()));
    }
}
