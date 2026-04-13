# Programming Principles

## SOLID, GRASP, DRY

### SOLID
- Single Responsibility Principle
- Open and Closed Principle
- Liskov Substitution Principle
- Interface Segregation Principle
- Dependency Inversion Principle

### GRASP
General Responsibility Assignment Software Principle

- **Information Expert** - Assign responsibility to the class that has the needed data
- **Creator** - A class that uses or contains another should create it
- **Controller** - Handles system events (often a service/controller class)
- **Low Coupling** - Reduce dependencies between classes
- **High Cohesion** - Keep classes focused and well organized
- **Polymorphism** - Use polymorphism instead of conditionals when behavior varies
- **Pure Fabrication** - Create artificial classes to improve design (services for example)
- **Indirection** - Use intermediaries to reduce coupling
- **Protected Variations** - Protect elements from changes by wrapping unstable parts

### DRY
Don't Repeat Yourself

Duplizierten Code vermeiden, 
sodass Aenderungen an nur einem Platz getaetigt werden muessen.

## Nutzung

### 1. Single Responsibility Principle
Das Single Responsibility Principle besagt, 
dass eine Klasse nur eine einzige Verantwortung 
und nur einen Grund zur Aenderung haben soll.

Dies wird beispielsweise in der Klasse IngredientController beruecksichtigt.
Der Controller ist ausschliesslich fuer die HTTP-Kommunikation zustaendig.
Er nimmt Anfragen entgegen, 
delegiert an den Service und gibt Antworten zurueck.

```java
@PostMapping("/create")
public Ingredient createIngredient(@RequestBody IngredientDTO dto) {
    return ingredientApplicationService.createIngredient(dto.name(), dto.picture(), dto.base());
}
```

Das Prinzip wird auch auf der Layerebene eingehalten.
Die API Endpunkte liegen alle innerhalb von Controllern im Technical Layer.
Die Geschaeftslogik liegt in ApplicationServices,
die Domaenenlogik im Domain Layer.
So hat jede Ebene eine eigene Verantwortung.


### 2. Open/Closed Principle
Das Open/Closed Principle besagt, dass eine Klasse offen fuer Erweiterungen
aber geschlossen fuer Aenderungen sein soll.

Dies wird in den Repository Interfaces beruecksichtigt.
Die Repositories erweitern JpaRepository und erhalten dadurch automatisch
alle Standard CRUD-Operationen.
Neue Methoden koennen hinzugefuegt werden ohne die bestehenden zu aendern:

```java
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    List<Ingredient> findByBase(boolean base);
    List<Ingredient> findByNameContaining(String name);
}
```

### 3. Dependency Inversion Principle
Das Dependency Inversion Principle besagt, 
dass Klassen von Abstraktionen abhaengen sollen,
nicht von konkreten Implementierungen.

Dies wird in den Application Services beruecksichtigt.
Die Services haengen von den Repository-Interfaces ab,
nicht von deren konkreten Implementierungen.
Zur Laufzeit die konkreten Implementierungen von Spring injiziert.

```java
@Service
public class IngredientApplicationService {
    private final IngredientRepository ingredientRepository;

    @Autowired
    public IngredientApplicationService(IngredientRepository ingredientRepository /*,  ... */) {
        this.ingredientRepository = ingredientRepository;
    }
}
```

### 4. DRY
Das DRY Prinzip besagt, 
dass jede Information oder Logik nur einmal im System vorhanden sein soll.

Dies wird beispielsweise durch die Methode buildEntriesMap in RecipeApplicationService beruecksichtigt.
Die Logik zum Aufbau der Zutaten und Lagerbestands Zuordnung wird von mehreren Methoden
benoetigt und wurde daher in eine private Hilfsmethode extrahiert:

```java
private Map<Ingredient, WarehouseEntry> buildEntriesMap(Recipe recipe) {
    Map<Ingredient, WarehouseEntry> entries = new HashMap<>();
    recipe.getIngredientAmounts().keySet().forEach(ingredient -> {
        WarehouseEntry entry = warehouseEntryRepository.findByIngredientId(ingredient.getId())
                .orElseThrow(() -> new IllegalArgumentException("Warehouse entry not found"));
        entries.put(ingredient, entry);
    });
    entries.put(recipe.getProduce(), warehouseEntryRepository
            .findByIngredientId(recipe.getProduce().getId())
            .orElseThrow(() -> new IllegalArgumentException("Warehouse entry not found")));
    return entries;
}
```

Auch wurden mehrere Methoden umgeschrieben und durch andere Methoden durchgeleitet um duplizierten Code zu vermeiden.
Ein Beispiel hierfuer sind die Methoden innerhalb WarehouseEntry um den amount direkt zu bearbeiten:

```java
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


### 5. Information Expert
Das Information Expert Prinzip besagt, 
dass Verantwortlichkeiten der Klasse zugewiesen werden sollen,
die die notwendigen Informationen besitzt.

Dies wird in der Klasse WarehouseEntry beruecksichtigt.
Da WarehouseEntry die einzige Klasse,
ist die den aktuellen Lagerbestand kennt,
ist sie und Ihre Methoden auch fuer dessen Verwaltung und Validierung zustaendig.

Die Validierung,
dass der Lagerbestand nicht negativ werden darf,
liegt direkt in WarehouseEntry und nicht in einem Service,
da WarehouseEntry der Information Expert fuer den Lagerbestand ist.

```java
public void setAmount(int amount) {
    if (this.amount - amount < 0) {
        throw new IllegalArgumentException("Stored amount cannot be negative!");
    }
    this.amount = amount;
}
```