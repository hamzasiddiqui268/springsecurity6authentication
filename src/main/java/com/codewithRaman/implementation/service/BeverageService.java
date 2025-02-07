package com.codeWithRaman.implementation.service;

import com.codeWithRaman.implementation.model.Beverage;
import com.codeWithRaman.implementation.repository.BeverageRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BeverageService {

    private final BeverageRepository beverageRepository;

    public BeverageService(BeverageRepository beverageRepository) {
        this.beverageRepository = beverageRepository;
    }

    // Fetch all beverages
    public List<Beverage> getAllBeverages() {
        return beverageRepository.findAll();
    }

    // Fetch a beverage by its ID
    public Beverage getBeverageById(Long id) {
        return beverageRepository.findById(id).orElse(null);
    }

    // Save a new beverage
    public Beverage saveBeverage(Beverage beverage) {
        return beverageRepository.save(beverage);
    }
}
