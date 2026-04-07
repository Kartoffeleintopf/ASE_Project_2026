package ase.warehouse;

import ase.ingredient.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface WarehouseEntryRepository extends JpaRepository<WarehouseEntry, Long> {
    // Ingredient method(s)
    Optional<WarehouseEntry> findByIngredient(Ingredient ingredient);

    // Amount methods
    List<WarehouseEntry> findByAmountGreaterThan(int amount);
    List<WarehouseEntry> findByAmount (int amount);
    List<WarehouseEntry> findByAmountLessThan(int amount);
}
