package ase.recipe;

import ase.ErrorMessages;
import ase.ingredient.Ingredient;
import ase.ingredient.IngredientRepository;
import ase.production.ProductionService;
import ase.warehouse.WarehouseEntry;
import ase.warehouse.WarehouseEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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

    // CRUD

    @Transactional
    public Recipe createRecipe(String name, long produceId, Map<Long, Integer> ingredientAmounts) {
        Ingredient produce = ingredientRepository.findById(produceId)
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessages.INGREDIENT_NOT_FOUND.getMessage()));
        if (produce.isBase()) {
            throw new IllegalStateException(ErrorMessages.INGREDIENT_IS_BASE.getMessage());

        }
        if (recipeRepository.findRecipeByProduce(produce).isPresent()) {
            throw new IllegalArgumentException(ErrorMessages.INGREDIENT_ALREADY_HAS_RECIPE.getMessage());
        }
        RecipeBuilder builder = new RecipeBuilder(name, produce);
        ingredientAmounts.forEach((ingredientId, amount) -> {
            Ingredient ingredient = ingredientRepository.findById(ingredientId)
                    .orElseThrow(() -> new IllegalArgumentException(ErrorMessages.INGREDIENT_NOT_FOUND.getMessage()));
            if (ingredientRequiresSelf(produce, ingredient)) {
                throw new IllegalArgumentException(ErrorMessages.REQUIRED_SELF.getMessage());
            }
            builder.addIngredient(ingredient, amount);
        });
        Recipe recipe = builder.build();
        recipeRepository.save(recipe);
        //produce.setRecipe(saved);
        ingredientRepository.save(produce);
        return recipe;
    }

    // Read

    @Transactional
    public Recipe updateRecipe(long id, long produceId, String name, Map<Long, Integer> ingredientAmounts) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessages.RECIPE_NOT_FOUND.getMessage()));

        //recipe.setName(name);
        Ingredient newProduce;
        if (recipe.getProduce().getId() != produceId) {
            newProduce = ingredientRepository.findById(produceId)
                    .orElseThrow(() -> new IllegalArgumentException(ErrorMessages.INGREDIENT_NOT_FOUND.getMessage()));

            if (newProduce.isBase()) {
                throw new IllegalStateException(ErrorMessages.CANNOT_ASSIGN_BASE_AS_PRODUCE.getMessage());
            }
            if (recipeRepository.findRecipeByProduce(newProduce).isPresent()) {
                throw new IllegalStateException(ErrorMessages.INGREDIENT_ALREADY_HAS_RECIPE.getMessage());
            }
        }
        else  {
            newProduce = recipe.getProduce();
        }
        final RecipeBuilder recipeBuilder = new RecipeBuilder(name, newProduce);

        //recipe.getIngredientAmounts().clear();

        ingredientAmounts.forEach((ingredientId, amount) -> {
            Ingredient ingredient = ingredientRepository.findById(ingredientId)
                    .orElseThrow(() -> new IllegalArgumentException(ErrorMessages.INGREDIENT_NOT_FOUND.getMessage()));
            if (ingredientRequiresSelf(recipe.getProduce(), ingredient)) {
                throw new IllegalArgumentException(ErrorMessages.REQUIRED_SELF.getMessage());
            }
            recipeBuilder.addIngredient(ingredient, amount);
        });
        Recipe newRecipe = recipeBuilder.build();
        return recipeRepository.save(newRecipe);
    }

    @Transactional
    public void deleteRecipe(long id) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessages.RECIPE_NOT_FOUND.getMessage()));
        Ingredient produce = recipe.getProduce();
        //produce.setRecipe(null);
        ingredientRepository.save(produce);
        recipeRepository.delete(recipe);
    }

    //FIND

    public Recipe findRecipeById(long id) {
        return recipeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessages.RECIPE_NOT_FOUND.getMessage()));
    }

    public Optional<Recipe> findByProduceID(long id) {
        return recipeRepository.findRecipeByProduce(
                ingredientRepository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException(ErrorMessages.INGREDIENT_NOT_FOUND.getMessage()))
        );
    }

    public List<Recipe> findAllRecipes() {
        return recipeRepository.findAll();
    }

    public List<Recipe> findByNameContaining(String name) {
        return recipeRepository.findByNameContaining(name);
    }

    // PROD

    private boolean ingredientRequiresSelf(Ingredient produce, Ingredient ingredient) {
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
                    .orElseThrow(() -> new IllegalArgumentException(ErrorMessages.WAREHOUSE_ENTRY_NOT_FOUND.getMessage()));
            entries.put(ingredient, entry);
        });
        entries.put(recipe.getProduce(), warehouseEntryRepository.findByIngredientId(recipe.getProduce().getId())
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessages.WAREHOUSE_ENTRY_NOT_FOUND.getMessage())));
        return entries;
    }

    public boolean isRecipeProducible(long id, int times) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessages.RECIPE_NOT_FOUND.getMessage()));
        return productionService.isRecipeProducible(recipe, buildEntriesMap(recipe), times);
    }

    @Transactional
    public void produceRecipeMultiple(long id, int times) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessages.RECIPE_NOT_FOUND.getMessage()));
        Map<Ingredient, WarehouseEntry> entries = buildEntriesMap(recipe);
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
}