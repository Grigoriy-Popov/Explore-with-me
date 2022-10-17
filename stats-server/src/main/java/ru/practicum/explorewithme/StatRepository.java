package ru.practicum.explorewithme;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface StatRepository extends JpaRepository<EndpointHit, Long> {

    Long countDistinctByTimestampBetweenAndUri(LocalDateTime start, LocalDateTime end, String uri);

    Long countByTimestampBetweenAndUri(LocalDateTime start, LocalDateTime end, String uri);

}
