package aChaushev.architects.web.it;

import aChaushev.architects.config.TestSecurityConfig;
import aChaushev.architects.service.ExRateService;
import aChaushev.architects.service.exception.ApiObjectNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestSecurityConfig.class)
public class CurrencyControllerIT {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private ExRateService mockExRateService;

  @Test
  public void testConvert() throws Exception {
    String from = "SUD";
    String to = "ZWD";
    BigDecimal amount = new BigDecimal("100");
    BigDecimal expectedResult = new BigDecimal("50");

    when(mockExRateService.convert(from, to, amount)).thenReturn(expectedResult);

    mockMvc.perform(get("/api/convert")
        .param("from", from)
        .param("to", to)
        .param("amount", String.valueOf(amount.intValue()))
    ).andExpect(status().isOk())
        .andExpect(jsonPath("$.from").value(from))
        .andExpect(jsonPath("$.to").value(to))
        .andExpect(jsonPath("$.amount").value(amount))
        .andExpect(jsonPath("$.result").value(expectedResult));
  }

  @Test
  public void testConversionNotFound() throws Exception {
    String from = "SUD";
    String to = "ZWD";
    BigDecimal amount = new BigDecimal("100");

    when(mockExRateService.convert(from, to, amount))
        .thenThrow(new ApiObjectNotFoundException("Test message", "TestId"));

    mockMvc.perform(get("/api/convert")
        .param("from", from)
        .param("to", to)
        .param("amount", String.valueOf(amount.intValue()))
    ).andExpect(status().isNotFound());
  }

}
