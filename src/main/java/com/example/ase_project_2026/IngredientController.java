package com.example.ase_project_2026;

import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/ingredients")
public class IngredientController {

    private final IngredientRepository repository;

    public IngredientController(IngredientRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public Ingredient createIngredient(@RequestBody Ingredient ingredient) {
        return repository.save(ingredient);
    }

    @GetMapping("/{ingredientID}")
    public Ingredient getIngredientByID(@PathVariable long ingredientID) {
        return repository.findById(ingredientID).orElse(null);
    }

    @GetMapping("/name/{ingredientID}")
    public String getIngredientNameByID(@PathVariable long ingredientID) {
        return Objects.requireNonNull(repository.findById(ingredientID).orElse(null)).getName();
    }

    // ToDO
    /*
    Zutaten bearbeiten oder loeschen

    Rezept produzieren (maybe need another controller for that?!

    Lagerbestand einsehen Neues rezept erstellen
     */
}