package ase;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ErrorMessagesTest {

    @Test
    void ingredientNotFound() {
        assertEquals("Ingredient not found", ErrorMessages.INGREDIENT_NOT_FOUND.getMessage());
    }

    @Test
    void recipeNotFound() {
        assertEquals("Recipe not found", ErrorMessages.RECIPE_NOT_FOUND.getMessage());
    }

    @Test
    void warehouseEntryNotFound() {
        assertEquals("Warehouse entry not found", ErrorMessages.WAREHOUSE_ENTRY_NOT_FOUND.getMessage());
    }

    @Test
    void recipeNotProducible() {
        assertEquals("Recipe is not producible", ErrorMessages.RECIPE_NOT_PRODUCIBLE.getMessage());
    }

    @Test
    void ingredientIsBase() {
        assertEquals("Cannot create recipe for a base ingredient", ErrorMessages.INGREDIENT_IS_BASE.getMessage());
    }

    @Test
    void ingredientAlreadyHasRecipe() {
        assertEquals("Ingredient already has a recipe", ErrorMessages.INGREDIENT_ALREADY_HAS_RECIPE.getMessage());
    }

    @Test
    void cannotAssignBaseAsProduce() {
        assertEquals("Cannot assign a base ingredient as produce", ErrorMessages.CANNOT_ASSIGN_BASE_AS_PRODUCE.getMessage());
    }

    @Test
    void requiredSelf() {
        assertEquals("Recipe requires Product for Creation", ErrorMessages.REQUIRED_SELF.getMessage());
    }

    @Test
    void storedAmountNegative() {
        assertEquals("Stored amount cannot be negative", ErrorMessages.STORED_AMOUNT_NEGATIVE.getMessage());
    }

    @Test
    void cannotDeleteIngredientContainedInRecipe() {
        assertEquals("Cannot delete ingredient contained in a recipe", ErrorMessages.CANNOT_DELETE_INGREDIENT_CONTAINED_IN_RECIPE.getMessage());
    }

    @Test
    void cannotDeleteIngredientProducedByRecipe() {
        assertEquals("Cannot delete ingredient produced by a recipe", ErrorMessages.CANNOT_DELETE_INGREDIENT_PRODUCED_BY_RECIPE.getMessage());
    }

    @Test
    void insufficientStock() {
        assertEquals("Insufficient stock to produce recipe", ErrorMessages.INSUFFICIENT_STOCK.getMessage());
    }
}