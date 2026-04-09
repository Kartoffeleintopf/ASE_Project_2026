package ase.ingredient;

import jakarta.persistence.*;

@Entity
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private long id;

    @Column
    private String name;

    @Column
    private PictureLink picture;

    @Column
    private Boolean base;

    /*
    @OneToOne(mappedBy = "produce")
    private Recipe recipe;
    */

    protected Ingredient() {}

    public Ingredient(String name, String picture, boolean base) {
        this.name = name;
        this.picture = new PictureLink(picture);
        this.base = base;
        /*
        if (base) {
            recipe = null;
        }*/
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture.getUrl();
    }

    public void setPicture(String picture) {
        this.picture = new PictureLink(picture);
    }

    public Boolean isBase() {
        return base;
    }

    //public Recipe getRecipe() { return recipe; }

    //public void setRecipe(Recipe recipe) { this.recipe = recipe; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ingredient)) return false;
        Ingredient other = (Ingredient) o;
        return this.id == other.id;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }
}
