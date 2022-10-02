package ru.practicum.explorewithme.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewEventDto {
    @NotBlank
    @Size(min = 20, max = 2000)
    private String annotation;

    @NotNull
    private Long categoryId;

    @NotBlank
    @Size(min = 20, max = 7000)
    private String description;

    @NotNull
    private LocalDateTime eventDate;

    @NotNull
    private Location location;

    @NotNull
    private Boolean paid;

    @PositiveOrZero
    @NotNull
    private Integer participantLimit;

    @NotNull
    private Boolean requestModeration;

    @NotBlank
    @Size(min = 20, max = 7000)
    private String title;

//    public NewEventDto(String annotation, Integer category, String description, LocalDateTime eventDate,
//                       Location location, Boolean paid, Integer participantLimit, Boolean requestModeration,
//                       String title) {
//        this.annotation = annotation;
//        this.category = category;
//        this.description = description;
//        this.eventDate = eventDate;
//        this.location = location;
//        this.paid = paid;
//        this.participantLimit = participantLimit;
//        this.requestModeration = requestModeration;
//        this.title = title;
//    }
}
