package com.codeWithRaman.implementation.service;

import com.codeWithRaman.implementation.model.Bottle;
import com.codeWithRaman.implementation.repository.BottleRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BottleService {

    private final BottleRepository bottleRepository;

    public BottleService(BottleRepository bottleRepository) {
        this.bottleRepository = bottleRepository;
    }

    // Save a new bottle
    public Bottle saveBottle(Bottle bottle) {
        return bottleRepository.save(bottle);
    }

    // Get all bottles
    public List<Bottle> getAllBottles() {
        return bottleRepository.findAll();
    }

    // Get a bottle by its ID
    public Bottle getBottleById(Long id) {
        return bottleRepository.findById(id).orElse(null);
    }
}
