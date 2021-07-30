package com.example.marcosfracchiawe.service;

import com.example.marcosfracchiawe.entities.BTCPrice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class SchedulerService {

    private static final Logger logger = LoggerFactory.getLogger(SchedulerService.class);

    private static final int VALUE =  10000;

    @Autowired
    private BTCService btcService;

    @Autowired
    private PersistenceService persistenceService;

    /**
     * Call to api to retrieve the bitcoin price and save it in a file
     */
    @Scheduled(fixedDelay = VALUE)
    public void scheduleTaskWithFixedRate() {
        logger.info("Retrieve BTC price data");
        BTCPrice btcPrice = btcService.getBtcPrice();
        persistenceService.saveData(btcPrice);
    }
}
