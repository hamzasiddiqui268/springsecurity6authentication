package com.codeWithRaman.implementation.repository;

import com.codeWithRaman.implementation.model.Bottle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BottleRepository extends JpaRepository<Bottle, Long> {
    // Custom queries can be added here
}
