package ase.recipe.trial;

import ase.ingredient.Ingredient;
import ase.recipe.Recipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class RecipeApplicationServiceFacade {
    private final RecipeApplicationServiceCRUD crudService;
    private final RecipeApplicationServiceFIND findService;
    private final RecipeApplicationServicePROD prodService;

    @Autowired
    public RecipeApplicationServiceFacade(RecipeApplicationServiceCRUD crudService,
                                          RecipeApplicationServiceFIND findService,
                                          RecipeApplicationServicePROD prodService) {
        this.crudService = crudService;
        this.findService = findService;
        this.prodService = prodService;
    }
    // CRUD

    public Recipe createRecipe(String name, long produceId, Map<Long, Integer> ingredientAmounts) {
        return crudService.createRecipe(name, produceId, ingredientAmounts);
    }

    // Read

    @Transactional
    public Recipe updateRecipe(long id, long produceId, String name, Map<Long, Integer> ingredientAmounts) {
        return  crudService.updateRecipe(id, produceId, name, ingredientAmounts);
    }

    @Transactional
    public void deleteRecipe(long id) {
        crudService.deleteRecipe(id);
    }

    //FIND

    public Recipe findRecipeById(long id) {
        return findService.findRecipeById(id);
    }

    public Optional<Recipe> findByProduceID(long id) {
        return findService.findByProduceID(id);
    }

    public List<Recipe> findAllRecipes() {
        return findService.findAllRecipes();
    }

    public List<Recipe> findByNameContaining(String name) {
        return findService.findByNameContaining(name);
    }

    // PROD


    public boolean isRecipeProducible(long id, int times) {
        return prodService.isRecipeProducible(id, times);
    }

    @Transactional
    public void produceRecipeMultiple(long id, int times) {
        prodService.produceRecipeMultiple(id, times);
    }

    public Map<Ingredient, Integer> getDirectIngredients(long recipeId) {
        return prodService.getDirectIngredients(recipeId);
    }

    public Map<Ingredient, Integer> getBaseIngredients(long recipeId) {
        return prodService.getBaseIngredients(recipeId);
    }

    private void collectBaseIngredients(Recipe recipe, Map<Ingredient, Integer> baseIngredients, int multiplier) {
        prodService.getBaseIngredients(recipe.getId());
    }
}