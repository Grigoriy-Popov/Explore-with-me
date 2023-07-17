package ru.practicum.explorewithme.compilation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.compilation.dto.NewCompilationDto;
import ru.practicum.explorewithme.event.Event;
import ru.practicum.explorewithme.event.service.PublicEventService;
import ru.practicum.explorewithme.exceptions.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final PublicEventService eventService;

    @Override
    public Compilation getById(Long compId) {
        return compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException(String.format("Compilation with id %d not found", compId)));
    }

    @Override
    public Compilation create(NewCompilationDto newCompilationDto) {
        List<Event> events = newCompilationDto.getEvents().stream()
                .map(eventService::getById)
                .collect(Collectors.toList());
        Compilation compilation = Compilation.builder()
                .events(events)
                .pinned(newCompilationDto.isPinned())
                .title(newCompilationDto.getTitle())
                .build();
        return compilationRepository.save(compilation);
    }

    @Override
    public void deleteById(Long compId) {
        compilationRepository.deleteById(compId);
    }

    @Override
    public void deleteEventFromCompilation(Long compId, Long eventId) {
        Compilation compilation = getById(compId);
        Event event = eventService.getById(eventId);
        List<Event> events = compilation.getEvents();
        events.remove(event);
        compilation.setEvents(events);
        compilationRepository.save(compilation);
    }

    @Override
    public void addEventToCompilation(Long compId, Long eventId) {
        Compilation compilation = getById(compId);
        Event event = eventService.getById(eventId);
        List<Event> events = compilation.getEvents();
        events.add(event);
        compilation.setEvents(events);
        compilationRepository.save(compilation);
    }

    @Override
    public void pin(Long compId) {
        Compilation compilation = getById(compId);
        compilation.setPinned(true);
        compilationRepository.save(compilation);
    }

    @Override
    public void unpin(Long compId) {
        Compilation compilation = getById(compId);
        compilation.setPinned(false);
        compilationRepository.save(compilation);
    }

    @Override
    public List<Compilation> getAll(Boolean pinned, Integer from, Integer size) {
        Pageable page = PageRequest.of(from / size, size);
        return compilationRepository.findAllByPinned(pinned, page);
    }
}
