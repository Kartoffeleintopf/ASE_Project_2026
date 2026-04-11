package ase.recipe;

import ase.ingredient.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    List<Recipe> findByNameContaining(String name);
    Optional<Recipe> findRecipeByProduceId(Long produceId);
    Optional<Recipe> findRecipeByProduce(Ingredient produce);
}
