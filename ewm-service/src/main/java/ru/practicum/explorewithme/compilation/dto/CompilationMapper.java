package ru.practicum.explorewithme.compilation.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.explorewithme.compilation.Compilation;
import ru.practicum.explorewithme.event.dto.EventMapper;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class CompilationMapper {

    public static CompilationDto toDto(Compilation compilation) {
        return CompilationDto.builder()
                .id(compilation.getId())
                .events(EventMapper.toShortDto(compilation.getEvents()))
                .pinned(compilation.isPinned())
                .title(compilation.getTitle())
                .build();
    }

    public static List<CompilationDto> toDto(Collection<Compilation> compilations) {
        return compilations.stream()
                .map(CompilationMapper::toDto)
                .collect(Collectors.toList());
    }
}
