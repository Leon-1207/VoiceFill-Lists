# Aufbau der Daten

Die Daten werden in **Listen, Listenseiten, Spalten, Kategorien** und **Werten** organisiert.

## Listen

**Beispiel** : "Essensabfrage", "Brandschutzliste"

Von einer Liste können beliebig viele Seiten generiert werden. Jede Liste hat einen Namen und mehrere Spalten. Eine der Spalten ist, stellt den Primärschlüssel dar. Es muss einstellbar sein, welche Spalte als Primärschlüssel dient. Über den Primärschlüssel kann zum Beispiel ein Eintrag per Voice Command entfernt werden → "Eintrag entfernen _Max Mustermann_". Die Reihenfolge der Spalten bestimmt den Aufbau der Voice Commands (z.B. Name, Wochentag, Gericht). Zusätzlich wird noch eine eindeutige ID geniert und gespeichert.

**Classname:** DataListTemplate

## Listenseite

**Beispiel** : "Essensabfrage KW20"

Jede Liste kann beliebig oft geniert und ausgefüllt werden. Das bezeichnen wir dann als Listenseite. Eine Datei hat also einen Namen, eine Liste und beliebig viele Einträge (diese werden als Zeilen dargestellt). Zusätzlich wird noch eine eindeutige ID geniert und gespeichert.

**Classname:** DataListPage

## Spalten

Eine Spalte gehört zu je einer Liste. Sie gibt an, welche Werte eingetragen werden können. Am Anfang werden wir nur den Spaltentyp "Wert aus Kategorie" einbauen. Später wäre es aber auch denkbar, Spaltentypen wie "Datum" oder "Uhrzeit" einzubauen. Damit könnte der User weitere Arten von Listen ausfüllen.

**Classname:** DataListColumn

## Wertekategorie

**Beispiel:**"Namen Gruppe A", "Namen Gruppe B", "Gerichte" oder "Wochentage"

Eine Wertekategorie ist eine Sammlung von mehreren Werten. Eine Wertekategorie kann in mehreren Listen gleichzeitig vertreten sein. So kann zum Beispiel die Wertekategorie "Gerichte" sowohl in der Liste "Essensabfrage Gruppe A" als auch "Essensabfrage Gruppe B" vorkommen. Zusätzlich wird noch eine eindeutige ID geniert und gespeichert.

**Classname:** DataListValueGroup

## Kategorie-Wert

**Beispiel:**"Montag", "Max Mustermann"

Ein angelegter Wert hat einen Namen und mehrere Strings, die angeben, wie der Wert ausgesprochen wird (dadurch können auch ausländische Namen erkannt werden). Es werden mehrere Strings abgespeichert, um Abweichungen bei der Aussprache besser tolerieren zu können. Die Strings enthalten die phonetische Repräsentation der Aussprache. Ein Kategorie-Wert kann in mehreren Wertekategorien vorkommen. So kann "Max Mustermann" sowohl in der Wertekategorie "Namen Gruppe A" als auch "Mitarbeiternamen" vorkommen. Zusätzlich wird noch eine eindeutige ID geniert und gespeichert.

**Classname:** ValueGroupEntry
