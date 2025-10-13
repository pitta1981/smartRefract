# Editor Geometria Stendimento - Guida Utente

## Panoramica

Il nuovo Editor Geometria Stendimento è un'interfaccia moderna e intuitiva per gestire la configurazione dei geofoni e delle energizzazioni (shot) nel progetto di rifrazione sismica.

## Caratteristiche Principali

### 1. **Interfaccia a Tabelle**
- **Tabella Geofoni**: Visualizza e modifica la posizione di ciascun geofono
  - Numero canale
  - Posizione assoluta (m)
  - Posizione relativa (m)
  
- **Tabella Energizzazioni**: Gestisce i punti di energizzazione
  - Numero shot
  - Nome file associato
  - Posizione assoluta (m)

### 2. **Anteprima Grafica Real-Time**
- Visualizzazione istantanea della geometria
- **Geofoni**: Rappresentati in **ciano** (triangoli)
- **Shot**: Rappresentati in **rosso** (stelle esplosive)
- Scala metrica con griglia di riferimento
- Etichette e distanze configurabili

### 3. **Import/Export CSV**
Supporto completo per l'importazione e l'esportazione della geometria in formato CSV.

#### Formato File CSV

Il file CSV utilizza il seguente formato:

```csv
# SmartRefract Geometry Export
# Spacing: 2.0m
# Start Position: 0.0m
Type,Number/File,Position(m),Info

# Geophones
0,0.000,0.000,GEOPHONE
1,2.000,2.000,GEOPHONE
2,4.000,4.000,GEOPHONE
...

# Shots
0,shot01.dat,-5.000,Before spread,SHOT
1,shot02.dat,23.000,Center,SHOT
2,shot03.dat,51.000,After spread,SHOT
```

#### Struttura CSV

- **Header**: Righe che iniziano con `#` sono commenti
- **Tipo**: `GEOPHONE` o `SHOT`
- **Geofoni**: `channelNumber,absolutePosition,relativePosition,GEOPHONE`
- **Shot**: `shotNumber,fileName,absolutePosition,description,SHOT`

### 4. **Validazione Automatica**
- Controllo della validità delle posizioni
- Verifica della consistenza dei dati
- Messaggi di errore e avviso chiari

## Come Utilizzare l'Editor

### Apertura dell'Editor

1. Aprire un progetto in SmartRefract
2. Selezionare il menu **File > Set Geometry** (o la scorciatoia corrispondente)
3. Si aprirà il dialogo "Editor Geometria Stendimento"

### Impostazione Parametri Base

Nel pannello superiore, configurare:

- **Spaziatura geofoni (m)**: Distanza tra geofoni consecutivi (es: 2.0)
- **Posizione iniziale (m)**: Posizione del primo geofono (es: 0.0)
- **Numero geofoni**: Quantità di geofoni nello stendimento (es: 24)

Premere **Applica** per aggiornare la tabella geofoni.

### Generazione Automatica Geofoni

1. Impostare i parametri base
2. Cliccare **Auto-genera** nella sezione geofoni
3. La tabella verrà popolata automaticamente con le posizioni calcolate

### Modifica Manuale

#### Geofoni
- Cliccare sulla cella "Posizione (m)" per modificarla
- Premere INVIO per confermare
- L'anteprima si aggiorna automaticamente

#### Shot
- Cliccare **Aggiungi** per creare un nuovo shot
- Inserire il nome file e la posizione
- Modificare la posizione direttamente nella tabella
- Selezionare e cliccare **Rimuovi** per eliminare uno shot

### Import da CSV

1. Cliccare **Importa CSV**
2. Selezionare il file CSV
3. Il sistema validerà il file e mostrerà eventuali errori/avvisi
4. Confermare l'importazione
5. Geofoni e shot verranno caricati automaticamente

### Export in CSV

1. Configurare la geometria desiderata
2. Cliccare **Esporta CSV**
3. Scegliere la destinazione e il nome del file
4. Il file verrà salvato con tutti i dati della geometria

### Anteprima Grafica

L'anteprima in tempo reale mostra:

- **Geofoni (ciano)**: Triangoli lungo la linea base
- **Shot (rosso)**: Stelle esplosive
- **Scala metrica**: Con divisioni ogni 5 metri
- **Etichette**: Numeri di canale e shot

Opzioni visualizzazione:
- ☑ **Mostra griglia**: Linee verticali di riferimento
- ☑ **Mostra etichette**: Numeri identificativi
- ☑ **Mostra distanze**: Informazioni riepilogative

### Conferma e Applicazione

1. Verificare la geometria nell'anteprima
2. Cliccare **OK** (verde) per applicare le modifiche al progetto
3. Cliccare **Annulla** (rosso) per chiudere senza salvare

## Esempi Pratici

### Esempio 1: Stendimento Standard

- **Geofoni**: 24 canali, spaziatura 2m, inizio a 0m
- **Shot**: 
  - Shot 1: -5m (prima dello stendimento)
  - Shot 2: 23m (centro stendimento)
  - Shot 3: 51m (dopo lo stendimento)

### Esempio 2: Geometria Irregolare

Per geometrie non uniformi, importare un CSV personalizzato con posizioni specifiche per ogni geofono.

### Esempio 3: Multiple Energizzazioni

Aggiungere tutti gli shot necessari con la funzione "Aggiungi", specificando file e posizione per ciascuno.

## Risoluzione Problemi

### "Nessuna geometria definita"

- Verificare che ci siano almeno un geofono e uno shot
- Utilizzare **Auto-genera** per creare i geofoni

### "Errore durante l'importazione CSV"

- Verificare che il file rispetti il formato
- Controllare che non ci siano caratteri speciali
- Verificare che le posizioni siano numeri validi

### "Geometria non valida"

- Assicurarsi che tutte le posizioni siano definite
- Verificare che non ci siano valori NaN o infiniti
- Controllare che i numeri di canale/shot siano sequenziali

## Note Tecniche

- **Encoding**: Il CSV usa UTF-8
- **Separatore**: Virgola (,)
- **Decimali**: Punto (.)
- **Precisione**: 3 decimali per le posizioni

## File di Esempio

Un file CSV di esempio può essere generato automaticamente tramite:
```java
GeometryCSVHandler.createExampleCSV(new File("example_geometry.csv"));
```

## Supporto

Per ulteriori informazioni o assistenza, consultare la documentazione completa di SmartRefract o contattare il supporto tecnico.

---

**SmartRefract** - Sistema avanzato per l'interpretazione di dati di rifrazione sismica
