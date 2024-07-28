package aChaushev.architects.init;

import aChaushev.architects.model.entity.Event;
import aChaushev.architects.repository.EventRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.util.List;

@Component
public class EventScheduler {

    private final EventRepository eventRepository;

    public EventScheduler(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

//    @Scheduled(fixedRate = 86400000) // Executes every 24 hours (86400000 milliseconds)
    @Scheduled(cron = "0 0 0 * * ?") // Executes at midnight every day
    public void deleteEventIfDateIsOld() {
        LocalDate now = LocalDate.now();

        // Fetch events with date before today
        List<Event> oldEvents = eventRepository.findByDateBefore(now);

        if (!oldEvents.isEmpty()) {
            eventRepository.deleteAll(oldEvents);
            System.out.println("Deleted " + oldEvents.size() + " old events.");
        } else {
            System.out.println("No old events found.");
        }
    }
}

