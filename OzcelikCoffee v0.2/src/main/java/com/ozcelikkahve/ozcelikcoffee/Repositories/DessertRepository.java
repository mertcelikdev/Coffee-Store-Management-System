package com.ozcelikkahve.ozcelikcoffee.Repositories;

import com.ozcelikkahve.ozcelikcoffee.Models.Coffees;
import com.ozcelikkahve.ozcelikcoffee.Models.Desserts;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DessertRepository extends JpaRepository<Desserts , Long> {
    public List<Desserts> findAllByOrderByNameAsc();
}
