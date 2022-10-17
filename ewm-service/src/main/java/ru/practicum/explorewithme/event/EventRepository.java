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

    @Query("SELECT MIN(e.createdOn) FROM Event AS e")
    LocalDateTime getMinCreationDate();

    @Query("SELECT e FROM Event AS e WHERE e.initiator.id IN ?1 " +
            "AND e.state IN ?2 AND e.category.id IN ?3 " +
            "AND e.eventDate BETWEEN ?4 AND ?5")
    List<Event> getEventsByAdmin(List<Long> users, List<State> states, List<Long> categories,
                                 LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable page);

    @Query("SELECT e FROM Event AS e " +
            "WHERE ((UPPER(e.annotation) LIKE UPPER(CONCAT('%', ?1, '%')) " +
            "OR UPPER(e.description) LIKE UPPER(CONCAT('%', ?1, '%'))) " +
            "AND e.category.id IN ?2 " +
            "AND e.paid = ?3 " +
            "AND e.eventDate BETWEEN ?4 AND ?5) " +
            "ORDER BY e.eventDate DESC")
    List<Event> getAllEventsByPublicUser(String text, List<Long> categories, Boolean paid, LocalDateTime start,
                             LocalDateTime end,  Pageable pageable);

}
