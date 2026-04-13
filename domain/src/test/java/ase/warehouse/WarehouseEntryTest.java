package ase.warehouse;

import ase.ingredient.Ingredient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WarehouseEntryTest {

    private Ingredient ingredient;
    private WarehouseEntry warehouseEntry;

    @BeforeEach
    void setUp() {
        ingredient = new Ingredient("Tomato", "link", true);
        warehouseEntry = new WarehouseEntry(ingredient);
    }

    @Test
    void getIngredient() {
        assertEquals(ingredient, warehouseEntry.getIngredient());
    }

    @Test
    void getAmount() {
        assertEquals(0, warehouseEntry.getAmount());
    }

    @Test
    void setAmount() {
        warehouseEntry.setAmount(10);
        assertEquals(10, warehouseEntry.getAmount());
    }

    @Test
    void setAmountNegativeThrows() {
        assertThrows(IllegalArgumentException.class, () -> warehouseEntry.setAmount(-1));
    }

    @Test
    void addAmount() {
        warehouseEntry.addAmount(5);
        assertEquals(5, warehouseEntry.getAmount());
    }

    @Test
    void addNegativeAmount() {
        warehouseEntry.addAmount(10);
        warehouseEntry.addAmount(-5);
        assertEquals(5, warehouseEntry.getAmount());
    }

    @Test
    void addNegativeAmountThrows() {
        assertThrows(IllegalArgumentException.class, () -> warehouseEntry.addAmount(-5));
    }

    @Test
    void subtractAmount() {
        warehouseEntry.addAmount(10);
        warehouseEntry.subtractAmount(4);
        assertEquals(6, warehouseEntry.getAmount());
    }

    @Test
    void subtractNegativeAmount() {
        warehouseEntry.subtractAmount(-10);
        assertEquals(10, warehouseEntry.getAmount());
    }


    @Test
    void subtractAmountNegativeThrows() {
        assertThrows(IllegalArgumentException.class, () -> warehouseEntry.subtractAmount(1));
    }

    @Test
    void subtractExactAmount() {
        warehouseEntry.addAmount(10);
        warehouseEntry.subtractAmount(10);
        assertEquals(0, warehouseEntry.getAmount());
    }
}