package com.codeWithRaman.implementation.service;

import com.codeWithRaman.implementation.model.Crate;
import com.codeWithRaman.implementation.repository.CrateRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CrateService {

    private final CrateRepository crateRepository;

    public CrateService(CrateRepository crateRepository) {
        this.crateRepository = crateRepository;
    }

    // Save a new crate
    public Crate saveCrate(Crate crate) {
        return crateRepository.save(crate);
    }

    // Get all crates
    public List<Crate> getAllCrates() {
        return crateRepository.findAll();
    }

    // Get a crate by its ID
    public Crate getCrateById(Long id) {
        return crateRepository.findById(id).orElse(null);
    }
}
