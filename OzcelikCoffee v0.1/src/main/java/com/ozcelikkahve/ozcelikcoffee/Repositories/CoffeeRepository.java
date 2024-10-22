package com.ozcelikkahve.ozcelikcoffee.Repositories;

import com.ozcelikkahve.ozcelikcoffee.Models.Coffees;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoffeeRepository extends JpaRepository<Coffees , Long > {
}
