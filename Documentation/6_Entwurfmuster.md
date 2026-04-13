# Entwurfsmuster

## Builder Pattern - RecipeBuilder

### Einsatz
Das Builder Pattern wurde fuer die Erstellung von Rezepten (Recipe) implementiert.
Anstatt ein Rezept direkt zu instanziieren und anschliessend Zutaten hinzuzufuegen,
wird ein RecipeBuilder verwendet:

```
RecipeBuilder builder = new RecipeBuilder(name, produce);
ingredientAmounts.forEach((ingredientId, amount) -> {
    Ingredient ingredient = ingredientRepository.findById(ingredientId)...;
    builder.addIngredient(ingredient, amount);
});
Recipe recipe = builder.build();
```

### Begruendung
Das Builder Pattern wurde hier eingesetzt, 
weil die Erstellung eines Rezepts ein mehrstufiger Prozess ist:

1. Name und Zielzutat werden benoetigt um den Builder zu initialisieren
2. Zutaten mit Mengenangaben werden schrittweise hinzugefuegt
3. Das fertige Rezept wird durch build() erzeugt

Dies trennt den Konstruktionsprozess von der Repraesentation der Builder akkumuliert alle Informationen bevor das finale Objekt erzeugt wird.

### Vorteile
- Die Konstruktion des Rezepts ist klar strukturiert und lesbar
- Der Builder kann flexibel mit beliebig vielen Zutaten befuellt werden
- Das Rezept wird erst durch build() finalisiert,
  wodurch sichergestellt wird dass alle Informationen vorhanden sind bevor das Objekt verwendet wird
- Trennung der Konstruktionslogik von der Rezept-Klasse selbst

### Nachteile
- Einfuehrung einer zusaetzlichen Klasse (RecipeBuilder)
- Fuer einfache Rezepte ohne Zutaten ist der Builder nicht notwendig

### Ohne dieses Muster
Ohne den Builder wuerde die Konstruktion direkt im Service stattfinden:

```
Recipe recipe = new Recipe(name, produce);
ingredientAmounts.forEach((ingredientId, amount) -> {
    Ingredient ingredient = ingredientRepository.findById(ingredientId)...;
    recipe.addIngredient(ingredient, amount);
});
```

Dies ist funktional aequivalent, jedoch ist die Konstruktionslogik
nicht von der Verwendung des Objekts getrennt und liegt direkt im Service.
Der Builder macht die Konstruktionsabsicht expliziter und kapselt
den Aufbauprozess in einer dedizierten Klasse.

## TODO