/*
 * Classe per rappresentare la posizione di un geofono nello stendimento
 */
package it.vs30.myeditor.geometry;

/**
 * Rappresenta un geofono con la sua posizione assoluta e relativa
 * 
 * @author smartRefract Team
 */
public class GeophonePosition {
    
    private int channelNumber;
    private double absolutePosition;  // Absolute position in meters
    private double elevation;         // Elevation/altitude in meters
    
    /**
     * Constructor
     * @param channelNumber Channel number (geophone)
     * @param absolutePosition Absolute position in meters
     */
    public GeophonePosition(int channelNumber, double absolutePosition) {
        this.channelNumber = channelNumber;
        this.absolutePosition = absolutePosition;
        this.elevation = 0.0;
    }
    
    /**
     * Complete constructor
     */
    public GeophonePosition(int channelNumber, double absolutePosition, double elevation) {
        this.channelNumber = channelNumber;
        this.absolutePosition = absolutePosition;
        this.elevation = elevation;
    }

    // Getters e Setters
    public int getChannelNumber() {
        return channelNumber;
    }

    public void setChannelNumber(int channelNumber) {
        this.channelNumber = channelNumber;
    }

    public double getAbsolutePosition() {
        return absolutePosition;
    }

    public void setAbsolutePosition(double absolutePosition) {
        this.absolutePosition = absolutePosition;
    }

    public double getElevation() {
        return elevation;
    }

    public void setElevation(double elevation) {
        this.elevation = elevation;
    }
    
    /**
     * Validates the geophone position
     * @return true if valid
     */
    public boolean isValid() {
        return channelNumber >= 0 && !Double.isNaN(absolutePosition) && !Double.isInfinite(absolutePosition);
    }
    
    /**
     * Converts to simplified CSV format
     * @return CSV String: channelNumber,absolutePosition,elevation
     */
    public String toCSV() {
        return String.format("%d,%.3f,%.3f", channelNumber, absolutePosition, elevation);
    }
    
    /**
     * Creates object from CSV line (simplified format)
     * @param csvLine CSV line in format: channelNumber,absolutePosition,elevation
     * @return GeophonePosition object or null if parsing fails
     */
    public static GeophonePosition fromCSV(String csvLine) {
        try {
            String[] parts = csvLine.split(",");
            if (parts.length >= 3) {
                int channel = Integer.parseInt(parts[0].trim());
                double absPos = Double.parseDouble(parts[1].trim());
                double elev = Double.parseDouble(parts[2].trim());
                return new GeophonePosition(channel, absPos, elev);
            }
        } catch (NumberFormatException e) {
            System.err.println("Error parsing CSV geophone: " + e.getMessage());
        }
        return null;
    }
    
    @Override
    public String toString() {
        return String.format("Geophone #%d: %.2fm (elev: %.2fm)", channelNumber, absolutePosition, elevation);
    }
    
    /**
     * Calcola la distanza da un punto
     */
    public double distanceFrom(double position) {
        return Math.abs(absolutePosition - position);
    }
}
