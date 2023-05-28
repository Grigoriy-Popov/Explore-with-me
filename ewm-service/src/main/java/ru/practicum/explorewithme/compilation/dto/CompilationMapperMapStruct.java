package ru.practicum.explorewithme.compilation.dto;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.practicum.explorewithme.compilation.Compilation;
import ru.practicum.explorewithme.event.dto.EventMapperMapStruct;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = EventMapperMapStruct.class)
public interface CompilationMapperMapStruct {

    CompilationDto toDto(Compilation compilation);

    List<CompilationDto> toDto(Collection<Compilation> compilations);
}
