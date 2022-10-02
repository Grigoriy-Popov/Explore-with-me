package ru.practicum.explorewithme.exceptions;

public class ItemIsBookedException extends RuntimeException {
    public ItemIsBookedException(String message) {
        super(message);
    }
}
