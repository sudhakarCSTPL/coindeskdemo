package com.example.democoindeskapi.entity;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@JsonIdentityReference
@Getter
@Setter
@NoArgsConstructor
public class BitcoinValueEntity {

    String iso;
    String name;
    String slug;
    String ingestionStart;
    String interval;
    String src;
    DayOfThePrice max;
    DayOfThePrice min;


}
