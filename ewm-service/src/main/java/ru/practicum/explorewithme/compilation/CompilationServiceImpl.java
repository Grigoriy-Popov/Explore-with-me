package ru.practicum.explorewithme.compilation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.compilation.dto.NewCompilationDto;
import ru.practicum.explorewithme.event.Event;
import ru.practicum.explorewithme.event.PublicEventService;
import ru.practicum.explorewithme.exceptions.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final PublicEventService eventService;

    @Override
    public Compilation getCompilationById(Long compId) {
        return compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException(String.format("Compilation with id %d not found", compId)));
    }

    @Override
    public Compilation createCompilation(NewCompilationDto newCompilationDto) {
        List<Event> events = newCompilationDto.getEvents().stream()
                .map(eventService::getEventById)
                .collect(Collectors.toList());
        Compilation compilation = Compilation.builder()
                .events(events)
                .pinned(newCompilationDto.isPinned())
                .title(newCompilationDto.getTitle())
                .build();
        return compilationRepository.save(compilation);
    }

    @Override
    public void deleteCompilationById(Long compId) {
        compilationRepository.deleteById(compId);
    }

    @Override
    public void deleteEventFromCompilation(Long compId, Long eventId) {
        Compilation compilation = getCompilationById(compId);
        Event event = eventService.getEventById(eventId);
        List<Event> events = compilation.getEvents();
        events.remove(event);
        compilation.setEvents(events);
        compilationRepository.save(compilation);
    }

    @Override
    public void addEventToCompilation(Long compId, Long eventId) {
        Compilation compilation = getCompilationById(compId);
        Event event = eventService.getEventById(eventId);
        List<Event> events = compilation.getEvents();
        events.add(event);
        compilation.setEvents(events);
        compilationRepository.save(compilation);
    }

    @Override
    public void pinCompilation(Long compId) {
        Compilation compilation = getCompilationById(compId);
        compilation.setPinned(true);
        compilationRepository.save(compilation);
    }

    @Override
    public void unpinCompilation(Long compId) {
        Compilation compilation = getCompilationById(compId);
        compilation.setPinned(false);
        compilationRepository.save(compilation);
    }

    @Override
    public List<Compilation> getAllCompilations(Boolean pinned, Integer from, Integer size) {
        Pageable page = PageRequest.of(from / size, size);
        return compilationRepository.getAllByPinned(pinned, page);
    }
}
