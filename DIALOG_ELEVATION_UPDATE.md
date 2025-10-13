# Dialog Elevation Display Update

## Summary
Modified the geometry editor dialog to display elevation values only when they are different from zero (already set by user).

## Changes Made

### 1. GeophoneTableModel.getValueAt()
**Before:**
```java
case 2: return String.format("%.2f", geo.getElevation());
```

**After:**
```java
case 2:
    // Show elevation only if it's different from zero
    return Math.abs(geo.getElevation()) > 0.01 ? String.format("%.2f", geo.getElevation()) : "";
```

### 2. ShotTableModel.getValueAt()
**Before:**
```java
case 3: return String.format("%.2f", shot.getElevation());
```

**After:**
```java
case 3:
    // Show elevation only if it's different from zero
    return Math.abs(shot.getElevation()) > 0.01 ? String.format("%.2f", shot.getElevation()) : "";
```

### 3. autoGenerateGeophones()
**Before:**
```java
GeophonePosition geo = new GeophonePosition(i, absPos, i * spacing);
```

**After:**
```java
// Create geophones with elevation = 0.0 (no elevation data)
GeophonePosition geo = new GeophonePosition(i, absPos, 0.0);
```

## Behavior

### Table Display
- **Elevation = 0.0**: Cell appears empty (shows "")
- **Elevation ≠ 0.0**: Shows formatted value (e.g., "105.20")

### Data Initialization
- **Auto-generated geophones**: Elevation = 0.0 (empty cell)
- **Manually added shots**: Elevation = 0.0 (empty cell)
- **Loaded from project**: Elevation = 0.0 (empty cell)
- **Imported from CSV**: Elevation as specified in CSV file

### Editing
- User can still edit elevation cells (even when empty)
- Editing an empty cell sets the elevation value
- Setting elevation back to 0.0 makes the cell appear empty again

## User Experience

### Clean Interface
- Tables show elevation only when relevant
- Reduces visual clutter for flat terrain surveys
- Clear indication when elevation data exists

### Data Integrity
- Zero elevations are still stored as 0.0 in memory
- CSV export includes all elevation values (including zeros)
- Preview panel shows topographic profile when elevations exist

## Testing

### Test 1: Auto-generate Geophones
1. Set parameters and click "Auto-generate"
2. **Expected**: Elevation column shows empty cells

### Test 2: Edit Elevation
1. Click on empty elevation cell
2. Enter "105.5"
3. Press Enter
4. **Expected**: Cell shows "105.50"

### Test 3: Reset to Zero
1. Change elevation to "0.0"
2. Press Enter
3. **Expected**: Cell appears empty again

### Test 4: Import CSV with Elevations
1. Import CSV with non-zero elevations
2. **Expected**: Elevation cells show values, topographic profile appears

## Date
2025-10-13

## Author
smartRefract Team
