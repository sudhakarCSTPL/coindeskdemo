package com.example.democoindeskapi.service;

import com.example.democoindeskapi.config.ConfigBase;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
public class CurrencyConversionService {

    private static final Logger logger = LoggerFactory.getLogger(CurrencyConversionService.class);
    @Value("${currencyConversion.url}")
    String currencyConversionUrl = "";
    @Autowired
    ConfigBase configBase;

    public Double getCurrencyValue(String currency) {
        logger.info(" in getCurrencyValue");

        try {
            String responseData = WebClient.builder().baseUrl(configBase.getCurrencyConversionUrl()).build().get().uri("?target=" + currency).retrieve().bodyToMono(String.class).block();
            JSONObject jsonObject = new JSONObject(responseData);
            JSONObject data = jsonObject.getJSONObject("data");
            logger.info("data      " + data);
            logger.info("mid" + data.getBigDecimal("mid"));
            Double bigDecimal = data.getDouble("mid");
            return bigDecimal;
        } catch (WebClientResponseException ex) {
            logger.error("Error      " + ex.toString());
            throw ex;
        }

    }
}