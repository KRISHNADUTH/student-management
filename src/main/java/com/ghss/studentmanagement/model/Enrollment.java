package com.ghss.studentmanagement.model;

import java.time.LocalDate;

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
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    public Enrollment(LocalDate enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    private LocalDate enrollmentDate;

    // @Override
    // public String toString() {
    //     return "Enrollment [id=" + id + ", student=" + student + ", enrollmentDate=" + enrollmentDate + "]";
    // }

}