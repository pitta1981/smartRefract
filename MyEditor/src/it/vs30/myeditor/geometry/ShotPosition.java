/*
 * Classe per rappresentare la posizione di un'energizzazione (shot) nello stendimento
 */
package it.vs30.myeditor.geometry;

/**
 * Rappresenta un punto di energizzazione con la sua posizione
 * 
 * @author smartRefract Team
 */
public class ShotPosition {
    
    private int shotNumber;
    private String fileName;
    private double absolutePosition;  // Absolute position in meters
    private double elevation;         // Elevation/altitude in meters
    private String description;
    
    /**
     * Constructor
     * @param shotNumber Shot number
     * @param fileName Associated file name
     * @param absolutePosition Absolute position in meters
     */
    public ShotPosition(int shotNumber, String fileName, double absolutePosition) {
        this.shotNumber = shotNumber;
        this.fileName = fileName;
        this.absolutePosition = absolutePosition;
        this.elevation = 0.0;
        this.description = "";
    }
    
    /**
     * Complete constructor
     */
    public ShotPosition(int shotNumber, String fileName, double absolutePosition, double elevation, String description) {
        this.shotNumber = shotNumber;
        this.fileName = fileName;
        this.absolutePosition = absolutePosition;
        this.elevation = elevation;
        this.description = description != null ? description : "";
    }

    // Getters e Setters
    public int getShotNumber() {
        return shotNumber;
    }

    public void setShotNumber(int shotNumber) {
        this.shotNumber = shotNumber;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    /**
     * Validates the shot position
     * @return true if valid
     */
    public boolean isValid() {
        return shotNumber >= 0 && 
               fileName != null && !fileName.isEmpty() &&
               !Double.isNaN(absolutePosition) && 
               !Double.isInfinite(absolutePosition);
    }
    
    /**
     * Converts to simplified CSV format (no description, no type label)
     * @return CSV String: fileName,absolutePosition,elevation
     */
    public String toCSV() {
        return String.format("%s,%.3f,%.3f", fileName, absolutePosition, elevation);
    }
    
    /**
     * Creates object from CSV line (simplified format)
     * @param csvLine CSV line in format: fileName,absolutePosition,elevation
     * @return ShotPosition object or null if parsing fails
     */
    public static ShotPosition fromCSV(String csvLine) {
        try {
            String[] parts = csvLine.split(",");
            if (parts.length >= 3) {
                String fname = parts[0].trim();
                double absPos = Double.parseDouble(parts[1].trim());
                double elev = Double.parseDouble(parts[2].trim());
                // shotNum will be assigned by the importer based on order
                return new ShotPosition(0, fname, absPos, elev, "");
            }
        } catch (NumberFormatException e) {
            System.err.println("Error parsing CSV shot: " + e.getMessage());
        }
        return null;
    }
    
    @Override
    public String toString() {
        return String.format("Shot #%d (%s): %.2fm (elev: %.2fm)", shotNumber, fileName, absolutePosition, elevation);
    }
    
    /**
     * Calcola la distanza da un punto
     */
    public double distanceFrom(double position) {
        return Math.abs(absolutePosition - position);
    }
    
    /**
     * Restituisce una descrizione formattata per la posizione relativa rispetto allo stendimento
     * @param spazIn Posizione iniziale dello stendimento
     * @param spaz Spaziatura tra geofoni
     * @param numChannels Numero di canali
     * @return Descrizione formattata (es: "3.5" o "-5m" o "+10m")
     */
    public String getRelativeDescription(double spazIn, double spaz, int numChannels) {
        double endPos = spazIn + (numChannels - 1) * spaz;
        
        if (absolutePosition < spazIn) {
            return String.format("%.1fm", absolutePosition - spazIn);
        } else if (absolutePosition >= spazIn && absolutePosition <= endPos) {
            double relPos = (absolutePosition - spazIn) / spaz;
            return String.format("%.1f", relPos);
        } else {
            return String.format("+%.1fm", absolutePosition - endPos);
        }
    }
}
