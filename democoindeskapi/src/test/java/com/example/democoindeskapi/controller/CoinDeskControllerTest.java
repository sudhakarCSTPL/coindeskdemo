package com.example.democoindeskapi.controller;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDate;
@SpringBootTest
@AutoConfigureMockMvc
public class CoinDeskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldReturnDefaultMessage() throws Exception {

        this.mockMvc.perform(get("/api/v1/coindesk/value?startdate=2019-02-10&enddate=2024-07-10&currency=INR"))
                .andDo(print())
                .andExpect(status().isOk())
               .andExpect(content().string(containsString("bitcoin")));
    }


}
