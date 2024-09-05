package com.ghss.studentmanagement.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ghss.studentmanagement.dto.CourseDto;
import com.ghss.studentmanagement.dto.ResponseDto;
import com.ghss.studentmanagement.service.StudentManagementService;
import com.ghss.studentmanagement.service.impl.CourseServiceImpl;


@RestController
@RequestMapping(path = "/courses", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class CourseController {

    @Autowired
    CourseServiceImpl courseServiceImpl;
    
    @Autowired
    StudentManagementService studentManagementService;

    @PostMapping("/add-course")
    public ResponseEntity<ResponseDto> addCourse(@Valid @RequestBody CourseDto courseDto) {
        // System.out.println(courseDto);
        courseServiceImpl.addCourse(courseDto);
        studentManagementService.loadData();
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto("OK", "Course Added"));
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<CourseDto>> getAllCourses(){
        return courseServiceImpl.getAllCourses();
    }
}
