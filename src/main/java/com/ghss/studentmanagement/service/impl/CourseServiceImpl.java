package com.ghss.studentmanagement.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ghss.studentmanagement.dto.CourseDto;
import com.ghss.studentmanagement.exception.ResourseAlreadyExistsException;
import com.ghss.studentmanagement.mapper.CourseMapper;
import com.ghss.studentmanagement.model.Course;
import com.ghss.studentmanagement.repo.CourseRepository;
import com.ghss.studentmanagement.service.ICourseService;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class CourseServiceImpl implements ICourseService{

    @Autowired
    CourseRepository courseRepository;

    @Override
    public void addCourse(CourseDto courseDto) {
        if(courseDto != null){
            Optional<Course> optionalCourse = courseRepository.findByCourseName(courseDto.getCourseName());
            if(optionalCourse.isPresent()){
                throw new ResourseAlreadyExistsException("Course","name",courseDto.getCourseName());
            } else {
                Course course = CourseMapper.mapToCourse(courseDto, new Course());
                courseRepository.save(course);
            }
        } else {
            throw new NullPointerException("Please provide valid entries");
        }
    }
    
}
