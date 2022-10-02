package ru.practicum.explorewithme.exceptions;

public class UserHasNotBookedItem extends RuntimeException {
    public UserHasNotBookedItem(String message) {
        super(message);
    }
}
