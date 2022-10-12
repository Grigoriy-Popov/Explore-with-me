package ru.practicum.explorewithme;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static ru.practicum.explorewithme.Constants.DATE_TIME_PATTERN;

@Service
@RequiredArgsConstructor
public class StatServiceImpl implements StatService {
    private final StatRepository statRepository;

    @Override
    public EndpointHit createHit(EndpointHit endpointHit) {
        return statRepository.save(endpointHit);
    }

    @Override
    public List<ViewStats> getStat(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        List<ViewStats> stats = new ArrayList<>();
        for (String uri : uris) {
            ViewStats viewStats = ViewStats.builder()
                    .app("ewm-main-service")
                    .uri(uri)
                    .build();
            if (unique) {
                viewStats.setHits(statRepository.countDistinctByTimestampBetweenAndUri(start, end, uri));
            } else {
                viewStats.setHits(statRepository.countByTimestampBetweenAndUri(start, end, uri));
            }
            stats.add(viewStats);
        }
        return stats;
    }
}
