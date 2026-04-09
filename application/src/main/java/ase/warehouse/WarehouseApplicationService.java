package ase.warehouse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        return findByAmountGreaterThan(0);
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

    public WarehouseEntry findByIngredientID(long ingredientID) {
        return warehouseEntryRepository.findByIngredient(ingredientID)
                .orElseThrow(() -> new IllegalArgumentException("Warehouse entry not found"));
    }

    public WarehouseEntry findEntryByID(Long id) {
        return warehouseEntryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Warehouse entry not found"));
    }

    @Transactional
    public WarehouseEntry addAmount(long id, int amount) {
        WarehouseEntry entry = findEntryByID(id);
        entry.addAmount(amount);
        return warehouseEntryRepository.save(entry);
    }

    @Transactional
    public WarehouseEntry subtractAmount(long id, int amount) {
        WarehouseEntry entry = findEntryByID(id);
        entry.subtractAmount(amount);
        return warehouseEntryRepository.save(entry);
    }

    @Transactional
    public WarehouseEntry setAmount(long id, int amount) {
        WarehouseEntry entry = findEntryByID(id);
        entry.setAmount(amount);
        return warehouseEntryRepository.save(entry);
    }
}