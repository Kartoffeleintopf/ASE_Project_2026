package ase.recipe.trial;

import ase.ErrorMessages;
import ase.ingredient.IngredientRepository;
import ase.recipe.Recipe;
import ase.recipe.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecipeApplicationServiceFIND {
    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;

    @Autowired
    public RecipeApplicationServiceFIND(RecipeRepository recipeRepository,
                                    IngredientRepository ingredientRepository) {
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
    }

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
}
