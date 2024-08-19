package com.example.democoindeskapi.service;

import com.example.democoindeskapi.entity.BitcoinValueEntity;
import com.example.democoindeskapi.entity.DayOfThePrice;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

@Service
public class DataCacheService {

    private static final Logger logger = LoggerFactory.getLogger(DataCacheService.class);
    @Autowired(required = true)
    CurrencyConversionService currencyConversionService;
    TreeMap<String, Double> cacheData = new TreeMap<String, Double>();

    void updateCache(JSONArray array) {

        logger.info("  Array length" + array.length());
        for (int i = 1; i < array.length(); i++) {
            JSONArray long1 = array.getJSONArray(i);
            Date daydate = new Date(long1.getLong(0));
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String dateString = formatter.format(daydate);
            cacheData.put(dateString, long1.getDouble(1));
        }
    }

    public BitcoinValueEntity getFromCache(LocalDate start, LocalDate end, String currency) {
        logger.info("startDate  :" + start + "endDate : " + end + "currency" + currency);
        BitcoinValueEntity bitcoinValueEntity = new BitcoinValueEntity();
        if (cacheData.size() > 1) {
            List<Double[]> list = new ArrayList<>();

            try {
                SortedMap<String, Double> subSet = cacheData.subMap(start.toString(), true, end.toString(), true);

                String maxday = "";
                String minday = "";
                DayOfThePrice maxmap = new DayOfThePrice();
                DayOfThePrice minmap = new DayOfThePrice();
                Iterator<String> s = subSet.keySet().iterator();
                String key = s.next();
                Double value = subSet.get(key);
                Double min = value;
                Double max = value;
                while (s.hasNext()) {
                    key = s.next();
                    value = subSet.get(key);

                    if (max < value) {
                        max = value;
                        maxday = key;
                    } else if (min > value) {
                        min = value;
                        minday = key;
                    }
                }
                logger.info(maxday + "  " + max + "  " + minday + "  " + min);
                Double currencyrate = 1.0;
                String name = "Bitcoin";
                try {
                    currencyrate = currencyConversionService.getCurrencyValue(currency);
                    logger.info(" currencyRate " + currencyrate);
                } catch (Exception ex) {
                    name = name + " value in USD only";
                }
                ;


                maxmap.setDate(maxday.trim().toString());
                minmap.setDate(minday.trim().toString());
                minmap.setValue(min * currencyrate);
                maxmap.setValue(max * currencyrate);
                bitcoinValueEntity.setIso("iso");
                bitcoinValueEntity.setSlug("slug");
                bitcoinValueEntity.setSrc("src");
                bitcoinValueEntity.setIngestionStart("ingestionStart");
                bitcoinValueEntity.setName(name);
                bitcoinValueEntity.setInterval("interval");

            } catch (IllegalArgumentException illegalArgumentException) {
                throw new IllegalArgumentException(" something went working check the dates");
            }
        }
        return bitcoinValueEntity;
    }


}
