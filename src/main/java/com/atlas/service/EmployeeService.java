
package com.atlas.service;

import com.atlas.dto.EmployeeCreateDTO;
import com.atlas.entity.Department;
import com.atlas.entity.Employee;
import com.atlas.exception.ConflictException;
import com.atlas.exception.ResourceNotFoundException;
import com.atlas.repository.DepartmentRepository;
import com.atlas.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    public EmployeeService(
            EmployeeRepository employeeRepository,
            DepartmentRepository departmentRepository) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
    }

    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Employee not found with id: " + id));
    }

    @Transactional
    public Employee createEmployee(EmployeeCreateDTO dto) {

        if (employeeRepository.existsByEmail(dto.getEmail())) {
            throw new ConflictException(
                    "Email already exists: " + dto.getEmail());
        }

        if (employeeRepository.existsByMatricule(dto.getMatricule())) {
            throw new ConflictException(
                    "Matricule already exists: " + dto.getMatricule());
        }

        Department department = departmentRepository
                .findById(dto.getDepartmentId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Department not found with id: "
                                        + dto.getDepartmentId()));

        Employee employee = new Employee();
        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setEmail(dto.getEmail());
        employee.setMatricule(dto.getMatricule());
        employee.setDepartment(department);

        return employeeRepository.save(employee);
    }
}
