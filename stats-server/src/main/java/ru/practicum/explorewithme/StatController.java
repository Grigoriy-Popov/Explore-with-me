package ru.practicum.explorewithme;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.explorewithme.Constants.DATE_TIME_PATTERN;

@RestController
@RequiredArgsConstructor
@Slf4j
public class StatController {
    private final StatService statService;

    @GetMapping("/stats")
    public List<ViewStats>getStat(@RequestParam @DateTimeFormat(pattern = DATE_TIME_PATTERN) LocalDateTime start,
                                  @RequestParam @DateTimeFormat(pattern = DATE_TIME_PATTERN) LocalDateTime end,
                                  @RequestParam List<String> uris,
                                  @RequestParam(required = false, defaultValue = "false") Boolean unique) {
        log.info("getStat, uris - {}", uris);
        return statService.getStat(start, end, uris, unique);
    }

    @PostMapping("/hit")
    public EndpointHit hit(@RequestBody EndpointHit endpointHit) {
        log.info("hit");
        return statService.createHit(endpointHit);
    }
}
