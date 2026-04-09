package ase.recipe;

import java.util.Map;

public record RecipeDTO(String name, long produceId, Map<Long, Integer> ingredientAmounts) {}