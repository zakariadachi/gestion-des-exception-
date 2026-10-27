
package com.atlas.repository;

import com.atlas.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeRepository
        extends JpaRepository<Employee, Long> {

    boolean existsByEmail(String email);

    boolean existsByMatricule(String matricule);

    Optional<Employee> findByEmail(String email);

    boolean existsByDepartmentId(Long departmentId);
}
