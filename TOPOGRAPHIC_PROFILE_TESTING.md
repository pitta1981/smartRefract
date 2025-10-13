# Topographic Profile Feature - Testing Guide

## Overview
The geometry editor now supports displaying and editing topographic profiles based on elevation data.

## New Features Implemented

### 1. Topographic Profile Visualization
- **When elevation data exists**: Profile line connects geophone positions showing terrain
- **When no elevation data**: Flat baseline (elevation = 0.0 for all)
- **Visual elements**:
  - Dark green line (2.5px) connecting geophones
  - Semi-transparent green fill below profile
  - Geophones and shots positioned at their actual elevations

### 2. Elevation Axis Scale
- Displayed on left side of preview panel
- Shows elevation values in meters
- Tick marks every 1m, 2m, 5m, 10m, 20m, or 50m (depending on range)
- Only visible when elevation data exists

### 3. Real-time Updates
- Editing elevation values in tables immediately updates the profile
- Position and elevation columns are both editable
- Preview refreshes automatically on any table edit

## How to Test

### Test 1: Load Flat Geometry (No Elevation)
1. Open Geometry Editor
2. Import `example_geometry.csv` (all elevations = 0.0)
3. **Expected**: Flat horizontal baseline, no elevation scale

### Test 2: Load Topographic Geometry
1. Open Geometry Editor
2. Import `example_geometry_with_elevations.csv` (elevations 100m-108m)
3. **Expected**: 
   - Sloped profile line connecting geophones
   - Green fill below terrain
   - Elevation scale on left showing 100m-108m range
   - Geophones positioned at varying heights

### Test 3: Edit Elevations in Table
1. Load geometry with elevations
2. Click on elevation cell in Geophones table
3. Change value (e.g., from 100.0 to 110.0)
4. Press Enter
5. **Expected**: 
   - Profile updates immediately
   - Geophone position moves vertically
   - Elevation scale adjusts if needed
   - Profile line re-draws connecting points

### Test 4: Auto-generate Geophones
1. Set parameters: Spacing=2m, Start=0m, Num=24
2. Click "Auto-generate"
3. **Expected**: 
   - All geophones created with elevation = 0.0
   - Flat baseline shown

### Test 5: Manual Elevation Entry
1. Auto-generate 24 geophones
2. Manually edit elevations to create a slope:
   - Ch 0: 100.0m
   - Ch 6: 103.0m
   - Ch 12: 105.0m
   - Ch 18: 107.0m
   - Ch 23: 110.0m
3. **Expected**: 
   - Profile shows gradual upward slope
   - Elevation scale appears (100m-110m)

### Test 6: Export and Re-import
1. Create geometry with varied elevations
2. Export to CSV
3. Open exported file in text editor
4. **Expected CSV format**:
   ```
   0,0.000,100.0,GEOPHONE
   1,2.000,102.5,GEOPHONE
   ```
5. Re-import the CSV
6. **Expected**: Profile restores correctly

### Test 7: Shot Elevations
1. Add shots with different elevations
2. **Expected**:
   - Shots positioned at their elevation
   - Elevation value displayed below shot label (if non-zero)

## Visual Indicators

### Geophone Display
- **Triangle symbol**: Positioned at elevation
- **Channel number**: Above triangle (Y - 20px)
- **Elevation value**: Above channel number (Y - 30px), shown if > 0.01m

### Shot Display
- **Star symbol**: Positioned at elevation
- **Shot label**: Below star (Y + 40px)
- **Elevation value**: Below label (Y + 52px), shown if > 0.01m

### Profile Line
- **Color**: Dark green RGB(100, 150, 100)
- **Width**: 2.5px
- **Style**: Solid line connecting geophone positions

### Fill Area
- **Color**: Semi-transparent green RGB(100, 150, 100, 40)
- **Area**: Between profile line and bottom reference

## Elevation Scale Details

### Scale Intervals
| Elevation Range | Interval |
|-----------------|----------|
| < 5m            | 1m       |
| 5m - 10m        | 2m       |
| 10m - 25m       | 5m       |
| 25m - 50m       | 10m      |
| 50m - 100m      | 20m      |
| > 100m          | 50m      |

### Scale Position
- **X**: Left margin - 10px from tick marks
- **Y**: Centered at each elevation marker
- **Format**: "100m", "105m", etc.

## Code Changes Summary

### GeometryPreviewPanel.java
- **paintComponent()**: Added elevation data detection and Y-scale calculation
- **drawGeophones()**: Calculates Y position based on elevation
- **drawShots()**: Calculates Y position based on elevation
- **drawTopographicProfile()**: New method to draw terrain profile
- **drawElevationScale()**: New method to draw Y-axis scale
- **calculateOptimalElevationSpacing()**: Helper for scale intervals

### CSV Format
```csv
# Format: Channel/ShotNum,Position(m),Elevation(m),TYPE
0,0.000,100.0,GEOPHONE
1,shot01.dat,-5.000,99.5,Shot before spread,SHOT
```

## Known Behavior

1. **Zero elevations**: When all elevations are 0.0, displays flat baseline (no profile)
2. **Mixed data**: If some geophones have elevation and others don't, those with 0.0 appear at baseline
3. **Scale adjustment**: Profile uses 60% of available vertical space
4. **Centering**: Profile is centered around average elevation

## Performance Notes

- Profile updates on every table edit (real-time)
- Sorting geophones by position for profile drawing
- Efficient polygon rendering for fill area

## Date
2025-10-13

## Author
smartRefract Team
