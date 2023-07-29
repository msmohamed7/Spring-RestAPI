package com.example.demo;

import static org.junit.Assert.fail; 
import static org.junit.jupiter.api.Assertions.assertEquals; 
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import com.example.demo.rest.CompanyController;
import com.example.demo.service.CompanyService;
import com.example.exception.InvalidCompanyDetailsException;
import com.example.exception.ResourceNotFoundException;


@ExtendWith(MockitoExtension.class)
public class ServiceTest {

    @Mock
    private CompanyRepository companyRepository;

    @InjectMocks
    private CompanyService companyService;
    
    @InjectMocks
    private CompanyController companyController;


    @Test
    public void testGetById() throws ResourceNotFoundException {
        long id = 1L;
        Optional<CompanyDetails> mockUser = Optional.of(new CompanyDetails("Facebook", "9074837562", 500000));
        when(companyRepository.findById(id)).thenReturn(mockUser);
 
        CompanyDetails response = null;
        response = companyService.getById(id);
 
        assertNotNull(response);
        assertEquals("Facebook", response.getName());
        assertEquals("9074837562", response.getPhnNo());
        assertEquals(500000, response.getRent());
    } 
      
    @Test
    public void testGetById_CompanyNotFound() {
    	
        Long companyId = 2L;

        when(companyRepository.findById(companyId)).thenReturn(Optional.empty());
        
        assertThrows(ResourceNotFoundException.class, () -> companyService.getById(companyId));

    } 
    
    @Test
    public void testGetCompany() {
    	List<CompanyDetails> companies = new ArrayList<>();
    	companies.add(new CompanyDetails("PayPal", "7853126495", 600000));
    	companies.add(new CompanyDetails("BMW", "7853126495", 600000));
    	companies.add(new CompanyDetails("Benz", "7853126495", 600000));
        companies.add(new CompanyDetails("Audi", "7853126495", 600000));
        
        when(companyRepository.findAll()).thenReturn(companies);
        
        List<CompanyDetails> response1 = null;
        response1 = companyService.getCompany();
         
        assertNotNull(response1);
        assertEquals(4, response1.size()); 
    } 
     
    @Test
    public void testSaveCompany() throws InvalidCompanyDetailsException {
    	CompanyDetails validCompany = new CompanyDetails("PayPal", "7853126495", 600000);

        when(companyRepository.save(validCompany)).thenReturn(validCompany);
        CompanyDetails savedCompany = null;
		savedCompany = companyService.saveCompany(validCompany); 
        assertEquals(validCompany, savedCompany);
 
        CompanyDetails invalidCompany1 = new CompanyDetails("", "7853126495", 600000);
        assertThrows(InvalidCompanyDetailsException.class, () -> companyService.saveCompany(invalidCompany1));

  
        CompanyDetails invalidCompany2 = new CompanyDetails("PayPal", "", 600000);
        assertThrows(InvalidCompanyDetailsException.class, () -> companyService.saveCompany(invalidCompany2));

          
        CompanyDetails invalidCompany3 = new CompanyDetails();
        invalidCompany3.setName("Sample Company");
        invalidCompany3.setPhnNo("1234567890");
        invalidCompany3.setRent(null);
        assertThrows(InvalidCompanyDetailsException.class, () -> companyService.saveCompany(invalidCompany3));
    } 
    
    /*
    @Test
    public void testSorting() {	
    	List<CompanyDetails> company_sort = new ArrayList<>();
    	company_sort.add(new CompanyDetails("PayPal", "785312649", 520000));
    	company_sort.add(new CompanyDetails("BMW", "7853126495", 480000));
    	company_sort.add(new CompanyDetails("Benz", "7853126495", 600000));
    	company_sort.add(new CompanyDetails("Audi", "7853126495", 658000));
    	
    	List<CompanyDetails> sort = new ArrayList<>();
    	sort.add(new CompanyDetails("Audi", "785312649", 658000));
    	sort.add(new CompanyDetails("Benz", "7853126495", 600000));
    	sort.add(new CompanyDetails("PayPal", "7853126495", 520000));
    	sort.add(new CompanyDetails("BMW", "7853126495", 480000));
    	
    	when(companyRepository.findAll(Sort.by(Sort.Direction.DESC, "rent"))).thenReturn(sort);
    	List<CompanyDetails> sorting = companyService.sorting();
    	
    	verify(companyRepository, times(1)).findAll(Sort.by(Sort.Direction.DESC, "rent"));
    	 
    	assertEquals(2, sorting.size());
    	//assertEquals(company_sort.get(2).getName(), sorting.get(1).getName());
    	assertEquals("Audi", sort.get(0).getName());    
    }*/    
    
