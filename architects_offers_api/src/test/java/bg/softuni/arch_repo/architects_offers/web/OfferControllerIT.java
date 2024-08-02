package bg.softuni.arch_repo.architects_offers.web;

import bg.softuni.arch_repo.architects_offers.model.entity.Offer;
import bg.softuni.arch_repo.architects_offers.repository.OfferRepository;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class OfferControllerIT {

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private MockMvc mockMvc;

    @AfterEach
    public void tearDown() {
        offerRepository.deleteAll();
    }

    @Test
    public void testGetById() throws Exception {
        // Arrange
        var actualEntity = createTestOffer();

        // ACT
        ResultActions result = mockMvc
                .perform(get("/offers/{id}", actualEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON));

        // Assert
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(actualEntity.getId().intValue())))
                .andExpect(jsonPath("$.description", is(actualEntity.getDescription())))
                .andExpect(jsonPath("$.createdOn", is(actualEntity.getCreatedOn())))
                .andExpect(jsonPath("$.validUntil", is(actualEntity.getValidUntil())))
                .andExpect(jsonPath("$.price", is(actualEntity.getPrice().intValue())));
    }

    @Test
    public void testOfferNotFound() throws Exception {
        mockMvc
                .perform(get("/offers/{id}", "1000000")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateOffer() throws Exception {
        MvcResult result = mockMvc.perform(post("/offers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                  {
                                    "createdOn": "2024-07-21",
                                    "validUntil": "2024-08-21",
                                    "price": 123.45,
                                    "description": "Test description"
                                  }
                                """)
                ).andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andReturn();

        String body = result.getResponse().getContentAsString();
        int id = JsonPath.read(body, "$.id");

        Optional<Offer> createdOfferOpt = offerRepository.findById((long) id);

        Assertions.assertTrue(createdOfferOpt.isPresent());
        Offer createdOffer = createdOfferOpt.get();

        Assertions.assertEquals("Test description", createdOffer.getDescription());
        Assertions.assertEquals(new BigDecimal("123.45"), createdOffer.getPrice());
        Assertions.assertEquals("2024-07-21", createdOffer.getCreatedOn());
        Assertions.assertEquals("2024-08-21", createdOffer.getValidUntil());
    }

    @Test
    public void testDeleteOffer() throws Exception {
        var actualEntity = createTestOffer();

        mockMvc.perform(delete("/offers/{id}", actualEntity.getId())
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent());

        Assertions.assertTrue(
                offerRepository.findById(actualEntity.getId()).isEmpty()
        );
    }

    private Offer createTestOffer() {

        Offer offer = new Offer();
        offer.setCreatedOn("2024-07-21");
        offer.setValidUntil("2024-08-21");
        offer.setPrice(new BigDecimal("2000"));
        offer.setDescription("test offer");


        return offerRepository.save(offer);
    }
}
