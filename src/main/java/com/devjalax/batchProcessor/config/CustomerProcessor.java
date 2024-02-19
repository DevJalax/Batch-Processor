package com.devjalax.batchProcessor.config;

import org.springframework.batch.item.ItemProcessor;

import com.devjalax.batchProcessor.entity.Customer;

public class CustomerProcessor implements ItemProcessor<Customer,Customer>{

	@Override
	public Customer process(Customer customer) throws Exception {
		
		return customer;
	}

}
