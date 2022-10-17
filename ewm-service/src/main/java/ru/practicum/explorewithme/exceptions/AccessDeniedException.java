package ru.practicum.explorewithme.exceptions;

public class AccessDeniedException extends IllegalArgumentException {
    public AccessDeniedException(String s) {
        super(s);
    }
}
