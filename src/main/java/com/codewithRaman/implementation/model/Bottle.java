package com.codeWithRaman.implementation.model;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;

@Entity
public class Bottle extends Beverage {

    @Positive(message = "Volume must be greater than 0")
    private double volume; // Volume in liters, must be > 0

    private boolean isAlcoholic; // Whether the bottle is alcoholic
    private double volumePercent; // Alcohol percentage if it's an alcoholic drink

    @Positive(message = "In stock quantity must be >= 0")
    private int inStock; // Number of bottles in stock, must be >= 0

    @NotEmpty(message = "Supplier name cannot be empty")
    private String supplier; // Supplier name, cannot be null or empty

    // Getters and Setters
    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public boolean isAlcoholic() {
        return isAlcoholic;
    }

    public void setAlcoholic(boolean alcoholic) {
        isAlcoholic = alcoholic;
    }

    public double getVolumePercent() {
        return volumePercent;
    }

    public void setVolumePercent(double volumePercent) {
        this.volumePercent = volumePercent;
    }

    public int getInStock() {
        return inStock;
    }

    public void setInStock(int inStock) {
        this.inStock = inStock;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }
}
