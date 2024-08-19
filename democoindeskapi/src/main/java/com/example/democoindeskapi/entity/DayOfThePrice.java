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
public class DayOfThePrice {
    String date;
    Double value;
}
