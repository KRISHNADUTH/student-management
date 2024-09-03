package com.ghss.studentmanagement.mapper;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;

import com.ghss.studentmanagement.dto.CourseDto;
import com.ghss.studentmanagement.dto.StudentDto;
import com.ghss.studentmanagement.model.Course;
import com.ghss.studentmanagement.model.Enrollment;
import com.ghss.studentmanagement.model.Student;
import com.ghss.studentmanagement.repo.CourseRepository;
import com.ghss.studentmanagement.service.StudentManagementService;
public class StudentMapper {

    @Autowired
    CourseRepository courseRepository;  

    

    public static Student mapToStudent(StudentDto studentDto, Student student) {
        student.setEnrollmentDate(studentDto.getEnrollmentDate());
        student.setName(studentDto.getName());
        List<CourseDto> courseDtos = studentDto.getCourses();
            int pendingFee = 0;
            int totalFees = 0;
            int paidFees = 0;
            for (CourseDto courseDto : courseDtos) {
                Enrollment newEnrollment = new Enrollment(student.getEnrollmentDate());
                Course course = StudentManagementService.findByCourseName(courseDto.getCourseName().toLowerCase()).get();
                totalFees+=course.getCourseFee();
                paidFees+=courseDto.getCourseFee();
                course.addEnrollment(newEnrollment);
                student.addEnrollment(newEnrollment);
            }
            pendingFee = totalFees-paidFees;
            student.setPendingFee(pendingFee);
        
        return student;
    }

    public static StudentDto  mapToStudentDto(Student student, StudentDto studentDto){
        studentDto.setEnrollmentDate(student.getEnrollmentDate());
        studentDto.setName(student.getName());
        List<CourseDto> courseDtos = new ArrayList<>();
        for(Enrollment enrollment:student.getEnrollments()){
            courseDtos.add(new CourseDto(enrollment.getCourse().getCourseName(), enrollment.getCourse().getCourseFee()));
        }
        studentDto.setCourses(courseDtos);
        studentDto.setPendingFee(student.getPendingFee());
        
        return studentDto;
    }

}
