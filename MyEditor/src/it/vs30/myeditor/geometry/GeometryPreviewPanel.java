/*
 * Advanced visual panel for geometry preview
 */
package it.vs30.myeditor.geometry;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * GeometryPreviewPanel renders a top-down view of a seismic spread.
 * It draws geophones (cyan) and shots (red), an optional grid, a
 * topographic profile (if elevation data is available), and scale
 * annotations. This class is a pure view component and does not
 * modify the underlying model.
 *
 * Dependencies / expected data shapes:
 * - GeophonePosition: expected to provide at least
 *     - getAbsolutePosition(): double (meters along the spread)
 *     - getElevation(): double (elevation in meters)
 *     - getChannelNumber(): int
 * - ShotPosition: expected to provide at least
 *     - getAbsolutePosition(): double (meters along the spread)
 *     - getElevation(): double (elevation in meters)
 *     - getShotNumber(): int
 *
 * Coordinates and units: positions and spacing are in meters; elevations
 * are in meters. Elevation drawing is enabled only when at least one
 * element reports a non-zero elevation (tolerance 0.01 m).
 *
 * Author: smartRefract Team
 */
public class GeometryPreviewPanel extends JPanel {
    
    private List<GeophonePosition> geophones;
    private List<ShotPosition> shots;
    private double spacing;
    private double startPosition;
    private boolean showGrid = true;
    private boolean showLabels = true;
    private boolean showDistances = true;
    
    // Colors
    private static final Color GEOPHONE_COLOR = new Color(0, 255, 255); // Ciano
    private static final Color SHOT_COLOR = new Color(255, 50, 50);     // Rosso
    private static final Color BACKGROUND_COLOR = Color.BLACK;
    private static final Color GRID_COLOR = new Color(50, 50, 50);
    private static final Color TEXT_COLOR = Color.WHITE;
    private static final Color BASELINE_COLOR = Color.GRAY;
    
    // Margins
    private static final int MARGIN_TOP = 60;
    private static final int MARGIN_BOTTOM = 80;
    private static final int MARGIN_LEFT = 50;
    private static final int MARGIN_RIGHT = 50;
    
    public GeometryPreviewPanel() {
        geophones = new ArrayList<>();
        shots = new ArrayList<>();
        spacing = 2.0;
        startPosition = 0.0;
        setBackground(BACKGROUND_COLOR);
        setPreferredSize(new Dimension(800, 200));
        setMinimumSize(new Dimension(400, 150));
    }
    
    /**
     * Imposta la geometria da visualizzare
     */
    /**
     * Set the geometry to display in the preview.
     *
     * @param geophones  list of geophone positions (may be null)
     * @param shots      list of shot positions (may be null)
     * @param spacing    nominal spacing between channels in meters
     * @param startPosition starting abscissa (meters)
     */
    public void setGeometry(List<GeophonePosition> geophones, List<ShotPosition> shots, 
                           double spacing, double startPosition) {
        this.geophones = geophones != null ? new ArrayList<>(geophones) : new ArrayList<>();
        this.shots = shots != null ? new ArrayList<>(shots) : new ArrayList<>();
        this.spacing = spacing;
        this.startPosition = startPosition;
        repaint();
    }
    
    /**
     * Aggiorna solo i geofoni
     */
    /**
     * Update only the geophone list and repaint. The provided list will
     * be copied to avoid external mutation of the internal state.
     *
     * @param geophones list of geophone positions
     */
    public void setGeophones(List<GeophonePosition> geophones) {
        this.geophones = geophones != null ? new ArrayList<>(geophones) : new ArrayList<>();
        repaint();
    }
    
    /**
     * Aggiorna solo gli shot
     */
    /**
     * Update only the shot list and repaint. The provided list will be
     * copied to avoid external mutation of the internal state.
     *
     * @param shots list of shot positions
     */
    public void setShots(List<ShotPosition> shots) {
        this.shots = shots != null ? new ArrayList<>(shots) : new ArrayList<>();
        repaint();
    }
    
    public void setShowGrid(boolean showGrid) {
        this.showGrid = showGrid;
        repaint();
    }
    
    public void setShowLabels(boolean showLabels) {
        this.showLabels = showLabels;
        repaint();
    }
    
