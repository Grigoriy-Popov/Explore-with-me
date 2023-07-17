package ru.practicum.explorewithme.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

import static ru.practicum.explorewithme.Constants.DATE_TIME_PATTERN;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEventRequest {
    @NotBlank(message = "Annotation can not be empty")
    @NotEmpty(message = "Annotation can not be empty")
    private String annotation;

    @NotNull(message = "Category id can not be empty")
    private Long category;

    @NotBlank(message = "Description can not be empty")
    @NotEmpty(message = "Description can not be empty")
    private String description;

    @NotNull(message = "Event date can not be empty")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = DATE_TIME_PATTERN)
    private LocalDateTime eventDate;

    @NotNull(message = "Event id can not be empty")
    private Long eventId;

    @NotNull(message = "Event should be paid or not")
    private Boolean paid;

    @PositiveOrZero(message = "Participation limit can not be negative")
    @NotNull(message = "Participation limit can not be empty")
    private Integer participantLimit;

    @NotBlank(message = "Title can not be empty")
    @Size(min = 3, max = 120, message = "Minimal length of title is 3 symbols, maximum - 120")
    private String title;
}
