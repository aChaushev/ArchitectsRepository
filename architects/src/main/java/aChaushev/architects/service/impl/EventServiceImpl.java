package aChaushev.architects.service.impl;

import aChaushev.architects.model.dto.EventAddDTO;
import aChaushev.architects.model.dto.EventDTO;
import aChaushev.architects.model.entity.Event;
import aChaushev.architects.model.entity.User;
import aChaushev.architects.repository.EventRepository;
import aChaushev.architects.repository.UserRepository;
import aChaushev.architects.service.EventService;
import aChaushev.architects.service.exception.ObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    public EventServiceImpl(EventRepository eventRepository,
                            ModelMapper modelMapper,
                            UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
    }

    @Override
    public void addEvent(EventAddDTO eventAddDTO, Long userId) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("User not found"));
        Event event = this.modelMapper.map(eventAddDTO, Event.class);
        event.setUser(user);
        eventRepository.save(event);
    }

    @Override
    public List<EventDTO> getAllEvents() {
        List<Event> events = this.eventRepository.findAll();
        List<EventDTO> eventDTOs = new ArrayList<>();
        for (Event event : events) {
            EventDTO eventDTO = this.modelMapper.map(event, EventDTO.class);
            eventDTOs.add(eventDTO);
        }
        return eventDTOs;
    }

//    @Override
//    public List<EventDTO> getUserEvents(Long userId) {
//        User user = userRepository.findById(userId).orElse(null);
//        List<Event> events = this.eventRepository.findByUser(user);
//        List<EventDTO> userEventDTOs = new ArrayList<>();
//        for (Event event : events) {
//            EventDTO userEvent = this.modelMapper.map(event, EventDTO.class);
//            userEventDTOs.add(userEvent);
//        }
//        return userEventDTOs;
//    }

    @Override
    public List<EventDTO> getUserEvents(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        List<Event> events = eventRepository.findByUser(user);
        return events.stream()
                .map(event -> modelMapper.map(event, EventDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public EventDTO getEventById(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ObjectNotFoundException("Event not found"));
        return modelMapper.map(event, EventDTO.class);
    }


    @Override
    public boolean isEventOwner(Long eventId, Long userId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ObjectNotFoundException("Event not found"));
        return event.getUser().getId().equals(userId);
    }

    @Override
    public void removeEvent(Long eventId) {
        eventRepository.deleteById(eventId);
    }
}
