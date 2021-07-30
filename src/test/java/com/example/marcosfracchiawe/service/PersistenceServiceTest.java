package com.example.marcosfracchiawe.service;

import com.example.marcosfracchiawe.entities.BTCPrice;
import com.example.marcosfracchiawe.mock.entities.MockBTCPrice;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PersistenceServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(PersistenceService.class);

    private MockBTCPrice mockBTCPrice;

    String fileName = "BTCPriceHistoryTest.txt";
    String fileNameRead = "BTCPriceHistoryTestRead.txt";

    @InjectMocks
    private PersistenceService persistenceService;

    @Before
    public void setUp() throws Exception {

        this.mockBTCPrice = MockBTCPrice.getInstance();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void saveData(){

        BTCPrice btcPrice = mockBTCPrice.getBtcPrices().get(0);

        ReflectionTestUtils.setField(persistenceService, "fileName", fileName);

        persistenceService.saveData(btcPrice);

        Path path = Paths.get(fileName);
        assertTrue(Files.exists(path));

        List<BTCPrice> prices = persistenceService.getHistoryData();

        assertEquals(prices.size(),1);

        try {
            Files.delete(path);
        } catch (IOException e) {
            logger.error("Fail when try to delete file test created. Exception: {}",e.getMessage());
        }
    }

    @Test
    public void getHistoryData(){

        ReflectionTestUtils.setField(persistenceService, "fileName", fileNameRead);

        List<BTCPrice> prices = persistenceService.getHistoryData();

        assertEquals(prices.size(),4);
    }
}
