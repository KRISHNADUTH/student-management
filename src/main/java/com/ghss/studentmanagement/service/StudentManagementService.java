package com.ghss.studentmanagement.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ghss.studentmanagement.constatnts.StudentManagementConstants;
import com.ghss.studentmanagement.dto.StudentDto;
import com.ghss.studentmanagement.mapper.StudentMapper;
import com.ghss.studentmanagement.model.Course;
import com.ghss.studentmanagement.model.Student;
import com.ghss.studentmanagement.repo.CourseRepository;
import com.ghss.studentmanagement.repo.StudentRepository;

import lombok.Getter;
import lombok.Setter;

@Service
@Getter
@Setter
public class StudentManagementService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    StudentMapper studentMapper;

    private List<Student> students;
    private List<Course> courses;

    @PostConstruct
    public void loadData() {
        students = studentRepository.findAll();
        courses = courseRepository.findAll();

        System.out.println("Loaded " + students.size() + " students.");
        System.out.println("Loaded " + courses.size() + " courses.");
    }

    public ResponseEntity<Object> findNthStudentByEnrollmentDateWithHighestPendingFee(int n, LocalDate date) {
        if (students.size() < 1)
            return new ResponseEntity<>(StudentManagementConstants.MESSAGE_NO_ENROLLMENT, HttpStatus.BAD_REQUEST);
        List<Student> studentsWithPendingFeesorted = students.stream()
                .filter(s -> s.getEnrollmentDate().isEqual(date) && s.getPendingFee() > 0)
                .sorted((o1, o2) -> {
                    return o1.getPendingFee() < o2.getPendingFee() ? 1 : -1;
                }).collect(Collectors.toList());
        int studentsWithPendingFee = studentsWithPendingFeesorted.size();
        if (studentsWithPendingFee <= 0)
            return new ResponseEntity<>(
                    String.format(StudentManagementConstants.MESSAGE_NO_PENDING_FEE_PAYMENT_YEAR, date),
                    HttpStatus.BAD_REQUEST);
        if (n > studentsWithPendingFee)
            return new ResponseEntity<>(
                    (studentsWithPendingFee > 1 ? studentsWithPendingFee + " are pending with the fee payment"
                            : "Only one student is pending with the fee payment"),
                    HttpStatus.BAD_REQUEST);
        StudentDto studentDto = studentMapper.mapToStudentDto(studentsWithPendingFeesorted.get(n - 1),
                new StudentDto());
        return new ResponseEntity<>(studentDto, HttpStatus.OK);
    }

    public Optional<Course> findByCourseName(String courseName) {
        return courses.stream().filter(c -> c.getCourseName().equals(courseName)).findFirst();
    }

    public ResponseEntity<Object> findStudentsWithNoFeesInLastYearAndMultipleCourses() {
        if (students.size() < 1)
            return new ResponseEntity<>(StudentManagementConstants.MESSAGE_NO_ENROLLMENT, HttpStatus.BAD_REQUEST);
        LocalDate oneYearAgo = LocalDate.now().minus(1, ChronoUnit.YEARS);

        List<Student> studentIdsWithMultipleCoursesNoFeesPrevYear = students.stream()
                .filter(s -> s.getEnrollmentDate().getYear() == oneYearAgo.getYear() && s.getEnrollments().size() > 1)
                .filter(s -> s.getEnrollments().stream().allMatch(e -> e.getAmountPaid() == 0))
                .collect(Collectors.toList());
        if (studentIdsWithMultipleCoursesNoFeesPrevYear.size() < 1) {
            return new ResponseEntity<>(
                    String.format(StudentManagementConstants.MESSAGE_NO_PENDING_FEE_PAYMENT_YEAR, oneYearAgo),
                    HttpStatus.BAD_REQUEST);
        }
        List<StudentDto> studentDtos = new ArrayList<>();
        for (Student student : studentIdsWithMultipleCoursesNoFeesPrevYear) {
            studentDtos.add(studentMapper.mapToStudentDto(student, new StudentDto()));
        }

        return ResponseEntity.status(HttpStatus.OK).body(studentDtos);
    }

    public ResponseEntity<Object> getAverageFeeCollectedPerStudentPerBatch() {
        if (students.size() < 1)
            return new ResponseEntity<>(StudentManagementConstants.MESSAGE_NO_ENROLLMENT, HttpStatus.BAD_REQUEST);
        Map<LocalDate, Double> totalFeePerBatch = new TreeMap<>();
        Map<LocalDate, Integer> noOfEnrollmentsPerBatch = new TreeMap<>();
        Map<LocalDate, Double> avgFeePerBatch = new TreeMap<>();

        for (Student student : students) {
            LocalDate enrollmentDate = student.getEnrollmentDate();

            totalFeePerBatch.put(enrollmentDate,
                    totalFeePerBatch.getOrDefault(enrollmentDate, 0.0) + student.getTotalFeePaid());

            noOfEnrollmentsPerBatch.put(enrollmentDate,
                    noOfEnrollmentsPerBatch.getOrDefault(enrollmentDate, 0) + 1);
        }
        for (LocalDate date : totalFeePerBatch.keySet()) {
            avgFeePerBatch.put(date, totalFeePerBatch.get(date) / noOfEnrollmentsPerBatch.get(date));
        }

        return ResponseEntity.status(HttpStatus.OK).body(avgFeePerBatch);
    }

    public ResponseEntity<Object> findTop5StudentsWithLongestDelinquentPaymentHistory() {
        if (students.size() < 1)
            return new ResponseEntity<>(StudentManagementConstants.MESSAGE_NO_ENROLLMENT, HttpStatus.BAD_REQUEST);

        List<Student> topStudents = students.stream().filter(s -> s.getPendingFee() > 0)
                .sorted((s1, s2) -> s1.getPendingFee() > s2.getPendingFee() ? -1 : 1).collect(Collectors.toList());
        List<Student> top5Students = topStudents.size() > 5 ? topStudents.subList(0, 5) : topStudents;

        if (top5Students.size() < 1)
            return new ResponseEntity<Object>(StudentManagementConstants.MESSAGE_NO_PENDING_FEE_PAYMENT,
                    HttpStatus.BAD_REQUEST);

        List<StudentDto> studentDtos = new ArrayList<>();
        for (Student student : top5Students) {
            studentDtos.add(studentMapper.mapToStudentDto(student, new StudentDto()));
        }

        return ResponseEntity.status(HttpStatus.OK).body(studentDtos);
    }

    public ResponseEntity<Object> findStudentsEnrolledInAllCoursesButNotPaidFees() {
        if (students.size() < 1)
            return new ResponseEntity<>(StudentManagementConstants.MESSAGE_NO_ENROLLMENT, HttpStatus.BAD_REQUEST);

        int totalNoOfCourses = courses.size();
        List<Student> studentsEnrolledInAllCourseAndNoFee = students.stream()
                .filter(s -> (s.getEnrollments().size() == totalNoOfCourses && s.getPendingFee() > 0))
                .collect(Collectors.toList());
        
        List<StudentDto> studentDtos = new ArrayList<>();

        for (Student student : studentsEnrolledInAllCourseAndNoFee) {
            studentDtos.add(studentMapper.mapToStudentDto(student, new StudentDto()));
        }

        return ResponseEntity.status(HttpStatus.OK).body(studentDtos);
    }

    public List<Course> getAllCourses() {
        return courses;
    }

    public Optional<Student> existByUserId(String userId) {
        return students.stream().filter(s -> s.getUserId().equals(userId)).findFirst();
    }

    /*
     * public Map<LocalDate, Double> getAverageFeeCollectedPerCoursePerBatch() {
     * Map<LocalDate, Double> totalFeePerBatch = new TreeMap<>();
     * Map<LocalDate, Integer> noOfEnrollmentsPerBatch = new TreeMap<>();
     * Map<LocalDate, Double> avgFeePerBatch = new TreeMap<>();
     * 
     * for (Enrollment enrollment : enrollments) {
     * LocalDate enrollmentDate = enrollment.getEnrollmentDate();
     * totalFeePerBatch.put(enrollmentDate,
     * totalFeePerBatch.getOrDefault(enrollmentDate, 0.0) +
     * enrollment.getAmountPaid());
     * noOfEnrollmentsPerBatch.put(enrollmentDate,
     * noOfEnrollmentsPerBatch.getOrDefault(enrollmentDate, 0) + 1);
     * }
     * 
     * for (LocalDate date : totalFeePerBatch.keySet()) {
     * avgFeePerBatch.put(date, totalFeePerBatch.get(date) /
     * noOfEnrollmentsPerBatch.get(date));
     * }
     * 
     * return avgFeePerBatch;
     * }
     */

}