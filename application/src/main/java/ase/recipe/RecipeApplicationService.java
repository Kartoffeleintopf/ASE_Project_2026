package ase.recipe;

import ase.ingredient.Ingredient;
import ase.ingredient.IngredientRepository;
import ase.production.ProductionService;
import ase.warehouse.WarehouseEntry;
import ase.warehouse.WarehouseEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class RecipeApplicationService {
    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;
    private final WarehouseEntryRepository warehouseEntryRepository;
    private final ProductionService productionService;

    @Autowired
    public RecipeApplicationService(RecipeRepository recipeRepository,
                                    IngredientRepository ingredientRepository,
                                    WarehouseEntryRepository warehouseEntryRepository,
                                    ProductionService productionService) {
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
        this.warehouseEntryRepository = warehouseEntryRepository;
        this.productionService = productionService;
    }

    @Transactional
    public Recipe createRecipe(String name, long produceId, Map<Long, Integer> ingredientAmounts) {
        Ingredient produce = ingredientRepository.findById(produceId)
                .orElseThrow(() -> new IllegalArgumentException("Ingredient not found"));
        if (produce.isBase()) {
            throw new IllegalStateException("Cannot create recipe for a base ingredient");
        }
        if(recipeRepository.findRecipeByProduce(produce).isPresent()){
            throw new IllegalArgumentException("Ingredient already has a recipe");
        }
        RecipeBuilder builder = new RecipeBuilder(name, produce);
        ingredientAmounts.forEach((ingredientId, amount) -> {
            Ingredient ingredient = ingredientRepository.findById(ingredientId)
                    .orElseThrow(() -> new IllegalArgumentException("Ingredient not found"));
            builder.addIngredient(ingredient, amount);
        });
        Recipe recipe = builder.build();
        Recipe saved = recipeRepository.save(recipe);
        //produce.setRecipe(saved);
        ingredientRepository.save(produce);
        return saved;
    }

    public Recipe findRecipeById(long id) {
        return recipeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Recipe not found"));
    }

    public Optional<Recipe> findByProduceID(long id) {
        return recipeRepository.findRecipeByProduce(
                ingredientRepository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Ingredient not found"))
        );
    }

    public List<Recipe> findAllRecipes() {
        return recipeRepository.findAll();
    }

    public List<Recipe> findByNameContaining(String name) {
        return recipeRepository.findByNameContaining(name);
    }

    @Transactional
    public Recipe updateRecipe(long id,
                               long produceId,
                               String name,
                               Map<Long, Integer> ingredientAmounts) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Recipe not found"));
        recipe.setName(name);

        // Checking if Produce changes, if new Produce is a base Ingredient
        // and then if this Produce already has another Recipe
        if (recipe.getProduce().getId() != produceId) {
            Ingredient newProduce = ingredientRepository.findById(produceId)
                    .orElseThrow(() -> new IllegalArgumentException("Ingredient not found"));
            if (newProduce.isBase()) {
                throw new IllegalStateException("Cannot assign a base ingredient as produce");
            }
            if (recipeRepository.findRecipeByProduce(newProduce).isPresent()) {
                throw new IllegalStateException("Ingredient already has a recipe");
            }
            recipe.setProduce(newProduce);
        }

        // Clearing and Updating Map with new Ingredients and associated amounts
        recipe.getIngredientAmounts().clear();
        ingredientAmounts.forEach((ingredientId, amount) -> {
            Ingredient ingredient = ingredientRepository.findById(ingredientId)
                    .orElseThrow(() -> new IllegalArgumentException("Ingredient not found"));
            recipe.addIngredient(ingredient, amount);
        });
        return recipeRepository.save(recipe);
    }

    @Transactional
    public void deleteRecipe(long id) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Recipe not found"));
        Ingredient produce = recipe.getProduce();
        //produce.setRecipe(null);
        ingredientRepository.save(produce);
        recipeRepository.delete(recipe);
    }

    /*
    @Transactional
    public void produceRecipe(long id) {
        produceRecipeMultiple(id, 1);
    }
    */

    private Map<Ingredient, WarehouseEntry> buildEntriesMap(Recipe recipe) {
        Map<Ingredient, WarehouseEntry> entries = new HashMap<>();
        recipe.getIngredientAmounts().keySet().forEach(ingredient -> {
            WarehouseEntry entry = warehouseEntryRepository.findByIngredientId(ingredient.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Warehouse entry not found"));
            entries.put(ingredient, entry);
        });
        entries.put(recipe.getProduce(), warehouseEntryRepository.findByIngredientId(recipe.getProduce().getId())
                .orElseThrow(() -> new IllegalArgumentException("Warehouse entry not found")));
        return entries;
    }

    public boolean isRecipeProducible(long id, int times) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Recipe not found"));
        return productionService.isRecipeProducible(recipe, buildEntriesMap(recipe), times);
    }

    @Transactional
    public void produceRecipeMultiple(long id, int times) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Recipe not found"));
        Map<Ingredient, WarehouseEntry> entries = buildEntriesMap(recipe);
        productionService.produceRecipe(recipe, entries, times);
        entries.values().forEach(warehouseEntryRepository::save);
    }
}