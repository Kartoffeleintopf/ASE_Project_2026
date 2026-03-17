# Advanced Software Engineering Project 2026

## Lager und Rezeptverwaltung
Die Anwendung dient der Lager- und Rezepturverwaltung. Nutzer können Zutaten sowie Rezepte
erstellen, bearbeiten und löschen. Rezepte bestehen aus einer oder mehreren Zutaten, welche
jeweils in einer bestimmten Menge benötigt werden.

Alle Zutaten und Rezepte werden persistent in einer Datenbank gespeichert. Die Ergebnisse eines
Rezepts können wiederum als Zutat in anderen Rezepten verwendet werden, wodurch mehrstufige
Produktionsketten möglich sind.

Jede Zutat besitzt einen aktuellen Lagerbestand, der angibt, wie viele Einheiten dieser Zutat
verfügbar sind. Rezepte können „produziert“ werden, wobei die benötigten Zutaten aus dem
Lagerbestand entfernt und das Ergebnis des Rezepts dem Lager hinzugefügt wird. Der Lagerbestand
aller Zutaten kann jederzeit eingesehen werden, optional gefiltert nach Zutaten mit einem Bestand
größer als null.

Zutaten verfügen über verschiedene Attribute wie Name und ein Bild.
Zutaten, die nicht das Ergebnis eines Rezepts sind, werden als Grundzutaten bezeichnet. Bei der
Erstellung einer Zutat kann festgelegt werden, ob es sich um eine Grundzutat oder um ein durch ein
Rezept herstellbares Produkt handelt.

## Feedback
Das Thema hat die Freigabe erhalten!
Eine Lager- & Rezeptverwaltung mit Produktionsfunktion und mehrstufigen Produktionsketten bietet sehr viel fachliche Tiefe und eignet sich hervorragend für DDD, Clean Architecture sowie Tests und Entwurfsmuster.

### Empfehlungen für die Umsetzung:
- Fachlichen Regeln/Invarianten vor Beginn der Implementierung klar zu definieren
  - Bestände dürfen nicht negativ werden (Produktion nur, wenn ausreichend vorhanden ist).
  - „Produzierbar“-Zutat braucht genau ein Rezept; Grundzutaten haben kein Rezept.
  - Abhängigkeiten beim Löschen/Ändern: Ein Rezept/Zutat darf nicht entfernt werden, wenn es noch referenziert wird (oder du definierst eine klare Migrationsregel).
  - Produktionsketten: Keine Zyklen (ein Produkt darf nicht indirekt wieder zu sich selbst führen), sonst gibt es Endlosschleifen.

### Konkretisierung nötig
- „Rezept produzieren“ ist der fachliche Kern
  - Wird immer genau 1 Einheit produziert oder eine frei wählbare Menge?
  - Was passiert bei Teilmengen / Rundungen?

- Transaktionslogik: Zutaten-Abzug und Ergebnis-Zugang sollten atomar sein (alles oder nichts).

- Das Bild-Attribut ist ok, aber ich würde Dir empfehlen, es eher schlank zu halten (z. B. URL/Dateipfad), damit der Fokus auf der Domänenlogik bleibt.
--> Bild wird dummy Bildlink enthalten

- Bitte lege die Datenbank zeitnah fest (z. B. PostgreSQL oder SQLite). „tbd“ ist nicht schlimm, aber du solltest früh eine Entscheidung treffen, um Modellierung und Persistenz konsistent umzusetzen.
--> MariaDB, JPA

## ToDO
- Unit Tests
- the entire logic
- everything really

## temporary Documentation
- run the project using Maven by typing the below command while located in the base folder
```
./mvnw spring-boot:run
```
due to the refactor work currently going on this doesn't work

## MariaDB Setup
Enthält die Schritte die getätigt wurden um die Datenbank einzurichten
### MariaDB als root User starten
```
sudo mariadb -u root
```
### Datenbank anlegen
```
Create DATABASE ase_db
```
### Nutzer für die Datenbank anlegen
```
CREATE USER 'allpowerful'@'localhost' IDENTIFIED BY '';
GRANT ALL PRIVILEGES ON ase_db.* TO 'allpowerful'@'localhost';
FLUSH PRIVILEGES;
```
### Nutzerdetails in application.properties eintragen
