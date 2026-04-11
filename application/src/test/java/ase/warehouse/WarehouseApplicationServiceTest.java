package ase.warehouse;

import ase.ingredient.Ingredient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WarehouseApplicationServiceTest {

    @Mock
    private WarehouseEntryRepository warehouseEntryRepository;

    @InjectMocks
    private WarehouseApplicationService warehouseApplicationService;

    private Ingredient ingredient;
    private WarehouseEntry warehouseEntry;

    @BeforeEach
    void setUp() {
        ingredient = new Ingredient("Tomato", "link", true);
        warehouseEntry = new WarehouseEntry(ingredient);
    }

    @Test
    void findAll() {
        when(warehouseEntryRepository.findAll()).thenReturn(List.of(warehouseEntry));
        List<WarehouseEntry> result = warehouseApplicationService.findAll();
        assertEquals(1, result.size());
        verify(warehouseEntryRepository).findAll();
    }

    @Test
    void findAllInStock() {
        when(warehouseEntryRepository.findByAmountGreaterThan(0)).thenReturn(List.of(warehouseEntry));
        List<WarehouseEntry> result = warehouseApplicationService.findAllInStock();
        assertEquals(1, result.size());
    }

    @Test
    void findByAmount() {
        when(warehouseEntryRepository.findByAmount(0)).thenReturn(List.of(warehouseEntry));
        List<WarehouseEntry> result = warehouseApplicationService.findByAmount(0);
        assertEquals(1, result.size());
    }

    @Test
    void findByAmountGreaterThan() {
        when(warehouseEntryRepository.findByAmountGreaterThan(5)).thenReturn(List.of());
        List<WarehouseEntry> result = warehouseApplicationService.findByAmountGreaterThan(5);
        assertEquals(0, result.size());
    }

    @Test
    void findByAmountLessThan() {
        when(warehouseEntryRepository.findByAmountLessThan(5)).thenReturn(List.of(warehouseEntry));
        List<WarehouseEntry> result = warehouseApplicationService.findByAmountLessThan(5);
        assertEquals(1, result.size());
    }

    @Test
    void findByAmountBetween() {
        when(warehouseEntryRepository.findByAmountGreaterThanAndAmountLessThan(0, 10)).thenReturn(List.of(warehouseEntry));
        List<WarehouseEntry> result = warehouseApplicationService.findByAmountBetween(0, 10);
        assertEquals(1, result.size());
    }

    @Test
    void findEntryByIDNotFound() {
        when(warehouseEntryRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () ->
                warehouseApplicationService.findEntryByID(99L));
    }

    @Test
    void findByIngredientIDNotFound() {
        when(warehouseEntryRepository.findByIngredientId(99L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () ->
                warehouseApplicationService.findByIngredientID(99L));
    }
}