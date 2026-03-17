package com.example.ase_project_2026;

import jakarta.persistence.*;

@Entity
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private long id;// should be saved as foreign key in lager for example or recipe

    @Column
    private String name;

    @Column
    private String picture; // represents pictureLink

    @Column
    private Boolean base;

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

    public Boolean isBase() {
        return base;
    }
}
