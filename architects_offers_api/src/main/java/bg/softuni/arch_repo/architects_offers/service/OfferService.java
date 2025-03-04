package bg.softuni.arch_repo.architects_offers.service;

import bg.softuni.arch_repo.architects_offers.model.dto.OfferAddDTO;
import bg.softuni.arch_repo.architects_offers.model.dto.OfferDTO;

import java.util.List;

public interface OfferService {


  OfferDTO createOffer(OfferAddDTO offerAddDTO);

  OfferDTO getOfferById(Long id);

  List<OfferDTO> getAllOffers();;

  void cleanupOldOffers();
}
