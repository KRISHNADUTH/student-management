package com.ghss.studentmanagement.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;

    private String name;

    private LocalDate enrollmentDate;

    private double totalFeePaid;

    private double pendingFee;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Enrollment> enrollments = new ArrayList<>();

    public void addEnrollment(Enrollment enrollment){
        enrollment.setStudent(this);
        enrollment.setEnrollmentDate(this.getEnrollmentDate());
        enrollments.add(enrollment);
    }

    @Override
    public String toString() {
        return "Student [id=" + id + ", userId=" + userId + ", name=" + name + ", enrollmentDate=" + enrollmentDate
                + ", totalFeePaid=" + totalFeePaid + ", pendingFee=" + pendingFee + "]";
    }
    
}