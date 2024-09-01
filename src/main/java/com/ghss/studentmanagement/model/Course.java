package com.ghss.studentmanagement.model;


import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String courseName;
    private double courseFee;
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Enrollment> enrollments = new ArrayList<>();
    
    public void addEnrollment(Enrollment enrollment){
        enrollment.setCourse(this);
        enrollment.setEnrollmentDate(LocalDate.now());
        enrollments.add(enrollment);
    }
}
