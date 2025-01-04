package com.ozcelikkahve.ozcelikcoffee.Repositories;

import com.ozcelikkahve.ozcelikcoffee.Models.Employees;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository

public interface EmployeeRepository extends JpaRepository<Employees , Long > {


    public List<Employees> findAllByOrderByFirstNameAsc();
}
