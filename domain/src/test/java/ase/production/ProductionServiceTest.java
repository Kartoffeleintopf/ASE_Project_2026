package ase.production;

import ase.ingredient.Ingredient;
import ase.recipe.Recipe;
import ase.warehouse.WarehouseEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ProductionServiceTest {

    private ProductionService productionService;

    private Ingredient baseIngredient;
    private Ingredient product;

    private Recipe recipe;
    private Map<Ingredient, WarehouseEntry> entries;

    private int times;

    @BeforeEach
    void setUp() {
        productionService = new ProductionService();

        baseIngredient = new Ingredient("tomato", "tomato.png", true);
        product = new Ingredient("tomatosoup", "tomatosoup.png", false);

        recipe = new Recipe("TomatosoupRecipe", product);
        recipe.addIngredient(baseIngredient, 2);

        entries = new HashMap<>();

        // enough ingredients for production
        WarehouseEntry entryForBase = new WarehouseEntry(baseIngredient);
        WarehouseEntry entryForProduct = new WarehouseEntry(product);

        entryForBase.addAmount(5);
        entries.put(baseIngredient, entryForBase);

        times = 2;
    }

    @Test
    void isRecipeProducibleTrue() {
        boolean result = productionService.isRecipeProducible(recipe, entries, times);
        assertTrue(result);
    }

    @Test
    void isRecipeProducibleFalse() {
        entries.get(baseIngredient).subtractAmount(4); // now only 1 left

        boolean result = productionService.isRecipeProducible(recipe, entries, times);

        assertFalse(result);
    }
}