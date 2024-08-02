package aChaushev.architects.web;


import aChaushev.architects.model.dto.OfferAddDTO;
import aChaushev.architects.service.OfferService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/offer")
public class OfferController {

    private final OfferService offerService;

    public OfferController(OfferService offerService) {
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
            @Valid OfferAddDTO offerAddDTO,
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

//  @GetMapping("/{id}")
//  public String offerDetails(@PathVariable("id") Long id,
//      Model model) {
//
//    model.addAttribute("offerDetails", offerService.getOfferDetails(id));
//
//    return "offers/offer-details";
//  }

//  @ResponseStatus(code = HttpStatus.NOT_FOUND)
//  @ExceptionHandler(ObjectNotFoundException.class)
//  public ModelAndView handleObjectNotFound(ObjectNotFoundException onfe) {
//    ModelAndView modelAndView = new ModelAndView("offer-not-found");
//    modelAndView.addObject("offerId", onfe.getId());
//
//    return modelAndView;
//  }

    @DeleteMapping("/{id}")
    public String deleteOffer(@PathVariable("id") Long id) {
        offerService.deleteOffer(id);
        return "redirect:/offers/all";
    }
}

