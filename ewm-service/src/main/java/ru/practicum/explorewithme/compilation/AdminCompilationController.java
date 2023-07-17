package ru.practicum.explorewithme.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.compilation.dto.CompilationDto;
import ru.practicum.explorewithme.compilation.dto.CompilationMapper;
import ru.practicum.explorewithme.compilation.dto.NewCompilationDto;

@RestController
@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
@Slf4j
public class AdminCompilationController {
    private final CompilationService compilationService;

    @PostMapping
    public CompilationDto createCompilation(@RequestBody @Validated NewCompilationDto newCompilationDto) {
        log.info("hit endpoint - createCompilation");
        return CompilationMapper.toDto(compilationService.createCompilation(newCompilationDto));
    }

    @DeleteMapping("/{compId}")
    public void deleteCompilationById(@PathVariable Long compId) {
        log.info("hit endpoint - deleteCompilationById - {}", compId);
        compilationService.deleteCompilationById(compId);
    }

    @DeleteMapping("/{compId}/events/{eventId}")
    public void deleteEventFromCompilation(@PathVariable Long compId, @PathVariable Long eventId) {
        log.info("hit endpoint - deleteEventFromCompilation: compilation id - {}, event id - {}", compId, eventId);
        compilationService.deleteEventFromCompilation(compId, eventId);
    }

    @PatchMapping("/{compId}/events/{eventId}")
    public void addEventToCompilation(@PathVariable Long compId, @PathVariable Long eventId) {
        log.info("hit endpoint - addEventToCompilation: compilation id - {}, event id - {}", compId, eventId);
        compilationService.addEventToCompilation(compId, eventId);
    }

    @PatchMapping("/{compId}/pin")
    public void pinCompilation(@PathVariable Long compId) {
        log.info("hit endpoint - pinCompilation: compilation id - {}", compId);
        compilationService.pinCompilation(compId);
    }

    @DeleteMapping("/{compId}/pin")
    public void unpinCompilation(@PathVariable Long compId) {
        log.info("hit endpoint - unpinCompilation: compilation id - {}", compId);
        compilationService.unpinCompilation(compId);
    }
}
