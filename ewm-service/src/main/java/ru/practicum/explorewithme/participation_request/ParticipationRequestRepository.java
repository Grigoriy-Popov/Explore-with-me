package ru.practicum.explorewithme.participation_request;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.event.Event;

import java.util.List;

public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Long> {
    Boolean existsByRequesterIdAndEventId(Long userId, Long eventId);

    Integer countByEventIdAndStatusIs(Long eventId, RequestStatus requestStatus);

    List<ParticipationRequest> findAllByRequesterId(Long userId);

    List<ParticipationRequest> findAllByEventId(Long eventId);

    List<ParticipationRequest> findAllByEventAndStatusIs(Event event, RequestStatus status);
}
