package com.ghss.studentmanagement.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ghss.studentmanagement.dto.StudentDto;
import com.ghss.studentmanagement.service.StudentManagementService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping(path = "/student", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class StudentManagementController {

    StudentManagementService studentManagementService;

    // @GetMapping("/")
    // public void loadData(){
    // studentManagementService.loadData();
    // }

    @GetMapping("/{n}/nth-highest-pending-fee")
    public ResponseEntity<Object> findNthStudentByEnrollmentDateWithHighestPendingFee(@PathVariable int n,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return studentManagementService.findNthStudentByEnrollmentDateWithHighestPendingFee(n, date);
    }

    // Returns list of students who have enrolled in multiple courses and not paid
    // any fees in prev year, with the details of fees of those courses
    @GetMapping("/lastyear-no-fee-multiple-courses")
    public List<StudentDto> findStudentsWithNoFeesInLastYearAndMultipleCourses() {
        return studentManagementService.findStudentsWithNoFeesInLastYearAndMultipleCourses();
    }

    @GetMapping("/avg-fees")
    public Map<LocalDate, Double> getAverageFeeCollectedPerStudentPerBatch() {
        return studentManagementService.getAverageFeeCollectedPerStudentPerBatch();
    }

    @GetMapping("/top-5-longest-delinquent-payment")
    public List<StudentDto> findTop5StudentsWithLongestDelinquentPaymentHistory() {
        return studentManagementService.findTop5StudentsWithLongestDelinquentPaymentHistory();
    }

    @GetMapping("/enrolled-in-all-courses-not-paid")
    public List<StudentDto> findStudentsEnrolledInAllCoursesButNotPaidFees() {
        return studentManagementService.findStudentsEnrolledInAllCoursesButNotPaidFees();
    }


}
