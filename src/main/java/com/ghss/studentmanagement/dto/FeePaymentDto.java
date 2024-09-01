package com.ghss.studentmanagement.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class FeePaymentDto {

    private double amount;

    private String studentName;

    private LocalDate paymentDate;
}
