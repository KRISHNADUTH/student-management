package com.ghss.studentmanagement.model;


import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
// @ToString
@NoArgsConstructor
@AllArgsConstructor
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String courseName;
    private double courseFee;
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Enrollment> enrollments = new ArrayList<>();
    
    public void addEnrollment(Enrollment enrollment){
        enrollment.setCourse(this);
        // enrollment.setEnrollmentDate(LocalDate.now());
        enrollments.add(enrollment);
    }

    @Override
    public String toString() {
        return "Course [id=" + id + ", courseName=" + courseName + ", courseFee=" + courseFee + "]";
    }
    
}
