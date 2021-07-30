package com.example.marcosfracchiawe.service;

import com.example.marcosfracchiawe.entities.BTCPrice;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BTCService {

    private static final Logger logger = LoggerFactory.getLogger(BTCService.class);

    @Value("${cex.url}")
    private String cexApiUrl;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Autowired
    private PersistenceService persistenceService;

    /**
     * Call to cex.io api to get the price of bitcoin
     * @return BTCPrice entity with the data retrieved from the api
     */
    public BTCPrice getBtcPrice(){

        String jsonBtcPrice = webClientBuilder.build()
                .get()
                .uri(cexApiUrl)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        try {
            BTCPrice btcPrice = new ObjectMapper().readValue(jsonBtcPrice, BTCPrice.class);
            btcPrice.setDateTime(LocalDateTime.now());
            return btcPrice;
        }catch (Exception e){
            logger.error("BTCService.getBtcPrice - Fail when convert string json data to entity BTCPrice.");
            return null;
        }
    }

    /**
     * Return the history data of bitcoin price
     * @return List of BTCPrice with the history
     */
    public List<BTCPrice> getBtcPriceHistory() {

        return persistenceService.getHistoryData().stream()
                .sorted(Comparator.comparing(BTCPrice::getDateTime))
                .collect(Collectors.toList());
    }

    /**
     * Search the closest value of the date time received by parameter on historic data
     * @param dateTime
     * @return
     */
    public Optional<BTCPrice> getBtcPriceByDateTime(LocalDateTime dateTime) {

        List<BTCPrice> prices = this.getBtcPriceHistory();

        return prices.stream()
                .filter(btcPrice -> btcPrice.getDateTime().equals(dateTime))
                .findFirst();
    }

    /**
     * Calculate the average of prices of bitcoin between the two local date time received by parameter
     * @param dateTimeBegin
     * @param dateTimeEnd
     * @return
     */
    public Double getBtcPriceAvg(LocalDateTime dateTimeBegin, LocalDateTime dateTimeEnd) {

        return  this.getBtcPriceHistory()
                .stream()
                .filter(btcPrice ->  (btcPrice.getDateTime().compareTo(dateTimeBegin) >= 0)
                        && (btcPrice.getDateTime().compareTo(dateTimeEnd) < 1))
                .collect(Collectors.toList())
                .stream()
                .mapToDouble(BTCPrice::getLprice)
                .average()
                .orElse(Double.NaN);
    }

    /**
     * Return the max price saved on history data
     * @return
     */
    public Double getMaxPrice(){

        return  this.getBtcPriceHistory()
                .stream()
                .mapToDouble(BTCPrice::getLprice)
                .max()
                .orElse(Double.NaN);
    }
}
