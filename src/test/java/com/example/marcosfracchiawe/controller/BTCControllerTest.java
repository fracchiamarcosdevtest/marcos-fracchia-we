package com.example.marcosfracchiawe.controller;

import com.example.marcosfracchiawe.entities.BTCPrice;
import com.example.marcosfracchiawe.mock.entities.MockBTCPrice;
import com.example.marcosfracchiawe.mock.entities.MockBTCPriceResponse;
import com.example.marcosfracchiawe.service.BTCService;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BTCControllerTest {

    private MockMvc mockMvc;

    private MockBTCPriceResponse mockBTCPriceResponse;
    private MockBTCPrice mockBTCPrice;

    @InjectMocks
    private BTCController btcController;

    @Mock
    private BTCService btcService;

    @Before
    public void setUp() {

        mockBTCPriceResponse = MockBTCPriceResponse.getInstance();
        mockBTCPrice = MockBTCPrice.getInstance();

        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(btcController).build();
    }

    @Test
    public void retrieveBtcPriceHistory() throws Exception {

        List<BTCPrice> prices = mockBTCPrice.getBtcPrices();
        Mockito.when(btcService.getBtcPriceHistory()).thenReturn(prices);

        ArrayNode responseExpected = mockBTCPriceResponse.getHistoryData();
        String response = MockBTCPriceResponse.asJsonString(responseExpected);

        mockMvc.perform(get("/btc/price/history")
                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(content().json(response))
                .andExpect(status().isOk())
                .andDo(print());
    }

}
