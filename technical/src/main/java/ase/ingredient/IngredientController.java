package ase.ingredient;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/ingredients")
public class IngredientController {

    private final IngredientApplicationService ingredientApplicationService;

    public IngredientController(IngredientApplicationService ingredientApplicationService) {
        this.ingredientApplicationService = ingredientApplicationService;
    }

    @PostMapping("/create")
    public Ingredient createIngredient(@RequestBody IngredientDTO dto) {
        return ingredientApplicationService.createIngredient(dto.name(), dto.picture(), dto.base());
    }

    @GetMapping("/all")
    public List<Ingredient> getAllIngredients() {
        return ingredientApplicationService.findAllIngredients();
    }

    private List<Ingredient> getByBase(boolean base) {
        return ingredientApplicationService.findByBase(base);
    }

    @GetMapping("/base")
    public List<Ingredient> getBaseIngredients() {
        return getByBase(true);
    }

    @GetMapping("/nonbase")
    public List<Ingredient> getNonBaseIngredients() {
        return getByBase(false);
    }

    @GetMapping("/{id}")
    public Optional<Ingredient> getIngredientById(@PathVariable long id) {
        return ingredientApplicationService.findIngredientById(id);
    }

    @GetMapping("/search")
    public List<Ingredient> searchByName(@RequestParam String name) {
        return ingredientApplicationService.findByNameContaining(name);
    }

    @PutMapping("/update/{id}")
    public Ingredient updateIngredient(@PathVariable long id, @RequestBody IngredientDTO dto) {
        return ingredientApplicationService.updateIngredient(id, dto.name(), dto.picture());
    }

    @DeleteMapping("/delete/{id}")
    public void deleteIngredient(@PathVariable long id) {
        ingredientApplicationService.deleteIngredient(id);
    }
}