package ru.practicum.explorewithme.participationRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/requests")
@RequiredArgsConstructor
@Slf4j
public class ParticipationRequestController {
    private final ParticipationRequestService participationRequestService;

    @GetMapping
    public List<ParticipationRequestDto> getUserRequestsByRequester(@PathVariable Long userId) {
        log.info("getUserRequestsByRequester, userId - {}", userId);
        return ParticipationRequestMapper.toDtoList(participationRequestService.getUserRequestsByRequester(userId));
    }

    @PostMapping
    public ParticipationRequestDto createRequest(@PathVariable Long userId,
                                                 @RequestParam Long eventId) {
        log.info("createRequest, userId - {}, eventId - {}", userId, eventId);
        return ParticipationRequestMapper.toDto(participationRequestService.createRequest(userId, eventId));
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto cancelRequestByRequester(@PathVariable Long userId,
                                                            @PathVariable Long requestId) {
        log.info("cancelRequestByRequester, userId - {}, requestId - {}", userId, requestId);
        return ParticipationRequestMapper.toDto(participationRequestService.cancelRequestByRequester(userId, requestId));
    }
}
