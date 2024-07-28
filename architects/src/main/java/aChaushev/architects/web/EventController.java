package aChaushev.architects.web;

import aChaushev.architects.model.dto.EventAddDTO;
import aChaushev.architects.model.dto.EventDTO;
import aChaushev.architects.model.user.AppUserDetails;
import aChaushev.architects.service.EventService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@RequestMapping("/event")
@Controller
public class EventController {

    private final EventService eventService;
    private final ModelMapper modelMapper;

    public EventController(EventService eventService, ModelMapper modelMapper) {
        this.eventService = eventService;
        this.modelMapper = modelMapper;
    }

    @ModelAttribute("eventAddDTO")
    public EventAddDTO initEventAddDTO() {
        return new EventAddDTO();
    }

    @GetMapping("/all")
    public String getAllEvents(@AuthenticationPrincipal AppUserDetails userDetails, Model model) {
        Long userId = userDetails.getId();

        List<EventDTO> allEvents = eventService.getAllEvents();
        model.addAttribute("allEvents", allEvents);

        List<EventDTO> userEvents = eventService.getUserEvents(userId);
        model.addAttribute("userEvents", userEvents);

        return "events/all-events";
    }

    @GetMapping("/add")
    public String getAddEvent(Model model) {
        if (!model.containsAttribute("eventAddDTO")) {
            model.addAttribute("eventAddDTO", new EventAddDTO());
        }
        return "events/add-event";
    }

    @PostMapping("/add")
    public String doAddEvent(
            @Valid @ModelAttribute("eventAddDTO") EventAddDTO eventAddDTO,
            BindingResult bindingResult,
            @AuthenticationPrincipal AppUserDetails userDetails,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes
                    .addFlashAttribute("eventAddDTO", eventAddDTO)
                    .addFlashAttribute("org.springframework.validation.BindingResult.eventAddDTO", bindingResult);

            return "redirect:/event/add";
        }

        this.eventService.addEvent(eventAddDTO, userDetails.getId());

        return "redirect:/event/all";
    }

    @GetMapping("/details/{id}")
    public String getEventDetails(@PathVariable("id") Long eventId, Model model) {
        EventDTO event = eventService.getEventById(eventId);
        model.addAttribute("event", event);
        return "events/event-details";
    }


    @DeleteMapping("/remove/{id}")
    public String removeEvent(@PathVariable("id") Long eventId,
                              @AuthenticationPrincipal AppUserDetails userDetails) {
        if (!eventService.isEventOwner(eventId, userDetails.getId())) {
            throw new AccessDeniedException("You are not authorized to delete this event.");
        }
        this.eventService.removeEvent(eventId);
        return "redirect:/event/all";
    }
}

