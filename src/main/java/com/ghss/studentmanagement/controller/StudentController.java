package com.ghss.studentmanagement.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ghss.studentmanagement.dto.ResponseDto;
import com.ghss.studentmanagement.dto.StudentDto;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping(path = "/students", produces = MediaType.APPLICATION_JSON_VALUE)
public class StudentController {
    
    
    
    @PostMapping("/add-student")
    public ResponseEntity<ResponseDto> addStudent(@RequestBody StudentDto studentDto){

        return null;
    }
}
