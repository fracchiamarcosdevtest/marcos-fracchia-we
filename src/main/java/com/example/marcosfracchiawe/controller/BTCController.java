package com.example.marcosfracchiawe.controller;

import com.example.marcosfracchiawe.entities.BTCPrice;
import com.example.marcosfracchiawe.entities.response.AvgPercentDiffResponse;
import com.example.marcosfracchiawe.service.BTCService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/btc", headers = "Accept=application/json", produces = {"application/json"})
public class BTCController {

    @Autowired
    private BTCService btcService;

    @GetMapping("/price/history")
    @ApiOperation("Return history of bitcoin price retrieved from cex.io api")
    public ResponseEntity<List<BTCPrice>> retrieveBtcPriceHistory(){

        return ResponseEntity.ok(btcService.getBtcPriceHistory());
    }

    @GetMapping("/price")
    @ApiOperation("Search bitcoin price by date time on history data retrieved from cex.io api")
    public ResponseEntity<BTCPrice> getBtcPriceByDateTime(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime){

        Optional<BTCPrice> priceOpt = btcService.getBtcPriceByDateTime(dateTime);

        if(priceOpt.isPresent()){
            return ResponseEntity.ok(priceOpt.get());
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/price/avg")
    @ApiOperation("Calculate average of prices of bitcoin between two date time and percentage difference against the max value")
    public ResponseEntity<AvgPercentDiffResponse> getBtcPriceByDateTime(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTimeBegin,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTimeEnd){

        Double avg = btcService.getBtcPriceAvg(dateTimeBegin,dateTimeEnd);
        Double max = btcService.getMaxPrice();

        if(avg.equals(Double.NaN) || max.equals(Double.NaN)){
            return ResponseEntity.noContent().build();
        }

        Double percentDiff = ((max - avg)/max) * 100;
        return ResponseEntity.ok(new AvgPercentDiffResponse(avg,percentDiff));
    }
}
