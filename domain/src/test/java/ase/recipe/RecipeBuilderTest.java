package ase.recipe;

import ase.ingredient.Ingredient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RecipeBuilderTest {

    private Ingredient produce;
    private Ingredient ingredientA;
    private RecipeBuilder builder;

    @BeforeEach
    void setUp() {
        produce = new Ingredient("Sauce", "link", false);
        ingredientA = new Ingredient("Tomato", "link", true);
        builder = new RecipeBuilder("Tomato Sauce", produce);
    }

    @Test
    void buildRecipe() {
        Recipe recipe = builder.addIngredient(ingredientA, 5).build();
        assertEquals("Tomato Sauce", recipe.getName());
        assertEquals(produce, recipe.getProduce());
        assertTrue(recipe.containsIngredient(ingredientA));
    }

    @Test
    void buildRecipeNoIngredients() {
        Recipe recipe = builder.build();
        assertEquals("Tomato Sauce", recipe.getName());
        assertTrue(recipe.getIngredientAmounts().isEmpty());
    }

    @Test
    void buildRecipeCorrectAmount() {
        Recipe recipe = builder.addIngredient(ingredientA, 5).build();
        assertEquals(5, recipe.getIngredientAmount(ingredientA));
    }

    @Test
    void buildRecipeCorrectProduce() {
        Recipe recipe = builder.build();
        assertEquals(produce, recipe.getProduce());
    }
}