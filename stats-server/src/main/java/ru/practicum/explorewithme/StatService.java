package ru.practicum.explorewithme;

import java.time.LocalDateTime;
import java.util.List;

public interface StatService {

    EndpointHit createHit(EndpointHit endpointHit);

    List<ViewStats> getStat(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);

}
