package com.ghss.studentmanagement.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

    private static List<Student> students;
    private static List<Course> courses;
    private static List<Enrollment> enrollments;

    @PostConstruct
    public void loadData() {
        students = studentRepository.findAll();
        courses = courseRepository.findAll();
        enrollments = enrollmentRepository.findAll();

        System.out.println("Loaded " + students.size() + " students.");
        System.out.println("Loaded " + courses.size() + " courses.");
        System.out.println("Loaded " + enrollments.size() + " enrollments.");
    }

    public StudentDto findNthStudentByEnrollmentDateWithHighestPendingFee(int n, LocalDate date) {
        List<Student> sortedStudents = students.stream().filter(s -> s.getEnrollmentDate().isEqual(date))
                .sorted((o1, o2) -> {
                    return o1.getPendingFee() < o2.getPendingFee() ? 1 : -1;
                }).collect(Collectors.toList());
        if (n <= 0 || n > sortedStudents.size())
            throw new IllegalArgumentException("Invalid index : " + n);
        return StudentMapper.mapToStudentDto(sortedStudents.get(n - 1), new StudentDto());
    }

    public static Optional<Course> findByCourseName(String courseName) {
        return courses.stream().filter(c -> c.getCourseName().equals(courseName)).findFirst();
    }

    public List<StudentDto> findStudentsWithNoFeesInLastYearAndMultipleCourses() {
        LocalDate oneYearAgo = LocalDate.now().minus(1, ChronoUnit.YEARS);

        List<Student> studentIdsWithMultipleCoursesNoFeesPrevYear = students.stream().filter(s -> {
            if (s.getEnrollmentDate().getYear() == oneYearAgo.getYear() && s.getEnrollments().size() > 1) {
                return true;
            } else {
                return false;
            }
        }).filter(s -> s.getEnrollments().stream().filter(e -> e.getAmountPaid() == 0).count() == s.getEnrollments()
                .size())
                .collect(Collectors.toList());

        List<StudentDto> studentDtos = new ArrayList<>();
        for (Student student : studentIdsWithMultipleCoursesNoFeesPrevYear) {
            studentDtos.add(StudentMapper.mapToStudentDto(student, new StudentDto()));
        }

        return studentDtos;
    }

}