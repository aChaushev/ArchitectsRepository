package aChaushev.architects.service;

import aChaushev.architects.model.dto.EventAddDTO;
import aChaushev.architects.model.dto.EventDTO;

import java.util.List;

public interface EventService {
    void addEvent(EventAddDTO eventAddDTO, Long userId);

    List<EventDTO> getAllEvents();

    List<EventDTO> getUserEvents(Long userId);

    EventDTO getEventById(Long eventId);

    boolean isEventOwner(Long eventId, Long userId);

    void removeEvent(Long eventId);
}
