package ru.practicum.explorewithme.exceptions;

public class UserIsNotOwnerException extends IllegalArgumentException {
    public UserIsNotOwnerException(String s) {
        super(s);
    }
}
