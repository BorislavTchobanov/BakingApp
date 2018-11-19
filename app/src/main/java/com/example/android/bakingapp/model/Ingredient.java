package com.example.android.bakingapp.model;


import java.io.Serializable;
import java.util.List;

public class Ingredient implements Serializable {

    private int quantity;
    private String name;
    private String measure;


    public Ingredient(int quantity, String name, String measure) {
        this.quantity = quantity;
        this.name = name;
        this.measure = measure;

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

    public String getIngredients() {
        return measure;
    }

    public void setIngredients(String measure) {
        this.measure = measure;
    }

}
