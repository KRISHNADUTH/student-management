package com.ghss.studentmanagement.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ghss.studentmanagement.model.FeePayment;
@Repository
public interface FeePaymentRepository extends JpaRepository<FeePayment, Long> {
}