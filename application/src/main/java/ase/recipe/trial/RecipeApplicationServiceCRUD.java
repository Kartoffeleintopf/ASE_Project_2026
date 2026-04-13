package ase.recipe.trial;

import ase.ErrorMessages;
import ase.ingredient.Ingredient;
import ase.ingredient.IngredientRepository;
import ase.recipe.Recipe;
import ase.recipe.RecipeBuilder;
import ase.recipe.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class RecipeApplicationServiceCRUD {

    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;
    private final RecipeValidationService recipeValidationService;

    @Autowired
    public RecipeApplicationServiceCRUD(RecipeRepository recipeRepository,
                                    IngredientRepository ingredientRepository,
                                        RecipeValidationService recipeValidationService) {
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
        this.recipeValidationService = recipeValidationService;
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
            if (recipeValidationService.ingredientRequiresSelf(produce, ingredient)) {
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
            if (recipeValidationService.ingredientRequiresSelf(recipe.getProduce(), ingredient)) {
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
}
