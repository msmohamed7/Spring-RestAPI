package com.example.demo.service;

import java.util.Comparator;
import java.util.List; 
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.demo.CompanyDetails;
import com.example.demo.CompanyRepository;
import com.example.exception.InvalidCompanyDetailsException;
import com.example.exception.ResourceNotFoundException;


@Service
public class CompanyService {
	
	@Autowired
	private CompanyRepository companyRepo; 
	 
	
	public CompanyDetails getById(Long companyId) throws ResourceNotFoundException{
		Optional<CompanyDetails> company = companyRepo.findById(companyId);
	    if (company.isPresent()) {
	    	return company.get();
	    } else{
	    	throw new ResourceNotFoundException("Company not found with id: " + companyId);
	    }
	} 
   
	public List<CompanyDetails> getCompany() {
		return companyRepo.findAll();
	}  
   
	/*
    public List<CompanyDetails> sorting() {
        List<CompanyDetails> sorted = companyRepo.findAll();
        sorted(Sort.by(Sort.Direction.DESC, "rent"));
        return sorted.subList(0, Math.min(2, sorted.size()));
    }*/
	
	public List<CompanyDetails> sorting() {
	    List<CompanyDetails> sorted = companyRepo.findAll();
	    sorted.sort(Comparator.comparingInt(CompanyDetails::getRent).reversed());
	    return sorted.subList(0, Math.min(2, sorted.size()));
	} 

	public CompanyDetails saveCompany(CompanyDetails companyDetails) throws InvalidCompanyDetailsException {
		if (companyDetails.getName() == null || companyDetails.getName().isEmpty()) {
            throw new InvalidCompanyDetailsException("Name is required.");
        }
		if (companyDetails.getPhnNo() == null || companyDetails.getPhnNo().isEmpty()) {
            throw new InvalidCompanyDetailsException("Phone numberis required.");
        }
		if (companyDetails.getRent() == null) {
            throw new InvalidCompanyDetailsException("rent is required.");
        }
		return companyRepo.save(companyDetails);
	} 
	   
	public CompanyDetails updateCompany(CompanyDetails companyDetails ,long companyId) throws ResourceNotFoundException {
		Optional<CompanyDetails> existingCompanyOptional = companyRepo.findById(companyId);
	    if (existingCompanyOptional.isPresent()) {
	        CompanyDetails existingCompany = existingCompanyOptional.get();

	        if (companyDetails.getName() != null) {
	            existingCompany.setName(companyDetails.getName());
	        }
	        if (companyDetails.getPhnNo() != null) {
	            existingCompany.setPhnNo(companyDetails.getPhnNo());
	        }
	        if (companyDetails.getRent() != null) {
	            existingCompany.setRent(companyDetails.getRent());
	        }
	        CompanyDetails updatedCompany = companyRepo.save(existingCompany);
	        return updatedCompany;
	    } else {
	        throw new ResourceNotFoundException("Company not found with id: " + companyId);
	    }
	} 
	  
	public String deleteCompany(Long companyId ) throws ResourceNotFoundException {
		Optional<CompanyDetails> company = companyRepo.findById(companyId);
		if (company.isPresent()) {
	    	companyRepo.deleteById(companyId);
	    	return "Deleted";
	    } else{
	    	throw new ResourceNotFoundException("Company not found with id: " + companyId);
	    }
	}
	
}