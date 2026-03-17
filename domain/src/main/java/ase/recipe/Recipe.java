package ase.recipe;

import jakarta.persistence.*;

@Entity
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private long id;

    @Column
    private String name;

    //Ingredient save ith amounts
    //Map<int, Ingredient> ingredientAmounts = new Map<int, Ingredient>();

    protected Recipe() {}

    public Recipe(String name /* put ingredients and associated amounts in */) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}