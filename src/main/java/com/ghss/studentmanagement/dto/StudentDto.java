package com.ghss.studentmanagement.dto;

import java.time.LocalDate;
import java.util.List;

import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentDto {

    @Size(min = 3, max = 30, message = "Name should have characters between 3-30.")
    private String name;

    private String userId;

    private LocalDate enrollmentDate;

    private List<CourseDto> courses;

    @PositiveOrZero(message = "Pending fees should be positive or negative.")
    private double pendingFee;

}

