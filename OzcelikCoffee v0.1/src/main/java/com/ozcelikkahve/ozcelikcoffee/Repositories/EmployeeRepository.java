package com.ozcelikkahve.ozcelikcoffee.Repositories;

import com.ozcelikkahve.ozcelikcoffee.Models.Employees;
import org.springframework.data.jpa.repository.JpaRepository;
// buraları kontrol et
public interface EmployeeRepository extends JpaRepository<Employees , Long > {
}
