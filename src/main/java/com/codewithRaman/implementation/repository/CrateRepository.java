package com.codeWithRaman.implementation.repository;

import com.codeWithRaman.implementation.model.Crate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CrateRepository extends JpaRepository<Crate, Long> {
    // Custom queries can be added here
}
