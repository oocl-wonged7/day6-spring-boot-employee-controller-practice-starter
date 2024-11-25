package com.oocl.springbootemployee.repository;

import com.oocl.springbootemployee.model.Company;
import com.oocl.springbootemployee.model.Employee;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CompanyRepository {
    List<Company> companies = new ArrayList<>();
    public CompanyRepository() {
        companies.add(new Company(1, "OOCL"));
        companies.add(new Company(2, "OOCL2"));
        companies.add(new Company(3, "OOCL3"));
    }

    public List<Company> getAll() {
        return companies;
    }
}
