package com.example.demo;
 

import jakarta.persistence.Column;  
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;



@Entity 
@Table(name = "companyone")
public class CompanyDetails {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@Column(name = "company_name")
	private String name;
	@Column(name = "phn_No")
	private String phnNo;
	@Column(name = "rent")
	private Integer rent;
	
	public CompanyDetails() {}
	
	public CompanyDetails(String name,String phnNo,int rent) {
		this.name=name;
		this.phnNo=phnNo;
		this.rent=rent;
	}


	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhnNo() {
		return phnNo;
	}

	public void setPhnNo(String phnNo) {
		this.phnNo = phnNo;
	}

	public Integer getRent() {
		return rent;
	}

	public void setRent(Integer rent) {
		this.rent = rent;
	}


}