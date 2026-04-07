package ase.ingredient;

import ase.warehouse.WarehouseEntry;
import ase.warehouse.WarehouseEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IngredientApplicationService {
    private final IngredientRepository ingredientRepository;
    private final WarehouseEntryRepository warehouseEntryRepository;

    @Autowired
    public IngredientApplicationService(IngredientRepository ingredientRepository,
                                        WarehouseEntryRepository warehouseEntryRepository) {
        this.ingredientRepository = ingredientRepository;
        this.warehouseEntryRepository = warehouseEntryRepository;
    }

    public Ingredient createIngredient(String name, String picture, boolean base) {
        Ingredient ingredient = ingredientRepository.save(new Ingredient(name, picture, base));
        warehouseEntryRepository.save(new WarehouseEntry(ingredient));
        return ingredient;
    }

    public Ingredient updateIngredient(long id, String name, String picture) {
        Ingredient ingredient = ingredientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ingredient not found"));
        ingredient.setName(name);
        ingredient.setPicture(picture);
        return ingredientRepository.save(ingredient);
    }

    public void deleteIngredient(long id) {
        // ToDo
        // Careful of interdependencies
        // Options
        /*
        1. doesn't exist
        2. exists but is produced by a recipe
        3. exists but is used in a recipe
        4. exists with no interdependencies
         */
        Ingredient ingredient =  ingredientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ingredient not found"));

        //ingredientRepository.delete(ingredient);
    }

    public List<Ingredient> findAllIngredients() {
        return ingredientRepository.findAll();
    }

    public List<Ingredient> findByBase(boolean base) {
        return ingredientRepository.findByBase(base);
    }

    public List<Ingredient> findByNameContaining(String name) {
        return ingredientRepository.findByNameContaining(name);
    }
}
