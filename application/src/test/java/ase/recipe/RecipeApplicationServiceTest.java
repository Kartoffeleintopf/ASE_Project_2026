package ase.recipe;

import ase.ingredient.Ingredient;
import ase.ingredient.IngredientRepository;
import ase.production.ProductionService;
import ase.warehouse.WarehouseEntry;
import ase.warehouse.WarehouseEntryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecipeApplicationServiceTest {

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private IngredientRepository ingredientRepository;

    @Mock
    private WarehouseEntryRepository warehouseEntryRepository;

    @InjectMocks
    private RecipeApplicationService recipeApplicationService;

    @Mock
    private ProductionService productionService;

    private Ingredient produce;
    private Ingredient ingredientA;
    private Recipe recipe;
    private WarehouseEntry produceEntry;
    private WarehouseEntry ingredientAEntry;

    @BeforeEach
    void setUp() {
        produce = new Ingredient("Sauce", "link", false);
        ingredientA = new Ingredient("Tomato", "link", true);
        recipe = new Recipe("Tomato Sauce", produce);
        recipe.addIngredient(ingredientA, 5);
        ingredientAEntry = new WarehouseEntry(ingredientA);
        ingredientAEntry.addAmount(10);
    }

    @Test
    void createRecipeBaseIngredientThrows() {
        when(ingredientRepository.findById(ingredientA.getId())).thenReturn(Optional.of(ingredientA));

        assertThrows(IllegalStateException.class, () ->
                recipeApplicationService.createRecipe("Recipe", ingredientA.getId(), Map.of()));
    }

    @Test
    void createRecipeAlreadyHasRecipeThrows() {
        when(ingredientRepository.findById(produce.getId())).thenReturn(Optional.of(produce));
        when(recipeRepository.findRecipeByProduce(produce)).thenReturn(Optional.of(recipe));

        assertThrows(IllegalArgumentException.class, () ->
                recipeApplicationService.createRecipe("Recipe", produce.getId(), Map.of()));
    }

    @Test
    void findRecipeById() {
        when(recipeRepository.findById(recipe.getId())).thenReturn(Optional.of(recipe));

        Recipe result = recipeApplicationService.findRecipeById(recipe.getId());

        assertEquals(recipe, result);
    }

    @Test
    void findRecipeByIdNotFound() {
        when(recipeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () ->
                recipeApplicationService.findRecipeById(99L));
    }

    @Test
    void findAllRecipes() {
        when(recipeRepository.findAll()).thenReturn(List.of(recipe));

        List<Recipe> result = recipeApplicationService.findAllRecipes();

        assertEquals(1, result.size());
        verify(recipeRepository).findAll();
    }

    @Test
    void findByNameContaining() {
        when(recipeRepository.findByNameContaining("Tomato")).thenReturn(List.of(recipe));

        List<Recipe> result = recipeApplicationService.findByNameContaining("Tomato");

        assertEquals(1, result.size());
        verify(recipeRepository).findByNameContaining("Tomato");
    }

    @Test
    void findByProduceId() {
        when(ingredientRepository.findById(produce.getId())).thenReturn(Optional.of(produce));
        when(recipeRepository.findRecipeByProduce(produce)).thenReturn(Optional.of(recipe));

        Optional<Recipe> result = recipeApplicationService.findByProduceID(produce.getId());

        assertTrue(result.isPresent());
        assertEquals(recipe, result.get());
    }

    @Test
    void deleteRecipe() {
        when(recipeRepository.findById(recipe.getId())).thenReturn(Optional.of(recipe));

        recipeApplicationService.deleteRecipe(recipe.getId());

        verify(recipeRepository).delete(recipe);
    }

    @Test
    void produceRecipeMultiple() {
        ingredientAEntry.addAmount(10);
        when(recipeRepository.findById(recipe.getId())).thenReturn(Optional.of(recipe));
        when(warehouseEntryRepository.findByIngredientId(anyLong()))
                .thenReturn(Optional.of(ingredientAEntry));

        recipeApplicationService.produceRecipeMultiple(recipe.getId(), 1);

        verify(warehouseEntryRepository, times(2)).findByIngredientId(anyLong());
        verify(warehouseEntryRepository, atLeastOnce()).save(any(WarehouseEntry.class));
    }

    @Test
    void createRecipeWithCyclicDependencyThrows() {
        when(ingredientRepository.findById(anyLong())).thenReturn(Optional.of(produce));
        when(recipeRepository.findRecipeByProduce(any())).thenReturn(Optional.empty())
                .thenReturn(Optional.of(recipe));

        assertThrows(IllegalArgumentException.class, () ->
                recipeApplicationService.createRecipe("Recipe", produce.getId(), Map.of(produce.getId(), 1)));
    }
}