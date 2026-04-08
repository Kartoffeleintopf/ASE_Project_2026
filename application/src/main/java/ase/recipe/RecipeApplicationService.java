package ase.recipe;

import ase.ingredient.Ingredient;
import ase.ingredient.IngredientRepository;
import ase.warehouse.WarehouseEntry;
import ase.warehouse.WarehouseEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class RecipeApplicationService {
    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;
    private final WarehouseEntryRepository warehouseEntryRepository;

    @Autowired
    public RecipeApplicationService(RecipeRepository recipeRepository,
                                    IngredientRepository ingredientRepository,
                                    WarehouseEntryRepository warehouseEntryRepository) {
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
        this.warehouseEntryRepository = warehouseEntryRepository;
    }

    public Recipe createRecipe(String name, long produceId, Map<Long, Integer> ingredientAmounts) {
        Ingredient produce = ingredientRepository.findById(produceId)
                .orElseThrow(() -> new IllegalArgumentException("Ingredient not found"));
        if (produce.isBase()) {
            throw new IllegalStateException("Cannot create recipe for a base ingredient");
        }
        if (produce.getRecipe() != null) {
            throw new IllegalStateException("Ingredient already has a recipe");
        }
        Recipe recipe = new Recipe(name, produce);
        ingredientAmounts.forEach((ingredientId, amount) -> {
            Ingredient ingredient = ingredientRepository.findById(ingredientId)
                    .orElseThrow(() -> new IllegalArgumentException("Ingredient not found"));
            recipe.addIngredient(ingredient, amount);
        });
        Recipe saved = recipeRepository.save(recipe);
        produce.setRecipe(saved);
        ingredientRepository.save(produce);
        return saved;
    }

    public Recipe findRecipeById(long id) {
        return recipeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Recipe not found"));
    }

    public Optional<Recipe> findByProduce(Ingredient ingredient) {
        return recipeRepository.findByProduce(ingredient);
    }

    public List<Recipe> findAllRecipes() {
        return recipeRepository.findAll();
    }

    public List<Recipe> findByNameContaining(String name) {
        return recipeRepository.findByNameContaining(name);
    }

    public Recipe updateRecipe(long id, String name, Map<Long, Integer> ingredientAmounts) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Recipe not found"));
        recipe.setName(name);
        recipe.getIngredientAmounts().clear();
        ingredientAmounts.forEach((ingredientId, amount) -> {
            Ingredient ingredient = ingredientRepository.findById(ingredientId)
                    .orElseThrow(() -> new IllegalArgumentException("Ingredient not found"));
            recipe.addIngredient(ingredient, amount);
        });
        return recipeRepository.save(recipe);
    }

    public void deleteRecipe(long id) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Recipe not found"));
        Ingredient produce = recipe.getProduce();
        produce.setRecipe(null);
        ingredientRepository.save(produce);
        recipeRepository.delete(recipe);
    }

    public void produceRecipe(long id) {
        produceRecipeMultiple(id, 1);
    }

    public void produceRecipeMultiple(long id, int times) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Recipe not found"));
        recipe.getIngredientAmounts().forEach((ingredient, amount) -> {
            WarehouseEntry entry = warehouseEntryRepository.findByIngredient(ingredient)
                    .orElseThrow(() -> new IllegalArgumentException("Warehouse entry not found"));
            entry.subtractAmount(amount * times);
            warehouseEntryRepository.save(entry);
        });
        WarehouseEntry produceEntry = warehouseEntryRepository.findByIngredient(recipe.getProduce())
                .orElseThrow(() -> new IllegalArgumentException("Warehouse entry not found"));
        produceEntry.addAmount(times);
        warehouseEntryRepository.save(produceEntry);
    }
}