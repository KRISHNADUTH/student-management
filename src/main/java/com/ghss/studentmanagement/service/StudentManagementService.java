package com.ghss.studentmanagement.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ghss.studentmanagement.dto.StudentDto;
import com.ghss.studentmanagement.mapper.StudentMapper;
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
        System.out.println("Enrollments - "+enrollments.toString());
    }

    public StudentDto findNthStudentByEnrollmentDateWithHighestPendingFee(int n, LocalDate date) {
        System.out.println("Calling loadData().............................................................................................................");
        loadData();
        List<Student> sortedStudents = students.stream().filter(s -> s.getEnrollmentDate().isEqual(date)).sorted((o1, o2) -> {
            return o1.getPendingFee() < o2.getPendingFee() ? 1:-1;
        }).collect(Collectors.toList());
        if (n <= 0 || n > sortedStudents.size())
            throw new IllegalArgumentException("Invalid index : "+n);
        return StudentMapper.mapToStudentDto(sortedStudents.get(n - 1), new StudentDto());
    }

}