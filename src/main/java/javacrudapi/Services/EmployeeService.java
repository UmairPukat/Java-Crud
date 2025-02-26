package javacrudapi.Services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.Query;
import jakarta.persistence.StoredProcedureQuery;
import javacrudapi.Exception.KeyNotFoundException;
import javacrudapi.Interfaces.IEmployeeService;
import javacrudapi.Model.Employee;
import javacrudapi.Repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService implements IEmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EntityManager entityManager;

    @Override
    public List<Employee> getAllEmployees() {
        String sql = "SELECT * FROM all_employees_view;";
        Query query = entityManager.createNativeQuery(sql);
        List<Employee> result = query.getResultList();
        return result;
    }

    @Override
    public Employee getEmployeeById(Long id) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        if (optionalEmployee.isPresent()) {
            return optionalEmployee.get();
        }
        throw new KeyNotFoundException("Employee not found with id: " + id);
    }

    @Override
    public boolean createEmployee(Employee employee) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("add_employee");
        query.registerStoredProcedureParameter("first_name", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("last_name", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("email", String.class, ParameterMode.IN);
        query.setParameter("first_name", employee.getFirstName());
        query.setParameter("last_name", employee.getLastName());
        query.setParameter("email", employee.getEmail());
        query.execute();
        return  true;
    }

    @Override
    public boolean updateEmployee(Long id, Employee employee) {
        Employee existingEmployee = getEmployeeById(id);
        if (existingEmployee != null) {
            existingEmployee.setFirstName(employee.getFirstName());
            existingEmployee.setLastName(employee.getLastName());
            existingEmployee.setEmail(employee.getEmail());
            employeeRepository.save(existingEmployee);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteEmployee(Long id) {
        Employee existingEmployee = getEmployeeById(id);
        if (existingEmployee != null) {
            employeeRepository.deleteById(id);
            return true;
        }
        return false;
    }
}