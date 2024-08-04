package aChaushev.architects.repository;

import aChaushev.architects.model.entity.Event;
import aChaushev.architects.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByUser(User user);
    List<Event> findByDateBefore(LocalDate date);

    Optional<Event> findByName(String name);
}
