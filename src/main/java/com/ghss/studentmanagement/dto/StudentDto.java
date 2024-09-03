package com.ghss.studentmanagement.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentDto {

    private String name;

    private LocalDate enrollmentDate;

    private List<CourseDto> courses;

    private double pendingFee;

}

