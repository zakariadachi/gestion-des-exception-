
package com.atlas.service;

import com.atlas.entity.Department;
import com.atlas.exception.ConflictException;
import com.atlas.exception.ResourceNotFoundException;
import com.atlas.repository.DepartmentRepository;
import com.atlas.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;

    public DepartmentService(
            DepartmentRepository departmentRepository,
            EmployeeRepository employeeRepository) {
        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
    }

    public Department createDepartment(String name) {
        if (departmentRepository.existsByNameIgnoreCase(name)) {
            throw new ConflictException(
                    "Department with name '" + name + "' already exists");
        }
        Department department = new Department(name);
        return departmentRepository.save(department);
    }

    public void deleteDepartment(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Department not found with id: " + id));

        boolean hasEmployees = employeeRepository
                .existsByDepartmentId(id);

        if (hasEmployees) {
            throw new ConflictException(
                    "Cannot delete department because it has employees");
        }

        departmentRepository.delete(department);
    }
}
