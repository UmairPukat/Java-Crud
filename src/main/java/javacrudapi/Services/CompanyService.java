package javacrudapi.Services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.Query;
import jakarta.persistence.StoredProcedureQuery;
import javacrudapi.Exception.KeyNotFoundException;
import javacrudapi.Interfaces.ICompanyService;
import javacrudapi.Model.Company;
import javacrudapi.Repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyService implements ICompanyService {
    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private EntityManager entityManager;

    @Override
    public List<Company> getAllCompany() {
        String sql = "SELECT * FROM all_companies_view";
        Query query = entityManager.createNativeQuery(sql);
        List<Company> result = query.getResultList();
        return result;
    }

    @Override
    public Company getCompanyById(Long id) {
        Optional<Company> optionalCompany = companyRepository.findById(id);
        if (optionalCompany.isPresent()) {
            return optionalCompany.get();
        }
        throw new KeyNotFoundException("Company not found with id: " + id);
    }

    @Override
    public boolean createCompany(Company company) {
        StoredProcedureQuery storedProcedure = entityManager.createStoredProcedureQuery("insert_company");
        storedProcedure.registerStoredProcedureParameter("name", String.class, ParameterMode.IN);
        storedProcedure.registerStoredProcedureParameter("address", String.class, ParameterMode.IN);
        storedProcedure.setParameter("name", company.getName());
        storedProcedure.setParameter("address", company.getAddress());
        storedProcedure.execute();
        return  true;
    }

    @Override
    public boolean updateCompany(Long id, Company company) {
        Company existingCompany = getCompanyById(id);
        if (existingCompany != null) {
            existingCompany.setName(company.getName());
            existingCompany.setAddress(company.getAddress());
            companyRepository.save(existingCompany);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteCompany(Long id) {
        Company existingEmployee = getCompanyById(id);
        if (existingEmployee != null) {
            companyRepository.deleteById(id);
            return true;
        }
        return false;
    }
}

