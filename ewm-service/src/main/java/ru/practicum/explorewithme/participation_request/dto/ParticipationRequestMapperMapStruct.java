package ru.practicum.explorewithme.participation_request.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.practicum.explorewithme.participation_request.ParticipationRequest;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ParticipationRequestMapperMapStruct {

    @Mapping(source = "request.event.id", target = "event")
    @Mapping(source = "request.requester.id", target = "requester")
    ParticipationRequestDto toDto(ParticipationRequest request);

    List<ParticipationRequestDto> toDto(Collection<ParticipationRequest> requests);
}
