package ase.recipe;

import ase.ingredient.Ingredient;
import jakarta.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private long id;

    @Column
    private String name;

    @OneToOne
    @JoinColumn(name = "ingredient_id")
    private Ingredient produce;

    @ElementCollection
    @CollectionTable(name = "recipe_ingredient", joinColumns = @JoinColumn(name = "recipe_id"))
    @MapKeyJoinColumn(name = "ingredient_id")
    @Column(name = "amount")
    private Map<Ingredient, Integer> ingredientAmounts = new HashMap<>();

    protected Recipe() {}

    public Recipe(String name, Ingredient produce) {
        this.name = name;
        this.produce = produce;
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


    public Ingredient getProduce() {
        return produce;
    }

    public void setProduce(Ingredient produce) {
        this.produce = produce;
    }

    public void addIngredient(Ingredient ingredient, int amount) {
        ingredientAmounts.put(ingredient, amount);
    }

    public void removeIngredient(Ingredient ingredient) {
        ingredientAmounts.remove(ingredient);
    }

    public boolean containsIngredient(Ingredient ingredient) {
        return ingredientAmounts.containsKey(ingredient);
    }
}