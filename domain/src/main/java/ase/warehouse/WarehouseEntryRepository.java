package ase.warehouse;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface WarehouseEntryRepository extends JpaRepository<WarehouseEntry, Long> {
    // Ingredient methods
    Optional<WarehouseEntry> findByIngredientId(long id);
    void deleteByIngredientId(long id);

    // Amount methods
    List<WarehouseEntry> findByAmountGreaterThan(int amount);
    List<WarehouseEntry> findByAmount (int amount);
    List<WarehouseEntry> findByAmountLessThan(int amount);
    List<WarehouseEntry> findByAmountGreaterThanAndAmountLessThan(int amount, int amount2);
}
