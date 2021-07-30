package com.example.marcosfracchiawe.service;

import com.example.marcosfracchiawe.entities.BTCPrice;
import com.example.marcosfracchiawe.mock.entities.MockBTCPrice;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SchedulerServiceTest {

    private MockBTCPrice mockedBtcPrice;

    @InjectMocks
    private SchedulerService schedulerService;

    @Mock
    private BTCService btcService;

    @Mock
    private PersistenceService persistenceService;

    @Before
    public void setUp() throws Exception {

        this.mockedBtcPrice = MockBTCPrice.getInstance();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void scheduleTaskWithFixedRate(){

        BTCPrice btcPrice = mockedBtcPrice.getBtcPrices().get(0);

        Mockito.when(btcService.getBtcPrice()).thenReturn(btcPrice);
        Mockito.doNothing().when(persistenceService).saveData(any());

        schedulerService.scheduleTaskWithFixedRate();

        verify(btcService, times(1)).getBtcPrice();
        verify(persistenceService, times(1)).saveData(any());
    }
}
