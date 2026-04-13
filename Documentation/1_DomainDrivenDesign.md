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


## API Endpunkte
Die API Endpunkte fuer den WarehouseController welche sich mit der Modifikation der Zutatenmengen befassen,
nutzen das WarehouseEntryDTO um die Mengen (amounts) zu verwalten.
Dadurch befinden sich in den API Aufrufen nicht zwei Zahlen (IngredientID und Mengen/amount),
sondern nur eine, Die IngredientID.
Eine noch bessere Loesung ist es die IngredientID aus dem DTO zu entnehmen.

### Produzieren
Beim API Endpunkte erstellen ist mir aufgefallen,
dass ein Rezept produzieren und eine herstellbare Zutat produzieren auseinandergehalten werden muss.
Da beide das selbe Wort (produzieren/produce) verwenden.
Um diesen Problem bei der API Endpunkte Benennung aus dem Weg zu gehen,
wird das produzieren eines Rezeptes umbenannt (Ausfuehren/execute).

## Taktische Muster des DDD

### Entities
#### Zutat (Ingredient)
Zutat ist als Entitaet definiert, 
jede Zutat besitzt eine eigene Identitaet welche durch ihre id gekennzeichnet ist.
Dies wird durch den Ueberschrieb der equals() Methode sichergestellt:

```
@Override
public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Ingredient)) return false;
    Ingredient other = (Ingredient) o;
    return this.id == other.id;
}
```

#### Rezept (Recipe)
Recipe ist ebenfalls als Entitaet implementiert, 
genau wie die Zutat besitzt auch jedes Rezeot eine id.

#### Lagereintrag (WarehouseEntry)
Der Lagereintrag verwaltet die Anzahlen der vorhandenen Zutaten, 
daher jede erstellte Zutat erhaelt auch einen Eintrag welcher initial immer die Anzahl 0 enthaelt.
Der Lagereintrag ist ebenfalls eine Entitaet, mit eigener id.

### Repositories
Repositories abstrahieren den Zugriff auf die Datenbank 
und stellen eine Sammlung von Methoden bereit, 
zur Interaktion mit dieser.

Neben den Standard CRUD-Operationen (CREATE, READ, UPDATE, DELETE), 
koennen auch selbst Methoden definiert werden welche von JPA in die entsprechenden Datenbankzugriffe umgewnadelt werden.

Es gibt die folgenden 3 Repositories:
- IngredientRepository
- RecipeRepository
- WarehouseEntryRepository

### Value Objects

#### Bildlink (PictureLink)
Bildlink ist ein Value Object, da der Bildlink keine eigene Identitaet benoetigt.
Zwei Bildlink sind also identisch mit derselben url sind identisch, 
unabhaengig davon zu welcher Zutat sie gehoeren.

Da Bildlink ein Value Object ist, 
also immutable ist gibt es entsprechend keine setUrl Methode.
Eine Aenderung erfordert ein neues Objekt.

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

### Domain Service

#### Produktionsservice (ProductionService)
ProductionService ist als Domain Service implementiert, da die Produktionslogik nicht
natuerlich zu einer einzelnen Entity gehoert.
Sie benoetigt sowohl Recipe als auch WarehouseEntry und koordiniert deren Interaktion.

Der Domain Service haelt keine Referenzen auf Repositories und ist damit unabhaengig von der Infrastruktur.
Alle benoedigten Objekte werden vom Application Layer uebergeben:

```
public void produceRecipe(Recipe recipe, Map<Ingredient, WarehouseEntry> entries, int times) {
    if (!isRecipeProducible(recipe, entries, times)) {
        throw new IllegalArgumentException("Recipe is not producible");
    }
    recipe.getIngredientAmounts().forEach((ingredient, amount) ->
            entries.get(ingredient).subtractAmount(amount * times));
    entries.get(recipe.getProduce()).addAmount(times);
}
```

Der Service stellt sicher dass:
- Alle Zutaten ausreichend vorhanden sind bevor die Produktion beginnt (atomare Pruefung)
- Die Lagerbestaende korrekt aktualisiert werden
- Produktionsketten rekursiv traversiert werden koennen via getDirectIngredients() oder getBaseIngredients