package ru.practicum.explorewithme.category;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Page<Category> findAll(Pageable page);

    @Query("SELECT c.name FROM Category c")
    List<String> getAllNames();
}
