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

import javacrudapi.Model.Employee;
import javacrudapi.Repository.EmployeeRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {EmployeeService.class})
@ExtendWith(SpringExtension.class)
class EmployeeServiceTest {
    @MockBean
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeService employeeService;

    @MockBean
    private EntityManager entityManager;

    /**
     * Method under test: {@link EmployeeService#getAllEmployees()}
     */
    @Test
    void testGetAllEmployees() {
        // Arrange
        List<Employee> mockEmployees = new ArrayList<>();
        // Add mock employees to the list
        mockEmployees.add(new Employee(1L, "John", "Doe", "john@example.com"));
        mockEmployees.add(new Employee(2L, "Jane", "Smith", "jane@example.com"));

        Query mockQuery = Mockito.mock(Query.class);
        when(entityManager.createNativeQuery(anyString())).thenReturn(mockQuery);
        when(mockQuery.getResultList()).thenReturn(mockEmployees);

        // Act
        List<Employee> result = employeeService.getAllEmployees();

        // Assert
        assertEquals(mockEmployees.size(), result.size());
        for (int i = 0; i < mockEmployees.size(); i++) {
            Employee expectedEmployee = mockEmployees.get(i);
            Employee actualEmployee = result.get(i);
            assertEquals(expectedEmployee.getId(), actualEmployee.getId());
            assertEquals(expectedEmployee.getFirstName(), actualEmployee.getFirstName());
            assertEquals(expectedEmployee.getLastName(), actualEmployee.getLastName());
            assertEquals(expectedEmployee.getEmail(), actualEmployee.getEmail());
        }
    }