    @Test
    public void testSorting() {	
    	List<CompanyDetails> company_sort = new ArrayList<>();
    	company_sort.add(new CompanyDetails("PayPal", "785312649", 520000));
    	company_sort.add(new CompanyDetails("BMW", "7853126495", 480000));
    	company_sort.add(new CompanyDetails("Benz", "7853126495", 600000));
    	company_sort.add(new CompanyDetails("Audi", "7853126495", 658000));
    	
    	
    	when(companyRepository.findAll()).thenReturn(company_sort);
    	List<CompanyDetails> sorting = companyService.sorting();
    	
    	verify(companyRepository, times(1)).findAll();
    	 
    	assertEquals(2, sorting.size());
    	assertEquals("Audi", sorting.get(0).getName());  
    	assertEquals("Benz", sorting.get(1).getName()); 
    }
      
    @Test 
    public void testUpdateCompany_ExistingCompany() throws ResourceNotFoundException {
        // Create a sample company with updated details
        long companyId = 1L;
        CompanyDetails existingCompany = new CompanyDetails("Existing Company", "1234567890", 1000);
        CompanyDetails updatedDetails = new CompanyDetails("Updated Company", "9876543210", 2000);

        when(companyRepository.findById(companyId)).thenReturn(Optional.of(existingCompany));

        when(companyRepository.save(existingCompany)).thenReturn(updatedDetails);

        CompanyDetails result = companyService.updateCompany(updatedDetails, companyId);
        
        assertEquals(updatedDetails, result);
        verify(companyRepository, times(1)).findById(companyId);
        verify(companyRepository, times(1)).save(existingCompany);
    }
 
    @Test
    public void testUpdateCompany_NonExistingCompany() {

        long companyId = 2L;
        CompanyDetails updatedDetails = new CompanyDetails("Updated Company", "9876543210", 2000);

        when(companyRepository.findById(companyId)).thenReturn(Optional.empty());
        
        assertThrows(ResourceNotFoundException.class, () -> companyService.updateCompany(updatedDetails, companyId));

        verify(companyRepository, times(1)).findById(companyId);

        verify(companyRepository, never()).save(any()); 
    }
     
   
    @Test 
    public void testDeleteCompany_Success() throws ResourceNotFoundException {

        Long companyId = 1L; 
        CompanyDetails existingCompany = new CompanyDetails("Paypal", "7894563210", 100000);
        existingCompany .setId(companyId);
        Optional<CompanyDetails> companyDetails = Optional.of(existingCompany);
        
        when(companyRepository.findById(companyId)).thenReturn(Optional.of(existingCompany));
        doNothing().when(companyRepository).deleteById(companyId);
        
        String result = companyService.deleteCompany(companyId);
        verify(companyRepository).deleteById(companyId);
        verify(companyRepository).findById(companyId);
        assertEquals("Deleted", result);
    }   
    
    @Test
    public void testDeleteCompanyFail() {
    	
        Long companyId = 2L;
        
        when(companyRepository.findById(companyId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> companyService.deleteCompany(companyId));

        // Verify that the findById method is called with the correct companyId
        verify(companyRepository).findById(companyId);

        // Verify that the deleteById method is not called in this case
        verifyNoMoreInteractions(companyRepository);

    } 

    
    //----------------------------------------------------------------------------------------------------------------------------
    

    
    
    
    
    
} 
