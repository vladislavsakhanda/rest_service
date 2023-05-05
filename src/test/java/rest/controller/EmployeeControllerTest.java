package rest.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import rest.model.Employee;
import rest.repository.EmployeeRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeRepository employeeRepository;

    @Test
    public void testGetAllEmployees() throws Exception {
        Employee employee1 = new Employee();
        employee1.setId(1L);
        employee1.setName("John Doe");
        employee1.setLevel("Junior");
        employee1.setType("Full-time");

        Employee employee2 = new Employee();
        employee2.setId(2L);
        employee2.setName("Jane Smith");
        employee2.setLevel("Senior");
        employee2.setType("Part-time");

        List<Employee> employees = Arrays.asList(employee1, employee2);

        when(employeeRepository.findAll()).thenReturn(employees);

        mockMvc.perform(get("/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[1].name").value("Jane Smith"));

        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    public void testGetEmployeeById() throws Exception {
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setName("John Doe");
        employee.setLevel("Junior");
        employee.setType("Full-time");

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        mockMvc.perform(get("/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.level").value("Junior"))
                .andExpect(jsonPath("$.type").value("Full-time"));

        verify(employeeRepository, times(1)).findById(1L);
    }

    @Test
    public void testCreateEmployee() throws Exception {
        Employee employee = new Employee();
        employee.setName("John Doe");
        employee.setLevel("Junior");
        employee.setType("Full-time");

        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"John Doe\",\"level\":\"Junior\",\"type\":\"Full-time\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.level").value("Junior"))
                .andExpect(jsonPath("$.type").value("Full-time"));

        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    public void testUpdateEmployee() throws Exception {
        // Given
        Employee existingEmployee = new Employee();
        existingEmployee.setId(1L);
        existingEmployee.setName("John Doe");
        existingEmployee.setLevel("Junior");
        existingEmployee.setType("Full-time");

        Employee updatedEmployee = new Employee();
        updatedEmployee.setName("Updated Name");
        updatedEmployee.setLevel("Senior");
        updatedEmployee.setType("Part-time");

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(existingEmployee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(updatedEmployee);

        // When/Then
        mockMvc.perform(put("/employees/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Updated Name\",\"level\":\"Senior\",\"type\":\"Part-time\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Name"))
                .andExpect(jsonPath("$.level").value("Senior"))
                .andExpect(jsonPath("$.type").value("Part-time"));

        verify(employeeRepository, times(1)).findById(1L);
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    public void testDeleteEmployee() throws Exception {
        // Given
        Long employeeId = 1L;

        // When/Then
        mockMvc.perform(delete("/employees/{id}", employeeId))
                .andExpect(status().isOk());

        verify(employeeRepository, times(1)).deleteById(employeeId);
    }

    @Test
    public void testGetEmployeeById_WhenEmployeeNotFound() throws Exception {
        // Given
        Long nonExistingEmployeeId = 100L;
        when(employeeRepository.findById(nonExistingEmployeeId)).thenReturn(Optional.empty());

        // When/Then
        mockMvc.perform(get("/employees/{id}", nonExistingEmployeeId))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testHandleException() throws Exception {
        // Given
        when(employeeRepository.findAll()).thenThrow(new RuntimeException("Internal Server Error"));

        // When/Then
        mockMvc.perform(get("/employees"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("An error occurred"))
                .andExpect(jsonPath("$.status").value(500));
    }

}