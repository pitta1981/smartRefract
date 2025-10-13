/*
 * Dialog moderno per la gestione della geometria dello stendimento
 */
package it.vs30.myeditor.geometry;

import org.myorg.myapi.FirstBrakeList;
import org.myorg.myapi.Indagine;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Dialog avanzato per la gestione della geometria con:
 * - Tabelle separate per geofoni ed energizzazioni
 * - Anteprima grafica real-time
 * - Import/Export CSV
 * - Validazione dati
 * 
 * @author smartRefract Team
 */
public class GeometryEditorDialog extends JDialog {
    
    private Indagine project;
    private double spacing = 2.0;
    private double startPosition = 0.0;
    private int numChannels = 24;
    
    private List<GeophonePosition> geophones;
    private List<ShotPosition> shots;
    
    private JTable geophoneTable;
    private JTable shotTable;
    private GeophoneTableModel geophoneModel;
    private ShotTableModel shotModel;
    private GeometryPreviewPanel previewPanel;
    
    private JTextField spacingField;
    private JTextField startPosField;
    private JSpinner numChannelsSpinner;
    
    private boolean approved = false;
    
    public GeometryEditorDialog(Frame owner) {
        super(owner, "Spread Geometry Editor", true);
        geophones = new ArrayList<>();
        shots = new ArrayList<>();
        initComponents();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setSize(1100, 750);
        setLocationRelativeTo(getOwner());
        
        // Panel principale con BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Top panel con parametri
        JPanel topPanel = createParametersPanel();
        mainPanel.add(topPanel, BorderLayout.NORTH);
        
        // Center panel con tabelle e preview
        JSplitPane centerSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        centerSplit.setResizeWeight(0.6);
        
        // Tabelle
        JPanel tablesPanel = createTablesPanel();
        centerSplit.setTopComponent(tablesPanel);
        
        // Preview
        JPanel previewContainer = createPreviewPanel();
        centerSplit.setBottomComponent(previewContainer);
        
        mainPanel.add(centerSplit, BorderLayout.CENTER);
        
        // Bottom panel con pulsanti
        JPanel bottomPanel = createButtonPanel();
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private JPanel createParametersPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Spread Parameters"));
        
        // Spacing
        panel.add(new JLabel("Geophone spacing (m):"));
        spacingField = new JTextField(String.valueOf(spacing), 8);
        spacingField.addActionListener(e -> updateGeometry());
        panel.add(spacingField);
        
        // Start position
        panel.add(new JLabel("Start position (m):"));
        startPosField = new JTextField(String.valueOf(startPosition), 8);
        startPosField.addActionListener(e -> updateGeometry());
        panel.add(startPosField);
        
        // Number of channels
        panel.add(new JLabel("Number of geophones:"));
        numChannelsSpinner = new JSpinner(new SpinnerNumberModel(numChannels, 1, 256, 1));
        numChannelsSpinner.addChangeListener(e -> updateGeophoneCount());
        panel.add(numChannelsSpinner);
        
        // Apply button
        JButton applyButton = new JButton("Apply");
        applyButton.addActionListener(e -> updateGeometry());
        panel.add(applyButton);
        
        return panel;
    }
    
