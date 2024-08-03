package bg.softuni.arch_repo.architects_offers.service.impl;

import bg.softuni.arch_repo.architects_offers.model.dto.OfferAddDTO;
import bg.softuni.arch_repo.architects_offers.model.dto.OfferDTO;
import bg.softuni.arch_repo.architects_offers.model.entity.Offer;
import bg.softuni.arch_repo.architects_offers.repository.OfferRepository;
import bg.softuni.arch_repo.architects_offers.service.OfferService;
import bg.softuni.arch_repo.architects_offers.service.exception.ObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OfferServiceImpl implements OfferService {

  private final Logger LOGGER = LoggerFactory.getLogger(OfferService.class);
  private final OfferRepository offerRepository;
  private final Period retentionPeriod;
  private final ModelMapper modelMapper;

  public OfferServiceImpl(OfferRepository offerRepository,
                          @Value("${offers.retention.period}")  Period retentionPeriod,
                          ModelMapper modelMapper) {
    this.offerRepository = offerRepository;
      this.retentionPeriod = retentionPeriod;
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
  public List<OfferDTO> getAllOffers() {
    return offerRepository
            .findAll()
            .stream()
            .map(offer -> modelMapper.map(offer, OfferDTO.class))
            .collect(Collectors.toList());
  }

  @Override
  public void cleanupOldOffers() {
    Instant now = Instant.now();
    Instant deleteBefore = now.minus(retentionPeriod);
    LOGGER.info("Removing all offers older than " + deleteBefore);
    offerRepository.deleteOldOffers(deleteBefore);
    LOGGER.info("Old orders were removed");
  }
}
