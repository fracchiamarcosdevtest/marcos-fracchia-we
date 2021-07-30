package com.example.marcosfracchiawe.mock.entities;

import com.example.marcosfracchiawe.entities.BTCPrice;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class MockBTCPrice {

    private List<BTCPrice> btcPrices;
    private List<String> btcPricesJson;

    private MockBTCPrice(){
        this.btcPrices = storageBtcPrices();
        this.btcPricesJson = storageBtcPricesJson();
    }

    public static MockBTCPrice getInstance(){
        return new MockBTCPrice();
    }

    private List<BTCPrice> storageBtcPrices(){

        LocalDateTime time = LocalDateTime.now();
        BTCPrice btcPrice1 = new BTCPrice(38745.45,"BTC","USD", time);
        BTCPrice btcPrice2 = new BTCPrice(41745.28,"BTC","USD", time.plusSeconds(10));
        BTCPrice btcPrice3 = new BTCPrice(39745.96,"BTC","USD", time.plusSeconds(20));
        BTCPrice btcPrice4 = new BTCPrice(38235.72,"BTC","USD", time.plusSeconds(30));
        BTCPrice btcPrice5 = new BTCPrice(39122.32,"BTC","USD", time.plusSeconds(40));
        BTCPrice btcPrice6 = new BTCPrice(40001.98,"BTC","USD", time.plusSeconds(50));

        return Arrays.asList(btcPrice1,btcPrice2,btcPrice3,btcPrice4,btcPrice5,btcPrice6);
    }

    private List<String> storageBtcPricesJson(){

        String jsonString1 = new JSONObject()
                .put("lprice", "39934.8")
                .put("curr1", "BTC")
                .put("curr2", "USD")
                .toString();
        String jsonString2 = new JSONObject()
                .put("lprice", "39987.18")
                .put("curr1", "BTC")
                .put("curr2", "USD")
                .toString();
        String jsonString3 = new JSONObject()
                .put("lprice", "38134.8")
                .put("curr1", "BTC")
                .put("curr2", "USD")
                .toString();

        return Arrays.asList(jsonString1,jsonString2,jsonString3);
    }

    public List<BTCPrice> getBtcPrices(){

        return this.btcPrices;
    }

    public List<String> getBtcPricesJson(){

        return this.btcPricesJson;
    }
}
