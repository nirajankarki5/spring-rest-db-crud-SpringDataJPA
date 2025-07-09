package com.example.restdb.dao;

import com.example.restdb.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    // that's it.. no coded needed
}
