package aChaushev.architects.service.impl;

import aChaushev.architects.model.dto.EventAddDTO;
import aChaushev.architects.model.dto.EventDTO;
import aChaushev.architects.model.entity.Event;
import aChaushev.architects.model.entity.User;
import aChaushev.architects.repository.EventRepository;
import aChaushev.architects.repository.UserRepository;
import aChaushev.architects.service.exception.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EventServiceImplTest {

    private EventServiceImpl toTest;

    @Captor
    private ArgumentCaptor<Event> eventEntityCaptor;

    @Mock
    private EventRepository mockEventRepository;

    @Mock
    private UserRepository mockUserRepository;

    @Mock
    private ModelMapper mockModelMapper;

    @BeforeEach
    void setUp() {
        toTest = new EventServiceImpl(mockEventRepository, mockModelMapper, mockUserRepository);

        lenient().when(mockModelMapper.map(any(EventAddDTO.class), eq(Event.class))).thenAnswer(invocation -> {
            EventAddDTO dto = invocation.getArgument(0);
            Event event = new Event();
            event.setName(dto.getName());
            event.setImageUrl(dto.getImageURL());
            event.setDescription(dto.getDescription());
            event.setDate(dto.getInputDate());
            return event;
        });
    }

    @Test
    void testAddEvent() {
        // Arrange
        EventAddDTO eventAddDTO = new EventAddDTO();
        eventAddDTO.setName("New Event");
        eventAddDTO.setImageURL("http://image.url");
        eventAddDTO.setDescription("Event Description");
        eventAddDTO.setInputDate(LocalDate.now());

        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        when(mockUserRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act
        toTest.addEvent(eventAddDTO, userId);

        // Assert
        verify(mockEventRepository).save(eventEntityCaptor.capture());
        Event actualSavedEntity = eventEntityCaptor.getValue();

        assertNotNull(actualSavedEntity);
        assertEquals(eventAddDTO.getName(), actualSavedEntity.getName());
        assertEquals(eventAddDTO.getImageURL(), actualSavedEntity.getImageUrl());
        assertEquals(eventAddDTO.getDescription(), actualSavedEntity.getDescription());
        assertEquals(eventAddDTO.getInputDate(), actualSavedEntity.getDate());
        assertEquals(user, actualSavedEntity.getUser());
    }

    @Test
    void testAddEvent_UserNotFound() {
        // Arrange
        EventAddDTO eventAddDTO = new EventAddDTO();
        eventAddDTO.setName("New Event");
        eventAddDTO.setImageURL("http://image.url");
        eventAddDTO.setDescription("Event Description");
        eventAddDTO.setInputDate(LocalDate.now());

        Long userId = 1L;

        when(mockUserRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ObjectNotFoundException.class, () -> toTest.addEvent(eventAddDTO, userId));
        verify(mockEventRepository, never()).save(any());
    }

    @Test
    void testGetAllEvents() {
        // Arrange
        Event event = new Event();
        event.setName("Event 1");
        event.setImageUrl("http://image.url");
        event.setDescription("Description 1");
        event.setDate(LocalDate.now());
        User user = new User();
        user.setId(1L);
        event.setUser(user);

        when(mockEventRepository.findAll()).thenReturn(List.of(event));

        EventDTO eventDTO = new EventDTO();
        eventDTO.setName("Event 1");
        eventDTO.setImageUrl("http://image.url");
        eventDTO.setDescription("Description 1");
        eventDTO.setDate(LocalDate.now());
        eventDTO.setUser(user);
        when(mockModelMapper.map(event, EventDTO.class)).thenReturn(eventDTO);

        // Act
        List<EventDTO> result = toTest.getAllEvents();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Event 1", result.get(0).getName());
        assertEquals("http://image.url", result.get(0).getImageUrl());
        assertEquals("Description 1", result.get(0).getDescription());
        assertEquals(LocalDate.now(), result.get(0).getDate());
        assertEquals(user, result.get(0).getUser());
    }

    @Test
    void testGetUserEvents() {
        // Arrange
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        Event event = new Event();
        event.setName("Event 1");
        event.setImageUrl("http://image.url");
        event.setDescription("Description 1");
        event.setDate(LocalDate.now());
        event.setUser(user);

        when(mockUserRepository.findById(userId)).thenReturn(Optional.of(user));
        when(mockEventRepository.findByUser(user)).thenReturn(List.of(event));

        EventDTO eventDTO = new EventDTO();
        eventDTO.setName("Event 1");
        eventDTO.setImageUrl("http://image.url");
        eventDTO.setDescription("Description 1");
        eventDTO.setDate(LocalDate.now());
        eventDTO.setUser(user);
        when(mockModelMapper.map(event, EventDTO.class)).thenReturn(eventDTO);

        // Act
        List<EventDTO> result = toTest.getUserEvents(userId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Event 1", result.get(0).getName());
        assertEquals("http://image.url", result.get(0).getImageUrl());
        assertEquals("Description 1", result.get(0).getDescription());
        assertEquals(LocalDate.now(), result.get(0).getDate());
        assertEquals(user, result.get(0).getUser());
    }

    @Test
    void testGetEventById() {
        // Arrange
        Long eventId = 1L;
        Event event = new Event();
        event.setName("Event 1");
        event.setImageUrl("http://image.url");
        event.setDescription("Description 1");
        event.setDate(LocalDate.now());
        User user = new User();
        user.setId(1L);
        event.setUser(user);

        when(mockEventRepository.findById(eventId)).thenReturn(Optional.of(event));

        EventDTO eventDTO = new EventDTO();
        eventDTO.setName("Event 1");
        eventDTO.setImageUrl("http://image.url");
        eventDTO.setDescription("Description 1");
        eventDTO.setDate(LocalDate.now());
        eventDTO.setUser(user);
        when(mockModelMapper.map(event, EventDTO.class)).thenReturn(eventDTO);

        // Act
        EventDTO result = toTest.getEventById(eventId);

        // Assert
        assertNotNull(result);
        assertEquals("Event 1", result.getName());
        assertEquals("http://image.url", result.getImageUrl());
        assertEquals("Description 1", result.getDescription());
        assertEquals(LocalDate.now(), result.getDate());
        assertEquals(user, result.getUser());
    }

    @Test
    void testGetEventById_EventNotFound() {
        // Arrange
        Long eventId = 1L;
        when(mockEventRepository.findById(eventId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ObjectNotFoundException.class, () -> toTest.getEventById(eventId));
    }

    @Test
    void testIsEventOwner() {
        // Arrange
        Long eventId = 1L;
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        Event event = new Event();
        event.setName("Event 1");
        event.setImageUrl("http://image.url");
        event.setDescription("Description 1");
        event.setDate(LocalDate.now());
        event.setUser(user);

        when(mockEventRepository.findById(eventId)).thenReturn(Optional.of(event));

        // Act
        boolean isOwner = toTest.isEventOwner(eventId, userId);

        // Assert
        assertTrue(isOwner);
    }

    @Test
    void testIsEventOwner_EventNotFound() {
        // Arrange
        Long eventId = 1L;
        Long userId = 1L;

        when(mockEventRepository.findById(eventId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ObjectNotFoundException.class, () -> toTest.isEventOwner(eventId, userId));
    }

    @Test
    void testRemoveEvent() {
        // Arrange
        Long eventId = 1L;

        // Act
        toTest.removeEvent(eventId);

        // Assert
        verify(mockEventRepository).deleteById(eventId);
    }
}

