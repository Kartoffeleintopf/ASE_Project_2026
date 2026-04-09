# Domain Driven Design (DDD)
## Analyse der Ubiquitous Language
Relevante Begriffe werden mit ihrer fachlichen Bedeutung, Aufgaben und Regeln dokumentiert

### Zutat (Ingredient)
Eine Zutat ist ein im System verwaltetes Object welches Eigenschaften besitzt wie bspw. Lagerbestand und welches in Rezepten verwendet werden kann.
Auf der Domainebene kann die base Variable nicht geändert werden, dies verhindert dass Service oder die Technical Ebene diese Invariante verändern können

#### Eigenschaften
- Name
- Bild
- Lagerbestand

#### Regeln
- Kann eine Grundzutat sein oder eine durch ein Rezept herstellbare Zutat sein.
- Kann in einem Rezept für andere Zutaten verwendet werden.
- Lagerbestand darf nicht kleiner als 0 sein (Lagerbestand >= 0)

### Grundzutat (BaseIngredient)
Eine Grundzutat ist eine Zutat welche nicht durch ein Rezept erzeugt werden kann

#### Eigenschaften
- Name
- Bild
- Lagerbestand

#### Regeln
- Darf nicht das Resultat eines Rezept sein
- Kann in einem Rezept für andere Zutaten verwendet werden.
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
- Kann in einem Rezept für andere Zutaten verwendet werden.
- Lagerbestand darf nicht kleiner als 0 sein (Lagerbestand >= 0)

### Rezept (Recipe)
Ein Rezept beschreibt die Kombination an Zutaten und Anweisungen die verwendet werden müssen um eine Herstellbare Zutat zu erzeugen.

#### Eigenschaften
- Name
- Zielzutat (Herstellbare Zutat)
- Liste benötigter Zutaten mit Mengenangaben

#### Regeln
- Erfordert Zielzutat und benötigte Zutaten um gültig zu sein

### Lager
Das Lager enthält alle Zutaten jeder Art in den vorhandenen Mengen

#### Eigenschaften
- Zutaten mit zugehörigen Mengenangaben

#### Regeln
- Die zugehörigen Mengenangaben jeder Zutat dürfen nicht unter null sinken

### Entscheidung ob vererbung
Soll Ingredient eine Superklasse sein die an BaseIngredient und an RecipeIngredient vererbt.
Nein

## Produzieren
Beim API Endpunkte erstellen ist mir aufgefallen,
dass ein Rezept produzieren und eine herstellbare Zutat produzieren auseinandergehalten werden muss.
Da beide das selbe Wort (produzieren/produce) verwenden.
Um diesen Problem bei der API Endpunkte Benennung aus dem Weg zu gehen,
wird das produzieren eines Rezeptes umbenannt (Ausführen/execute).

## API Endpunkte
Die API Endpunkte fuer den WarehouseController welche sich mit der Modifikation der Zutatenmengen befassen,
nutzen das WarehouseEntryDTO um die Mengen (amounts) zu verwalten.
Dadurch befinden sich in den API Aufrufen nicht zwei Zahlen (IngredientID und Mengen/amount),
sondern nur eine, Die IngredientID.