    public void setShowDistances(boolean showDistances) {
        this.showDistances = showDistances;
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        // Anti-aliasing for better graphics
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        int width = getWidth();
        int height = getHeight();
        
        // If there is no geometry to draw show a placeholder message
        if (geophones.isEmpty() && shots.isEmpty()) {
            drawEmptyMessage(g2d, width, height);
            return;
        }
        
        // Calculate position limits
        double minPos = Double.MAX_VALUE;
        double maxPos = Double.MIN_VALUE;
        
        for (GeophonePosition geo : geophones) {
            minPos = Math.min(minPos, geo.getAbsolutePosition());
            maxPos = Math.max(maxPos, geo.getAbsolutePosition());
        }
        
        for (ShotPosition shot : shots) {
            minPos = Math.min(minPos, shot.getAbsolutePosition());
            maxPos = Math.max(maxPos, shot.getAbsolutePosition());
        }
        
        if (minPos == Double.MAX_VALUE) {
            minPos = startPosition;
            maxPos = startPosition + spacing * Math.max(24, geophones.size());
        }
        
    // Add 10% horizontal margin to avoid drawing on the edges
        double range = maxPos - minPos;
        minPos -= range * 0.1;
        maxPos += range * 0.1;
        
    // Calculate elevation limits (for topographic profile). We consider
    // elevation data present only when values exceed a small tolerance.
        double minElev = 0;
        double maxElev = 0;
        boolean hasElevationData = false;
        
        for (GeophonePosition geo : geophones) {
            if (Math.abs(geo.getElevation()) > 0.01) {
                hasElevationData = true;
                minElev = Math.min(minElev, geo.getElevation());
                maxElev = Math.max(maxElev, geo.getElevation());
            }
        }
        
        for (ShotPosition shot : shots) {
            if (Math.abs(shot.getElevation()) > 0.01) {
                hasElevationData = true;
                minElev = Math.min(minElev, shot.getElevation());
                maxElev = Math.max(maxElev, shot.getElevation());
            }
        }
        
    // Calculate drawing scales
        int drawWidth = width - MARGIN_LEFT - MARGIN_RIGHT;
        int drawHeight = height - MARGIN_TOP - MARGIN_BOTTOM;
        double scaleX = drawWidth / (maxPos - minPos);
        
        // Calculate Y scale for elevation (if there's elevation data)
        double scaleY = 1.0;
        int baselineY = height / 2;
        
        if (hasElevationData && (maxElev - minElev) > 0.01) {
            double elevRange = maxElev - minElev;
            // Use 60% of available height for elevation profile
            scaleY = (drawHeight * 0.6) / elevRange;
            // Center the profile vertically
            baselineY = MARGIN_TOP + drawHeight / 2;
        }
        
        // Draw grid lines if enabled
        if (showGrid) {
            drawGrid(g2d, minPos, maxPos, scaleX, drawWidth, height, baselineY);
        }
        
        // Draw topographic profile if elevation data exists
        if (hasElevationData) {
            drawTopographicProfile(g2d, scaleX, scaleY, minPos, minElev, maxElev, baselineY);
        } else {
            // Draw flat baseline
            drawBaseline(g2d, drawWidth, baselineY);
        }
        
        // Draw geophones
        drawGeophones(g2d, scaleX, scaleY, minPos, minElev, maxElev, baselineY, hasElevationData);
        
        // Draw shots
        drawShots(g2d, scaleX, scaleY, minPos, minElev, maxElev, baselineY, hasElevationData);
        
        // Draw scale and labels
        if (showLabels) {
            drawScale(g2d, minPos, maxPos, scaleX, drawWidth, height, baselineY);
            if (hasElevationData) {
                drawElevationScale(g2d, minElev, maxElev, scaleY, baselineY);
            }
        }
        
        // Draw distances if enabled
        if (showDistances && !geophones.isEmpty()) {
            drawSpacingInfo(g2d, width, height);
        }
    }
    
    /**
     * Draw a centered placeholder message when no geometry is available.
     * This message is non-intrusive and uses the shot color for visibility.
     *
     * @param g2d    graphics context
     * @param width  component width
     * @param height component height
     */
    private void drawEmptyMessage(Graphics2D g2d, int width, int height) {
        g2d.setColor(SHOT_COLOR);
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        String message = "No geometry defined";
        FontMetrics fm = g2d.getFontMetrics();
        int messageWidth = fm.stringWidth(message);
        g2d.drawString(message, (width - messageWidth) / 2, height / 2);
    }
    
