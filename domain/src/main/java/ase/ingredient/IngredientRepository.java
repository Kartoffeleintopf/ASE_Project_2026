package ase.ingredient;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    List<Ingredient> findByBase(boolean base);
    Optional<Ingredient> findByNameContaining(String name);
}
