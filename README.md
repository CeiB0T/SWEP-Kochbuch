# SWEP-Kochbuch
## Beschreibung
Das SWEP-Kochbuch ist im Rahmen eines Universitätsprojektes entstanden. Wir alle haben Interesse am Kochen, können es aber nicht alle gleich gut,
wir wollten also ein digitales Kochbuch erstellen, bei dem man als Autor auch die weniger erfahrenen Köche der WG oder Familie erreichen kann.
Es handelt sich um ein Kochbuch als Java-Desktopanwendung in welchem Rezepte angelgt und kategorisiert werden können.

- Zutaten werden seperat gespeichert und können für verschiedene Rezepte verwendet werden.
- Zu den Rezepten werden QR-Codes mit den benötigten Zutaten in der angegebenen Menge erstellt um sie für die Einkaufsliste auf das Handy zu übernehen.
- Ein weiteres Detail unseres Kochbuchs ist die Möglichkeit ein Definitionsbuch für Zubereitungsmethoden anzulegen (falls man sich notieren möchte, was eigentlich *unterheben* bedeutet😀)

## Genutze Technologien
- Java
- JavaFX (mit Sceenbuilder) [Link zu Java FX] (https://openjfx.io/)
- JSON-Dateivormat (Googels GSON) [Link zum Repository] (https://github.com/google/gson)


## Herausforderungen
- Eine besondere Herausforderung für uns war die Vermittlung zwischen dem UI und der Datenhaltung.
- Die Übertragung von Daten zwischen Verschiedenen UI-Stages läuft aktuell über statische Variablen, wir wären interssiert an Vlternativen zu dieser Lösung.

### Ideen, die wir bisher nicht umgesetzt haben
- [ ] Definitionen direkt mit dem Rezept verknüpfen und (zum Beispiel als Tooltip beim hovern) direkt bei der Zubereitungmethode im Rezept anzeigen.
- [ ] Für die QR-Code Mengen anhand der Personenanzahl dynamisch ampassen.
- [ ] Suchen für die Zutaten erstellen.

--- 
## Installieren und Starten

### Systemvoraussetzungen

---
## Benutzung

### Startseite
Von der Startseite können folgende Aktionen ausgeführt werden:
1. Definitionsbuch-Menü öffnen -> ==Klick auf Button *Definitionsbuch*==
2. Kategorien-Menü öffnen -> ==Klick auf Button *Kategorie*==
3. Menü "Neues Rezept erstellen" öffnen ==Klick auf Button *neues Rezept*==
4. Anwendung schließen -> ==Klick auf Button *Verlassen* oder wie gewohnt über das "Kreutzchen"==
5. Bestehendes Rezept öffnen
6. Rezepte Suchen

#### Bestehendes Rezept öffnen
Möglichkeit 1
1. Gesuchtes Rezept in Liste *Rezepte* auswählen
2. Mit Doppelklick Detailansicht öffnen
Möglickkeit 2
- Mit Doppelklick aus der Suche öffnen





