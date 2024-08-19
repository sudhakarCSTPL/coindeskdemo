package com.example.democoindeskapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Configuration
@Profile("dev")
public class ConfigLoadLocal implements ConfigBase {

    @Value("${currencyConversion.url}")
    String currencyConversionUrl = "";
    @Value("${spring.profiles.active}")
    private String activeProfile;
    @Value("${coindesk-url}")
    private String coindeskurl;

    public String getActiveProfile() {
        return activeProfile;
    }

    public String getCoindeskurl() {
        return coindeskurl;
    }

    public String getCurrencyConversionUrl() {
        return currencyConversionUrl;
    }
}
