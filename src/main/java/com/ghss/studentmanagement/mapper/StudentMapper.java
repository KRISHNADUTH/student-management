package com.ghss.studentmanagement.mapper;

import com.ghss.studentmanagement.dto.StudentDto;
import com.ghss.studentmanagement.model.Student;
public class StudentMapper {

    // @Autowired
    // CourseRepository courseRepository;

    public static Student mapToSrudent(StudentDto studentDto, Student student) {
        // student.setEnrollmentDate(studentDto.getEnrollmentDate());
        student.setName(studentDto.getName());
        // student.setPendingFee(studentDto.getPendingFee());
        return student;
    }

    public static StudentDto mapToStudentDto(Student student, StudentDto studentDto){
        studentDto.setEnrollmentDate(student.getEnrollmentDate());
        studentDto.setName(student.getName());
        studentDto.setPendingFee(student.getPendingFee());
        return studentDto;
    }

}
