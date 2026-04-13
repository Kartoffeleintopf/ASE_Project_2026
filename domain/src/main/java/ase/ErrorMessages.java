package ase;

public enum ErrorMessages {
    INGREDIENT_NOT_FOUND("Ingredient not found"),
    RECIPE_NOT_FOUND("Recipe not found"),
    WAREHOUSE_ENTRY_NOT_FOUND("Warehouse entry not found"),
    RECIPE_NOT_PRODUCIBLE("Recipe is not producible"),
    INGREDIENT_IS_BASE("Cannot create recipe for a base ingredient"),
    INGREDIENT_ALREADY_HAS_RECIPE("Ingredient already has a recipe"),
    Required_Self("Recipe requires Product for Creation"),
    STORED_AMOUNT_NEGATIVE("Stored amount cannot be negative"),
    INSUFFICIENT_STOCK("Insufficient stock to produce recipe");

    private final String message;

    ErrorMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}