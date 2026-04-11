# Domain Driven Design (DDD)
## Analyse der Ubiquitous Language
Relevante Begriffe werden mit ihrer fachlichen Bedeutung, Aufgaben und Regeln dokumentiert

### Zutat (Ingredient)
Eine Zutat ist ein im System verwaltetes Object welches Eigenschaften besitzt wie bspw. Lagerbestand und welches in Rezepten verwendet werden kann.
Auf der Domainebene kann die base Variable nicht geaendert werden, dies verhindert dass Service oder die Technical Ebene diese Invariante veraendern koennen

#### Eigenschaften
- Name
- Bild
- Lagerbestand

#### Regeln
- Kann eine Grundzutat sein oder eine durch ein Rezept herstellbare Zutat sein.
- Kann in einem Rezept fuer andere Zutaten verwendet werden.
- Lagerbestand darf nicht kleiner als 0 sein (Lagerbestand >= 0)

### Grundzutat (BaseIngredient)
Eine Grundzutat ist eine Zutat welche nicht durch ein Rezept erzeugt werden kann

#### Eigenschaften
- Name
- Bild
- Lagerbestand

#### Regeln
- Darf nicht das Resultat eines Rezept sein
- Kann in einem Rezept fuer andere Zutaten verwendet werden.
- Lagerbestand darf nicht kleiner als 0 sein (Lagerbestand >= 0)

### Herstellbare Zutat ()
Eine Herstellbare Zutat ist eine Zutat welche das Resultat eines Rezeptes ist.

#### Eigenschaften
- Name
- Bild
- Lagerbestand
- Rezept

#### Regeln
- Muss das Resultat genau eines Rezeptes sein
- Kann in einem Rezept fuer andere Zutaten verwendet werden.
- Lagerbestand darf nicht kleiner als 0 sein (Lagerbestand >= 0)

### Rezept (Recipe)
Ein Rezept beschreibt die Kombination an Zutaten und Anweisungen die verwendet werden muessen um eine Herstellbare Zutat zu erzeugen.

#### Eigenschaften
- Name
- Zielzutat (Herstellbare Zutat)
- Liste benoetigter Zutaten mit Mengenangaben

#### Regeln
- Erfordert Zielzutat und benoetigte Zutaten um gueltig zu sein

### Lager
Das Lager enthaelt alle Zutaten jeder Art in den vorhandenen Mengen

#### Eigenschaften
- Zutaten mit zugehoerigen Mengenangaben

#### Regeln
- Die zugehoerigen Mengenangaben jeder Zutat duerfen nicht unter null sinken

### Entscheidung ob vererbung
Soll Ingredient eine Superklasse sein die an BaseIngredient und an RecipeIngredient vererbt.
Nein

## Produzieren
Beim API Endpunkte erstellen ist mir aufgefallen,
dass ein Rezept produzieren und eine herstellbare Zutat produzieren auseinandergehalten werden muss.
Da beide das selbe Wort (produzieren/produce) verwenden.
Um diesen Problem bei der API Endpunkte Benennung aus dem Weg zu gehen,
wird das produzieren eines Rezeptes umbenannt (Ausfuehren/execute).

## API Endpunkte
Die API Endpunkte fuer den WarehouseController welche sich mit der Modifikation der Zutatenmengen befassen,
nutzen das WarehouseEntryDTO um die Mengen (amounts) zu verwalten.
Dadurch befinden sich in den API Aufrufen nicht zwei Zahlen (IngredientID und Mengen/amount),
sondern nur eine, Die IngredientID.
Eine noch bessere Loesung ist es die IngredientID aus dem DTO zu entnehmen.

## Taktische Muster des DDD

### Aggregate

#### Rezept (Recipe) als Aggregate Root
Das Rezept ist als Aggregate Root implementiert. Es kontrolliert den gesamten Zugriff auf seine
Zutaten-Mengen-Zuordnung (ingredientAmounts). Externe Klassen koennen die Zuordnung nicht
direkt manipulieren, sondern muessen dies ueber die Methoden des Rezepts tun:

- addIngredient(ingredient, amount)
- removeIngredient(ingredient)
- setIngredientAmount(ingredient, amount)

Dies stellt sicher, dass die Konsistenz der Zutaten-Mengen-Zuordnung stets durch das Rezept
gewaehrleistet wird. Die Eintraege in ingredientAmounts besitzen keine eigene Identitaet außerhalb
des Rezepts — sie existieren nur als Teil des Aggregats.

Beispiel aus dem Source Code:
```
// Zugriff nur ueber Recipe moeglich
recipe.addIngredient(ingredient, amount);  // korrekt
recipe.ingredientAmounts.put(ingredient, amount);  // nicht moeglich, da private
```

#### Lager (Warehouse)
Das Lager als Gesamtkonzept besteht aus einzelnen WarehouseEntry Objekten, wobei jeder
WarehouseEntry die Zutat und ihren Lagerbestand kapselt. Der Zugriff auf Lagerbestaende
erfolgt ausschließlich ueber die Methoden von WarehouseEntry:

- addAmount(amount)
- subtractAmount(amount)
- setAmount(amount)

Diese Methoden stellen sicher dass der Lagerbestand nie unter null sinken kann.

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