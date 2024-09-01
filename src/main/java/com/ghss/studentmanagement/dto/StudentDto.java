package com.ghss.studentmanagement.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class StudentDto {

    private String name;

    private LocalDate enrollmentDate;

    private List<CourseDto> courses;

    private double pendingFee;

}

