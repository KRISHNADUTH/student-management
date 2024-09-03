package com.ghss.studentmanagement.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import com.ghss.studentmanagement.dto.CourseDto;
import com.ghss.studentmanagement.dto.StudentDto;
import com.ghss.studentmanagement.mapper.StudentMapper;
import com.ghss.studentmanagement.model.Course;
import com.ghss.studentmanagement.model.Enrollment;
import com.ghss.studentmanagement.model.Student;
import com.ghss.studentmanagement.repo.CourseRepository;
import com.ghss.studentmanagement.repo.StudentRepository;
import com.ghss.studentmanagement.service.IStudentService;
import com.ghss.studentmanagement.service.StudentManagementService;

@Service
public class StudentServiceImpl implements IStudentService {
    @Autowired
    StudentRepository studentRepository;

    @Autowired
    CourseRepository courseRepository;

    @Override
    public void addStudent(StudentDto studentDto) {
        if (studentDto != null) {
            
            Student student = StudentMapper.mapToStudent(studentDto, new Student());
            System.out.println(student);
            studentRepository.save(student);
        } else {
            throw new NullPointerException("Please provide valid entries");
        }
    }

}
