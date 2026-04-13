package ase.recipe.trial;

import ase.ErrorMessages;
import ase.ingredient.Ingredient;
import ase.production.ProductionService;
import ase.recipe.Recipe;
import ase.recipe.RecipeRepository;
import ase.warehouse.WarehouseEntry;
import ase.warehouse.WarehouseEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class RecipeValidationService {
    private final RecipeRepository recipeRepository;
    private final ProductionService productionService;
    private final WarehouseEntryRepository warehouseEntryRepository;

    @Autowired
    public RecipeValidationService(RecipeRepository recipeRepository,
                                   ProductionService productionService,
                                   WarehouseEntryRepository warehouseEntryRepository) {
        this.recipeRepository = recipeRepository;
        this.productionService = productionService;
        this.warehouseEntryRepository = warehouseEntryRepository;
    }

    public boolean ingredientRequiresSelf(Ingredient produce, Ingredient ingredient) {
        if (ingredient.equals(produce)) {
            return true;
        }
        Optional<Recipe> subRecipe = recipeRepository.findRecipeByProduce(ingredient);
        if (subRecipe.isEmpty()) {
            return false;
        }
        for (Ingredient subIngredient : subRecipe.get().getIngredientAmounts().keySet()) {
            if (ingredientRequiresSelf(produce, subIngredient)) {
                return true;
            }
        }
        return false;
    }


    public Map<Ingredient, WarehouseEntry> buildEntriesMap(Recipe recipe) {
        Map<Ingredient, WarehouseEntry> entries = new HashMap<>();
        recipe.getIngredientAmounts().keySet().forEach(ingredient -> {
            WarehouseEntry entry = warehouseEntryRepository.findByIngredientId(ingredient.getId())
                    .orElseThrow(() -> new IllegalArgumentException(ErrorMessages.WAREHOUSE_ENTRY_NOT_FOUND.getMessage()));
            entries.put(ingredient, entry);
        });
        entries.put(recipe.getProduce(), warehouseEntryRepository.findByIngredientId(recipe.getProduce().getId())
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessages.WAREHOUSE_ENTRY_NOT_FOUND.getMessage())));
        return entries;
    }
}