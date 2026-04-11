package ase.recipe;

import ase.ingredient.Ingredient;

public class RecipeBuilder {
    private String name;
    private Ingredient produce;
    private final Recipe recipe;

    public RecipeBuilder(String name, Ingredient produce) {
        this.name = name;
        this.produce = produce;
        this.recipe = new Recipe(name, produce);
    }

    public RecipeBuilder addIngredient(Ingredient ingredient, int amount) {
        recipe.addIngredient(ingredient, amount);
        return this;
    }

    public Recipe build() {
        return recipe;
    }
}