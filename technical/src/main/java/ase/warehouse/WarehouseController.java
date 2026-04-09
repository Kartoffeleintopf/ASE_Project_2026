package ase.warehouse;

import ase.ingredient.IngredientApplicationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/warehouse")
public class WarehouseController {

    private final WarehouseApplicationService warehouseApplicationService;
    private final IngredientApplicationService ingredientApplicationService;

    public WarehouseController(WarehouseApplicationService warehouseApplicationService,
                               IngredientApplicationService ingredientApplicationService) {
        this.warehouseApplicationService = warehouseApplicationService;
        this.ingredientApplicationService = ingredientApplicationService;
    }

    // General Queries

    @GetMapping("/all")
    public List<WarehouseEntry> findAll() {
        return warehouseApplicationService.findAll();
    }

    @GetMapping("/instock")
    public List<WarehouseEntry> findAllInStock() {
        return warehouseApplicationService.findAllInStock();
    }

    @GetMapping("/ingredient/{ingredientId}")
    public WarehouseEntry findByIngredientID(@PathVariable long ingredientId) {
        return warehouseApplicationService.findByIngredientID(ingredientId);
    }

    // Queries by Amount

    @GetMapping("/{amount}")
    public List<WarehouseEntry> findByAmount(@PathVariable int amount) {
        return warehouseApplicationService.findByAmount(amount);
    }

    // Modification of WarehouseEntry amounts
}