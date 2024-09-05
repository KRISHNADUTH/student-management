package com.ghss.studentmanagement.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ghss.studentmanagement.dto.CourseDto;
import com.ghss.studentmanagement.exception.ResourseAlreadyExistsException;
import com.ghss.studentmanagement.mapper.CourseMapper;
import com.ghss.studentmanagement.model.Course;
import com.ghss.studentmanagement.repo.CourseRepository;
import com.ghss.studentmanagement.service.ICourseService;
import com.ghss.studentmanagement.service.StudentManagementService;

@Service
public class CourseServiceImpl implements ICourseService{

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    StudentManagementService studentManagementService;

    @Override
    public void addCourse(CourseDto courseDto) {
        if(courseDto != null){
            Optional<Course> optionalCourse = courseRepository.findByCourseName(courseDto.getCourseName().toLowerCase());
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

    public ResponseEntity<List<CourseDto>> getAllCourses() {
        List<Course> courses = studentManagementService.getAllCourses();
        List<CourseDto> courseDtos = new ArrayList<>();
        for(Course course:courses){
            courseDtos.add(CourseMapper.mapToCourseDto(course, new CourseDto()));
        }
        if(!courseDtos.isEmpty())
            return new ResponseEntity<List<CourseDto>>(courseDtos, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    
}
