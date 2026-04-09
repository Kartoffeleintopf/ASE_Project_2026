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

    @GetMapping("/all")
    public List<WarehouseEntry> findAll() {
        return warehouseApplicationService.findAll();
    }

    @GetMapping("/instock")
    public List<WarehouseEntry> findAllInStock() {
        return warehouseApplicationService.findAllInStock();
    }
}