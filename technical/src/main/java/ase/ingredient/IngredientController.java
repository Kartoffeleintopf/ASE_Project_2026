package ase.ingredient;

import org.springframework.web.bind.annotation.*;

import java.util.List;


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

    // ToDO
    /*
    getAll - filter based on base (true/false) or as additional methods, maybe routed through idk

    getByID

    searchByName

    update

    delete
     */
}