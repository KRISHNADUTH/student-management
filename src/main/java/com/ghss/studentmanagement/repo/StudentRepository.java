package com.ghss.studentmanagement.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ghss.studentmanagement.model.Student;
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
}
