package com.example.marcosfracchiawe.mock.entities;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class MockBTCPriceResponse {

    private MockBTCPrice mockBTCPrice;

    private MockBTCPriceResponse(){

        this.mockBTCPrice = MockBTCPrice.getInstance();
    }

    public static MockBTCPriceResponse getInstance(){
        return new MockBTCPriceResponse();
    }

    public ArrayNode getHistoryData(){

        ObjectNode node = new ObjectMapper().registerModule(new JavaTimeModule()).createObjectNode();

        ArrayNode arrayNode = new ObjectMapper().createArrayNode();
        mockBTCPrice.getBtcPrices()
                .stream()
                .forEach(btcPrice -> {
                    node.put("lprice", btcPrice.getLprice());
                    node.put("curr1", btcPrice.getCurr1());
                    node.put("curr2", btcPrice.getCurr2());
                    node.put("dateTime", btcPrice.getDateTime().toString());
                    arrayNode.add(node);
                });

        return arrayNode;
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
