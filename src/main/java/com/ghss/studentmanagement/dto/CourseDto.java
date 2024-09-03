package com.ghss.studentmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseDto {
    private String courseName;
    private double courseFee;  // This variable records whether student paid fees while enrolling course(POSTing student), value will be zero when no payment is made.
}                              // While POSTing new student it recods fees of a course.
