package ru.practicum.explorewithme.exceptions;

public class IncorrectStateException extends IllegalArgumentException {
    public IncorrectStateException(String s) {
        super(s);
    }
}
