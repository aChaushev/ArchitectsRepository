package bg.softuni.arch_repo.architects_offers.service.impl;

import bg.softuni.arch_repo.architects_offers.model.dto.OfferAddDTO;
import bg.softuni.arch_repo.architects_offers.model.dto.OfferDTO;
import bg.softuni.arch_repo.architects_offers.model.entity.Offer;
import bg.softuni.arch_repo.architects_offers.repository.OfferRepository;
import bg.softuni.arch_repo.architects_offers.service.OfferService;
import bg.softuni.arch_repo.architects_offers.service.exception.ObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OfferServiceImpl implements OfferService {

  private final OfferRepository offerRepository;
  private final ModelMapper modelMapper;

  public OfferServiceImpl(OfferRepository offerRepository, ModelMapper modelMapper) {
    this.offerRepository = offerRepository;
    this.modelMapper = modelMapper;
  }

  @Override
  public OfferDTO createOffer(OfferAddDTO offerAddDTO) {
    Offer offerEntity = modelMapper.map(offerAddDTO, Offer.class);
    Offer savedOffer = offerRepository.save(offerEntity);
    return modelMapper.map(savedOffer, OfferDTO.class);
  }

  @Override
  public OfferDTO getOfferById(Long id) {
    return offerRepository
            .findById(id)
            .map(offer -> modelMapper.map(offer, OfferDTO.class))
            .orElseThrow(ObjectNotFoundException::new);
  }

  @Override
  public void deleteOffer(Long offerId) {
    offerRepository.deleteById(offerId);
  }

  @Override
  public List<OfferDTO> getAllOffers() {
    return offerRepository
            .findAll()
            .stream()
            .map(offer -> modelMapper.map(offer, OfferDTO.class))
            .collect(Collectors.toList());
  }
}
