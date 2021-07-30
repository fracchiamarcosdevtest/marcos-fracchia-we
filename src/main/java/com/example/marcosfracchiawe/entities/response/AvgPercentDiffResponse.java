package com.example.marcosfracchiawe.entities.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.MathContext;

@Getter @Setter
public class AvgPercentDiffResponse {

    private Double average;
    private BigDecimal percentageDifference;

    public AvgPercentDiffResponse(Double average, Double percentageDifference){

        this.average = average;
        this.percentageDifference = BigDecimal.valueOf(percentageDifference).round(new MathContext(2));
    }
}
