package com.ghss.studentmanagement.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ghss.studentmanagement.constatnts.StudentManagementConstants;
import com.ghss.studentmanagement.dto.CourseDto;
import com.ghss.studentmanagement.dto.ResponseDto;
import com.ghss.studentmanagement.exception.ResourceAlreadyExistsException;
import com.ghss.studentmanagement.mapper.CourseMapper;
import com.ghss.studentmanagement.model.Course;
import com.ghss.studentmanagement.repo.CourseRepository;
import com.ghss.studentmanagement.service.ICourseService;
import com.ghss.studentmanagement.service.StudentManagementService;

@Service
public class CourseServiceImpl implements ICourseService {

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    StudentManagementService studentManagementService;

    @Override
    public void addCourse(CourseDto courseDto) {
        if (courseDto.getCourseName() != null && !courseDto.getCourseName().isEmpty()) {
            Optional<Course> optionalCourse = studentManagementService
                    .findByCourseName(courseDto.getCourseName().toLowerCase());
            if (optionalCourse.isPresent()) {
                throw new ResourceAlreadyExistsException("Course", "name", courseDto.getCourseName());
            } else {
                Course course = CourseMapper.mapToCourse(courseDto, new Course());
                courseRepository.save(course);
                studentManagementService.loadData();
            }
        } else {
            throw new IllegalArgumentException("Course name must not be null or empty.");
        }
    }

    public ResponseEntity<Object> getAllCourses() {
        List<Course> courses = studentManagementService.getAllCourses();
        if (Objects.isNull(courses)||!courses.isEmpty()){
            List<CourseDto> courseDtos = new ArrayList<>();
            for (Course course : courses) {
                courseDtos.add(CourseMapper.mapToCourseDto(course, new CourseDto()));
            }
            return new ResponseEntity<>(courseDtos, HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(
                    new ResponseDto(StudentManagementConstants.STATUS_404, StudentManagementConstants.MESSAGE_404),HttpStatus.NOT_FOUND);
    }

}