    private JPanel createTablesPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 10, 0));
        
        // Geophone table
        JPanel geophonePanel = new JPanel(new BorderLayout(5, 5));
        geophonePanel.setBorder(BorderFactory.createTitledBorder("Geophones"));
        
        geophoneModel = new GeophoneTableModel();
        geophoneTable = new JTable(geophoneModel);
        geophoneTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        geophoneTable.getSelectionModel().addListSelectionListener(e -> updatePreview());
        
        JScrollPane geophoneScroll = new JScrollPane(geophoneTable);
        geophonePanel.add(geophoneScroll, BorderLayout.CENTER);
        
        // Geophone buttons
        JPanel geoButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton geoAutoButton = new JButton("Auto-generate");
        geoAutoButton.addActionListener(e -> autoGenerateGeophones());
        geoButtonPanel.add(geoAutoButton);
        geophonePanel.add(geoButtonPanel, BorderLayout.SOUTH);
        
        panel.add(geophonePanel);
        
        // Shot table
        JPanel shotPanel = new JPanel(new BorderLayout(5, 5));
        shotPanel.setBorder(BorderFactory.createTitledBorder("Shots"));
        
        shotModel = new ShotTableModel();
        shotTable = new JTable(shotModel);
        shotTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        shotTable.getSelectionModel().addListSelectionListener(e -> updatePreview());
        
        JScrollPane shotScroll = new JScrollPane(shotTable);
        shotPanel.add(shotScroll, BorderLayout.CENTER);
        
        // Shot buttons
        JPanel shotButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton shotAddButton = new JButton("Add");
        shotAddButton.addActionListener(e -> addShot());
        JButton shotRemoveButton = new JButton("Remove");
        shotRemoveButton.addActionListener(e -> removeShot());
        shotButtonPanel.add(shotAddButton);
        shotButtonPanel.add(shotRemoveButton);
        shotPanel.add(shotButtonPanel, BorderLayout.SOUTH);
        
        panel.add(shotPanel);
        
        return panel;
    }
    
    private JPanel createPreviewPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Geometry Preview"));
        
        previewPanel = new GeometryPreviewPanel();
        previewPanel.setPreferredSize(new Dimension(800, 200));
        
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JCheckBox gridCheck = new JCheckBox("Show grid", true);
        gridCheck.addActionListener(e -> {
            previewPanel.setShowGrid(gridCheck.isSelected());
        });
        JCheckBox labelsCheck = new JCheckBox("Show labels", true);
        labelsCheck.addActionListener(e -> {
            previewPanel.setShowLabels(labelsCheck.isSelected());
        });
        JCheckBox distCheck = new JCheckBox("Show distances", true);
        distCheck.addActionListener(e -> {
            previewPanel.setShowDistances(distCheck.isSelected());
        });
        
        controlPanel.add(gridCheck);
        controlPanel.add(labelsCheck);
        controlPanel.add(distCheck);
        
        panel.add(controlPanel, BorderLayout.NORTH);
        panel.add(previewPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        
        // Help button
        JButton helpButton = new JButton("CSV Format Help");
        helpButton.addActionListener(e -> showCSVFormatHelp());
        panel.add(helpButton);
        
        panel.add(Box.createHorizontalStrut(10));
        
        // Import/Export
        JButton importButton = new JButton("Import CSV");
        importButton.addActionListener(e -> importCSV());
        panel.add(importButton);
        
        JButton exportButton = new JButton("Export CSV");
        exportButton.addActionListener(e -> exportCSV());
        panel.add(exportButton);
        
        panel.add(Box.createHorizontalStrut(20));
        
        // OK/Cancel
        JButton okButton = new JButton("OK");
        okButton.setBackground(new Color(102, 255, 102));
        okButton.addActionListener(e -> {
            if (validateGeometry()) {
                approved = true;
                dispose();
            }
        });
        panel.add(okButton);
        
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setBackground(new Color(255, 51, 51));
        cancelButton.addActionListener(e -> {
            approved = false;
            dispose();
        });
        panel.add(cancelButton);
        
        return panel;
    }
    
    /**
     * Shows CSV format help dialog
     */
    private void showCSVFormatHelp() {
        String helpText = 
            "CSV Geometry Format Guide\n" +
            "=========================\n\n" +
            "The CSV file uses a simple numeric format with two sections:\n\n" +
            "1. GEOPHONES\n" +
            "   Format: channel,position(m),elevation(m)\n" +
            "   Example:\n" +
            "     0,0.000,0.000\n" +
            "     1,2.000,0.000\n" +
            "     2,4.000,1.500\n\n" +
            "2. SHOTS\n" +
            "   Format: filename,position(m),elevation(m)\n" +
            "   Example:\n" +
            "     shot01.dat,-5.000,0.000\n" +
            "     shot02.dat,23.000,0.000\n" +
            "     shot03.dat,51.000,0.000\n\n" +
            "NOTES:\n" +
            "• Lines starting with '#' are comments (ignored)\n" +
            "• Empty lines are ignored\n" +
            "• Position and elevation values are in meters\n" +
            "• Elevation = 0 means no topographic correction\n" +
            "• The file automatically detects geophones vs shots:\n" +
            "  - If first field is a number → geophone\n" +
            "  - If first field is text → shot\n\n" +
            "EXAMPLE FILE:\n" +
            "# SmartRefract Geometry\n" +
            "# Geophones: channel,position(m),elevation(m)\n" +
            "0,0.000,0.000\n" +
            "1,2.000,0.000\n" +
            "2,4.000,0.500\n\n" +
            "# Shots: filename,position(m),elevation(m)\n" +
            "shot01.dat,-5.000,0.000\n" +
            "shot02.dat,11.500,0.000\n";
        
        JTextArea textArea = new JTextArea(helpText);
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textArea.setCaretPosition(0);
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(600, 500));
        
        JOptionPane.showMessageDialog(this, scrollPane, 
            "CSV Format Help", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Carica la geometria dal progetto
     */
    public void loadFromProject(Indagine project) {
        this.project = project;
        
        if (project == null || project.stesa == null || project.stesa.isEmpty()) {
            return;
        }
        
        // Carica i parametri dal primo shot
        FirstBrakeList firstShot = project.stesa.get(0);
        spacing = firstShot.spaz;
        startPosition = firstShot.spaz_in;
        numChannels = firstShot.ch;
        
        spacingField.setText(String.valueOf(spacing));
        startPosField.setText(String.valueOf(startPosition));
        numChannelsSpinner.setValue(numChannels);
        
        // Popola i geofoni dal progetto (posx e z)
        geophones.clear();
        if (firstShot.fb != null && firstShot.fb.length > 0) {
            int n = Math.min(numChannels, firstShot.fb.length);
            for (int i = 0; i < n; i++) {
                double absPos = firstShot.fb[i].posx;
                double elev = firstShot.fb[i].z;
                GeophonePosition geo = new GeophonePosition(i, absPos, elev);
                geophones.add(geo);
            }
            // Se i canali del progetto sono meno di quelli richiesti, completa a spaziatura regolare
            for (int i = firstShot.fb.length; i < numChannels; i++) {
                double absPos = startPosition + (i * spacing);
                geophones.add(new GeophonePosition(i, absPos, 0.0));
            }
        } else {
            // Nessun fb disponibile: genera in base ai parametri
            autoGenerateGeophones();
        }
        
        // Carica gli shot (posizione e quota)
        shots.clear();
        for (int i = 0; i < project.stesa.size(); i++) {
            FirstBrakeList fbl = project.stesa.get(i);
            String fileName = new File(fbl.fbp).getName();
            ShotPosition shot = new ShotPosition(i, fileName, fbl.scoppio);
            // shot elevation se presente nel modello
            try {
                shot.setElevation(fbl.shotElevation);
            } catch (Throwable t) {
                // compatibilità: campo opzionale non presente in vecchie build
            }
            shots.add(shot);
        }
        
        geophoneModel.fireTableDataChanged();
        shotModel.fireTableDataChanged();
        updatePreview();
    }
    
    /**
     * Applica la geometria al progetto
     */
    public void applyToProject(Indagine project) {
        if (project == null || project.stesa == null) {
            return;
        }
        
        // Aggiorna i parametri
        for (int i = 0; i < project.stesa.size(); i++) {
            FirstBrakeList fbl = project.stesa.get(i);
            fbl.spaz = spacing;
            fbl.spaz_in = startPosition;
            fbl.ch = numChannels;
            
            // Aggiorna la posizione e l'elevazione dello shot se presenti
            if (i < shots.size()) {
                ShotPosition sp = shots.get(i);
                fbl.scoppio = sp.getAbsolutePosition();
                try {
                    fbl.shotElevation = sp.getElevation();
                } catch (Throwable t) {
                    // compatibilità: campo opzionale
                }
            }
            
            // Assicura che il numero di canali corrisponda ai geofoni
            if (fbl.fb == null || fbl.fb.length != numChannels) {
                // Attenzione: questo re-inizializza i FirstBrake. Usare solo quando necessario.
                fbl.setChanel(numChannels);
            }

            // Trasferisci posizioni ed elevazioni dalla tabella ai FirstBrake
            int n = Math.min(numChannels, geophones.size());
            for (int ch = 0; ch < n; ch++) {
                GeophonePosition gp = geophones.get(ch);
                fbl.fb[ch].posx = gp.getAbsolutePosition();
                fbl.fb[ch].z = gp.getElevation();
            }
            // Se ci sono più canali che geofoni, completa a spaziatura regolare con quota 0
            for (int ch = geophones.size(); ch < numChannels; ch++) {
                fbl.fb[ch].posx = startPosition + (ch * spacing);
                fbl.fb[ch].z = 0.0;
            }
            // Mantieni allineata anche la lista 'linea'
            fbl.linea = new java.util.ArrayList<>(java.util.Arrays.asList(fbl.fb));
        }
    }
    
    private void autoGenerateGeophones() {
        try {
            spacing = Double.parseDouble(spacingField.getText());
            startPosition = Double.parseDouble(startPosField.getText());
            numChannels = (Integer) numChannelsSpinner.getValue();
            
            geophones.clear();
            for (int i = 0; i < numChannels; i++) {
                double absPos = startPosition + (i * spacing);
                // Create geophones with elevation = 0.0 (no elevation data)
                GeophonePosition geo = new GeophonePosition(i, absPos, 0.0);
                geophones.add(geo);
            }
            
            geophoneModel.fireTableDataChanged();
            updatePreview();
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, 
                "Parameter error: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateGeophoneCount() {
        numChannels = (Integer) numChannelsSpinner.getValue();
        autoGenerateGeophones();
    }
    
    private void updateGeometry() {
        autoGenerateGeophones();
    }
    
    private void updatePreview() {
        try {
            spacing = Double.parseDouble(spacingField.getText());
            startPosition = Double.parseDouble(startPosField.getText());
            previewPanel.setGeometry(geophones, shots, spacing, startPosition);
        } catch (NumberFormatException ex) {
            // Ignora errori durante la digitazione
        }
    }
    
    private void addShot() {
        String fileName = JOptionPane.showInputDialog(this, "Shot file name:");
        if (fileName == null || fileName.trim().isEmpty()) {
            return;
        }
        
        String posStr = JOptionPane.showInputDialog(this, "Shot position (m):", "0.0");
        if (posStr == null) {
            return;
        }
        
        try {
            double position = Double.parseDouble(posStr);
            int shotNum = shots.size();
            ShotPosition shot = new ShotPosition(shotNum, fileName, position);
            shots.add(shot);
            shotModel.fireTableDataChanged();
            updatePreview();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, 
                "Invalid position: " + posStr,
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void removeShot() {
        int selected = shotTable.getSelectedRow();
        if (selected >= 0 && selected < shots.size()) {
            shots.remove(selected);
            // Renumber shots
            for (int i = 0; i < shots.size(); i++) {
                shots.get(i).setShotNumber(i);
            }
            shotModel.fireTableDataChanged();
            updatePreview();
        }
    }
    
    private void importCSV() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Import Geometry from CSV");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("CSV Files", "csv"));
        
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            
            try {
                // Valida prima di importare
                GeometryCSVHandler.ValidationResult validation = GeometryCSVHandler.validateCSVFile(file);
                
                if (!validation.isValid()) {
                    int result = JOptionPane.showConfirmDialog(this,
                        "The file contains errors:\n" + validation.getFormattedMessage() + "\nContinue?",
                        "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                    
                    if (result != JOptionPane.YES_OPTION) {
                        return;
                    }
                }
                
                Object[] result = GeometryCSVHandler.importGeometry(file);
                @SuppressWarnings("unchecked")
                List<GeophonePosition> importedGeo = (List<GeophonePosition>) result[0];
                @SuppressWarnings("unchecked")
                List<ShotPosition> importedShots = (List<ShotPosition>) result[1];
                
                geophones.clear();
                geophones.addAll(importedGeo);
                shots.clear();
                shots.addAll(importedShots);
                
                // Aggiorna i parametri se ci sono geofoni
                if (geophones.size() > 1) {
                    double calcSpacing = geophones.get(1).getAbsolutePosition() - 
                                       geophones.get(0).getAbsolutePosition();
                    spacing = calcSpacing;
                    startPosition = geophones.get(0).getAbsolutePosition();
                    numChannels = geophones.size();
                    
                    spacingField.setText(String.format("%.2f", spacing));
                    startPosField.setText(String.format("%.2f", startPosition));
                    numChannelsSpinner.setValue(numChannels);
                }
                
                geophoneModel.fireTableDataChanged();
                shotModel.fireTableDataChanged();
                updatePreview();
                
                JOptionPane.showMessageDialog(this,
                    String.format("Imported %d geophones and %d shots", geophones.size(), shots.size()),
                    "Import completed", JOptionPane.INFORMATION_MESSAGE);
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                    "Error during import:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }
    
    private void exportCSV() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Export Geometry to CSV");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("CSV Files", "csv"));
        fileChooser.setSelectedFile(new File("geometry.csv"));
        
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            
            // Aggiungi estensione .csv se manca
            if (!file.getName().toLowerCase().endsWith(".csv")) {
                file = new File(file.getAbsolutePath() + ".csv");
            }
            
            try {
                boolean success = GeometryCSVHandler.exportGeometry(file, geophones, shots, 
                                                                    spacing, startPosition);
                if (success) {
                    JOptionPane.showMessageDialog(this,
                        "Geometry exported successfully to:\n" + file.getAbsolutePath(),
                        "Export completed", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                    "Error during export:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }
    
    private boolean validateGeometry() {
        if (geophones.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Define at least one geophone",
                "Validation", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        if (shots.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Define at least one shot",
                "Validation", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        return true;
    }
    
    public boolean isApproved() {
        return approved;
    }
    
    public double getSpacing() {
        return spacing;
    }
    
    public double getStartPosition() {
        return startPosition;
    }
    
    public List<ShotPosition> getShots() {
        return new ArrayList<>(shots);
    }
    
    // Table Models
    private class GeophoneTableModel extends AbstractTableModel {
        private final String[] columnNames = {"Channel", "Position (m)", "Elevation (m)"};
        
        @Override
        public int getRowCount() {
            return geophones.size();
        }
        
        @Override
        public int getColumnCount() {
            return columnNames.length;
        }
        
        @Override
        public String getColumnName(int column) {
            return columnNames[column];
        }
        
        @Override
        public Object getValueAt(int row, int column) {
            GeophonePosition geo = geophones.get(row);
            switch (column) {
                case 0: return geo.getChannelNumber();
                case 1: return String.format("%.2f", geo.getAbsolutePosition());
                case 2: 
                    // Show elevation only if it's different from zero
                    return Math.abs(geo.getElevation()) > 0.01 ? String.format("%.2f", geo.getElevation()) : "";
                default: return null;
            }
        }
        
        @Override
        public boolean isCellEditable(int row, int column) {
            return column == 1 || column == 2; // Position and elevation are editable
        }
        
        @Override
        public void setValueAt(Object value, int row, int column) {
            try {
                double newValue = Double.parseDouble(value.toString());
                GeophonePosition geo = geophones.get(row);
                if (column == 1) {
                    geo.setAbsolutePosition(newValue);
                } else if (column == 2) {
                    geo.setElevation(newValue);
                }
                fireTableRowsUpdated(row, row);
                updatePreview();
            } catch (NumberFormatException ex) {
                // Ignore invalid values
            }
        }
    }
    
    private class ShotTableModel extends AbstractTableModel {
        private final String[] columnNames = {"Shot", "File", "Position (m)", "Elevation (m)"};
        
        @Override
        public int getRowCount() {
            return shots.size();
        }
        
        @Override
        public int getColumnCount() {
            return columnNames.length;
        }
        
        @Override
        public String getColumnName(int column) {
            return columnNames[column];
        }
        
        @Override
        public Object getValueAt(int row, int column) {
            ShotPosition shot = shots.get(row);
            switch (column) {
                case 0: return shot.getShotNumber();
                case 1: return shot.getFileName();
                case 2: return String.format("%.2f", shot.getAbsolutePosition());
                case 3: 
                    // Show elevation only if it's different from zero
                    return Math.abs(shot.getElevation()) > 0.01 ? String.format("%.2f", shot.getElevation()) : "";
                default: return null;
            }
        }
        
        @Override
        public boolean isCellEditable(int row, int column) {
            return column == 2 || column == 3; // Position and elevation are editable
        }
        
        @Override
        public void setValueAt(Object value, int row, int column) {
            try {
                double newValue = Double.parseDouble(value.toString());
                ShotPosition shot = shots.get(row);
                if (column == 2) {
                    shot.setAbsolutePosition(newValue);
                } else if (column == 3) {
                    shot.setElevation(newValue);
                }
                fireTableRowsUpdated(row, row);
                updatePreview();
            } catch (NumberFormatException ex) {
                // Ignore invalid values
            }
        }
    }
}
