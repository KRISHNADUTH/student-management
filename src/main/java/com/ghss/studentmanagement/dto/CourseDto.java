package com.ghss.studentmanagement.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseDto {
    @NotEmpty(message = "Course name should not be null or empty.")
    private String courseName;
    @PositiveOrZero(message = "Course fee should not be negative")
    private double courseFee;  // This variable records whether student paid fees while enrolling course(POSTing student), value will be zero when no payment is made.
}                              // While POSTing new student it recods fees of a course.
