package aChaushev.architects.service;


import aChaushev.architects.model.dto.OfferAddDTO;
import aChaushev.architects.model.dto.OfferDTO;

import java.util.List;

public interface OfferService {

    void createOffer(OfferAddDTO offerAddDTO);

    void deleteOffer(long offerId);

    OfferDTO getOfferDetails(Long id);

    List<OfferDTO> getAllOffersSummary();
}
