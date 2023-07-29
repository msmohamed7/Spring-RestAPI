package com.example.demo.rest;
   
import java.util.List;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.CompanyDetails;
import com.example.demo.CompanyRepository;
import com.example.demo.service.CompanyService;
import com.example.exception.InvalidCompanyDetailsException;
import com.example.exception.ResourceNotFoundException;




@RestController 
public class CompanyController  {
	
	@Autowired
	CompanyRepository companyRepo;
	
	@Autowired
	CompanyService companyService;
	
	@GetMapping("/company/{id}")
	public CompanyDetails getCompanyId(@PathVariable("id") long companyId) throws ResourceNotFoundException {
		CompanyDetails companyDetails = companyService.getById(companyId);
		return companyDetails;
	}
	
	@GetMapping("/company") 
	public List<CompanyDetails> getCompany(){
		return companyService.getCompany();
	} 
	 
	@GetMapping("/company/sort")
	public List<CompanyDetails> sortCompany(){
		System.out.println(companyService.sorting().get(0).getName());
		return companyService.sorting();
	}
	 
	@PutMapping("/company/{id}")	
    public CompanyDetails updateCompany(@PathVariable("id") Long companyId, @RequestBody CompanyDetails companyDetails) throws ResourceNotFoundException{
		companyDetails.setId(companyId);
		return companyService.updateCompany(companyDetails, companyId);
    }
	
	@PostMapping("/company/save")
	public CompanyDetails saveCompany(@RequestBody CompanyDetails companyDetails) throws InvalidCompanyDetailsException {
		return companyService.saveCompany(companyDetails);
	} 
	 	
	@DeleteMapping("/company/{id}")
	public String deleteCompany(@PathVariable("id") Long companyId) throws ResourceNotFoundException {
	    return companyService.deleteCompany(companyId);
	     
	}
	

	@ExceptionHandler(ResourceNotFoundException.class)
    public String handleResourceNotFoundException(ResourceNotFoundException ex) {
        return ex.getMessage();
    }
	
	@ExceptionHandler(InvalidCompanyDetailsException.class)
    public String InvalidCompanyDetailsException(InvalidCompanyDetailsException ex) {
        return ex.getMessage();
    }

	
}