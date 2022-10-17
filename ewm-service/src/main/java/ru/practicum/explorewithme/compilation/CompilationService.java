package ru.practicum.explorewithme.compilation;

import ru.practicum.explorewithme.compilation.dto.NewCompilationDto;

import java.util.List;

public interface CompilationService {

    //ADMIN
    Compilation createCompilation(NewCompilationDto newCompilationDto);

    void deleteCompilationById(Long compId);

    void deleteEventFromCompilation(Long compId, Long eventId);

    void addEventToCompilation(Long compId, Long eventId);

    void pinCompilation(Long compId);

    void unpinCompilation(Long compId);

    //PUBLIC
    List<Compilation> getAllCompilations(Boolean pinned, Integer from, Integer size);

    Compilation getCompilationById(Long compilationId);

}
