package ase.warehouse;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface WarehouseEntryRepository extends JpaRepository<WarehouseEntry, Long> {
    List<WarehouseEntry> findByAmountGreaterThan(int amount);
    List<WarehouseEntry> findByAmount (int amount);
    List<WarehouseEntry> findByAmountLessThan(int amount);
}
