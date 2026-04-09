package ase.ingredient;

import ase.recipe.Recipe;
import ase.warehouse.WarehouseEntry;
import ase.warehouse.WarehouseEntryRepository;
import ase.recipe.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class IngredientApplicationService {
    private final IngredientRepository ingredientRepository;
    private final WarehouseEntryRepository warehouseEntryRepository;
    private final RecipeRepository recipeRepository;

    @Autowired
    public IngredientApplicationService(IngredientRepository ingredientRepository,
                                        WarehouseEntryRepository warehouseEntryRepository, RecipeRepository recipeRepository) {
        this.ingredientRepository = ingredientRepository;
        this.warehouseEntryRepository = warehouseEntryRepository;
        this.recipeRepository = recipeRepository;
    }

    @Transactional
    public Ingredient createIngredient(String name, String picture, boolean base) {
        Ingredient ingredient = ingredientRepository.save(new Ingredient(name, picture, base));
        warehouseEntryRepository.save(new WarehouseEntry(ingredient));
        return ingredient;
    }

    @Transactional
    public Ingredient updateIngredient(long id, String name, String picture) {
        Ingredient ingredient = ingredientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ingredient not found"));
        ingredient.setName(name);
        ingredient.setPicture(picture);
        return ingredientRepository.save(ingredient);
    }

    @Transactional
    public void deleteIngredient(long id) {
        Ingredient ingredient =  ingredientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ingredient not found"));
        if (!(ingredient.isBase()) && ingredient.getRecipe() != null) {
            throw new IllegalArgumentException("Cannot delete ingredient produced by a recipe");
        }
        for (Recipe recipe : recipeRepository.findAll()) {
            if (recipe.containsIngredient(ingredient)) {
                throw new IllegalArgumentException("Cannot delete ingredient contained in a recipe");
            }
        }
        warehouseEntryRepository.deleteByIngredient(ingredient);
        ingredientRepository.delete(ingredient);
    }

    public List<Ingredient> findAllIngredients() {
        return ingredientRepository.findAll();
    }

    public Optional<Ingredient> findIngredientById(long id) {
        return ingredientRepository.findById(id);
    }

    public List<Ingredient> findByBase(boolean base) {
        return ingredientRepository.findByBase(base);
    }

    public List<Ingredient> findByNameContaining(String name) {
        return ingredientRepository.findByNameContaining(name);
    }
}
