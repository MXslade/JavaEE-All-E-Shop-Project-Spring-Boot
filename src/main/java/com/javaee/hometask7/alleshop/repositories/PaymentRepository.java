package com.javaee.hometask7.alleshop.repositories;

import com.javaee.hometask7.alleshop.models.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
