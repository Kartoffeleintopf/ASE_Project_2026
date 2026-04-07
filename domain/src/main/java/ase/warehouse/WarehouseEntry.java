package ase.warehouse;

import ase.ingredient.Ingredient;
import jakarta.persistence.*;

@Entity
public class WarehouseEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;

    @Column
    private int amount;

    protected WarehouseEntry() {}

    public WarehouseEntry(Ingredient ingredient) {
        this.ingredient = ingredient;
        this.amount = 0;
    }

    public Ingredient getIngredient() { return ingredient; }

    public int getAmount() { return amount; }

    public void setAmount(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Stored amount cannot be negative!");
        }
        this.amount = amount; }

    public void addAmount(int amount) {
        setAmount(getAmount() + amount);
    }

    public void subtractAmount(int amount) {
        setAmount(getAmount() - amount);
    }
}