package ru.practicum.explorewithme.event;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explorewithme.user.User;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    boolean existsByCategoryId(Long categoryId);

    List<Event> findAllByInitiator(User user, Pageable page);

    @Query("SELECT MIN(e.createdOn) FROM Event e")
    LocalDateTime findMinCreationDate();

    @Query("SELECT e FROM Event e WHERE " +
            "e.initiator.id IN :users " +
            "AND e.state IN :states " +
            "AND e.category.id IN :categories " +
            "AND e.eventDate BETWEEN :rangeStart AND :rangeEnd")
    List<Event> findByAdmin(List<Long> users, List<State> states, List<Long> categories,
                            LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable page);

    @Query("SELECT e FROM Event e " +
            "WHERE ((UPPER(e.annotation) LIKE UPPER(CONCAT('%', :text, '%')) " +
            "OR UPPER(e.description) LIKE UPPER(CONCAT('%', :text, '%'))) " +
            "AND e.category.id IN :categories " +
            "AND e.paid = :paid " +
            "AND e.eventDate BETWEEN :start AND :end) " +
            "ORDER BY e.eventDate DESC")
    List<Event> findAllByPublicUser(String text, List<Long> categories, Boolean paid, LocalDateTime start,
                                    LocalDateTime end, Pageable pageable);

}
