package com.example.marcosfracchiawe.service;

import com.example.marcosfracchiawe.entities.BTCPrice;
import com.example.marcosfracchiawe.mock.entities.MockBTCPrice;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertNotNull;

public class BTCServiceTest {

    private MockBTCPrice mockedBtcPrice;

    @InjectMocks
    private BTCService btcService;

    @Mock
    private PersistenceService persistenceService;

    @Mock
    private WebClient.Builder webClientBuilder;

    @Before
    public void setUp() throws Exception {

        this.mockedBtcPrice = MockBTCPrice.getInstance();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getBtcPrice(){

        WebClient webClient = WebClient.builder().build();
        ReflectionTestUtils.setField(btcService, "cexApiUrl","https://cex.io/api/last_price/BTC/USD");

        Mockito.when(webClientBuilder.build()).thenReturn(webClient);

        BTCPrice btcPrice = btcService.getBtcPrice();

        assertNotNull(btcPrice);
    }

    @Test
    public void getBtcPriceHistory() {

        List<BTCPrice> prices = mockedBtcPrice.getBtcPrices();

        Mockito.when(persistenceService.getHistoryData()).thenReturn(prices);

        List<BTCPrice> result = btcService.getBtcPriceHistory();

        assertEquals(result.size(),prices.size());
        verify(persistenceService, times(1)).getHistoryData();
    }

    @Test
    public void getBtcPriceByDateTime(){

        BTCPrice btcPrice = mockedBtcPrice.getBtcPrices().get(0);
        List<BTCPrice> prices = mockedBtcPrice.getBtcPrices();

        Mockito.when(persistenceService.getHistoryData()).thenReturn(prices);

        Optional<BTCPrice> btcPriceResult = btcService.getBtcPriceByDateTime(btcPrice.getDateTime());

        assertNotNull(btcPriceResult.get());
        assertEquals(btcPriceResult.get().getLprice(),btcPrice.getLprice());
        verify(persistenceService, times(1)).getHistoryData();
    }

    @Test
    public void getBtcPriceAvg(){

        List<BTCPrice> prices = mockedBtcPrice.getBtcPrices();
        Mockito.when(persistenceService.getHistoryData()).thenReturn(prices);

        LocalDateTime dateTime = prices.get(0).getDateTime();
        Double avg = 39599.45166666667;

        Double avgResult = btcService.getBtcPriceAvg(dateTime,dateTime.plusSeconds(50));

        assertEquals(avg,avgResult);
        verify(persistenceService, times(1)).getHistoryData();
    }

    @Test
    public void getMaxPrice(){

        List<BTCPrice> prices = mockedBtcPrice.getBtcPrices();
        Mockito.when(persistenceService.getHistoryData()).thenReturn(prices);

        Double maxPrice = 41745.28;

        Double maxPriceResult = btcService.getMaxPrice();

        assertEquals(maxPrice,maxPriceResult);
        verify(persistenceService, times(1)).getHistoryData();
    }
}
