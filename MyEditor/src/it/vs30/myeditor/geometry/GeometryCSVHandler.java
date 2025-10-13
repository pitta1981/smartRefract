/*
 * CSV Handler per importare ed esportare la geometria
 */
package it.vs30.myeditor.geometry;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles geometry import and export in simplified CSV format
 * 
 * CSV Format (simplified - numeric only):
 * - Geophones: channel,position,elevation
 * - Shots: filename,position,elevation
 * 
 * @author smartRefract Team
 */
public class GeometryCSVHandler {
    
    private static final String GEOPHONE_HEADER = "# Geophones: channel,position(m),elevation(m)";
    private static final String SHOT_HEADER = "# Shots: filename,position(m),elevation(m)";
    private static final String ENCODING = "UTF-8";
    
    /**
     * Exports complete geometry to CSV file (simplified format)
     */
    public static boolean exportGeometry(File file, List<GeophonePosition> geophones, 
                                        List<ShotPosition> shots, 
                                        double spacing, double startPosition) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(file);
             OutputStreamWriter osw = new OutputStreamWriter(fos, ENCODING);
             BufferedWriter writer = new BufferedWriter(osw)) {
            
            // Header comments
            writer.write("# SmartRefract Geometry Export");
            writer.newLine();
            writer.write("# Spacing: " + spacing + "m");
            writer.newLine();
            writer.write("# Start Position: " + startPosition + "m");
            writer.newLine();
            writer.newLine();
            
            // Geophones section
            writer.write(GEOPHONE_HEADER);
            writer.newLine();
            for (GeophonePosition geo : geophones) {
                if (geo.isValid()) {
                    writer.write(geo.toCSV());
                    writer.newLine();
                }
            }
            
            writer.newLine();
            
            // Shots section
            writer.write(SHOT_HEADER);
            writer.newLine();
            for (ShotPosition shot : shots) {
                if (shot.isValid()) {
                    writer.write(shot.toCSV());
                    writer.newLine();
                }
            }
            
            return true;
        } catch (IOException e) {
            System.err.println("CSV export error: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Imports geometry from CSV file
     * @return Array with [0]=geophones List, [1]=shots List
     */
    public static Object[] importGeometry(File file) throws IOException {
        List<GeophonePosition> geophones = new ArrayList<>();
        List<ShotPosition> shots = new ArrayList<>();
        
        try (FileInputStream fis = new FileInputStream(file);
             InputStreamReader isr = new InputStreamReader(fis, ENCODING);
             BufferedReader reader = new BufferedReader(isr)) {
            
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                
                // Skip comments and empty lines
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                
                // Parse line: attempt to detect if it's a geophone or shot
                // Geophone: channel,position,elevation (first field is numeric)
                // Shot: filename,position,elevation (first field is string/filename)
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    try {
                        // Try parsing first field as integer (geophone channel)
                        int channel = Integer.parseInt(parts[0].trim());
                        double position = Double.parseDouble(parts[1].trim());
                        double elevation = Double.parseDouble(parts[2].trim());
                        GeophonePosition geo = new GeophonePosition(channel, position, elevation);
                        if (geo.isValid()) {
                            geophones.add(geo);
                        }
                    } catch (NumberFormatException e) {
                        // First field is not a number, try as shot (filename,position,elevation)
                        try {
                            String filename = parts[0].trim();
                            double position = Double.parseDouble(parts[1].trim());
                            double elevation = Double.parseDouble(parts[2].trim());
                            int shotNum = shots.size();
                            ShotPosition shot = new ShotPosition(shotNum, filename, position, elevation, "");
                            if (shot.isValid()) {
                                shots.add(shot);
                            }
                        } catch (NumberFormatException ex) {
                            System.err.println("Skipping invalid line: " + line);
                        }
                    }
                }
            }
            
        } catch (IOException e) {
            System.err.println("CSV import error: " + e.getMessage());
            throw e;
        }
        
        return new Object[]{geophones, shots};
    }
    
    /**
     * Creates an example CSV file with simplified format
     */
    public static void createExampleCSV(File file) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(file);
             OutputStreamWriter osw = new OutputStreamWriter(fos, ENCODING);
             BufferedWriter writer = new BufferedWriter(osw)) {
            
            writer.write("# SmartRefract Geometry - Example File");
            writer.newLine();
            writer.write("# Spacing: 2.0m");
            writer.newLine();
            writer.write("# Start Position: 0.0m");
            writer.newLine();
            writer.newLine();
            
            writer.write(GEOPHONE_HEADER);
            writer.newLine();
            for (int i = 0; i < 24; i++) {
                double pos = i * 2.0;
                writer.write(String.format("%d,%.3f,%.3f", i, pos, 0.0));
                writer.newLine();
            }
            
            writer.newLine();
            
            writer.write(SHOT_HEADER);
            writer.newLine();
            writer.write("shot01.dat,-5.000,0.000");
            writer.newLine();
            writer.write("shot02.dat,11.500,0.000");
            writer.newLine();
            writer.write("shot03.dat,51.000,0.000");
            writer.newLine();
        }
    }
    
    /**
     * Validates CSV file before import
     */
    public static ValidationResult validateCSVFile(File file) {
        ValidationResult result = new ValidationResult();
        
        if (!file.exists()) {
            result.addError("File not found: " + file.getAbsolutePath());
            return result;
        }
        
        if (!file.canRead()) {
            result.addError("Cannot read file: " + file.getAbsolutePath());
            return result;
        }
        
        try (FileInputStream fis = new FileInputStream(file);
             InputStreamReader isr = new InputStreamReader(fis, ENCODING);
             BufferedReader reader = new BufferedReader(isr)) {
            
            String line;
            int lineNumber = 0;
            int geophoneCount = 0;
            int shotCount = 0;
            
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                line = line.trim();
                
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    try {
                        // Try as geophone
                        int channel = Integer.parseInt(parts[0].trim());
                        double position = Double.parseDouble(parts[1].trim());
                        double elevation = Double.parseDouble(parts[2].trim());
                        geophoneCount++;
                    } catch (NumberFormatException e) {
                        // Try as shot
                        try {
                            String filename = parts[0].trim();
                            double position = Double.parseDouble(parts[1].trim());
                            double elevation = Double.parseDouble(parts[2].trim());
                            shotCount++;
                        } catch (NumberFormatException ex) {
                            result.addWarning("Line " + lineNumber + ": invalid format");
                        }
                    }
                } else {
                    result.addWarning("Line " + lineNumber + ": insufficient fields (need 3: value,position,elevation)");
                }
            }
            
            result.addInfo("Valid geophones found: " + geophoneCount);
            result.addInfo("Valid shots found: " + shotCount);
            
            if (geophoneCount == 0 && shotCount == 0) {
                result.addError("No valid data found in file");
            }
            
        } catch (IOException e) {
            result.addError("File read error: " + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * Classe per contenere i risultati della validazione
     */
    public static class ValidationResult {
        private List<String> errors = new ArrayList<>();
        private List<String> warnings = new ArrayList<>();
        private List<String> info = new ArrayList<>();
        
        public void addError(String error) {
            errors.add(error);
        }
        
        public void addWarning(String warning) {
            warnings.add(warning);
        }
        
        public void addInfo(String info) {
            this.info.add(info);
        }
        
        public boolean isValid() {
            return errors.isEmpty();
        }
        
        public List<String> getErrors() {
            return errors;
        }
        
        public List<String> getWarnings() {
            return warnings;
        }
        
        public List<String> getInfo() {
            return info;
        }
        
        public String getFormattedMessage() {
            StringBuilder sb = new StringBuilder();
            
            if (!errors.isEmpty()) {
                sb.append("ERRORI:\n");
                for (String error : errors) {
                    sb.append("  - ").append(error).append("\n");
                }
            }
            
            if (!warnings.isEmpty()) {
                sb.append("AVVISI:\n");
                for (String warning : warnings) {
                    sb.append("  - ").append(warning).append("\n");
                }
            }
            
            if (!info.isEmpty()) {
                sb.append("INFO:\n");
                for (String i : info) {
                    sb.append("  - ").append(i).append("\n");
                }
            }
            
            return sb.toString();
        }
    }
}
