package ru.practicum.explorewithme.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError notFound(NotFoundException e) {
        return ApiError.builder()
                .status(HttpStatus.NOT_FOUND)
                .reason("The required object was not found.")
                .message(e.getLocalizedMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(IncorrectDateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError incorrectDate(IncorrectDateException e) {
        return ApiError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .reason("Incorrect date")
                .message(e.getLocalizedMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(IncorrectStateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError incorrectState(IncorrectStateException e) {
        return ApiError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .reason("Incorrect state")
                .message(e.getLocalizedMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError incorrectState(AccessDeniedException e) {
        return ApiError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .reason("You don't have access to do this")
                .message(e.getLocalizedMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(AlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError alreadyExists(AlreadyExistsException e) {
        return ApiError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .reason("Object is already exists")
                .message(e.getLocalizedMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError conflict(ConflictException e) {
        return ApiError.builder()
                .status(HttpStatus.CONFLICT)
                .reason("Conflict")
                .message(e.getLocalizedMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

//    @ExceptionHandler(Throwable.class)
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    public ApiError throwable(Throwable e) {
//        return ApiError.builder()
//                .status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .reason("Internal server error.")
//                .message(e.getLocalizedMessage())
//                .timestamp(LocalDateTime.now())
//                .build();
//    }
}
