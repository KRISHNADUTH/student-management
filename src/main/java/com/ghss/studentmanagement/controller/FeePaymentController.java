package com.ghss.studentmanagement.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ghss.studentmanagement.dto.FeePaymentDto;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping(path = "/fee-payment", produces = MediaType.APPLICATION_JSON_VALUE)
public class FeePaymentController {
    @PostMapping("/add-fee-payment-data")
    public ResponseEntity<String> addfeeData(@RequestBody FeePaymentDto feePaymentDto){
        return null;
    }
}
