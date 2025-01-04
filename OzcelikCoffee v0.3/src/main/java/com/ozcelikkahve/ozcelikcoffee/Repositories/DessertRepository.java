package com.ozcelikkahve.ozcelikcoffee.Repositories;

import com.ozcelikkahve.ozcelikcoffee.Models.Coffees;
import com.ozcelikkahve.ozcelikcoffee.Models.Desserts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository

public interface DessertRepository extends JpaRepository<Desserts , Long> {
    public List<Desserts> findAllByOrderByNameAsc();
}
