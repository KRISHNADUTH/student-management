package com.ghss.studentmanagement.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

@Service
public class StudentServiceImpl implements IStudentService {
    @Autowired
    StudentRepository studentRepository;

    @Autowired
    CourseRepository courseRepository;

    @Override
    public void addStudent(StudentDto studentDto) {
        if (studentDto != null) {
            Student student = StudentMapper.mapToSrudent(studentDto, new Student());
            List<CourseDto> courseDtos = studentDto.getCourses();
            int pendingFee = 0;
            for (CourseDto courseDto : courseDtos) {
                Enrollment newEnrollment = new Enrollment();
                Course course = courseRepository.findByCourseName(courseDto.getCourseName()).get();
                pendingFee+=course.getCourseFee();
                course.addEnrollment(newEnrollment);
                student.addEnrollment(newEnrollment);
            }
            student.setEnrollmentDate(LocalDate.now());
            student.setPendingFee(pendingFee);
            studentRepository.save(student);
        } else {
            throw new NullPointerException("Please provide valid entries");
        }
    }

}
