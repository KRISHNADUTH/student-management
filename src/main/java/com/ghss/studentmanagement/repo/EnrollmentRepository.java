package com.ghss.studentmanagement.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ghss.studentmanagement.model.Enrollment;
@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
}
