package com.example.ase_project_2026;

import org.springframework.web.bind.annotation.*;

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

    // ToDO
    /*
    Zutaten bearbeiten oder loeschen

    Rezept produzieren (maybe need another controller for that?!

    Lagerbestand einsehen Neues rezept erstellen
     */
}