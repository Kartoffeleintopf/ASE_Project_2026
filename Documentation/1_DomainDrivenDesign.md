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