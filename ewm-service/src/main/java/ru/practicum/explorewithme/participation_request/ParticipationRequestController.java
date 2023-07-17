package ru.practicum.explorewithme.participation_request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.participation_request.dto.ParticipationRequestDto;
import ru.practicum.explorewithme.participation_request.dto.ParticipationRequestMapperMapStruct;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/requests")
@RequiredArgsConstructor
@Slf4j
public class ParticipationRequestController {
    private final ParticipationRequestService participationRequestService;
    private final ParticipationRequestMapperMapStruct requestMapper;

    @GetMapping
    public List<ParticipationRequestDto> getUserRequestsByRequester(@PathVariable Long userId) {
        log.info("hit endpoint - getUserRequestsByRequester, userId - {}", userId);
        List<ParticipationRequest> requests = participationRequestService.getUserRequestsByRequester(userId);
//        return ParticipationRequestMapper.toDto(requests);
        return requestMapper.toDto(requests);
    }

    @PostMapping
    public ParticipationRequestDto createRequest(@PathVariable Long userId,
                                                @RequestParam Long eventId) {
        log.info("hit endpoint - createRequest, userId - {}, eventId - {}", userId, eventId);
        ParticipationRequest request = participationRequestService.createRequest(userId, eventId);
//        return ParticipationRequestMapper.toDto(request);
        return requestMapper.toDto(request);
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto cancelRequestByRequester(@PathVariable Long userId,
                                                            @PathVariable Long requestId) {
        log.info("hit endpoint - cancelRequestByRequester, userId - {}, requestId - {}", userId, requestId);
        ParticipationRequest request = participationRequestService.cancelRequestByRequester(userId, requestId);
//        return ParticipationRequestMapper.toDto(request);
        return requestMapper.toDto(request);
    }
}
