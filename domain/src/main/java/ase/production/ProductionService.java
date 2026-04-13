package ase.production;

import ase.ingredient.Ingredient;
import ase.recipe.Recipe;
import ase.warehouse.WarehouseEntry;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ProductionService {

    public boolean isRecipeProducible(Recipe recipe, Map<Ingredient, WarehouseEntry> entries, int times) {
        for (Map.Entry<Ingredient, Integer> entry : recipe.getIngredientAmounts().entrySet()) {
            if (entries.get(entry.getKey()).getAmount() < entry.getValue() * times) {
                return false;
            }
        }
        return true;
    }

    public void produceRecipe(Recipe recipe, Map<Ingredient, WarehouseEntry> entries, int times) {
        if (!isRecipeProducible(recipe, entries, times)) {
            throw new IllegalArgumentException("Recipe is not producible");
        }
        recipe.getIngredientAmounts().forEach((ingredient, amount) ->
                entries.get(ingredient).subtractAmount(amount * times));

        entries.get(recipe.getProduce()).addAmount(times);
    }

    /*
    public List<Ingredient> getDirectIngredients(Recipe recipe) {
        return new ArrayList<>(recipe.getIngredientAmounts().keySet());
    }*/
}