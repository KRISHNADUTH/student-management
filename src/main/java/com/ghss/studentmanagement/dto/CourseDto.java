package com.ghss.studentmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseDto {
    private String courseName;
    private double courseFee;  // This variable records whether student paid fees while enrolling course, value will be zero when no payment is made.
}
