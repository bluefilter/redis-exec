package io.redispro.redisexec.controller;

import io.redispro.redisexec.dto.Employee;
import io.redispro.redisexec.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/postgres/employee", produces = {MediaType.APPLICATION_JSON_VALUE})
public class PostgresController {

    private final EmployeeService employeeService;

    // 모든 직원 조회
    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    // ID로 특정 직원 조회
    @GetMapping("/{id}")
    public Employee getEmployeeById(@PathVariable Long id) {
        return employeeService.getEmployeeById(id);
    }
}