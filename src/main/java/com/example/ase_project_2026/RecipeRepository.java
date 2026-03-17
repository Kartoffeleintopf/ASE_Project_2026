package com.example.ase_project_2026;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRepository extends JpaRepository<RecipeIngredient, Long> {
}
