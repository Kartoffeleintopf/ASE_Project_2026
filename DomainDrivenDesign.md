# Domain Driven Design (DDD)
## Analyse der Ubiquitous Language
Relevante Begriffe werden mit ihrer fachlichen Bedeutung, Aufgaben und Regeln dokumentiert

### Zutat (Ingredient)
Eine Zutat ist ein im System verwaltetes Object welches Eigenschaften besitzt wie bspw. Lagerbestand und welches in Rezepten verwendet werden kann.

#### Eigenschaften
- Name
- Bild
- Lagerbestand

#### Regeln
- Kann eine Grundzutate seinoder eine durch ein Rezept herstellbare Zutat sein.
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
- Die zugehörigen Mengenangaben jeder Zutat dürfen nicht unter null senken