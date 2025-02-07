package com.codeWithRaman.implementation.model;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.Positive;

@Entity
public class Crate extends Beverage {

    @Positive
    private int noOfBottles; // Number of bottles in the crate, must be > 0

    @Positive
    private int cratesInStock; // Number of crates in stock, must be >= 0

    // Getters and Setters

    public int getNoOfBottles() {
        return noOfBottles;
    }

    public void setNoOfBottles(int noOfBottles) {
        this.noOfBottles = noOfBottles;
    }

    public int getCratesInStock() {
        return cratesInStock;
    }

    public void setCratesInStock(int cratesInStock) {
        this.cratesInStock = cratesInStock;
    }
}