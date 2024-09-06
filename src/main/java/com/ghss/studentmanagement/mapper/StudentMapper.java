package com.ghss.studentmanagement.mapper;
import java.util.*;

import com.ghss.studentmanagement.dto.CourseDto;
import com.ghss.studentmanagement.dto.StudentDto;
import com.ghss.studentmanagement.model.Course;
import com.ghss.studentmanagement.model.Enrollment;
import com.ghss.studentmanagement.model.Student;
import com.ghss.studentmanagement.service.StudentManagementService;

public class StudentMapper {
    public static Student mapToStudent(StudentDto studentDto, Student student) {
        student.setEnrollmentDate(studentDto.getEnrollmentDate());
        student.setName(studentDto.getName());
        student.setUserId(studentDto.getUserId());
        List<CourseDto> courseDtos = studentDto.getCourses();
            int pendingFee = 0;
            int totalFees = 0;
            int paidFees = 0;
            for (CourseDto courseDto : courseDtos) {
                Enrollment newEnrollment = new Enrollment(student.getEnrollmentDate(),courseDto.getCourseFee());
                Course course = StudentManagementService.findByCourseName(courseDto.getCourseName().toLowerCase()).get();
                totalFees+=course.getCourseFee();
                paidFees+=courseDto.getCourseFee();
                course.addEnrollment(newEnrollment);
                student.addEnrollment(newEnrollment);
            }
        student.setTotalFeePaid(paidFees);
        pendingFee = totalFees-paidFees;
        student.setPendingFee(pendingFee);
        return student;
    }

    public static StudentDto  mapToStudentDto(Student student, StudentDto studentDto){
        studentDto.setEnrollmentDate(student.getEnrollmentDate());
        studentDto.setName(student.getName());
        studentDto.setUserId(student.getUserId());
        List<CourseDto> courseDtos = new ArrayList<>();
        for(Enrollment enrollment:student.getEnrollments()){
            courseDtos.add(new CourseDto(enrollment.getCourse().getCourseName(), enrollment.getCourse().getCourseFee()));
        }
        studentDto.setCourses(courseDtos);
        studentDto.setPendingFee(student.getPendingFee());
        
        return studentDto;
    }

}
