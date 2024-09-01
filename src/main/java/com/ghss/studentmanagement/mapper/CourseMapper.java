package com.ghss.studentmanagement.mapper;

import com.ghss.studentmanagement.dto.CourseDto;
import com.ghss.studentmanagement.model.Course;

public class CourseMapper {
    public static Course mapToCourse(CourseDto courseDto, Course course){
        course.setCourseName(courseDto.getCourseName());
        course.setCourseFee(courseDto.getCourseFee());
        return course;
    }

    public static CourseDto mapToCourseDto(Course course, CourseDto courseDto){
        courseDto.setCourseName(course.getCourseName());
        courseDto.setCourseFee(course.getCourseFee());
        return courseDto;
    }
}
