package com.devjalax.batchProcessor.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="Customers")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer {

	@Id
	@Column(name = "CUST_ID")
	private int id;
	
	@Column(name = "FIRST_NAME")
	private String firstName;
	
	@Column(name = "LAST_NAME")
	private String lastName;
	
	@Column(name = "EMAIL")
	private String email;
	
	@Column(name = "EMAIL2")
	private String secondEmail;
	
	@Column(name = "PROFESSION")
	private String profession;
	
}
