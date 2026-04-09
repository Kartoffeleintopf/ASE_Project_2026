package ase.warehouse;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/warehouse")
public class WarehouseController {

    private final WarehouseApplicationService warehouseApplicationService;

    public WarehouseController(WarehouseApplicationService warehouseApplicationService) {
        this.warehouseApplicationService = warehouseApplicationService;
    }

    // General Queries

    @GetMapping("/all")
    public List<WarehouseEntry> getAllWarehousesEntries() {
        return warehouseApplicationService.findAll();
    }

    @GetMapping("/instock")
    public List<WarehouseEntry> findAllInstockWarehouseEntries() {
        return warehouseApplicationService.findAllInStock();
    }

    @GetMapping("/ingredient/{ingredientId}")
    public WarehouseEntry findByIngredientID(@PathVariable long ingredientId) {
        return warehouseApplicationService.findByIngredientID(ingredientId);
    }


    // Queries by Amount

    @GetMapping("/amount/exact/{amount}")
    public List<WarehouseEntry> findByAmountExact(@PathVariable int amount) {
        return warehouseApplicationService.findByAmount(amount);
    }

    @GetMapping("/amount/greater/{amount}")
    public List<WarehouseEntry> findByAmountGreaterThan(@PathVariable int amount) {
        return warehouseApplicationService.findByAmountGreaterThan(amount);
    }

    @GetMapping("/amount/less/{amount}")
    public List<WarehouseEntry> findByAmountLessThan(@PathVariable int amount) {
        return warehouseApplicationService.findByAmountLessThan(amount);
    }

    @GetMapping("/amount/between/{start}/{end}")
    public List<WarehouseEntry> findByAmountBetween(@PathVariable int start, @PathVariable int end) {
        return warehouseApplicationService.findByAmountBetween(start, end);
    }


    // Modification of WarehouseEntry amounts

    @PutMapping("/add")
    public WarehouseEntry addAmount(@RequestBody WarehouseEntryDTO dto) {
        return warehouseApplicationService.addAmount(dto.ingredientId(), dto.amount());
    }

    @PutMapping("/subtract")
    public WarehouseEntry subtractAmount(@RequestBody WarehouseEntryDTO dto) {
        return warehouseApplicationService.subtractAmount(dto.ingredientId(), dto.amount());
    }

    @PutMapping("/set")
    public WarehouseEntry setAmount(@RequestBody WarehouseEntryDTO dto) {
        return warehouseApplicationService.setAmount(dto.ingredientId(), dto.amount());
    }
}