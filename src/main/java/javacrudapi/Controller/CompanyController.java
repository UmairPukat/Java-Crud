package javacrudapi.Controller;

import javacrudapi.Exception.BadRequestException;
import javacrudapi.Model.Company;
import javacrudapi.Services.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/companies")
public class CompanyController {
    @Autowired
    private CompanyService companyService;

    @GetMapping
    public List<Company> getAllCompany() {
        return companyService.getAllCompany();
    }

    @GetMapping("/{id}")
    public Company getCompanyById(@PathVariable Long id) {
        return companyService.getCompanyById(id);
    }

    @PostMapping
    public ResponseEntity<String> createCompany(@RequestBody Company company) {
        boolean result =  companyService.createCompany(company);
        if (result != false) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Company added successfully");
        } else {
            throw new BadRequestException("Failed to add company");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateCompany(@PathVariable Long id, @RequestBody Company company) {
        boolean result =  companyService.updateCompany(id, company);
        if (result != false) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Company updated successfully");
        } else {
            throw new BadRequestException("Failed to add company");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCompany(@PathVariable Long id) {
        boolean result = companyService.deleteCompany(id);
        if (result != false) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Company Deleted successfully");
        } else {
            throw new BadRequestException("Failed to delete company");
        }
    }

}