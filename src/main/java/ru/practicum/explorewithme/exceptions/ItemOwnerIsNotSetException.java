package ru.practicum.explorewithme.exceptions;

public class ItemOwnerIsNotSetException extends IllegalArgumentException {
    public ItemOwnerIsNotSetException(String s) {
        super(s);
    }
}
