package ru.practicum.explorewithme.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

import javax.validation.Valid;

@RestController
@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
@Slf4j
public class AdminCompilationController {
    private final CompilationService compilationService;

    @PostMapping
    public CompilationDto createCompilation(@Valid @RequestBody NewCompilationDto newCompilationDto) {
        log.trace("hit endpoint - createCompilation");
        return CompilationMapper.toDto(compilationService.create(newCompilationDto));
    }

    @DeleteMapping("/{compId}")
    public void deleteCompilationById(@PathVariable Long compId) {
        log.trace("hit endpoint - deleteCompilationById - {}", compId);
        compilationService.deleteById(compId);
    }

    @DeleteMapping("/{compId}/events/{eventId}")
    public void deleteEventFromCompilation(@PathVariable Long compId, @PathVariable Long eventId) {
        log.trace("hit endpoint - deleteEventFromCompilation: compilation id - {}, event id - {}", compId, eventId);
        compilationService.deleteEventFromCompilation(compId, eventId);
    }

    @PatchMapping("/{compId}/events/{eventId}")
    public void addEventToCompilation(@PathVariable Long compId, @PathVariable Long eventId) {
        log.trace("hit endpoint - addEventToCompilation: compilation id - {}, event id - {}", compId, eventId);
        compilationService.addEventToCompilation(compId, eventId);
    }

    @PatchMapping("/{compId}/pin")
    public void pinCompilation(@PathVariable Long compId) {
        log.trace("hit endpoint - pinCompilation: compilation id - {}", compId);
        compilationService.pin(compId);
    }

    @DeleteMapping("/{compId}/pin")
    public void unpinCompilation(@PathVariable Long compId) {
        log.trace("hit endpoint - unpinCompilation: compilation id - {}", compId);
        compilationService.unpin(compId);
    }
}
