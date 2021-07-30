package com.example.marcosfracchiawe.service;

import com.example.marcosfracchiawe.entities.BTCPrice;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Service
public class PersistenceService {

    private static final Logger logger = LoggerFactory.getLogger(PersistenceService.class);

    @Value("${persistence.file.name}")
    private String fileName;

    /**
     * Persist entity BTCPrice data on file in json format
     * @param btcPrice
     */
    public void saveData(BTCPrice btcPrice){

        try(BufferedWriter writer =  new BufferedWriter(new FileWriter(fileName, true));) {
            String btcPriceJson = this.mapEntityToJson(btcPrice);
            writer.write(btcPriceJson);
            writer.newLine();
        } catch (IOException e) {
            logger.error("PersistenceService.saveData - Fail Create or Write File - Exception: {}",e.getMessage());
        }
    }

    /**
     * Reed file and load all data of history prices of bitcoin
     * @return List of BTCPrice objects
     */
    public List<BTCPrice> getHistoryData(){

        List<BTCPrice> btcPrices = new ArrayList<>();
        Path path = Paths.get(fileName);
        try (Stream<String> lines = Files.lines(path, StandardCharsets.UTF_8);){
            lines.forEach(jsonData -> {
                 BTCPrice btcPrice = this.mapJsonToEntity(jsonData);
                 if(Objects.nonNull(btcPrice)){
                    btcPrices.add(btcPrice);
                 }
            });
        } catch (IOException e) {
            logger.error("PersistenceService.getHistoryData - " +
                    "Error trying to retrieve history data - Exception: {0}",e.getMessage());
        }
        return btcPrices;
    }

    /**
     * Using jackson library parse json string to object BTCPrice
     * @param jsonData
     * @return BTCPrice
     */
    private BTCPrice mapJsonToEntity(String jsonData){

        try {
            return  new ObjectMapper().readValue(jsonData, BTCPrice.class);
        } catch (JsonProcessingException e) {
            logger.error("PersistenceService.mapJsonToEntity - " +
                    "Error when try map string json data to entity BTCPrice. Exception: {0}", e.getMessage());
            return null;
        }
    }

    /**
     * Using jackson library parse object BTCPrice to json string
     * @param btcPrice
     * @return String
     */
    private String mapEntityToJson(BTCPrice btcPrice){

        try {
            return new ObjectMapper().writeValueAsString(btcPrice);
        } catch (JsonProcessingException e) {
            logger.error("PersistenceService.mapEntityToJson - Fail parse entity to json - Exception: {}",e.getMessage());
            return null;
        }
    }
}
