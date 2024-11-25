package com.oocl.springbootemployee.controller;

import com.oocl.springbootemployee.model.Employee;
import com.oocl.springbootemployee.model.Gender;
import com.oocl.springbootemployee.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class EmployeeControllerTest {

    @Autowired
    private MockMvc client;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private JacksonTester<List<Employee>> employeesJacksonTester;

    @Autowired
    private JacksonTester<Employee> employeeJacksonTester;

    @BeforeEach
    void setUp() {
        employeeRepository.getAll().clear();
        employeeRepository.addEmployee(new Employee(1, "Employee1", 10, Gender.MALE, 5000.0));
        employeeRepository.addEmployee(new Employee(2, "Employee2", 20, Gender.FEMALE, 15000.0));
        employeeRepository.addEmployee(new Employee(3, "Employee3", 30, Gender.MALE, 35000.0));
    }

    @Test
    void should_return_employees_when_get_all_given_employees() throws Exception {
        //Given
        List<Employee> expectedEmployees = employeeRepository.getAll();

        //When
        String employeesResponseString = client.perform(MockMvcRequestBuilders.get("/employees"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();

        //Then
        List<Employee> employees = employeesJacksonTester.parseObject(employeesResponseString);
        assertThat(employeesJacksonTester.parse(employeesResponseString)).usingRecursiveComparison().isEqualTo(expectedEmployees);
    }

    @Test
    void should_return_employee_when_get_by_id_given_id() throws Exception {
        //Given
        Employee expectedEmployee = employeeRepository.getEmployeeById(2);
        //When
        String employeeResponseString = client.perform(MockMvcRequestBuilders.get("/employees/" + expectedEmployee.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
        //Then
        Employee employee = employeeJacksonTester.parseObject(employeeResponseString);
        assertThat(employeeJacksonTester.parse(employeeResponseString)).usingRecursiveComparison().isEqualTo(expectedEmployee);
    }

    @Test
    void should_return_male_when_get_male_given_employees() throws Exception {
        //Given
        List<Employee> expectedEmployees = employeeRepository.getByGender(Gender.MALE);

        //When
        String employeesResponseString = client.perform(MockMvcRequestBuilders.get("/employees")
                        .param("gender", Gender.MALE.name()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();

        //Then
        assertThat(employeesJacksonTester.parse(employeesResponseString)).usingRecursiveComparison().isEqualTo(expectedEmployees);
    }

    @Test
    void should_create_employee_when_create_given_employee() throws Exception {
        //Given
        String employee = """
                {
                    "name": "E4",
                    "age": 12,
                    "gender": "MALE",
                    "salary": 10000.0
                }
                """;

        // When
        final Employee expected_employee = new Employee(4, "E4", 12, Gender.MALE, 10000.0);
        String employeeJson = client.perform((MockMvcRequestBuilders.post("/employees"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(employee))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn().getResponse().getContentAsString();

        // Then
        assertThat(employeeJacksonTester.parse(employeeJson)).usingRecursiveComparison().isEqualTo(expected_employee);
    }

    @Test
    void should_update_employee_when_put_given_employee_id() throws Exception {
        //Given
        String employee = """
                {
                    "age": 12,
                    "salary": 10000.0
                }
                """;

        // When
        final Employee expectedEmployee = new Employee(1, "E1", 12, Gender.MALE, 10000.0);
        String employeeJson = client.perform((MockMvcRequestBuilders.put("/employees/1"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(employee))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();

        // Then
        assertThat(employeeJacksonTester.parse(employeeJson)).usingRecursiveComparison().isEqualTo(expectedEmployee);
    }
}
