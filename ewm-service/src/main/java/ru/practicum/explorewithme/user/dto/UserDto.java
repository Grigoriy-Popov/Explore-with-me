package ru.practicum.explorewithme.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    Long id;

    @NotBlank (message = "Name can't be empty")
    String name;

    @Email (message = "Invalid email format")
    @NotBlank (message = "Email can't be empty")
    String email;
}

