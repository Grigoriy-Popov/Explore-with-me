package ru.practicum.explorewithme.compilation;

import ru.practicum.explorewithme.common_dto.PageInfo;
import ru.practicum.explorewithme.compilation.dto.NewCompilationDto;

import java.util.List;

public interface CompilationService {

    //ADMIN
    Compilation create(NewCompilationDto newCompilationDto);

    void deleteById(Long compId);

    void deleteEventFromCompilation(Long compId, Long eventId);

    void addEventToCompilation(Long compId, Long eventId);

    void pin(Long compId);

    void unpin(Long compId);

    //PUBLIC
    List<Compilation> getAll(Boolean pinned, PageInfo pageInfo);

    Compilation getById(Long compilationId);

}
