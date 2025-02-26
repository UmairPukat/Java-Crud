package javacrudapi.Services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.persistence.EntityManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import jakarta.persistence.ParameterMode;
import jakarta.persistence.Query;
import jakarta.persistence.StoredProcedureQuery;
import javacrudapi.Exception.KeyNotFoundException;

import javacrudapi.Model.Company;
import javacrudapi.Repository.CompanyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {CompanyService.class})
@ExtendWith(SpringExtension.class)
class CompanyServiceTest {
    @MockBean
    private CompanyRepository companyRepository;

    @Autowired
    private CompanyService companyService;

    @MockBean
    private EntityManager entityManager;

    /**
     * Method under test: {@link CompanyService#getAllCompany()}
     */
    @Test
    void testGetAllCompany() {
        // Arrange
        List<Company> mockCompanies = new ArrayList<>();
        // Add mock companies to the list
        mockCompanies.add(new Company(1L, "Company1", "Address1"));
        mockCompanies.add(new Company(2L, "Company2", "Address2"));

        Query mockQuery = Mockito.mock(Query.class);
        when(entityManager.createNativeQuery(anyString())).thenReturn(mockQuery);
        when(mockQuery.getResultList()).thenReturn(mockCompanies);

        // Act
        List<Company> result = companyService.getAllCompany();

        // Assert
        assertEquals(mockCompanies.size(), result.size());
        for (int i = 0; i < mockCompanies.size(); i++) {
            Company expectedCompany = mockCompanies.get(i);
            Company actualCompany = result.get(i);
            assertEquals(expectedCompany.getId(), actualCompany.getId());
            assertEquals(expectedCompany.getName(), actualCompany.getName());
            assertEquals(expectedCompany.getAddress(), actualCompany.getAddress());
        }
    }

    /**
     * Method under test: {@link CompanyService#getCompanyById(Long)}
     */
    @Test
    void testGetCompanyById() {
        Company company = new Company();
        company.setAddress("42 Main St");
        company.setId(1L);
        company.setName("Name");
        Optional<Company> ofResult = Optional.of(company);
        when(companyRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        assertSame(company, companyService.getCompanyById(1L));
        verify(companyRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link CompanyService#getCompanyById(Long)}
     */
    @Test
    void testGetCompanyById2() {
        Optional<Company> emptyResult = Optional.empty();
        when(companyRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);
        assertThrows(KeyNotFoundException.class, () -> companyService.getCompanyById(1L));
        verify(companyRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link CompanyService#getCompanyById(Long)}
     */
    @Test
    void testGetCompanyById3() {
        when(companyRepository.findById(Mockito.<Long>any())).thenThrow(new KeyNotFoundException("An error occurred"));
        assertThrows(KeyNotFoundException.class, () -> companyService.getCompanyById(1L));
        verify(companyRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link CompanyService#createCompany(Company)}
     */
    @Test
    void testCreateCompany() {
        // Arrange
        Company company = new Company();
        company.setAddress("42 Main St");
        company.setId(1L);
        company.setName("Name");

        StoredProcedureQuery mockQuery = Mockito.mock(StoredProcedureQuery.class);
        when(entityManager.createStoredProcedureQuery("insert_company")).thenReturn(mockQuery);
        when(mockQuery.execute()).thenReturn(true);
        // Act
        boolean result = companyService.createCompany(company);
        // Assert
        assertTrue(result);
        verify(entityManager).createStoredProcedureQuery("insert_company");
        verify(mockQuery).registerStoredProcedureParameter("name", String.class, ParameterMode.IN);
        verify(mockQuery).registerStoredProcedureParameter("address", String.class, ParameterMode.IN);
        verify(mockQuery).setParameter("name", company.getName());
        verify(mockQuery).setParameter("address", company.getAddress());
        verify(mockQuery).execute();
    }
    /**
     * Method under test: {@link CompanyService#updateCompany(Long, Company)}
     */
    @Test
    void testUpdateCompany() {
        Company company = new Company();
        company.setAddress("42 Main St");
        company.setId(1L);
        company.setName("Name");
        Optional<Company> ofResult = Optional.of(company);

        Company company2 = new Company();
        company2.setAddress("42 Main St");
        company2.setId(1L);
        company2.setName("Name");
        when(companyRepository.save(Mockito.<Company>any())).thenReturn(company2);
        when(companyRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        Company company3 = new Company();
        company3.setAddress("42 Main St");
        company3.setId(1L);
        company3.setName("Name");
        assertTrue(companyService.updateCompany(1L, company3));
        verify(companyRepository).save(Mockito.<Company>any());
        verify(companyRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link CompanyService#updateCompany(Long, Company)}
     */
    @Test
    void testUpdateCompany2() {
        Company company = new Company();
        company.setAddress("42 Main St");
        company.setId(1L);
        company.setName("Name");
        Optional<Company> ofResult = Optional.of(company);
        when(companyRepository.save(Mockito.<Company>any())).thenThrow(new KeyNotFoundException("An error occurred"));
        when(companyRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        Company company2 = new Company();
        company2.setAddress("42 Main St");
        company2.setId(1L);
        company2.setName("Name");
        assertThrows(KeyNotFoundException.class, () -> companyService.updateCompany(1L, company2));
        verify(companyRepository).save(Mockito.<Company>any());
        verify(companyRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link CompanyService#updateCompany(Long, Company)}
     */
    @Test
    void testUpdateCompany4() {
        Optional<Company> emptyResult = Optional.empty();
        when(companyRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);

        Company company = new Company();
        company.setAddress("42 Main St");
        company.setId(1L);
        company.setName("Name");
        assertThrows(KeyNotFoundException.class, () -> companyService.updateCompany(1L, company));
        verify(companyRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link CompanyService#deleteCompany(Long)}
     */
    @Test
    void testDeleteCompany() {
        Company company = new Company();
        company.setAddress("42 Main St");
        company.setId(1L);
        company.setName("Name");
        Optional<Company> ofResult = Optional.of(company);
        doNothing().when(companyRepository).deleteById(Mockito.<Long>any());
        when(companyRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        assertTrue(companyService.deleteCompany(1L));
        verify(companyRepository).findById(Mockito.<Long>any());
        verify(companyRepository).deleteById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link CompanyService#deleteCompany(Long)}
     */
    @Test
    void testDeleteCompany2() {
        Company company = new Company();
        company.setAddress("42 Main St");
        company.setId(1L);
        company.setName("Name");
        Optional<Company> ofResult = Optional.of(company);
        doThrow(new KeyNotFoundException("An error occurred")).when(companyRepository).deleteById(Mockito.<Long>any());
        when(companyRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        assertThrows(KeyNotFoundException.class, () -> companyService.deleteCompany(1L));
        verify(companyRepository).findById(Mockito.<Long>any());
        verify(companyRepository).deleteById(Mockito.<Long>any());
    }
    /**
     * Method under test: {@link CompanyService#deleteCompany(Long)}
     */
    @Test
    void testDeleteCompany4() {
        Optional<Company> emptyResult = Optional.empty();
        when(companyRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);
        assertThrows(KeyNotFoundException.class, () -> companyService.deleteCompany(1L));
        verify(companyRepository).findById(Mockito.<Long>any());
    }
}

