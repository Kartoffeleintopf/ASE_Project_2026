package ase.ingredient;

import org.springframework.web.bind.annotation.*;


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

    // ToDO
    /*
    getAll

    getByID

    searchByName

    update

    delete
     */
}