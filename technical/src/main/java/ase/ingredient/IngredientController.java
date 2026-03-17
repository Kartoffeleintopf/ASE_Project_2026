package ase.ingredient;

import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/ingredients")
public class IngredientController {

    private final IngredientRepository ingredientRepository;

    public IngredientController(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    @PostMapping
    public Ingredient createIngredient(@RequestBody Ingredient ingredient) {
        return ingredientRepository.save(ingredient);
    }

    @GetMapping("/{ingredientID}")
    public Ingredient getIngredientByID(@PathVariable long ingredientID) {
        return ingredientRepository.findById(ingredientID).orElse(null);
    }

    @GetMapping("/name/{ingredientID}")
    public String getIngredientNameByID(@PathVariable long ingredientID) {
        return Objects.requireNonNull(ingredientRepository.findById(ingredientID).orElse(null)).getName();
        // look at this snippet again, it seems stupid
    }

    @DeleteMapping("/delete/{ingredientID}")
    public void deleteIngredientByID(@PathVariable long ingredientID) {
        ingredientRepository.deleteById(ingredientID);
        //check for existence
    }
    // ToDO
    /*
    API design: more classification needed: maybe separate into actions or something or get inspiration

    Zutaten bearbeiten oder delete it

    Rezept produzieren (maybe need another controller for that?!

    Lagerbestand einsehen Neues rezept erstellen
     */
}