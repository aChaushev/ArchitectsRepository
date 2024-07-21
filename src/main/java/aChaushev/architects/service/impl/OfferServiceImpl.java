package aChaushev.architects.service.impl;

import aChaushev.architects.model.dto.OfferAddDTO;
import aChaushev.architects.model.dto.OfferDTO;
import aChaushev.architects.repository.OfferRepository;
import aChaushev.architects.service.ExRateService;
import aChaushev.architects.service.OfferService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class OfferServiceImpl implements OfferService {

  private Logger LOGGER = LoggerFactory.getLogger(OfferServiceImpl.class);
  private final RestClient offerRestClient;
  private final OfferRepository offerRepository;
  private final ExRateService exRateService;

  public OfferServiceImpl(
      @Qualifier("offersRestClient") RestClient offerRestClient,
      OfferRepository offerRepository,

      ExRateService exRateService) {
    this.offerRestClient = offerRestClient;
    this.offerRepository = offerRepository;
    this.exRateService = exRateService;
  }

  @Override
  public void createOffer(OfferAddDTO offerAddDTO) {
    LOGGER.info("Creating new offer...");

    // todo - fix baseUrl.
    offerRestClient
        .post()
        .uri("http://localhost:8081/offers")
        .body(offerAddDTO)
        .retrieve();
  }

  @Override
  public void deleteOffer(long offerId) {
    offerRepository.deleteById(offerId);
  }

  @Override
  public OfferDTO getOfferDetails(Long id) {

    return offerRestClient
        .get()
        .uri("http://localhost:8081/offers/{id}", id)
        .accept(MediaType.APPLICATION_JSON)
        .retrieve()
        .body(OfferDTO.class);
  }

  @Override
  public List<OfferDTO> getAllOffersSummary() {
    LOGGER.info("Get all offers...");

    return offerRestClient
        .get()
        .uri("http://localhost:8081/offers")
        .accept(MediaType.APPLICATION_JSON)
        .retrieve()
        .body(new ParameterizedTypeReference<>(){});
  }
}
