# Aufbau der PDF

## xref - Referenzen

Am Besten beginnt man mit dem Lesen des pdf ganz unten, weil da die Referenzen und der Einstiegspunkt in das pdf
spezifiziert sind.

Root - Verweis auf den Einstiegspunkt in das PDF

```
/Root 1 0 R
```

ID - Keine Ahnung

```
/ID [<7F85FB47E7D61823F0914F576E0E8705> <7F85FB47E7D61823F0914F576E0E8705>]
```

Size - Anzahl der Objekte

```
/Size 4
```

## Header

Ganz oben steht die PDF Version, gefolgt von einer Zeile mit ein paar kryptischen Zeichen

**Beispiel:**

```
%PDF-1.4
%����
```

## obj - Objekte

Die PDFs enthalten n "Objects", die einen verschiedenen "Type" haben können

**Beispiel:**

```
1 0 obj
<<
/Type /Catalog
/Version /1.4
/Pages 2 0 R
> >
endobj
```

## /Type - Verschiedene Typen

### Catalog - Root von pdf

Root Element der PDF.

- Version -> PDF Version
- Pages -> Seiten von dem PDF

In der Regel wird auf andere Objekte referenziert anstatt. Beispielsweise bei Pages wird auf das Objekt 2 0
referenziert.

```
/Pages 2 0 R
```

### Pages - Überblick über alle Seiten im PDF

Kids - Referenz zu den jeweiligen Seiten

```
/Kids [3 0 R]
```

Count - Anzahl der Seiten

```
/Count 1
```

### Page - Info zu einer einzelnen Seite

MediaBox - Ändert sich hoffentlich nicht mehr. Irgendwie die Seitengröße und dann doch nicht. Google for more.

```
/MediaBox [0.0 0.0 612.0 792.0]
```

Parent - Referenz zu den Pages

```
/Parent 1
```


