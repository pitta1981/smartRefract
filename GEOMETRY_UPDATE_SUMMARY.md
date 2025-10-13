# Geometry Editor Updates - English Translation & Elevation Support

## Summary
Updated the geometry editor system to:
1. Translate all user interface labels from Italian to English
2. Replace "relative position" field with "elevation" field in data models
3. Move geophone labels above the profile line in the preview panel
4. Display elevation values in the graphical preview

## Modified Files

### 1. GeophonePosition.java
**Changes:**
- Replaced `relativePosition` field with `elevation` field
- Updated constructors to accept elevation parameter
- Modified `toCSV()` to export elevation: `channelNumber,absolutePosition,elevation,GEOPHONE`
- Modified `fromCSV()` to import elevation from CSV
- Updated `toString()` to display elevation instead of relative position
- Translated all comments to English

### 2. ShotPosition.java
**Changes:**
- Added `elevation` field (previously only had position and filename)
- Updated constructors to accept elevation parameter
- Modified `toCSV()` to export elevation: `shotNumber,fileName,absolutePosition,elevation,description,SHOT`
- Modified `fromCSV()` to import elevation from CSV
- Updated `toString()` to display elevation
- Translated all comments to English

### 3. GeometryEditorDialog.java
**Changes:**
- Translated dialog title: "Editor Geometria Stendimento" → "Spread Geometry Editor"
- Translated all panel titles and labels:
  - "Parametri Stendimento" → "Spread Parameters"
  - "Spaziatura geofoni (m)" → "Geophone spacing (m)"
  - "Posizione iniziale (m)" → "Start position (m)"
  - "Numero geofoni" → "Number of geophones"
  - "Geofoni" → "Geophones"
  - "Energizzazioni (Shot)" → "Shots"
  - "Anteprima Geometria" → "Geometry Preview"
  - And many more...
- Updated GeophoneTableModel columns: `{"Canale", "Posizione (m)", "Relativa (m)"}` → `{"Channel", "Position (m)", "Elevation (m)"}`
- Updated ShotTableModel: Added elevation column `{"Shot", "File", "Position (m)", "Elevation (m)"}`
- Made both position AND elevation editable in tables
- Updated button labels: "Auto-genera" → "Auto-generate", "Aggiungi" → "Add", "Rimuovi" → "Remove", etc.
- Translated all JOptionPane messages to English
- Updated validation messages

### 4. GeometryPreviewPanel.java
**Changes:**
- **CRITICAL**: Moved geophone channel labels from `baselineY + 40` (below profile) to `baselineY - 20` (above profile)
- Added elevation display for geophones: if elevation is non-zero, displays `"%.1fm"` label at `baselineY - 30`
- Added elevation display for shots: if elevation is non-zero, displays `"%.1fm"` label at `baselineY + 52`
- Translated empty message: "Nessuna geometria definita" → "No geometry defined"
- Translated code comments to English

### 5. CSV Files
**Updated:**
- `example_geometry.csv` - Updated format to include elevation column (all values 0.0)
- **NEW FILE**: `example_geometry_with_elevations.csv` - Example with realistic elevation values (100m-108m range)

## CSV Format Changes

### Old Format
```csv
Type,Number/File,Position(m),Info
0,0.000,0.000,GEOPHONE
0,shot01.dat,-5.000,Shot description,SHOT
```

### New Format
```csv
Type,Number/File,Position(m),Elevation(m),Info
0,0.000,0.0,GEOPHONE
0,shot01.dat,-5.000,0.0,Shot description,SHOT
```

## Visual Changes in Preview

### Before
- Geophone labels (channel numbers) displayed BELOW the profile line at Y position `baselineY + 40`
- No elevation information shown
- Labels overlapping with shot positions

### After
- Geophone labels (channel numbers) displayed ABOVE the profile line at Y position `baselineY - 20`
- Elevation values displayed for non-zero elevations:
  - Geophones: at Y position `baselineY - 30`
  - Shots: at Y position `baselineY + 52`
- Clear separation between geophone and shot labels

## User Interface Translation Map

| Italian (Old) | English (New) |
|--------------|---------------|
| Editor Geometria Stendimento | Spread Geometry Editor |
| Parametri Stendimento | Spread Parameters |
| Spaziatura geofoni (m) | Geophone spacing (m) |
| Posizione iniziale (m) | Start position (m) |
| Numero geofoni | Number of geophones |
| Applica | Apply |
| Geofoni | Geophones |
| Energizzazioni (Shot) | Shots |
| Auto-genera | Auto-generate |
| Aggiungi | Add |
| Rimuovi | Remove |
| Anteprima Geometria | Geometry Preview |
| Mostra griglia | Show grid |
| Mostra etichette | Show labels |
| Mostra distanze | Show distances |
| Importa CSV | Import CSV |
| Esporta CSV | Export CSV |
| Annulla | Cancel |
| Canale | Channel |
| Posizione (m) | Position (m) |
| Relativa (m) | Elevation (m) |
| Errore | Error |
| Validazione | Validation |
| Importazione completata | Import completed |
| Esportazione completata | Export completed |

## Data Model Changes

### GeophonePosition
- **Removed**: `double relativePosition`
- **Added**: `double elevation`
- **Constructor**: Now accepts `(channelNumber, absolutePosition, elevation)`
- **Getters/Setters**: `getElevation()`, `setElevation(double)`

### ShotPosition
- **Added**: `double elevation` (new field)
- **Constructor**: Now accepts `(shotNumber, fileName, absolutePosition, elevation, description)`
- **Getters/Setters**: `getElevation()`, `setElevation(double)`

## Testing Notes

1. **CSV Import**: Test with both old and new format CSV files
2. **Table Editing**: Verify that both position and elevation columns are editable
3. **Preview Display**: Check that:
   - Geophone labels appear above the profile line
   - Elevation values display when non-zero
   - No overlapping between geophone and shot labels
4. **CSV Export**: Verify exported files contain elevation column
5. **Validation**: Ensure geometry validation still works correctly

## Backward Compatibility

**BREAKING CHANGE**: Old CSV files without elevation column will need to be updated or the import parser will need fallback logic to handle missing elevation values (currently defaults to 0.0 if missing).

## Date
2025-01-XX

## Author
smartRefract Team
