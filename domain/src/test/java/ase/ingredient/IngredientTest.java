package ase.ingredient;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IngredientTest {

    private Ingredient baseIngredient;
    private Ingredient nonBaseIngredient;

    @BeforeEach
    void setUp() {
        baseIngredient = new Ingredient("Tomato", "link", true);
        nonBaseIngredient = new Ingredient("Sauce", "link2", false);
    }

    @Test
    void getName() {
        assertEquals("Tomato", baseIngredient.getName());
    }

    @Test
    void setName() {
        baseIngredient.setName("Potato");
        assertEquals("Potato", baseIngredient.getName());
    }

    @Test
    void getPicture() {
        assertEquals("link", baseIngredient.getPicture());
    }

    @Test
    void setPicture() {
        baseIngredient.setPicture("newlink");
        assertEquals("newlink", baseIngredient.getPicture());
    }

    @Test
    void isBase() {
        assertTrue(baseIngredient.isBase());
        assertFalse(nonBaseIngredient.isBase());
    }

    @Test
    void baseIngredientHasNullRecipe() {
        assertNull(baseIngredient.getRecipe());
    }

    @Test
    void equalsSameObject() {
        assertEquals(baseIngredient, baseIngredient);
    }

    @Test
    void equalsDifferentObjectsSameId() {
        // two ingredients with id 0 (default long value before persistence)
        Ingredient other = new Ingredient("Tomato", "link", true);
        assertEquals(baseIngredient, other);
    }

    @Test
    void equalsDifferentType() {
        assertNotEquals(baseIngredient, "not an ingredient");
    }

    @Test
    void hashCodeConsistentWithEquals() {
        Ingredient other = new Ingredient("Tomato", "link", true);
        assertEquals(baseIngredient.hashCode(), other.hashCode());
    }
}