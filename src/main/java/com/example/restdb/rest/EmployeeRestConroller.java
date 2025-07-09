package com.example.restdb.rest;

import com.example.restdb.entity.Employee;
import com.example.restdb.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class EmployeeRestConroller {

    private EmployeeService employeeService;
    private ObjectMapper objectMapper;

    @Autowired
    public EmployeeRestConroller(EmployeeService theEmployeeService, ObjectMapper theObjectMapper) {
        employeeService = theEmployeeService;
        objectMapper = theObjectMapper;
    }

    @GetMapping("/employees")
    public List<Employee> findAll() {
        return employeeService.findAll();
    }

    @GetMapping("/employees/{employeeId}")
    public Employee getEmployee(@PathVariable int employeeId) {
        Employee employee = employeeService.findById(employeeId);

        if(employee == null) {
            throw new RuntimeException("Employee id not found - " + employeeId);
        }

        return employee;
    }

    @PostMapping("/employees")
    public Employee addEmployee(@RequestBody Employee theEmployee) {

        // just in case they pass id in JSON, set id to 0
        // to force insert new item, instead of updating
        theEmployee.setId(0);

        Employee employee = employeeService.save(theEmployee);
        return employee;
    }

    @PutMapping("/employees")
    public Employee updateEmployee(@RequestBody Employee theEmployee) {
        Employee emp = employeeService.save(theEmployee);

        return emp;
    }

    @PatchMapping("/employees/{employeeId}")
    public Employee patchEmployee(@PathVariable int employeeId, @RequestBody Map<String, Object> patchPayload) {
        Employee emp = employeeService.findById(employeeId);

        if(emp == null) {
            throw new RuntimeException("Employee not found!!!");
        }

        // throw exception if payload contains id. We do not want to update id
        if(patchPayload.containsKey("id")) {
            throw new RuntimeException("id not allowed in request body!!!");
        }
        
        Employee patchedEmployee = apply(patchPayload, emp);

        Employee dbEmployee = employeeService.save(patchedEmployee);

        return dbEmployee;
    }

    private Employee apply(Map<String, Object> patchPayload, Employee emp) {

        // convert employee object to JSON object node
        ObjectNode employeeNode = objectMapper.convertValue(emp, ObjectNode.class);

        // convert patchPayload map to JSON object node
        ObjectNode patchNode = objectMapper.convertValue(patchPayload, ObjectNode.class);

        // merge the patch
        employeeNode.setAll(patchNode);

        // convert object node to Employee class
        return objectMapper.convertValue(employeeNode, Employee.class);
    }

    @DeleteMapping("/employees/{employeeId}")
    public String deleteEmployee(@PathVariable int employeeId) {
        Employee tempEmp = employeeService.findById(employeeId);

        if(tempEmp == null) {
            throw new RuntimeException("Employee not found!!!");
        }
        employeeService.delete(employeeId);
        return "Employee deleted - " + employeeId;
    }
}
