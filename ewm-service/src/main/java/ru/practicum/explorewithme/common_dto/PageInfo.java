package ru.practicum.explorewithme.common_dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class PageInfo {
    int from;
    int size;
    String sort;
}
