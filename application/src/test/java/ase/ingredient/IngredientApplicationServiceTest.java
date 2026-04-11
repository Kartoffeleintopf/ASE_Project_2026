package ase.ingredient;

import ase.recipe.Recipe;
import ase.recipe.RecipeRepository;
import ase.warehouse.WarehouseEntry;
import ase.warehouse.WarehouseEntryRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;
import java.util.Optional;


    @ExtendWith(MockitoExtension.class)
    class IngredientApplicationServiceTest {

        @Mock
        private IngredientRepository ingredientRepository;

        @Mock
        private WarehouseEntryRepository warehouseEntryRepository;

        @Mock
        private RecipeRepository recipeRepository;

        @InjectMocks
        private IngredientApplicationService ingredientApplicationService;

        private Ingredient ingredient;

        @BeforeEach
        void setUp() {
            ingredient = new Ingredient("Tomato", "link", true);
        }

        @Test
        void createIngredient() {
            when(ingredientRepository.save(any(Ingredient.class))).thenReturn(ingredient);
            when(warehouseEntryRepository.save(any(WarehouseEntry.class))).thenReturn(new WarehouseEntry(ingredient));

            Ingredient result = ingredientApplicationService.createIngredient("Tomato", "link", true);

            assertEquals(ingredient, result);
            verify(ingredientRepository).save(any(Ingredient.class));
            verify(warehouseEntryRepository).save(any(WarehouseEntry.class));
        }

        @Test
        void updateIngredient() {
            when(ingredientRepository.findById(0L)).thenReturn(Optional.of(ingredient));
            when(ingredientRepository.save(any(Ingredient.class))).thenReturn(ingredient);

            Ingredient result = ingredientApplicationService.updateIngredient(0L, "NewName", "newlink");

            assertEquals("NewName", result.getName());
            assertEquals("newlink", result.getPicture());
            verify(ingredientRepository).save(ingredient);
        }

        @Test
        void updateIngredientNotFound() {
            when(ingredientRepository.findById(99L)).thenReturn(Optional.empty());

            assertThrows(IllegalArgumentException.class, () ->
                    ingredientApplicationService.updateIngredient(99L, "NewName", "newlink"));
        }

        @Test
        void deleteIngredient() {
            when(ingredientRepository.findById(0L)).thenReturn(Optional.of(ingredient));
            when(recipeRepository.findAll()).thenReturn(List.of());

            ingredientApplicationService.deleteIngredient(0L);

            verify(warehouseEntryRepository).deleteByIngredientId(0L);
            verify(ingredientRepository).deleteById(ingredient.getId());
        }

        @Test
        void deleteIngredientNotFound() {
            when(ingredientRepository.findById(99L)).thenReturn(Optional.empty());

            assertThrows(IllegalArgumentException.class, () ->
                    ingredientApplicationService.deleteIngredient(99L));
        }

        @Test
        void deleteIngredientProducedByRecipe() {
            Ingredient nonBase = new Ingredient("Sauce", "link", false);
            Recipe recipe = new Recipe("Recipe", nonBase);
            when(ingredientRepository.findById(0L)).thenReturn(Optional.of(nonBase));
            when(recipeRepository.findRecipeByProduce(nonBase)).thenReturn(Optional.of(recipe));

            assertThrows(IllegalArgumentException.class, () ->
                    ingredientApplicationService.deleteIngredient(0L));
        }

        @Test
        void deleteIngredientContainedInRecipe() {
            Ingredient produce = new Ingredient("Sauce", "link", false);
            Recipe recipe = new Recipe("Recipe", produce);
            recipe.addIngredient(ingredient, 5);
            when(ingredientRepository.findById(0L)).thenReturn(Optional.of(ingredient));
            when(recipeRepository.findAll()).thenReturn(List.of(recipe));

            assertThrows(IllegalArgumentException.class, () ->
                    ingredientApplicationService.deleteIngredient(0L));
        }

        @Test
        void findAllIngredients() {
            when(ingredientRepository.findAll()).thenReturn(List.of(ingredient));

            List<Ingredient> result = ingredientApplicationService.findAllIngredients();

            assertEquals(1, result.size());
            verify(ingredientRepository).findAll();
        }

        @Test
        void findByBase() {
            when(ingredientRepository.findByBase(true)).thenReturn(List.of(ingredient));

            List<Ingredient> result = ingredientApplicationService.findByBase(true);

            assertEquals(1, result.size());
            verify(ingredientRepository).findByBase(true);
        }

        @Test
        void findByNameContaining() {
            when(ingredientRepository.findByNameContaining("Tom")).thenReturn(List.of(ingredient));

            List<Ingredient> result = ingredientApplicationService.findByNameContaining("Tom");

            assertEquals(1, result.size());
            verify(ingredientRepository).findByNameContaining("Tom");
        }

        @Test
        void findIngredientById() {
            when(ingredientRepository.findById(0L)).thenReturn(Optional.of(ingredient));

            Optional<Ingredient> result = ingredientApplicationService.findIngredientById(0L);

            assertTrue(result.isPresent());
            assertEquals(ingredient, result.get());
        }

        @Test
        void findIngredientByIdNotFound() {
            when(ingredientRepository.findById(99L)).thenReturn(Optional.empty());

            Optional<Ingredient> result = ingredientApplicationService.findIngredientById(99L);

            assertFalse(result.isPresent());
        }
    }