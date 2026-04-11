package ase.production;

import ase.recipe.Recipe;
import ase.warehouse.WarehouseEntry;

import java.util.Map;

public class ProductionService {

    public void produceRecipe(Recipe recipe, Map<ase.ingredient.Ingredient, WarehouseEntry> entries, int times) {
        // First check all ingredients have sufficient stock
        recipe.getIngredientAmounts().forEach((ingredient, amount) -> {
            WarehouseEntry entry = entries.get(ingredient);
            if (entry.getAmount() < amount * times) {
                throw new IllegalArgumentException("Insufficient stock of ingredient: " + ingredient.getName());
            }
        });

        // Then subtract and add atomically
        recipe.getIngredientAmounts().forEach((ingredient, amount) -> {
            entries.get(ingredient).subtractAmount(amount * times);
        });

        entries.get(recipe.getProduce()).addAmount(times);
    }
}