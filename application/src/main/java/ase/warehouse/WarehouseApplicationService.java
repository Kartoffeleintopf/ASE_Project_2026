package ase.warehouse;

import ase.ingredient.Ingredient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WarehouseApplicationService {
    private final WarehouseEntryRepository warehouseEntryRepository;

    @Autowired
    public WarehouseApplicationService(WarehouseEntryRepository warehouseEntryRepository) {
        this.warehouseEntryRepository = warehouseEntryRepository;
    }

    public List<WarehouseEntry> findAll() {
        return warehouseEntryRepository.findAll();
    }

    public List<WarehouseEntry> findAllInStock() {
        return warehouseEntryRepository.findByAmountGreaterThan(0);
    }

    public List<WarehouseEntry> findByAmount(int amount) {
        return warehouseEntryRepository.findByAmount(amount);
    }

    public List<WarehouseEntry> findByAmountGreaterThan(int amount) {
        return warehouseEntryRepository.findByAmountGreaterThan(amount);
    }

    public List<WarehouseEntry> findByAmountLessThan(int amount) {
        return warehouseEntryRepository.findByAmountLessThan(amount);
    }

    public List<WarehouseEntry> findByAmountBetween(int start, int end) {
        return warehouseEntryRepository.findByAmountGreaterThanAndAmountLessThan(start, end);
    }

    public WarehouseEntry findByIngredient(Ingredient ingredient) {
        return warehouseEntryRepository.findByIngredient(ingredient)
                .orElseThrow(() -> new IllegalArgumentException("Warehouse entry not found"));
    }

    public WarehouseEntry findEntryByID(Long id) {
        return warehouseEntryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Warehouse entry not found"));
    }

    public WarehouseEntry addAmount(long id, int amount) {
        WarehouseEntry entry = findEntryByID(id);
        entry.addAmount(amount);
        return warehouseEntryRepository.save(entry);
    }

    public WarehouseEntry subtractAmount(long id, int amount) {
        WarehouseEntry entry = findEntryByID(id);
        entry.subtractAmount(amount);
        return warehouseEntryRepository.save(entry);
    }

    public WarehouseEntry setAmount(long id, int amount) {
        WarehouseEntry entry = findEntryByID(id);
        entry.setAmount(amount);
        return warehouseEntryRepository.save(entry);
    }
}