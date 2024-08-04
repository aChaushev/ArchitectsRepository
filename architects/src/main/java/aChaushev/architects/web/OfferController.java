package aChaushev.architects.web;


import aChaushev.architects.model.dto.OfferAddDTO;
import aChaushev.architects.repository.OfferRepository;
import aChaushev.architects.service.OfferService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/offer")
public class OfferController {

    private final OfferService offerService;

    public OfferController(OfferService offerService, OfferRepository offerRepository) {
        this.offerService = offerService;
    }

    @GetMapping("/all")
    public String getAllOffers(Model model) {
        model.addAttribute("allOffers", offerService.getAllOffersSummary());
        return "offers/offers";
    }


    @GetMapping("/add")
    public String newOffer(Model model) {
        if (!model.containsAttribute("offerAddDTO")) {
            model.addAttribute("offerAddDTO", new OfferAddDTO());
        }
        return "offers/offer-add";
    }

    @PostMapping("add")
    public String createOffer(
            @Valid @ModelAttribute("offerAddDTO") OfferAddDTO offerAddDTO,
            BindingResult bindingResult,
            RedirectAttributes rAtt) {

        if (bindingResult.hasErrors()) {
            rAtt.addFlashAttribute("offerAddDTO", offerAddDTO);
            rAtt.addFlashAttribute("org.springframework.validation.BindingResult.offerAddDTO", bindingResult);
            return "redirect:/offer/add";
        }

        offerService.createOffer(offerAddDTO);
        return "redirect:/offer/all";
    }
}

