package ase.recipe;

import ase.ingredient.IngredientApplicationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recipes")
public class RecipeController {

    private final RecipeApplicationService recipeApplicationService;
    private final IngredientApplicationService ingredientApplicationService;

    public RecipeController(RecipeApplicationService recipeApplicationService,
                            IngredientApplicationService ingredientApplicationService) {
        this.recipeApplicationService = recipeApplicationService;
        this.ingredientApplicationService = ingredientApplicationService;
    }

    @PostMapping("/create")
    public Recipe createRecipe(@RequestBody RecipeDTO dto) {
        return recipeApplicationService.createRecipe(dto.name(), dto.produceId(), dto.ingredientAmounts());
    }

    @GetMapping("/all")
    public List<Recipe> getAllRecipes() {
        return recipeApplicationService.findAllRecipes();
    }

    @GetMapping("/id/{id}")
    public Recipe getRecipeById(@PathVariable long id) {
        return recipeApplicationService.findRecipeById(id);
    }

    @GetMapping("/search")
    public List<Recipe> searchRecipeByName(@RequestParam String name) {
        return recipeApplicationService.findByNameContaining(name);
    }
}