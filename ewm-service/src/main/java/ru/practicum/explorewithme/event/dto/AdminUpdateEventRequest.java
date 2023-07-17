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
import ru.practicum.explorewithme.event.location.Location;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;

import static ru.practicum.explorewithme.Constants.DATE_TIME_PATTERN;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminUpdateEventRequest {
    @Size(min = 20, max = 2000, message = "Minimal length of annotation is 20 symbols, maximum - 2000")
    private String annotation;

    private Long category;

    @Size(min = 20, max = 7000, message = "Minimal length of description is 20 symbols, maximum - 7000")
    private String description;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = DATE_TIME_PATTERN)
    private LocalDateTime eventDate;

    private Location location;

    private Boolean paid;

    private Integer participantLimit;

    private Boolean requestModeration;

    @Size(min = 3, max = 120, message = "Minimal length of title is 3 symbols, maximum - 120")
    private String title;
}
