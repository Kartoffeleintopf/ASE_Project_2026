package ase.ingredient;

import ase.ingredient.Ingredient;
import ase.ingredient.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IngredientApplicationService {
    private final IngredientRepository ingredientRepository;

    @Autowired
    public IngredientApplicationService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }
/*
    public List<Ingredient> findAllIngredients() {
        return ingredientRepository.findAllIngredients();
    }*/
}
