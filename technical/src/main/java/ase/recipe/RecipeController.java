package ase.recipe;

import ase.ingredient.IngredientApplicationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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

    @PutMapping("/update/{id}")
    public Recipe updateRecipe(@PathVariable long id, @RequestBody RecipeDTO dto) {
        return recipeApplicationService.updateRecipe(id, dto.produceId(), dto.name(), dto.ingredientAmounts());
    }

    @DeleteMapping("/delete/{id}")
    public void deleteRecipe(@PathVariable long id) {
        recipeApplicationService.deleteRecipe(id);
    }

    @GetMapping("/produces/{ingredientId}")
    public Optional<Recipe> getRecipeByProduceID(@PathVariable long ingredientId) {
        return recipeApplicationService.findByProduceID(ingredientId);
    }

    // Production

    private void produceMultiple (long recipeID, int times) {
        recipeApplicationService.produceRecipeMultiple(recipeID, times);
    }

    @PostMapping("/produce/{ingredientId}")
    public void produceIngredient(@PathVariable long ingredientId) {
        Recipe recipe = recipeApplicationService.findByProduceID(ingredientId)
                .orElseThrow(() -> new IllegalArgumentException("Recipe not found"));
        produceMultiple(recipe.getId(), 1);
    }

    @PostMapping("/execute/{id}")
    public void produceRecipe(@PathVariable long id) {
        produceMultiple(id, 1);
    }

    @PostMapping("/execute/{id}/multiple/{times}")
    public void produceRecipeMultiple(@PathVariable long id, @PathVariable int times) {
        produceMultiple(id, times);
    }
}