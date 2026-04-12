# 5 Refactoring

#### Duplizierter Code — buildEntriesMap in RecipeApplicationService
Die Logik zum Aufbau der Zutaten-Lagerbestands-Zuordnung (Map<Ingredient, WarehouseEntry>)
war sowohl in produceRecipeMultiple als auch in isRecipeProducible dupliziert.
Beide Methoden enthielten denselben Code zum Abrufen der Lagerbestaende fuer jede Zutat im Rezept.

Refactoring: Extract Method --> DRY Prinzip
Die duplizierte Logik wurde in die private Hilfsmethode buildEntriesMap extrahiert,
welche von beiden Methoden aufgerufen wird. Dies reduziert die Codeduplizierung und
stellt sicher, dass Aenderungen an der Logik nur an einer Stelle vorgenommen werden muessen.

```
private Map<Ingredient, WarehouseEntry> buildEntriesMap(Recipe recipe) {
    Map<Ingredient, WarehouseEntry> entries = new HashMap<>();
    recipe.getIngredientAmounts().keySet().forEach(ingredient -> {
        WarehouseEntry entry = warehouseEntryRepository.findByIngredientId(ingredient.getId())
                .orElseThrow(() -> new IllegalArgumentException("Warehouse entry not found"));
        entries.put(ingredient, entry);
    });
    entries.put(recipe.getProduce(), warehouseEntryRepository.findByIngredientId(recipe.getProduce().getId())
                .orElseThrow(() -> new IllegalArgumentException("Warehouse entry not found")));
    return entries;
}
```

Duplizierte Code wurde hier also durch eine extra Methode verhindert,
ein anderes Refactoring welches gern genutzt wurde,
ist aehnliche Methoden durch allgemeinere Versionen durchzuleiten.
Ein Beispiel aus WarehouseEntry.java:
```
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
```

#### LayerDurchbruch — IngredientController
Der urspruengliche IngredientController griff direkt auf das IngredientRepository zu,
anstatt die Geschaeftslogik ueber den IngredientApplicationService abzuwickeln.
```
// Vorher - Controller greift direkt auf Repository zu
@PostMapping
public Ingredient createIngredient(@RequestBody Ingredient ingredient) {
    return ingredientRepository.save(ingredient);
}

// Nachher - Controller delegiert an Service
@PostMapping("/create")
public Ingredient createIngredient(@RequestBody IngredientDTO dto) {
    return ingredientApplicationService.createIngredient(dto.name(), dto.picture(), dto.base());
}
```

Refactoring: Move Method
Die Geschaeftslogik wurde in den IngredientApplicationService verschoben.
Der Controller ist nun ausschließlich fuer die HTTP-Kommunikation zustaendig,
waehrend der Service die fachliche Logik kapselt.

#### Unangemessene Naehe — Recipe.setProduce
Die Methode setProduce in der Recipe Klasse griff direkt auf den internen Zustand
von Ingredient zu, indem sie produce.setRecipe(this) aufrief. Dies fuehrte zu einer
engen Kopplung zwischen Recipe und Ingredient sowie zu zirkulaeren JSON-Referenzen
bei der Serialisierung, welche beim Testen der Endpunkte auffiel.
```
// Vorher - Recipe greift in den internen Zustand von Ingredient ein
public void setProduce(Ingredient produce) {
    this.produce = produce;
    produce.setRecipe(this);  // Unangemessene Naehe
}

// Nachher - Recipe setzt nur seinen eigenen Zustand
public void setProduce(Ingredient produce) {
    this.produce = produce;
}
```

Refactoring: Aufloesung der Referenz
Die bidirektionale Beziehung zwischen Recipe und Ingredient wurde aufgeloest.
Die Rueckreferenz von Ingredient zu Recipe wurde entfernt, da sie zu zirkulaeren
Abhaengigkeiten bei der JSON-Serialisierung fuehrte. Der Zugriff auf das Rezept
einer Zutat erfolgt nun ausschließlich ueber das RecipeRepository mittels
findRecipeByProduce(ingredient).