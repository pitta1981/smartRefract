# OpenRefract Format Implementation

## Panoramica

È stato implementato un nuovo sistema completo di caricamento e salvataggio per i progetti SmartRefract utilizzando il formato JSON `.orefract` (OpenRefract format). Questa implementazione risolve i problemi di compatibilità del formato precedente e fornisce una soluzione robusta e moderna.

## File Implementati

### Core Components

1. **`SimpleJsonParser.java`** - Parser JSON personalizzato per evitare dipendenze esterne
   - Parsing e serializzazione JSON completa
   - Gestione di tutti i tipi di dati del progetto
   - Supporto per codifica Base64 delle tracce

2. **`OpenRefractProject.java`** - Classi dati per il formato OpenRefract
   - `OpenRefractProject` - Classe principale del progetto
   - `OpenRefractTraceGroup` - Equivalente di FirstBrakeList
   - `OpenRefractFirstBreak` - Equivalente di FirstBrake
   - `OpenRefractTrace` - Dati delle tracce sismiche
   - Altre classi di supporto

3. **`OpenRefractWriter.java`** - Writer per il formato OpenRefract
   - Conversione da APIObject a formato JSON
   - Supporto completo per tutti i dati del progetto
   - Gestione di tracce, first breaks, e dromocrone

4. **`OpenRefractLoader.java`** - Loader per il formato OpenRefract
   - Conversione da formato JSON ad APIObject
   - Ricostruzione completa della struttura del progetto
   - Gestione robusta degli errori

5. **`OpenRefractTest.java`** - Classe di test per il nuovo formato
   - Test di salvataggio e caricamento
   - Verifica dell'integrità dei dati
   - Esempi di utilizzo

## Integrazione nell'Interfaccia Utente

### File di Salvataggio Aggiornati

1. **`SaveProject.java`** - Menu "Salva Progetto"
   - Supporto per formato .orefract come predefinito
   - Fallback compatibile per formati legacy
   - File chooser aggiornato con filtri appropriati

2. **`saveProjectAs.java`** - Menu "Salva Progetto Come"
   - Supporto completo per formato OpenRefract
   - Interfaccia unificata per tutti i formati
   - Mantenimento della compatibilità backwards

### File di Caricamento

1. **`OpenPrj.java`** - Menu "Apri Progetto"
   - Supporto per formato .orefract già presente
   - Caricamento automatico basato sull'estensione file
   - Gestione robusta degli errori con fallback

## Caratteristiche del Formato OpenRefract

### Vantaggi
- **Leggibilità**: Formato JSON leggibile dall'uomo
- **Robustezza**: Gestione degli errori migliorata
- **Estensibilità**: Facile aggiungere nuovi campi
- **Compatibilità**: Supporta tutti i dati esistenti
- **Efficienza**: Compressione Base64 per le tracce
- **Versioning**: Sistema di versioni per evoluzione futura

### Struttura del File
```json
{
  "version": "1.0",
  "formatName": "OpenRefract",
  "createdDate": "2025-06-30 12:00:00",
  "description": "SmartRefract seismic refraction project",
  "traceGroups": [
    {
      "channelCount": 24,
      "spacing": 5.0,
      "shotLocation": 60.0,
      "firstBreaks": [...],
      "traces": [...],
      "dromo": [...]
    }
  ]
}
```

## Utilizzo

### Salvataggio Programmatico
```java
APIObject project = // ... il tuo progetto
File outputFile = new File("progetto.orefract");
OpenRefractWriter.saveOpenRefractProject(outputFile, project);
```

### Caricamento Programmatico
```java
File inputFile = new File("progetto.orefract");
APIObject loadedProject = OpenRefractLoader.loadOpenRefractProject(inputFile);
```

### Nell'Interfaccia Utente
1. **Salvataggio**: Menu File → Salva Progetto → Seleziona formato "OpenRefract (*.orefract)"
2. **Caricamento**: Menu File → Apri Progetto → Seleziona file .orefract

## Compatibilità

- **Formato legacy (.txt)**: Completamente supportato per caricamento e salvataggio
- **Formato binario (.srefract)**: Supportato per caricamento
- **Nuovo formato (.orefract)**: Formato predefinito raccomandato

## Test

Eseguire `OpenRefractTest.java` per verificare il funzionamento:
```java
OpenRefractTest.testOpenRefractFormat();
```

## Note di Implementazione

1. **Parser JSON Personalizzato**: Evita dipendenze esterne mantenendo leggerezza
2. **Gestione Tracce**: Le tracce sismiche sono codificate in Base64 per efficienza
3. **Backward Compatibility**: Tutti i formati esistenti continuano a funzionare
4. **Error Handling**: Gestione robusta degli errori con messaggi informativi
5. **Memory Efficiency**: Parsing streaming per file di grandi dimensioni

## Risoluzione dei Problemi

### Problemi Comuni
- **File corrotto**: Il parser JSON fornisce messaggi di errore dettagliati
- **Versione non supportata**: Il sistema gestisce automaticamente versioni diverse
- **Dati mancanti**: Valori predefiniti per campi non presenti

### Log e Debug
- Messaggi informativi durante caricamento/salvataggio
- Stack trace dettagliati in caso di errori
- Verifica integrità dati

## Conclusioni

L'implementazione del formato OpenRefract fornisce una soluzione completa e robusta per i problemi di salvataggio e caricamento dei progetti SmartRefract. Il nuovo formato è progettato per essere estensibile, affidabile e compatibile con tutti i dati esistenti, garantendo al contempo prestazioni ottimali e facilità d'uso.
