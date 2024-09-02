package com.ghss.studentmanagement.service;


import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ghss.studentmanagement.model.Course;
import com.ghss.studentmanagement.model.Enrollment;
import com.ghss.studentmanagement.model.Student;
import com.ghss.studentmanagement.repo.CourseRepository;
import com.ghss.studentmanagement.repo.EnrollmentRepository;
import com.ghss.studentmanagement.repo.StudentRepository;

@Service
public class StudentManagementService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    // @Autowired
    // private FeePaymentRepository feePaymentRepository;

    private List<Student> students;
    private List<Course> courses;
    private List<Enrollment> enrollments;

    @PostConstruct
    public void loadData() {
        students = studentRepository.findAll();
        courses = courseRepository.findAll();
        enrollments = enrollmentRepository.findAll();

        System.out.println("Loaded " + students.size() + " students.");
        System.out.println("Loaded " + courses.size() + " courses.");
        System.out.println("Loaded " + enrollments.size() + " enrollments.");
    }

}