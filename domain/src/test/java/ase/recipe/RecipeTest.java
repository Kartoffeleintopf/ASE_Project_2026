package ase.recipe;

import ase.ingredient.Ingredient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class RecipeTest {

    private Ingredient produce;
    private Ingredient ingredientA;
    private Recipe recipe;
    private Map<Ingredient, Integer> ingredientAmounts;

    @BeforeEach
    void setUp() {
        produce = new Ingredient("Sauce", "link", false);
        ingredientA = new Ingredient("Tomato", "link", true);
        recipe = new Recipe("Tomato Sauce", produce);
        ingredientAmounts = new HashMap<>();
        ingredientAmounts.put(ingredientA, 10);
    }

    @Test
    void assertDefaultId() {
        assertEquals(0, recipe.getId());
    }

    @Test
    void getName() {
        assertEquals("Tomato Sauce", recipe.getName());
    }

    @Test
    void setName() {
        recipe.setName("New Name");
        assertEquals("New Name", recipe.getName());
    }

    @Test
    void getProduce() {
        assertEquals(produce, recipe.getProduce());
    }

    @Test
    void setProduce() {
        Ingredient newProduce = new Ingredient("Ketchup", "link", false);
        recipe.setProduce(newProduce);
        assertEquals(newProduce, recipe.getProduce());
    }

    /*
    @Test
    void setProduceSetsRecipeOnIngredient() {
        Ingredient newProduce = new Ingredient("Ketchup", "link", false);
        recipe.setProduce(newProduce);
        assertEquals(recipe, newProduce.getRecipe());
    }*/

    @Test
    void addIngredient() {
        recipe.addIngredient(ingredientA, 5);
        assertTrue(recipe.containsIngredient(ingredientA));
    }

    @Test
    void addIngredientStoresAmount() {
        recipe.addIngredient(ingredientA, 5);
        assertEquals(5, recipe.getIngredientAmounts().get(ingredientA));
    }

    @Test
    void setIngredientAmounts() {
        recipe.setIngredientAmounts(ingredientAmounts);
        assertEquals(ingredientAmounts, recipe.getIngredientAmounts());
    }

    @Test
    void getIngredientAmount() {
        recipe.addIngredient(ingredientA, 5);
        assertEquals(5, recipe.getIngredientAmount(ingredientA));
    }

    @Test
    void setIngredientAmount() {
        recipe.addIngredient(ingredientA, 5);
        recipe.setIngredientAmount(ingredientA, 10);
        assertEquals(10, recipe.getIngredientAmount(ingredientA));
    }

    @Test
    void removeIngredient() {
        recipe.addIngredient(ingredientA, 5);
        recipe.removeIngredient(ingredientA);
        assertFalse(recipe.containsIngredient(ingredientA));
    }

    @Test
    void containsIngredientTrue() {
        recipe.addIngredient(ingredientA, 5);
        assertTrue(recipe.containsIngredient(ingredientA));
    }

    @Test
    void containsIngredientFalse() {
        assertFalse(recipe.containsIngredient(ingredientA));
    }
}