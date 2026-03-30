package ase.warehouse;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
    List<Warehouse> findByAmountGreaterThan(int amount);
    List<Warehouse> findByAmount (int amount);
    List<Warehouse> findByAmountLessThan(int amount);
}
