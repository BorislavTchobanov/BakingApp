package com.example.android.bakingapp.model;


import java.io.Serializable;

public class Ingredient implements Serializable {

    private int quantity;
    private String measure;
    private String name;


    public Ingredient(int quantity, String measure, String name) {
        this.quantity = quantity;
        this.measure = measure;
        this.name = name;

    }


    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

}
