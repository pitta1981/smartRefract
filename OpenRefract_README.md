# OpenRefract Format (.orefract)

## Panoramica

Il formato OpenRefract è un nuovo formato basato su JSON per i progetti SmartRefract. Questo formato sostituisce il precedente formato binario con un approccio più moderno, leggibile e facilmente estensibile.

## Caratteristiche

- **Formato JSON**: Facilmente leggibile e modificabile
- **Estensione .orefract**: Chiara identificazione del formato
- **Compatibilità**: Supporta tutti i dati dei progetti SmartRefract esistenti
- **Compressione tracce**: I dati delle tracce sono codificati in Base64 per efficienza
- **Versioning**: Sistema di versioni per compatibilità futura
- **Metadati**: Include informazioni di creazione e modifica

## Struttura del File

Il file .orefract contiene:

```json
{
  "version": "1.0",
  "formatName": "OpenRefract",
  "createdDate": "2025-06-30 12:00:00",
  "modifiedDate": "2025-06-30 12:00:00",
  "description": "SmartRefract seismic refraction project",
  
  "displaySettings": {
    "isWhite": false,
    "proporz": false,
    "selectedTab": 0
  },
  
  "traceIndex": 0,
  "format": 1,
  
  "investigation": {
    "index": 0,
    "xy": 0.0,
    "xy2": 0.0,
    "licenza": true,
    "max3": 0,
    "maxR3": 0
  },
  
  "traceGroups": [
    {
      "channelCount": 24,
      "spacing": 5.0,
      "spacingIn": 0.0,
      "shotLocation": 60.0,
      "filePath": "/path/to/seismic/data.sgy",
      
      "firstBreaks": [
        {
          "channel": 0,
          "time": 0.123,
          "posX": 0.0,
          "enabled": true
        }
      ],
      
      "traces": [
        {
          "number": 0,
          "length": 1000,
          "sampleInterval": 0.001,
          "valueData": "base64_encoded_trace_data"
        }
      ]
    }
  ]
}
```

## Utilizzo

### Salvataggio

Il nuovo formato è automaticamente supportato dall'interfaccia di salvataggio di SmartRefract:

1. Apri il menu "File" > "Salva Progetto"
2. Seleziona il filtro "OpenRefract format (*.orefract)"
3. Inserisci il nome del file (l'estensione .orefract verrà aggiunta automaticamente)
4. Clicca "Salva"

### Caricamento

Per caricare un progetto OpenRefract:

1. Apri il menu "File" > "Apri Progetto"
2. Seleziona il filtro "OpenRefract format (*.orefract)"
3. Seleziona il file .orefract desiderato
4. Clicca "Apri"

### Programmazione

Per utilizzare il formato programmaticamente:

```java
// Salvataggio
APIObject project = // ... il tuo progetto
File outputFile = new File("progetto.orefract");
OpenRefractWriter.saveOpenRefractProject(outputFile, project);

// Caricamento
File inputFile = new File("progetto.orefract");
APIObject loadedProject = OpenRefractLoader.loadOpenRefractProject(inputFile);
```

## Vantaggi rispetto al formato precedente

1. **Leggibilità**: Il formato JSON è leggibile dall'uomo
2. **Debugging**: Più facile individuare problemi nei file di progetto
3. **Integrazione**: Facilmente utilizzabile con altri strumenti
4. **Estensibilità**: Facile aggiungere nuovi campi senza rompere la compatibilità
5. **Backup**: I file JSON sono più facili da gestire nei sistemi di backup
6. **Controllo versione**: Git e altri VCS gestiscono meglio i file di testo

## Compatibilità

- Il nuovo formato è completamente compatibile con tutti i progetti SmartRefract esistenti
- I file legacy (.txt e .srefract) continuano ad essere supportati
- È possibile convertire progetti esistenti salvandoli nel nuovo formato

## Note Tecniche

- I dati delle tracce sismiche sono codificati in Base64 per ridurre la dimensione del file
- Il sistema di versioning permette l'evoluzione futura del formato
- Tutti i metadati del progetto sono preservati nel nuovo formato
- Le performance di caricamento sono comparabili al formato binario precedente

## Estensioni Future

Il formato OpenRefract è progettato per supportare facilmente:

- Nuovi tipi di analisi sismica
- Metadati aggiuntivi sui progetti
- Integrazione con formati di dati standard (SEG-Y, SU, etc.)
- Supporto per progetti multi-utente
- Annotazioni e commenti sui progetti
