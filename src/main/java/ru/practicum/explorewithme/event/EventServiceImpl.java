package ru.practicum.explorewithme.event;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.event.category.Category;
import ru.practicum.explorewithme.event.category.CategoryService;
import ru.practicum.explorewithme.exceptions.IncorrectDateException;
import ru.practicum.explorewithme.user.User;
import ru.practicum.explorewithme.user.UserService;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final UserService userService;
    private final CategoryService categoryService;

    @Override
    public Event createEvent(NewEventDto newEventDto, Long userId) {
        if (newEventDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new IncorrectDateException("Event cannot begin earlier than 2 hours later");
        }
        User initiator = userService.getUserById(userId);
        Event event = EventMapper.fromNewEventDto(newEventDto);
        event.setInitiator(initiator);
        Category category = categoryService.getCategoryById(newEventDto.getCategoryId());
        event.setCategory(category);
    }

    @Override
    public Event cancelEvent(Long eventId, Long userId) {
        return null;
    }
}
