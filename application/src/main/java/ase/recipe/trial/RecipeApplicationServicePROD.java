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
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class RecipeApplicationServicePROD {
    private final RecipeRepository recipeRepository;
    private final WarehouseEntryRepository warehouseEntryRepository;
    private final ProductionService productionService;
    private final RecipeValidationService recipeValidationService;

    @Autowired
    public RecipeApplicationServicePROD(RecipeRepository recipeRepository,
                                    WarehouseEntryRepository warehouseEntryRepository,
                                    ProductionService productionService,
                                        RecipeValidationService recipeValidationService) {
        this.recipeRepository = recipeRepository;
        this.warehouseEntryRepository = warehouseEntryRepository;
        this.productionService = productionService;
        this.recipeValidationService = recipeValidationService;
    }

    public boolean isRecipeProducible(long id, int times) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessages.RECIPE_NOT_FOUND.getMessage()));
        return productionService.isRecipeProducible(recipe, recipeValidationService.buildEntriesMap(recipe), times);
    }

    @Transactional
    public void produceRecipeMultiple(long id, int times) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessages.RECIPE_NOT_FOUND.getMessage()));
        Map<Ingredient, WarehouseEntry> entries = recipeValidationService.buildEntriesMap(recipe);
        productionService.produceRecipe(recipe, entries, times);
        entries.values().forEach(warehouseEntryRepository::save);
    }

    public Map<Ingredient, Integer> getDirectIngredients(long recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessages.RECIPE_NOT_FOUND.getMessage()));
        return recipe.getIngredientAmounts();
    }

    public Map<Ingredient, Integer> getBaseIngredients(long recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessages.RECIPE_NOT_FOUND.getMessage()));
        Map<Ingredient, Integer> baseIngredients = new HashMap<>();
        collectBaseIngredients(recipe, baseIngredients, 1);
        return baseIngredients;
    }

    private void collectBaseIngredients(Recipe recipe, Map<Ingredient, Integer> baseIngredients, int multiplier) {
        for (Map.Entry<Ingredient, Integer> entry : recipe.getIngredientAmounts().entrySet()) {
            Ingredient ingredient = entry.getKey();
            int amount = entry.getValue() * multiplier;
            Optional<Recipe> subRecipe = recipeRepository.findRecipeByProduce(ingredient);
            if (subRecipe.isEmpty() || ingredient.isBase()) {
                baseIngredients.merge(ingredient, amount, Integer::sum);
            } else {
                collectBaseIngredients(subRecipe.get(), baseIngredients, amount);
            }
        }
    }
     /*
    @Transactional
    public void produceRecipe(long id) {
        produceRecipeMultiple(id, 1);
    }
    */
}
