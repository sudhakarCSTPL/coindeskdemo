package com.example.democoindeskapi.service;

import com.example.democoindeskapi.config.ConfigBase;
import com.example.democoindeskapi.controller.CoinDeskController;
import com.example.democoindeskapi.entity.BitcoinValueEntity;
import com.example.democoindeskapi.entity.DayOfThePrice;
import io.netty.handler.timeout.ReadTimeoutException;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.json.JSONObject;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;


import java.math.BigDecimal;
import java.net.ConnectException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
//import java.util.*;
import java.util.Date;
import java.util.concurrent.CompletableFuture;

@Service
public class CoinDeskReadDataService {

    private static final Logger logger = LoggerFactory.getLogger(CoinDeskReadDataService.class);

    @Value("${coindesk-url}")
    private String baseurl;
    @Autowired
    ConfigBase configBase;

    @Autowired
    DataCacheService dataCacheService;

    @Autowired(required = true)
    CurrencyConversionService currencyConversionService;



    public BitcoinValueEntity getData(LocalDate start, LocalDate end,String currency) {
        System.out.println("in service ");
        logger.info(  "startDate  :"+start+ "endDate : "+end+ "currency"+currency);
        BitcoinValueEntity bitcoinValueEntity = new BitcoinValueEntity();

        String responseData = WebClient.builder().baseUrl(configBase.getCoindeskurl()).build().get().uri("?start_date=" + start + "T00:00&end_date=" + end + "T23:59&ohlc=false").retrieve().bodyToMono(String.class)
                .doOnError(throwable -> {
                    if (throwable instanceof org.springframework.web.reactive.function.client.WebClientRequestException) {
                        throw new org.springframework.web.reactive.function.client.WebClientRequestException(throwable, HttpMethod.GET, ((WebClientRequestException) throwable).getUri(), ((WebClientRequestException) throwable).getHeaders());
                    } else if (throwable instanceof org.springframework.web.reactive.function.client.WebClientResponseException) {
                        System.out.println("throwable => {}" + throwable.toString() + "   " + ((WebClientResponseException) throwable).getStatusCode().value());
                        if (((WebClientResponseException) throwable).getStatusCode().value() != 500)
                            throw new WebClientResponseException(((WebClientResponseException) throwable).getStatusCode().value(), throwable.toString(), null, null, null);
                        else
                            throw new WebClientResponseException(500, throwable.toString(), null, null, null);
                    }
                })
                .onErrorResume(throwable -> throwable instanceof ReadTimeoutException || throwable instanceof ConnectException,
                        t -> Mono.error(new ConnectException())).block();
        if (responseData == null) {
            logger.info(" response Data data null");
        }


        DayOfThePrice maxmap = new DayOfThePrice();
        DayOfThePrice minmap = new DayOfThePrice();
        JSONObject jsonObject = new JSONObject(responseData);
        java.util.Iterator<String> keys = jsonObject.keys();
        Object data = jsonObject.get("data");
        JSONObject jsonObject1 = new JSONObject(new String(data.toString()));
        JSONArray array = jsonObject1.getJSONArray("entries");

        Object name = jsonObject1.getString("name");
        Object iso = jsonObject1.getString("iso");
        Object src = jsonObject1.getString("src");
        Object ingestionStart = jsonObject1.getString("ingestionStart");
        Object interval = jsonObject1.getString("interval");
        Object slug = jsonObject1.getString("slug");
        JSONArray startar = array.getJSONArray(0);
        Double max = startar.getDouble(1);
        Double min = startar.getDouble(1);
        long maxday = 0;
        long minday = 0;

        for (int i = 1; i < array.length(); i++) {
            JSONArray long1 = array.getJSONArray(i);
            int length = long1.length();
            if (max < long1.getDouble(1)) {
                max = long1.getDouble(1);
                maxday = long1.getLong(0);
            }
            if (min > long1.getDouble(1)) {
                min = long1.getDouble(1);
                minday = long1.getLong(0);
            }
        }
        logger.info(maxday + "  " + max + "  " + minday + "  " + min);

        Date maxdaydate = new Date(maxday);
        Date mindaydate = new Date(minday);
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        maxmap.setDate(formatter.format(maxdaydate));
        minmap.setDate(formatter.format(mindaydate));
        Double currencyrate =1.0;
        try {
         currencyrate = currencyConversionService.getCurrencyValue(currency);
         logger.info(  " currencyRate " + currencyrate  );
        }catch( Exception ex){
            name = name +" value in USD only";
        }
        minmap.setValue(min*currencyrate);
        maxmap.setValue(max*currencyrate);
        bitcoinValueEntity.setMin(minmap);
        bitcoinValueEntity.setMax(maxmap);
        bitcoinValueEntity.setIso(iso.toString());
        bitcoinValueEntity.setSlug(slug.toString());
        bitcoinValueEntity.setSrc(src.toString());
        bitcoinValueEntity.setIngestionStart(ingestionStart.toString());
        bitcoinValueEntity.setName(name.toString());
        bitcoinValueEntity.setInterval(interval.toString());
        if (array != null) {
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                dataCacheService.updateCache(array);
            });
        }
        logger.info(  " End of request in service "   );
        return bitcoinValueEntity;

    }


}
