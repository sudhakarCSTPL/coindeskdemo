package com.example.democoindeskapi.entity;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@JsonIdentityReference
@Getter
@Setter
@NoArgsConstructor
public class DayOfThePrice {
    String date;
    Double value;
}
