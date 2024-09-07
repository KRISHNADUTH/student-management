package com.ghss.studentmanagement.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ghss.studentmanagement.constatnts.StudentManagementConstants;
import com.ghss.studentmanagement.dto.ResponseDto;
import com.ghss.studentmanagement.dto.StudentDto;
import com.ghss.studentmanagement.service.StudentManagementService;
import com.ghss.studentmanagement.service.impl.StudentServiceImpl;

@RestController
@RequestMapping(path = "/students", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class StudentController {

    @Autowired
    StudentServiceImpl studentServiceImpl;
    
    @Autowired
    StudentManagementService studentManagementService;

    @PostMapping("/add-student")
    public ResponseEntity<ResponseDto> addStudent(@Valid @RequestBody StudentDto studentDto) {

        studentServiceImpl.addStudent(studentDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDto(StudentManagementConstants.STATUS_201, String.format(StudentManagementConstants.MESSAGE_201, "Student")));
    }

    @PostMapping("/add-students")
    public ResponseEntity<ResponseDto> addStudents(@Valid @RequestBody List<StudentDto> studentDtos) {
        for (StudentDto studentDto : studentDtos)
            studentServiceImpl.addStudent(studentDto);
        studentManagementService.loadData();
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDto("201", "Students added"));
    }
}
