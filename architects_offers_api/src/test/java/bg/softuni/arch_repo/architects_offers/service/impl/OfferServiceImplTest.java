package bg.softuni.arch_repo.architects_offers.service.impl;

import bg.softuni.arch_repo.architects_offers.model.dto.OfferAddDTO;
import bg.softuni.arch_repo.architects_offers.model.dto.OfferDTO;
import bg.softuni.arch_repo.architects_offers.model.entity.Offer;
import bg.softuni.arch_repo.architects_offers.repository.OfferRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.Period;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OfferServiceImplTest {

    @Mock
    private OfferRepository mockOfferRepository;

    @InjectMocks
    private OfferServiceImpl offerService;

    private Period retentionPeriod;

    @BeforeEach
    void setUp() {
        retentionPeriod = Period.ofDays(30);  // or whatever period you want to use
        offerService = new OfferServiceImpl(mockOfferRepository, retentionPeriod, new ModelMapper());
    }

    @Test
    void testCreateOffer() {
        // Arrange
        OfferAddDTO offerAddDTO = new OfferAddDTO();
        offerAddDTO.setName("New Offer");
        offerAddDTO.setCreatedOn("2024-01-01");
        offerAddDTO.setValidUntil("2024-02-01");
        offerAddDTO.setPrice(BigDecimal.valueOf(1000));
        offerAddDTO.setDescription("New offer description");

        Offer offerEntity = new ModelMapper().map(offerAddDTO, Offer.class);
        offerEntity.setId(1L);

        when(mockOfferRepository.save(any(Offer.class))).thenReturn(offerEntity);

        // Act
        OfferDTO createdOffer = offerService.createOffer(offerAddDTO);

        // Assert
        assertNotNull(createdOffer);
        assertEquals("New Offer", createdOffer.getName());
        verify(mockOfferRepository, times(1)).save(any(Offer.class));
    }

    @Test
    void testGetOfferById() {
        // Arrange
        Long offerId = 1L;
        Offer offerEntity = new Offer();
        offerEntity.setId(offerId);
        offerEntity.setName("Test Offer");
        offerEntity.setCreatedOn("2024-01-01");
        offerEntity.setValidUntil("2024-02-01");
        offerEntity.setPrice(BigDecimal.valueOf(1000));
        offerEntity.setDescription("Test description");

        when(mockOfferRepository.findById(offerId)).thenReturn(Optional.of(offerEntity));

        // Act
        OfferDTO offerDTO = offerService.getOfferById(offerId);

        // Assert
        assertNotNull(offerDTO);
        assertEquals("Test Offer", offerDTO.getName());
        verify(mockOfferRepository, times(1)).findById(offerId);
    }

    @Test
    void testGetAllOffers() {
        // Arrange
        Offer offerEntity = new Offer();
        offerEntity.setId(1L);
        offerEntity.setName("Test Offer");
        offerEntity.setCreatedOn("2024-01-01");
        offerEntity.setValidUntil("2024-02-01");
        offerEntity.setPrice(BigDecimal.valueOf(1000));
        offerEntity.setDescription("Test description");

        List<Offer> offers = Collections.singletonList(offerEntity);
        when(mockOfferRepository.findAll()).thenReturn(offers);

        // Act
        List<OfferDTO> offerDTOs = offerService.getAllOffers();

        // Assert
        assertNotNull(offerDTOs);
        assertEquals(1, offerDTOs.size());
        assertEquals("Test Offer", offerDTOs.get(0).getName());
        verify(mockOfferRepository, times(1)).findAll();
    }

    @Test
    void testCleanupOldOffers() {
        // Arrange
        Instant now = Instant.now();
        Instant deleteBefore = now.minus(retentionPeriod);

        // Act
        offerService.cleanupOldOffers();

        // Assert
        verify(mockOfferRepository).deleteOldOffers(argThat(argument -> {
            // Allow for a small difference in the argument values
            long toleranceMillis = 1000; // 1 second tolerance
            return Math.abs(argument.toEpochMilli() - deleteBefore.toEpochMilli()) < toleranceMillis;
        }));
    }
}
