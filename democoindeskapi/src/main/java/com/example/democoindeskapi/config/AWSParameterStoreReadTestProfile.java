package com.example.democoindeskapi.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Configuration
@Profile("awstest")
@Setter
@Getter
public class AWSParameterStoreReadTestProfile implements ConfigBase {



    @Value("${coindesk-url}")
    private String coindeskurl;

    @Value("${currencyConversion.url}")
    private String currencyConversionUrl ="";



    public String getCoindeskurl() {
        return coindeskurl;
    }

    public String getCurrencyConversionUrl() {
        return currencyConversionUrl;
    }
}
