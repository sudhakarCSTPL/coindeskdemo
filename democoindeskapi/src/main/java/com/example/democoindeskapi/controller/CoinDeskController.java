package com.example.democoindeskapi.controller;

import com.example.democoindeskapi.entity.BitcoinValueEntity;
import com.example.democoindeskapi.service.CoinDeskReadDataService;
import com.example.democoindeskapi.service.DataCacheService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/coindesk")
@CrossOrigin("*")
public class CoinDeskController {
    private static final Logger logger = LoggerFactory.getLogger(CoinDeskController.class);
    @Autowired(required = true)
    CoinDeskReadDataService coinDeskReadDataService;

    @Autowired(required = true)
    DataCacheService dataCacheService;


    @Operation(summary = "Get the Bitcoin value")
    @ApiResponses({@ApiResponse(responseCode = "200", content = {
            @Content(schema = @Schema(implementation = BitcoinValueEntity.class))}, description = "Success"),
            @ApiResponse(responseCode = "401", content = {@Content(schema = @Schema(), mediaType = "application/json")}, description = "You are not Authorized to view the resource"),
            @ApiResponse(responseCode = "203", content = {@Content(schema = @Schema(), mediaType = "application/json")}, description = "No content check the input details"),
            @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema(), mediaType = "application/json")}, description = " details production.api.coindesk.com' [A(1), AAAA(28)]"),
            @ApiResponse(responseCode = "503", content = {@Content(schema = @Schema(), mediaType = "application/json")}, description = "server busy; try after some time"),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema(), mediaType = "application/json")}, description = "Failed to resolve or Validation Failed check the input)]")})
    @GetMapping(value = "/value", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BitcoinValueEntity> getData(@RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startdate, @RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate enddate, @RequestParam(required = true) String currency) {
        logger.info("start  in controller " + currency);

        try {
            logger.info(" controller  ");
            BitcoinValueEntity bitcoinValueEntity = coinDeskReadDataService.getData(startdate, enddate, currency);
            if (bitcoinValueEntity == null) {
                return ResponseEntity.ok(dataCacheService.getFromCache(startdate, enddate, currency));
            }
            return ResponseEntity.ok(bitcoinValueEntity);
        } catch (WebClientResponseException ex) {
            logger.error("WebClientResponseException " + ex.getStatusCode().value());
            switch (ex.getStatusCode().value()) {

                case 400 -> {
                    return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
                }
                case 401, 403 -> {
                    return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
                }
                case 404 -> {
                    return new ResponseEntity<>(null, HttpStatus.METHOD_NOT_ALLOWED);
                }
                case 503 -> {
                    logger.error(ex.getMessage() + "  " + ex.getStatusCode().value());
                    return ResponseEntity.ok(dataCacheService.getFromCache(startdate, enddate, currency));
                }
                default -> {
                    logger.error(ex.getMessage() + "  default switch  " + ex.getStatusCode().value());
                    return ResponseEntity.ok(dataCacheService.getFromCache(startdate, enddate, currency));
//                    return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
                }

            }

        } catch (WebClientRequestException ex) {
            logger.error("WebClientRequestException  " + ex.getMessage());
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}
