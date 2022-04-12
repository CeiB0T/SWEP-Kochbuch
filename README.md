# SWEP-Kochbuch
## Beschreibung
Das SWEP-Kochbuch ist im Rahmen eines Universitätsprojektes entstanden. Wir alle haben Interesse am Kochen, können es aber nicht alle gleich gut,
wir wollten also ein digitales Kochbuch erstellen, bei dem man als Autor auch die weniger erfahrenen Köche der WG oder Familie erreichen kann.
Unser Kochbuch ist eine Java-Desktopanwendung in welcher Rezepte gelesen, angelegt und kategorisiert werden können.

- [x] Zutaten werden gespeichert und können für verschiedene Rezepte verwendet werden.
- [x] Zu den Rezepten werden QR-Codes mit den benötigten Zutaten in der angegebenen Menge erstellt um sie für die Einkaufsliste auf das Handy zu übernehmen.
- [x] Ein weiteres Detail unseres Kochbuchs ist die Möglichkeit ein Definitionsbuch für Zubereitungsmethoden anzulegen (falls man sich notieren möchte, was eigentlich *unterheben* bedeutet😀)

## Genutze Technologien
- Java
- JavaFX (mit Sceenbuilder) [Link zu Java FX] (https://openjfx.io/)
- JSON-Dateiformat (Google's GSON) [Link zum Repository] (https://github.com/google/gson)


## Herausforderungen
- Eine besondere Herausforderung für uns war die Vermittlung zwischen dem UI und der Datenhaltung.
- Die Übertragung von Daten zwischen Verschiedenen UI-Stages läuft aktuell über statische Variablen, wir wären interssiert an Alternativen zu dieser Lösung.

### Ideen, die wir bisher nicht umgesetzt haben
- [ ] Definitionen direkt mit dem Rezept verknüpfen und (zum Beispiel als Tooltip beim hovern) direkt bei der Zubereitungsmethode im Rezept anzeigen.
- [ ] Dynamisches Anpassen der Zutatenmengen innerhalb des QR-Codes anhand angegebener Personenzahl.
- [ ] Suche für die Zutaten erstellen.

--- 
## Installieren und Starten

### Systemvoraussetzungen
- Java >= 11

---
## Benutzung

### Startseite
Von der Startseite können folgende Aktionen ausgeführt werden:
1. Definitionsbuch-Menü öffnen -> ==Klick auf Button *Definitionsbuch*==
2. Kategorien-Menü öffnen -> ==Klick auf Button *Kategorie*==
3. Neues Rezept erstellen ==Klick auf Button *neues Rezept*==
4. Anwendung schließen -> ==Klick auf Button *Verlassen* oder wie gewohnt über das "Kreutzchen"==
5. Bestehendes Rezept öffnen
6. Rezepte Suchen

#### Bestehendes Rezept öffnen
Möglichkeit 1
1. Gesuchtes Rezept in Liste *Rezepte* auswählen
2. Mit Doppelklick Detailansicht öffnen

Möglickkeit 2
- Mit Doppelklick aus der Suche öffnen

#### Rezept suchen
Rezepte können auf drei verschiende Arten gefunden werden

1. Suche über Titel -> Eingabe des Rezepttitels (oder Teilen davon) zeigen den Rezepttitel in der Suche an
2. Suche über Zutat -> Wird der Name einer Zutat eingegeben, werden alle Rezepte mit dem Präfix *Zutat:* angezeigt, die diese enthalten
3. Suche über Kategorie -> Wird der Name einer Kategorie eingegeben, werden alle Rezepte angezeigt mit dem Präfix *Kategorie:*, die in dieser enthalten sind.

### Definitionsbuch
Im Definitionsbuch können Definitionen hinzugefügt, angesehen, geändert oder gelöscht werden

#### Definition hinzufügen
1. Klick auf *Definition Hinzufügen*
2. Die Felder Definitionstitel und das Feld für den Inhalt darunter sind nun editierar und können gefüllt werden.
3. *Speichern* klicken nicht vergessen. (Um eine Defintion abspeichern zu können, muss der Titel mindestens ein Zeichen (kein Leerzeichen) enthalten. Leerzeichen an Anfang und Ende werden entfernt)

#### Definition anzeigen
- Um eine Definition anzuzeigen, wird in der Liste auf das entsprechende Wort geklickt.

#### Definition bearbeiten
0. Um eine Definition zu ändern, muss diese zunächst angezeigt werden.
1. Klick auf *Bearbeiten* > Name und Inhalt der Definition können nun in den Feldern editiert werden.
2. Um die Definition speichern zu können, muss der Name mindestens 1 Zeichen enthalten, das kein Leerzeichen ist. Leerzeichen an Anfang und Ende werden entfernt
3. Mit Klick auf *Speichern* wird die Änderung übernommen.

#### Definition löschen
1. Zu löschende Definition durch klicken in der Liste auswählen
2. Klick auf *löschen*

### Kategorien
In *Kategorien* können neue Kategorien angelegt oder gelöscht werden. Auch können Rezepte zu diesen hinzugefügt oder aus ihnen entfernt werden. Rezepte können aus Kategorien heraus geöffnet werden.

#### Kategorie anlegen
1. Klick auf *Neue Kategorie*
2. Gewünschten Namen in Feld *Kategoriename* eintragen
3. *Speichern* klicken nicht vergessen. (Um eine Kategorie abspeichern zu können, muss der Titel mindestens ein Zeichen (kein Leerzeichen) enthalten. Leerzeichen an Anfang und Ende werden entfernt)

#### Kategorie löschen
1. Zu löschende Kategorie durch klicken in der Liste auswählen.
2. Button *Löschen* anklicken
3. Löschen im Dialog mit *Ok* bestätigen

#### Kategorie bearbeiten (umbenennen)
1. Zu bearbeitende Kategorie durch klicken in der Liste auswählen.
2. Klick auf *Bearbeiten*
3. Name in Textfeld wie gewünscht anpassen
4. Klick auf *Speichern* (Um eine Kategorie abspeichern zu können, muss der Titel mindestens ein Zeichen (kein Leerzeichen) enthalten. Leerzeichen an Anfang und Ende werden entfernt)

#### Rezept in Kategorie einfügen
1. Gewünschte Kategorie in der Liste anklicken
2. Gewünschtes Rezept in *Alle Rezepte* Liste anklicken
3. Klick auf *Zu Kategorie hinzufügen*

#### Rezept aus Kategorie entfernen
1. Klick auf das zu entfernende Rezept in der *Zugehörige Rezepte*-Liste
2. Klick auf *Löschen* (Das Rezept bleibt gespeichert, es wird nur aus der Kategorie entfernt)
- Der *Löschen* Button ist der selbe, der auch zum Löschen der ganzen Kategorie verwendet wird.

#### Rezept aus Kategorie heraus öffnen
1. Klick auf die gewünschte Kategorie in der Kategorienliste
2. Alle Rezepte dieser Kategorie erscheinen in der *zugehörige Rezepte*-Liste
3. Das Rezept kann von hier aus mit Doppelklick geöffnet werden.

### Neues Rezept anlegen
Nach dem Klick auf *Neues Rezept* können Titel, die empfohlene Personenanzahl und die Zubereitung angegeben werden.
- Zutaten können erst nach dem *speichern* hinzugefügt werden. (Wie das genau funktioniert wird weiter unten erklärt.)
- Um ein Rezept abzuspeichern muss sein Titel aus mindestens einem Zecihen (kein Leerzeichen) bestehen. Leerzeichen an anfang und Ende werden entfernt.

### vorhandenes Rezept bearbeiten
0. Um ein Rezept zu bearbeiten muss es zunächst geöffnet werden, dies ist über verschiedene, bereits beschriebene Wege möglich.
1. Klick auf *Bearbeiten*
2. Alle Textfelder können nun nach Wunsch verändert werden.
3. Klick auf *Speichern* nicht vergessen um die Änderung zu übernehmen. (Die obengenannten Regelungen für den Titel gelten auch hier)
4. Das Hinzufügen oder entfernen von Zutaten wird weiter unten erklärt.

### vorhandenes Rezept löschen
0. Rezept öffnen
1. Klick auf den Button *löschen*
2. Löschen bestätigen

### Zutat hinzufügen
0. Zutaten können nur bereits abgespeicherten Rezepten hinzugefügt werden. Das Rezept muss hierzu geöffnet sein.
1. Auf der Rezeptseite den Button *Neue Zutat* anklicken
2. Zutat entweder aus der Liste der bereits vorhandenen Zutaten mit einem Klick auswählen oder über *Zutat Hinzufügen* neu erstellen (Titel mind. 1 Zeichen (kein Leerzeichen), Leerzeichen an Anfang und Ende werden entfernt)
-  Wird eine Zutat aus der Liste ausgewählt muss nun *Bearbeiten* geklickt werden
3. Die Felder Menge und Einheit passend füllen (Achtung: Eine Menge muss eine Dezimalzahl (im Alltag üblicherweise verwendete Zahlen) sein. Anstatt eines Kommas muss ein Punkt geschrieben werden)
4. Auf *Speichern* Klicken. Die Zutat ist mit der entsprechenden Menge und Einheit dem Rezept hinzugefügt und erscheint in dessen Zutatenliste. Wurde die Zutat neu erstellt, ist sie nun auch in der Liste aller Zutaten zu finden. Es können nun weitere Zutaten auf die selbe Art hinzugefügt werden. 
5. Über den Button *Letztes Rezept* kann zu dem Rezept, das man gerade bearbeitet, zurückgekehrt werden.

### Zutat aus Rezept entfernen
0. Das entsprechende Rezept muss hierzu geöffnet sein.
1. Klick auf die zu entfernende Zutat in der *Rezeptzutaten*-Liste
2. Klick auf *Löschen* (selber Button der auch zum Löschen des ganzen Rezeptes verwendet wird)

### Zutat löschen
Achtung: Eine Zutat kann nur gelöscht werden, wenn sie zuvor aus allen Rezepten entfernt wird in denen sie verwendet wird.
(Welche Rezepte das sind, wird beim Versuch sie zu löschen in der Fehlermeldung angezeigt)

0. Zutaten-Menü über Klick auf *Neue Zutat* bei einem vorhandenen Rezept öffnen
1. Zutat in der Liste auswählen (anklicken)
2. Klick auf *Löschen*
3. Wenn die Zutat in Verwendung ist, aus allen Rezepten entfernen und *Löschen* anschließend nochmal versuchen.






