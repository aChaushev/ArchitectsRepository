package bg.softuni.arch_repo.architects_offers.web;

import bg.softuni.arch_repo.architects_offers.model.dto.OfferAddDTO;
import bg.softuni.arch_repo.architects_offers.model.dto.OfferDTO;
import bg.softuni.arch_repo.architects_offers.service.OfferService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/offers")
public class OfferController {

  private static final Logger LOGGER = LoggerFactory.getLogger(OfferController.class);
  private final OfferService offerService;

  public OfferController(OfferService offerService) {
    this.offerService = offerService;
  }

  @GetMapping("/{id}")
  public ResponseEntity<OfferDTO> getById(@PathVariable("id") Long id) {
    return ResponseEntity.ok(offerService.getOfferById(id));
  }

  @GetMapping
  public ResponseEntity<List<OfferDTO>> getAllOffers() {
    return ResponseEntity.ok(offerService.getAllOffers());
  }

  @PostMapping
  public ResponseEntity<OfferDTO> createOffer(@Valid @RequestBody OfferAddDTO offerAddDTO, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return ResponseEntity.badRequest().build();
    }

    LOGGER.info("Going to create an offer {}", offerAddDTO);

    OfferDTO offerDTO = offerService.createOffer(offerAddDTO);
    return ResponseEntity.created(
            ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(offerDTO.getId())
                    .toUri()
    ).body(offerDTO);
  }
}
