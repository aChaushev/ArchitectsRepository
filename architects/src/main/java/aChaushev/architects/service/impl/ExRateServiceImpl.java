package aChaushev.architects.service.impl;

import aChaushev.architects.config.ForexApiConfig;
import aChaushev.architects.model.dto.ExRatesDTO;
import aChaushev.architects.model.entity.ExRateEntity;
import aChaushev.architects.repository.ExRateRepository;
import aChaushev.architects.service.ExRateService;
import aChaushev.architects.service.exception.ApiObjectNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExRateServiceImpl implements ExRateService {

    private final Logger LOGGER = LoggerFactory.getLogger(ExRateServiceImpl.class);
    private final ExRateRepository exRateRepository;
    private final RestClient restClient;
    private final ForexApiConfig forexApiConfig;


    public ExRateServiceImpl(ExRateRepository exRateRepository,
                             @Qualifier("genericRestClient") RestClient restClient,
                             ForexApiConfig forexApiConfig) {
        this.exRateRepository = exRateRepository;
        this.restClient = restClient;
        this.forexApiConfig = forexApiConfig;
    }


    @Override
    public List<String> allSupportedCurrencies() {
        return exRateRepository
                .findAll()
                .stream()
                .map(ExRateEntity::getCurrency)
                .toList();
    }

    @Override
    public boolean hasInitializedExRates() {
        return exRateRepository.count() > 0;
    }

    @Override
    public ExRatesDTO fetchExRates() {
        return restClient
                .get()
                .uri(forexApiConfig.getUrl(), forexApiConfig.getKey())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(ExRatesDTO.class);

    }

    @Override
    public void updateRates(ExRatesDTO exRatesDTO) throws IllegalAccessException {
        LOGGER.info("Updating {} rates.", exRatesDTO.rates().size());

        if(!forexApiConfig.getBase().equals(exRatesDTO.base())) {
            throw new IllegalAccessException("The exchange rates that should be updated are not based on "
            + forexApiConfig.getBase() + " but rather on " + exRatesDTO.base());
        }

        exRatesDTO.rates().forEach((currency, rate) -> {
            ExRateEntity exRateEntity = exRateRepository.findByCurrency(currency)
                    .orElseGet(() -> new ExRateEntity()
                            .setCurrency(currency));

            exRateEntity.setRate(rate);

            exRateRepository.save(exRateEntity);
        });
    }

    private Optional<BigDecimal> findExRate(String from, String to) {

        if (Objects.equals(from, to)) {
            return Optional.of(BigDecimal.ONE);
        }

        // USD/BGN=x
        // USD/EUR=y

        // USD = x * BGN
        // USD = y * EUR

        // EUR/BGN = x / y

        Optional<BigDecimal> fromOpt = forexApiConfig.getBase().equals(from) ?
                Optional.of(BigDecimal.ONE) :
                exRateRepository.findByCurrency(from).map(ExRateEntity::getRate);

        Optional<BigDecimal> toOpt = forexApiConfig.getBase().equals(to) ?
                Optional.of(BigDecimal.ONE) :
                exRateRepository.findByCurrency(to).map(ExRateEntity::getRate);

        if (fromOpt.isEmpty() || toOpt.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(toOpt.get().divide(fromOpt.get(), 2, RoundingMode.HALF_DOWN));
        }
    }

    @Override
    public BigDecimal convert(String from, String to, BigDecimal amount) {
        return findExRate(from, to)
                .orElseThrow(() -> new ApiObjectNotFoundException("Conversion from " + from + " to " + to + " not " +
                        "possible!", from + "~" + to))
                .multiply(amount);
    }

    @Override
    public List<String> filterCurrencies(List<String> currencies) {
        List<String> requiredCurrencies = Arrays.asList("BGN", "USD", "EUR");
        return currencies.stream()
                .filter(requiredCurrencies::contains)
                .collect(Collectors.toList());
    }
}
