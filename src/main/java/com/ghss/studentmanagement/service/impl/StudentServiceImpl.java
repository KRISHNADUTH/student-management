package com.ghss.studentmanagement.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ghss.studentmanagement.dto.StudentDto;
import com.ghss.studentmanagement.exception.ResourceAlreadyExistsException;
import com.ghss.studentmanagement.mapper.StudentMapper;
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

    @Autowired
    StudentManagementService studentManagementService;

    @Autowired
    StudentMapper studentMapper;

    @Override
    public void addStudent(StudentDto studentDto) {
        if (studentDto.getName() != null && !studentDto.getName().isEmpty()) {
            if (studentManagementService.existByUserId(studentDto.getUserId()).isPresent()) {
                throw new ResourceAlreadyExistsException("Student", "user id", studentDto.getUserId());
            }
            Student student = studentMapper.mapToStudent(studentDto, new Student(),
                    studentManagementService.getAllCourses());
            studentRepository.save(student);
            studentManagementService.loadData();
        } else {
            throw new IllegalArgumentException("Course name must not be null or empty.");
        }
    }

}
