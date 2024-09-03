package com.ghss.studentmanagement.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ghss.studentmanagement.dto.StudentDto;
import com.ghss.studentmanagement.service.StudentManagementService;


@RestController
@RequestMapping(path = "/student", produces = MediaType.APPLICATION_JSON_VALUE)
public class StudentManagementController {


    @Autowired
    StudentManagementService studentManagementService;

    // @GetMapping("/")
    // public void loadData(){
    //     studentManagementService.loadData();
    // }

    @GetMapping("/{n}/nth-highest-pending-fee")
    public StudentDto findNthStudentByEnrollmentDateWithHighestPendingFee(@PathVariable int n,@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date){
        return studentManagementService.findNthStudentByEnrollmentDateWithHighestPendingFee(n, date);
    }

    @GetMapping("/lastyear-no-fee-multiple-courses")
    public List<StudentDto> findStudentsWithNoFeesInLastYearAndMultipleCourses(){
        return null;
    }

    @GetMapping("/avg-fees")
    public Map<LocalDate, Double> getAverageFeeCollectedPerStudentPerBatch(){
        return null;
    }

    @GetMapping("/top-5-longest-delinquent-payment")
    public List<StudentDto> findTop5StudentsWithLongestDelinquentPaymentHistory(){
        return null;
    }

    @GetMapping("/enrolled-in-all-courses-not-paid")
    public List<StudentDto> findStudentsEnrolledInAllCoursesButNotPaidFees(){
        return null;
    }

}
