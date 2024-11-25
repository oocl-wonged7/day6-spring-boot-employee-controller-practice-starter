package com.oocl.springbootemployee.controller;

import com.oocl.springbootemployee.model.Employee;
import com.oocl.springbootemployee.model.Gender;
import com.oocl.springbootemployee.repository.EmployeeRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeRepository employeeRepository;

    public EmployeeController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @GetMapping
    public List<Employee> getAll() {
        return employeeRepository.getAll();
    }

    @GetMapping(path = "/{id}")
    public Employee getEmployeeById(@PathVariable Integer id) {
        return employeeRepository.getEmployeeById(id);
    }

    @GetMapping(params = "gender")
    public List<Employee> getByGender(@RequestParam Gender gender) {
        return employeeRepository.getByGender(gender);
    }

    @PostMapping
    @ResponseStatus(code = org.springframework.http.HttpStatus.CREATED)
    public Employee addEmployee(@RequestBody Employee employee) {
        return employeeRepository.addEmployee(employee);
    }

    @PutMapping(path = "/{id}")
    public Employee updateEmployee(@PathVariable Integer id, @RequestBody Employee employee) {
        return employeeRepository.updateEmployee(id, employee.getAge(), employee.getSalary());
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(code = org.springframework.http.HttpStatus.NO_CONTENT)
    public Integer deleteEmployee(@PathVariable Integer id) {
        return employeeRepository.deleteEmployee(id);
    }
}
