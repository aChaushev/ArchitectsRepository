package aChaushev.architects.model.dto;

import java.math.BigDecimal;
import java.util.Map;

public record ExRatesDTO(String base, Map<String, BigDecimal> rates) {

    // енкапсулация на JSON-a
    // десериализация в Java
    /*
    {
  "base": "USD",
  "rates": {
    ...
    "BGN": 1.812082,
    ...
    "EUR": 0.927608,
    ...
  }
}
     */
}
