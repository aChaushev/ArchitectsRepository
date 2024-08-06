package aChaushev.architects.web;


import aChaushev.architects.model.dto.EventAddDTO;
import aChaushev.architects.model.dto.EventDTO;
import aChaushev.architects.model.user.AppUserDetails;
import aChaushev.architects.repository.EventRepository;
import aChaushev.architects.service.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class EventControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private EventController eventController;

    @Mock
    private EventService eventService;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private ModelMapper modelMapper;

    @Captor
    private ArgumentCaptor<EventAddDTO> eventAddDTOCaptor;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(eventController).build();
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testGetAllEvents() throws Exception {
        EventDTO eventDTO = new EventDTO();
        eventDTO.setId("1");
        eventDTO.setName("Test Event");

        when(eventService.getAllEvents()).thenReturn(Collections.singletonList(eventDTO));
        when(eventService.getUserEvents(anyLong())).thenReturn(Collections.singletonList(eventDTO));

        mockMvc.perform(MockMvcRequestBuilders.get("/event/all"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("events/all-events"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("allEvents"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("userEvents"))
                .andExpect(MockMvcResultMatchers.model().attribute("allEvents", hasItem(
                        allOf(
                                hasProperty("id", is("1")),
                                hasProperty("name", is("Test Event"))
                        )
                )))
                .andExpect(MockMvcResultMatchers.model().attribute("userEvents", hasItem(
                        allOf(
                                hasProperty("id", is("1")),
                                hasProperty("name", is("Test Event"))
                        )
                )));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testGetAddEvent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/event/add"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("events/add-event"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("eventAddDTO"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testDoAddEventSuccess() throws Exception {
        EventAddDTO eventAddDTO = new EventAddDTO();
        eventAddDTO.setName("Test Event");
        eventAddDTO.setDescription("This is a test event");
        eventAddDTO.setInputDate(LocalDate.now());

        when(eventRepository.findByName(anyString())).thenReturn(Optional.empty());
        doNothing().when(eventService).addEvent(any(EventAddDTO.class), anyLong());

        mockMvc.perform(MockMvcRequestBuilders.post("/event/add")
                        .with(SecurityMockMvcRequestPostProcessors.user(
                                new AppUserDetails("user", "password",
                                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")),
                                        1L, "user@example.com"
                                )
                        ))
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .flashAttr("eventAddDTO", eventAddDTO))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/event/all"));

        verify(eventService, times(1)).addEvent(any(EventAddDTO.class), anyLong());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testDoAddEventWithErrors() throws Exception {
        EventAddDTO eventAddDTO = new EventAddDTO();
        eventAddDTO.setName(""); // Invalid name to trigger validation error
        eventAddDTO.setDescription("Test Description");
        eventAddDTO.setInputDate(LocalDate.now());

        mockMvc.perform(MockMvcRequestBuilders.post("/event/add")
                        .flashAttr("eventAddDTO", eventAddDTO)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/event/add"))
                .andExpect(MockMvcResultMatchers.flash().attributeExists("eventAddDTO"))
                .andExpect(MockMvcResultMatchers.flash().attributeExists("org.springframework.validation.BindingResult.eventAddDTO"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testGetEventDetails() throws Exception {
        EventDTO eventDTO = new EventDTO();
        eventDTO.setId("1");
        eventDTO.setName("Test Event");
        eventDTO.setDescription("Event Description");

        when(eventService.getEventById(anyLong())).thenReturn(eventDTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/event/details/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("events/event-details"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("event"))
                .andExpect(MockMvcResultMatchers.model().attribute("event", hasProperty("name", is("Test Event"))));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testRemoveEvent() throws Exception {
        // Arrange
        when(eventService.isEventOwner(anyLong(), anyLong())).thenReturn(true);
        doNothing().when(eventService).removeEvent(anyLong());

        // Act & Assert
        mockMvc.perform(delete("/event/remove/1")
                        .with(csrf())) // Add CSRF token
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/event/all"));

        verify(eventService).removeEvent(anyLong());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testRemoveEventAccessDenied() throws Exception {
        when(eventService.isEventOwner(anyLong(), anyLong())).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.delete("/event/remove/1")
                        .with(SecurityMockMvcRequestPostProcessors.user(
                                new AppUserDetails("user", "password",
                                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")),
                                        1L, "user@example.com"
                                )
                        ))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }
}
