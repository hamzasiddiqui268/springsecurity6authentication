package com.codeWithRaman.implementation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.codeWithRaman.implementation.model.Beverage;

@Repository
public interface BeverageRepository extends JpaRepository<Beverage, Long> {
}
