package javacrudapi.Controller;

import javacrudapi.Exception.BadRequestException;
import javacrudapi.Model.Employee;
import javacrudapi.Services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @GetMapping("/{id}")
    public Employee getEmployeeById(@PathVariable Long id) {
        return employeeService.getEmployeeById(id);
    }

    @PostMapping
    public ResponseEntity<String> createEmployee(@RequestBody Employee employee) {
        boolean result = employeeService.createEmployee(employee);
        if (result != false) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Employee added successfully");
        } else {
            throw new BadRequestException("Failed to add employee");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateEmployee(@PathVariable Long id, @RequestBody Employee employee) {
        boolean result = employeeService.updateEmployee(id, employee);
        if (result != false) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Employee Updated successfully");
        } else {
            throw new BadRequestException("Failed to update employee");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable Long id) {
        boolean result = employeeService.deleteEmployee(id);
        if (result != false) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Employee Deleted successfully");
        } else {
            throw new BadRequestException("Failed to delete employee");
        }
    }

}