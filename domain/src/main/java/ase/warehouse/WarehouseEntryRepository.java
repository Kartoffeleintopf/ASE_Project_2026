package ase.warehouse;

import ase.ingredient.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface WarehouseEntryRepository extends JpaRepository<WarehouseEntry, Long> {
    // Ingredient methods
    Optional<WarehouseEntry> findByIngredient(Ingredient ingredient);
    void deleteByIngredient(Ingredient ingredient);

    // Amount methods
    List<WarehouseEntry> findByAmountGreaterThan(int amount);
    List<WarehouseEntry> findByAmount (int amount);
    List<WarehouseEntry> findByAmountLessThan(int amount);
    List<WarehouseEntry> findByAmountGreaterThanAndAmountLessThan(int amount, int amount2);
}
