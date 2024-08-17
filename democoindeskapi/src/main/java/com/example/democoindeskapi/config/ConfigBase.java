package com.example.democoindeskapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public interface ConfigBase {




    @Value("${coindesk-url}")
     String coindeskurl ="";



    public String getCoindeskurl() ;

    public String getCurrencyConversionUrl() ;
}
