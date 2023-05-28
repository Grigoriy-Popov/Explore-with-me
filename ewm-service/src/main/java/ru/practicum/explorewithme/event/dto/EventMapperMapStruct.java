package ru.practicum.explorewithme.event.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.practicum.explorewithme.category.CategoryService;
import ru.practicum.explorewithme.category.dto.CategoryMapperMapStruct;
import ru.practicum.explorewithme.event.Event;
import ru.practicum.explorewithme.user.dto.UserMapperMapStruct;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {CategoryMapperMapStruct.class, UserMapperMapStruct.class, CategoryService.class})
public interface EventMapperMapStruct {

    Event toEntityFromNewEventDto(NewEventDto newEventDto);

    @Mapping(target = "category", source = "event.category")
    @Mapping(target = "initiator", source = "event.initiator")
    FullEventDto toFullDto(Event event);

    @Mapping(target = "category", source = "event.category")
    @Mapping(target = "initiator", source = "event.initiator")
    ShortEventDto toShortDto(Event event);

    List<ShortEventDto> toShortDto(Collection<Event> events);

    List<FullEventDto> toFullDto(Collection<Event> events);
}