    private void drawGrid(Graphics2D g2d, double minPos, double maxPos, double scale, 
                         int drawWidth, int height, int baselineY) {
        g2d.setColor(GRID_COLOR);
        g2d.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 
                                     10.0f, new float[]{5.0f}, 0.0f));
        
    // Vertical grid lines every 5 meters
        double gridSpacing = 5.0;
        double startGrid = Math.floor(minPos / gridSpacing) * gridSpacing;
        
        for (double pos = startGrid; pos <= maxPos; pos += gridSpacing) {
            int x = MARGIN_LEFT + (int)((pos - minPos) * scale);
            if (x >= MARGIN_LEFT && x <= MARGIN_LEFT + drawWidth) {
                g2d.drawLine(x, MARGIN_TOP, x, height - MARGIN_BOTTOM);
            }
        }
        
        g2d.setStroke(new BasicStroke(1.0f));
    }
    
    /**
     * Draw a flat baseline (when no elevation data is present) to help
     * visually anchor geophones and shots.
     *
     * @param g2d draw context
     * @param drawWidth available horizontal drawing width
     * @param baselineY y coordinate of the baseline
     */
    private void drawBaseline(Graphics2D g2d, int drawWidth, int baselineY) {
        g2d.setColor(BASELINE_COLOR);
        g2d.setStroke(new BasicStroke(2.0f));
        g2d.drawLine(MARGIN_LEFT, baselineY, MARGIN_LEFT + drawWidth, baselineY);
        g2d.setStroke(new BasicStroke(1.0f));
    }
    
    private void drawGeophones(Graphics2D g2d, double scaleX, double scaleY, double minPos, 
                              double minElev, double maxElev, int baselineY, boolean hasElevationData) {
        g2d.setColor(GEOPHONE_COLOR);
        
        for (GeophonePosition geo : geophones) {
            int x = MARGIN_LEFT + (int)((geo.getAbsolutePosition() - minPos) * scaleX);
            
            // Calculate Y position based on elevation
            int y = baselineY;
            if (hasElevationData && (maxElev - minElev) > 0.01) {
                double avgElev = (minElev + maxElev) / 2.0;
                y = baselineY - (int)((geo.getElevation() - avgElev) * scaleY);
            }
            
            // Draw geophone symbol (triangle with stem)
            int[] xPoints = {x - 7, x + 7, x};
            int[] yPoints = {y, y, y - 12};
            g2d.fillPolygon(xPoints, yPoints, 3);
            g2d.setStroke(new BasicStroke(2.0f));
            g2d.drawLine(x, y, x, y + 25);
            g2d.setStroke(new BasicStroke(1.0f));
            
            // Channel label above the profile line
            if (showLabels) {
                g2d.setFont(new Font("Arial", Font.PLAIN, 10));
                String label = String.valueOf(geo.getChannelNumber());
                FontMetrics fm = g2d.getFontMetrics();
                int labelWidth = fm.stringWidth(label);
                g2d.drawString(label, x - labelWidth / 2, y - 20);
                
                // Draw elevation value if data exists
                if (hasElevationData && Math.abs(geo.getElevation()) > 0.01) {
                    g2d.setFont(new Font("Arial", Font.PLAIN, 8));
                    String elevLabel = String.format("%.1fm", geo.getElevation());
                    int elevWidth = g2d.getFontMetrics().stringWidth(elevLabel);
                    g2d.drawString(elevLabel, x - elevWidth / 2, y - 30);
                }
            }
        }
    }
    
    private void drawShots(Graphics2D g2d, double scaleX, double scaleY, double minPos, 
                          double minElev, double maxElev, int baselineY, boolean hasElevationData) {
        g2d.setColor(SHOT_COLOR);
        
        for (ShotPosition shot : shots) {
            int x = MARGIN_LEFT + (int)((shot.getAbsolutePosition() - minPos) * scaleX);
            
            // Calculate Y position based on elevation
            int y = baselineY;
            if (hasElevationData && (maxElev - minElev) > 0.01) {
                double avgElev = (minElev + maxElev) / 2.0;
                y = baselineY - (int)((shot.getElevation() - avgElev) * scaleY);
            }
            
            // Draw shot symbol (explosive star)
            int size = 10;
            g2d.fillRect(x - size/2, y - size - 10, size, size);
            
            // Cross lines for explosion effect
            g2d.setStroke(new BasicStroke(2.0f));
            g2d.drawLine(x - size, y - 10 - size/2, x + size, y - 10 - size/2);
            g2d.drawLine(x, y - 10 - size, x, y - 10);
            g2d.drawLine(x - size*7/10, y - 10 - size*7/10, x + size*7/10, y - 10 + size*7/10 - size);
            g2d.drawLine(x + size*7/10, y - 10 - size*7/10, x - size*7/10, y - 10 + size*7/10 - size);
            
            // Stem
            g2d.drawLine(x, y - 10, x, y + 25);
            g2d.setStroke(new BasicStroke(1.0f));
            
            // Shot label
            if (showLabels) {
                g2d.setFont(new Font("Arial", Font.BOLD, 10));
                String label = "S" + shot.getShotNumber();
                FontMetrics fm = g2d.getFontMetrics();
                int labelWidth = fm.stringWidth(label);
                g2d.drawString(label, x - labelWidth / 2, y + 40);
                
                // Draw elevation value if data exists
                if (hasElevationData && Math.abs(shot.getElevation()) > 0.01) {
                    g2d.setFont(new Font("Arial", Font.PLAIN, 8));
                    String elevLabel = String.format("%.1fm", shot.getElevation());
                    int elevWidth = g2d.getFontMetrics().stringWidth(elevLabel);
                    g2d.drawString(elevLabel, x - elevWidth / 2, y + 52);
                }
            }
        }
    }
    
    /**
    * Draws the topographic profile connecting geophone elevations.
    * The profile is drawn by sorting geophones by absolute position and
    * connecting their elevation samples with straight segments. A filled
    * polygon is produced to emphasize the terrain shape.
     */
    private void drawTopographicProfile(Graphics2D g2d, double scaleX, double scaleY, 
                                       double minPos, double minElev, double maxElev, int baselineY) {
        if (geophones.isEmpty()) {
            return;
        }
        
        // Sort geophones by position for drawing the profile line
        List<GeophonePosition> sortedGeophones = new ArrayList<>(geophones);
        sortedGeophones.sort((g1, g2) -> Double.compare(g1.getAbsolutePosition(), g2.getAbsolutePosition()));
        
        // Calculate average elevation for centering
        double avgElev = (minElev + maxElev) / 2.0;
        
        // Draw the profile line connecting geophones
        g2d.setColor(new Color(100, 150, 100)); // Dark green for terrain
        g2d.setStroke(new BasicStroke(2.5f));
        
        for (int i = 0; i < sortedGeophones.size() - 1; i++) {
            GeophonePosition geo1 = sortedGeophones.get(i);
            GeophonePosition geo2 = sortedGeophones.get(i + 1);
            
            int x1 = MARGIN_LEFT + (int)((geo1.getAbsolutePosition() - minPos) * scaleX);
            int y1 = baselineY - (int)((geo1.getElevation() - avgElev) * scaleY);
            
            int x2 = MARGIN_LEFT + (int)((geo2.getAbsolutePosition() - minPos) * scaleX);
            int y2 = baselineY - (int)((geo2.getElevation() - avgElev) * scaleY);
            
            g2d.drawLine(x1, y1, x2, y2);
        }
        
        // Fill area below profile to emphasize terrain
        g2d.setColor(new Color(100, 150, 100, 40)); // Semi-transparent green
        int[] xPoints = new int[sortedGeophones.size() + 2];
        int[] yPoints = new int[sortedGeophones.size() + 2];
        
        for (int i = 0; i < sortedGeophones.size(); i++) {
            GeophonePosition geo = sortedGeophones.get(i);
            xPoints[i] = MARGIN_LEFT + (int)((geo.getAbsolutePosition() - minPos) * scaleX);
            yPoints[i] = baselineY - (int)((geo.getElevation() - avgElev) * scaleY);
        }
        
    // Close the polygon at the bottom so the filled area covers the
    // terrain down to a fixed offset below the baseline.
        xPoints[sortedGeophones.size()] = xPoints[sortedGeophones.size() - 1];
        yPoints[sortedGeophones.size()] = baselineY + 50;
        xPoints[sortedGeophones.size() + 1] = xPoints[0];
        yPoints[sortedGeophones.size() + 1] = baselineY + 50;
        
        g2d.fillPolygon(xPoints, yPoints, sortedGeophones.size() + 2);
        
        g2d.setStroke(new BasicStroke(1.0f));
    }
    
    /**
     * Draws elevation scale on the left side
     */
    private void drawElevationScale(Graphics2D g2d, double minElev, double maxElev, 
                                    double scaleY, int baselineY) {
        g2d.setColor(TEXT_COLOR);
        g2d.setFont(new Font("Arial", Font.PLAIN, 9));
        
        double avgElev = (minElev + maxElev) / 2.0;
        double elevRange = maxElev - minElev;
        
        // Draw elevation markers every 5m or appropriate interval
        double elevSpacing = calculateOptimalElevationSpacing(elevRange);
        double startElev = Math.floor(minElev / elevSpacing) * elevSpacing;
        
        for (double elev = startElev; elev <= maxElev + elevSpacing; elev += elevSpacing) {
            int y = baselineY - (int)((elev - avgElev) * scaleY);
            
            // Draw tick mark
            g2d.drawLine(MARGIN_LEFT - 5, y, MARGIN_LEFT, y);
            
            // Draw elevation label
            String label = String.format("%.0fm", elev);
            FontMetrics fm = g2d.getFontMetrics();
            int labelWidth = fm.stringWidth(label);
            g2d.drawString(label, MARGIN_LEFT - labelWidth - 10, y + 4);
        }
    }
    
    /**
     * Calculate optimal elevation spacing for scale
     */
    private double calculateOptimalElevationSpacing(double range) {
        if (range < 5) return 1.0;
        if (range < 10) return 2.0;
        if (range < 25) return 5.0;
        if (range < 50) return 10.0;
        if (range < 100) return 20.0;
        return 50.0;
    }
    
    private void drawScale(Graphics2D g2d, double minPos, double maxPos, double scale, 
                          int drawWidth, int height, int baselineY) {
        g2d.setColor(TEXT_COLOR);
        g2d.setFont(new Font("Arial", Font.PLAIN, 11));
        
    // Top scale
    double scaleSpacing = calculateOptimalScaleSpacing(maxPos - minPos, drawWidth);
        double startScale = Math.ceil(minPos / scaleSpacing) * scaleSpacing;
        
        for (double pos = startScale; pos <= maxPos; pos += scaleSpacing) {
            int x = MARGIN_LEFT + (int)((pos - minPos) * scale);
            
            if (x >= MARGIN_LEFT && x <= MARGIN_LEFT + drawWidth) {
                // Tick mark
                g2d.drawLine(x, MARGIN_TOP - 5, x, MARGIN_TOP);
                
                // Label
                String label = String.format("%.1f", pos);
                FontMetrics fm = g2d.getFontMetrics();
                int labelWidth = fm.stringWidth(label);
                g2d.drawString(label, x - labelWidth / 2, MARGIN_TOP - 10);
            }
        }
        
        // Axis label
        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        String axisLabel = "Position (m)";
        FontMetrics fm = g2d.getFontMetrics();
        int labelWidth = fm.stringWidth(axisLabel);
        g2d.drawString(axisLabel, MARGIN_LEFT + (drawWidth - labelWidth) / 2, MARGIN_TOP - 30);
    }
    
    private void drawSpacingInfo(Graphics2D g2d, int width, int height) {
        g2d.setColor(TEXT_COLOR);
        g2d.setFont(new Font("Arial", Font.PLAIN, 11));
        
        String info = String.format("Spacing: %.2fm | Start: %.2fm | Geophones: %d | Shots: %d", 
                                   spacing, startPosition, geophones.size(), shots.size());
        FontMetrics fm = g2d.getFontMetrics();
        int infoWidth = fm.stringWidth(info);
        g2d.drawString(info, (width - infoWidth) / 2, height - MARGIN_BOTTOM + 25);
    }
    
    private double calculateOptimalScaleSpacing(double range, int pixelWidth) {
        double[] spacings = {0.5, 1, 2, 5, 10, 20, 50, 100};
    double targetTicks = pixelWidth / 100.0; // about one tick every ~100 pixels
        
        for (double spacing : spacings) {
            if (range / spacing <= targetTicks * 1.5) {
                return spacing;
            }
        }
        
        return spacings[spacings.length - 1];
    }
    
    /**
     * Clears the current geometry preview and repaints.
     */
    public void clear() {
        geophones.clear();
        shots.clear();
        repaint();
    }
}
