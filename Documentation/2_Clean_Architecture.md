# Clean Architecture

## Schichtarchitektur

Die Anwendung ist in drei Schichten aufgeteilt, welche als separate Maven-Module implementiert sind:

### Domain Layer (domain)
Die innerste Schicht der Anwendung. Sie enthaelt die Geschaeftslogik und die Domaenenmodelle
und ist vollstaendig unabhaengig von anderen Schichten. Sie hat keine Abhaengigkeiten auf
andere Module des Projekts.

Enthaelt:™
- Entities: Ingredient, Recipe, WarehouseEntry
- Value Objects: PictureLink
- Repositories (Interfaces): IngredientRepository, RecipeRepository, WarehouseEntryRepository
- Domain Service: ProductionService

### Application Layer (application)
Die mittlere Schicht. Sie orchestriert die Domaenenobjekte und implementiert die Anwendungsfaelle.
Sie haengt vom Domain Layer ab, kennt aber den Technical Layer nicht.

Enthaelt:
- Application Services: IngredientApplicationService, RecipeApplicationService, WarehouseApplicationService

### Technical Layer (technical)
Die aeusserste Schicht. Sie ist fuer die Kommunikation mit der Aussenwelt zustaendig —
HTTP-Anfragen, Datenbankzugriff und Spring Boot Konfiguration.

Enthaelt:
- Controllers: IngredientController, RecipeController, WarehouseController
- DTOs: IngredientDTO, RecipeDTO, WarehouseEntryDTO
- Spring Boot Hauptklasse: AseProject2026Application
- Globaler Exception Handler: GlobalExceptionHandler

## Abhaengigkeitsregel

Die Abhaengigkeiten zeigen immer von aussen nach innen:
Technical --> Application --> Domain

Der Domain Layer kennt weder den Application Layer noch den Technical Layer.
Der Application Layer kennt den Domain Layer aber nicht den Technical Layer.
Der Technical Layer kennt beide inneren Schichten.

Dies wird durch die Maven-Modulstruktur erzwungen:

```xml
<!-- application/pom.xml -->
<dependency>
    <groupId>com.example</groupId>
    <artifactId>domain</artifactId>
</dependency>

<!-- technical/pom.xml -->
<dependency>
    <groupId>com.example.application</groupId>
    <artifactId>application</artifactId>
</dependency>
```

## Begruendung der Schichtarchitektur

Die Schichtarchitektur wurde gewaehlt weil:

- **Unabhaengigkeit der Domaenenlogik** - Die Geschaeftslogik im Domain Layer ist
  vollstaendig unabhaengig von Spring Boot, JPA oder anderen Frameworks.
  Sie koennte theoretisch mit einer anderen Infrastruktur verwendet werden.

- **Testbarkeit** - Jede Schicht kann unabhaengig getestet werden.
  Domain Layer Tests benoetigen keine Datenbank oder Spring Context.
  Application Layer Tests verwenden Mocks fuer die Repositories.

- **Wartbarkeit** - Aenderungen in der Technical Layer (z.B. ein Controller)
  beeinflussen den Domain Layer nicht. Aenderungen in der Datenbankstruktur
  beeinflussen die Geschaeftslogik nicht.

- **Single Responsibility** - Jede Schicht hat eine klar definierte Aufgabe:
  Domain Layer definiert was die Anwendung tut,
  Application Layer orchestriert wie es getan wird,
  Technical Layer bestimmt womit es getan wird.