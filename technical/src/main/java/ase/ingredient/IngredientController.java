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

    @GetMapping("/base")
    public List<Ingredient> getBaseIngredients() {
        return ingredientApplicationService.findByBase(true);
    }

    @GetMapping("/nonbase")
    public List<Ingredient> getNonBaseIngredients() {
        return ingredientApplicationService.findByBase(false);
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

    // Could probably apply DRY here needs some work idk, later me i choose you GOOOOO!!!

    // ToDO
    /*
    getAll - filter based on base (true/false) or as additional methods, maybe routed through idk
    maybe not

    getByID

    searchByName

    update

    delete
     */
}