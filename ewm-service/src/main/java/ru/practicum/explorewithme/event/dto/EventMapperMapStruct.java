package ru.practicum.explorewithme.event.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.explorewithme.event.Event;

@Mapper
public interface EventMapperMapStruct {
//    @Mapping(target = )
    ShortEventDto toShortEventDto(Event event);
}