    /**
     * Method under test: {@link EmployeeService#getEmployeeById(Long)}
     */
    @Test
    void testGetEmployeeById() {
        Employee employee = new Employee();
        employee.setEmail("jane.doe@example.org");
        employee.setFirstName("Jane");
        employee.setId(1L);
        employee.setLastName("Doe");
        Optional<Employee> ofResult = Optional.of(employee);
        when(employeeRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        assertSame(employee, employeeService.getEmployeeById(1L));
        verify(employeeRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link EmployeeService#getEmployeeById(Long)}
     */
    @Test
    void testGetEmployeeById2() {
        Optional<Employee> emptyResult = Optional.empty();
        when(employeeRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);
        assertThrows(KeyNotFoundException.class, () -> employeeService.getEmployeeById(1L));
        verify(employeeRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link EmployeeService#getEmployeeById(Long)}
     */
    @Test
    void testGetEmployeeById3() {
        when(employeeRepository.findById(Mockito.<Long>any())).thenThrow(new KeyNotFoundException("An error occurred"));
        assertThrows(KeyNotFoundException.class, () -> employeeService.getEmployeeById(1L));
        verify(employeeRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link EmployeeService#createEmployee(Employee)}
     */
    @Test
    void testCreateEmployee() {
        // Arrange
        Employee employee = new Employee();
        employee.setEmail("jane.doe@example.org");
        employee.setFirstName("Jane");
        employee.setId(1L);
        employee.setLastName("Doe");

        StoredProcedureQuery mockQuery = Mockito.mock(StoredProcedureQuery.class);
        when(entityManager.createStoredProcedureQuery("add_employee")).thenReturn(mockQuery);
        when(mockQuery.execute()).thenReturn(true);

        // Act
        boolean result = employeeService.createEmployee(employee);

        // Assert
        assertTrue(result);
        verify(mockQuery).registerStoredProcedureParameter("first_name", String.class, ParameterMode.IN);
        verify(mockQuery).registerStoredProcedureParameter("last_name", String.class, ParameterMode.IN);
        verify(mockQuery).registerStoredProcedureParameter("email", String.class, ParameterMode.IN);
        verify(mockQuery).setParameter("first_name", employee.getFirstName());
        verify(mockQuery).setParameter("last_name", employee.getLastName());
        verify(mockQuery).setParameter("email", employee.getEmail());
        verify(mockQuery).execute();
    }

    /**
     * Method under test: {@link EmployeeService#updateEmployee(Long, Employee)}
     */
    @Test
    void testUpdateEmployee() {
        Employee employee = new Employee();
        employee.setEmail("jane.doe@example.org");
        employee.setFirstName("Jane");
        employee.setId(1L);
        employee.setLastName("Doe");
        Optional<Employee> ofResult = Optional.of(employee);

        Employee employee2 = new Employee();
        employee2.setEmail("jane.doe@example.org");
        employee2.setFirstName("Jane");
        employee2.setId(1L);
        employee2.setLastName("Doe");
        when(employeeRepository.save(Mockito.<Employee>any())).thenReturn(employee2);
        when(employeeRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        Employee employee3 = new Employee();
        employee3.setEmail("jane.doe@example.org");
        employee3.setFirstName("Jane");
        employee3.setId(1L);
        employee3.setLastName("Doe");
        assertTrue(employeeService.updateEmployee(1L, employee3));
        verify(employeeRepository).save(Mockito.<Employee>any());
        verify(employeeRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link EmployeeService#updateEmployee(Long, Employee)}
     */
    @Test
    void testUpdateEmployee2() {
        Employee employee = new Employee();
        employee.setEmail("jane.doe@example.org");
        employee.setFirstName("Jane");
        employee.setId(1L);
        employee.setLastName("Doe");
        Optional<Employee> ofResult = Optional.of(employee);
        when(employeeRepository.save(Mockito.<Employee>any())).thenThrow(new KeyNotFoundException("An error occurred"));
        when(employeeRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        Employee employee2 = new Employee();
        employee2.setEmail("jane.doe@example.org");
        employee2.setFirstName("Jane");
        employee2.setId(1L);
        employee2.setLastName("Doe");
        assertThrows(KeyNotFoundException.class, () -> employeeService.updateEmployee(1L, employee2));
        verify(employeeRepository).save(Mockito.<Employee>any());
        verify(employeeRepository).findById(Mockito.<Long>any());
    }
    /**
     * Method under test: {@link EmployeeService#updateEmployee(Long, Employee)}
     */
    @Test
    void testUpdateEmployee4() {
        Optional<Employee> emptyResult = Optional.empty();
        when(employeeRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);

        Employee employee = new Employee();
        employee.setEmail("jane.doe@example.org");
        employee.setFirstName("Jane");
        employee.setId(1L);
        employee.setLastName("Doe");
        assertThrows(KeyNotFoundException.class, () -> employeeService.updateEmployee(1L, employee));
        verify(employeeRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link EmployeeService#deleteEmployee(Long)}
     */
    @Test
    void testDeleteEmployee() {
        Employee employee = new Employee();
        employee.setEmail("jane.doe@example.org");
        employee.setFirstName("Jane");
        employee.setId(1L);
        employee.setLastName("Doe");
        Optional<Employee> ofResult = Optional.of(employee);
        doNothing().when(employeeRepository).deleteById(Mockito.<Long>any());
        when(employeeRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        assertTrue(employeeService.deleteEmployee(1L));
        verify(employeeRepository).findById(Mockito.<Long>any());
        verify(employeeRepository).deleteById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link EmployeeService#deleteEmployee(Long)}
     */
    @Test
    void testDeleteEmployee2() {
        Employee employee = new Employee();
        employee.setEmail("jane.doe@example.org");
        employee.setFirstName("Jane");
        employee.setId(1L);
        employee.setLastName("Doe");
        Optional<Employee> ofResult = Optional.of(employee);
        doThrow(new KeyNotFoundException("An error occurred")).when(employeeRepository).deleteById(Mockito.<Long>any());
        when(employeeRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        assertThrows(KeyNotFoundException.class, () -> employeeService.deleteEmployee(1L));
        verify(employeeRepository).findById(Mockito.<Long>any());
        verify(employeeRepository).deleteById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link EmployeeService#deleteEmployee(Long)}
     */
    @Test
    void testDeleteEmployee4() {
        Optional<Employee> emptyResult = Optional.empty();
        when(employeeRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);
        assertThrows(KeyNotFoundException.class, () -> employeeService.deleteEmployee(1L));
        verify(employeeRepository).findById(Mockito.<Long>any());
    }
}

