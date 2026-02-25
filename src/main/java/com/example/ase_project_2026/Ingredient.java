package com.example.ase_project_2026;

import jakarta.persistence.*;

@Entity
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;// should be saved as foreign key in lager for example or reicpe
    private String name;

    private String picture; // This will need to be changed to another type
    // research how to save pictures in Java
    // alternatively could represent url --> pictureLink
    private int amount = 0;
    private Boolean base;
    //private Recipe recipe; //this only applies in non base Ingredients...

    protected Ingredient() {}

    public Ingredient(String name, String picture, boolean base) {
        this.name = name;
        this.picture = picture;
        this.base = base;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Boolean isBase() {
        return base;
    }
}
