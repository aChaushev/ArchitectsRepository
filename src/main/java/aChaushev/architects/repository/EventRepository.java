package aChaushev.architects.repository;

import aChaushev.architects.model.entity.Event;
import aChaushev.architects.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByUser(User user);
}
