package com.devjalax.batchProcessor.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devjalax.batchProcessor.entity.Customer;

public interface CustRepo extends JpaRepository<Customer,Integer> {

}
