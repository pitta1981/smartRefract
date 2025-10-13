/*
 * OpenRefract Loader for SmartRefract
 * Loads APIObject projects from the new JSON-based .orefract format
 */
package it.vs30.myeditor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import org.myorg.myapi.APIObject;
import org.myorg.myapi.FirstBrake;
import org.myorg.myapi.FirstBrakeList;
import org.myorg.myapi.Indagine;
import org.myorg.myapi.JRetta;

/**
 * Loader for OpenRefract JSON format
 * 
 * @author SmartRefract Team
 */
public class OpenRefractLoader {
    
    /**
     * Loads an APIObject project from OpenRefract JSON format
     */
    public static APIObject loadOpenRefractProject(File file) throws IOException {
        // Read file content
        String json = new String(Files.readAllBytes(file.toPath()));
        
        // Parse JSON to OpenRefractProject
        OpenRefractProject project;
        try {
            project = SimpleJsonParser.parseProject(json);
        } catch (Exception e) {
            throw new IOException("Failed to parse OpenRefract file: " + e.getMessage(), e);
        }
        
        // Convert to APIObject
        return convertToAPIObject(project, file);
    }
    
    /**
     * Converts OpenRefractProject to APIObject
     */
    private static APIObject convertToAPIObject(OpenRefractProject project, File sourceFile) {
        APIObject apiObject = new APIObject();

        // Basic project information
        apiObject.trace_index = project.traceIndex;
        apiObject.FORMAT = project.format;
        apiObject.proj_file = sourceFile;

        // Convert display settings
        if (project.displaySettings != null) {
            apiObject.is_white = project.displaySettings.isWhite;
            apiObject.proporz = project.displaySettings.proporz;
            apiObject.selected_Tab = project.displaySettings.selectedTab;
        }

        // Convert investigation data
        if (project.investigation != null) {
            apiObject.proj = convertInvestigation(project.investigation);
        } else {
            apiObject.proj = new Indagine(); // Create default investigation
        }

        // Convert trace groups
        if (project.traceGroups != null && project.traceGroups.length > 0) {
            apiObject.TraceGroup = new ArrayList<>();
            for (OpenRefractTraceGroup traceGroup : project.traceGroups) {
                FirstBrakeList fbl = convertTraceGroup(traceGroup);
                apiObject.TraceGroup.add(fbl);
            }

            // Debug: stampa quante trace group e tracce sono state caricate
            System.out.println("[OpenRefractLoader] TraceGroup count: " + apiObject.TraceGroup.size());
            for (int i = 0; i < apiObject.TraceGroup.size(); i++) {
                FirstBrakeList fbl = apiObject.TraceGroup.get(i);
                System.out.println("[OpenRefractLoader] TraceGroup[" + i + "] tr.length: " + (fbl.tr != null ? fbl.tr.length : 0));
            }

            // Set current first brake list to the first one (or selected index)
            int index = Math.min(Math.max(apiObject.trace_index, 0), apiObject.TraceGroup.size() - 1);
            if (index >= 0) {
                apiObject.fb = apiObject.TraceGroup.get(index);
                if (apiObject.fb.tr != null && apiObject.fb.tr.length > 0) {
                    apiObject.tr = apiObject.fb.tr;
                } else {
                    System.out.println("[OpenRefractLoader] Nessuna traccia trovata in fb.tr!");
                }
            } else {
                System.out.println("[OpenRefractLoader] Indice TraceGroup non valido: " + index);
            }

            // Sync project data
            apiObject.proj.stesa = apiObject.TraceGroup;

        } else {
            apiObject.TraceGroup = new ArrayList<>();
            apiObject.fb = new FirstBrakeList();
            apiObject.tr = null;
            System.out.println("[OpenRefractLoader] Nessun TraceGroup trovato nel progetto!");
        }

        // Sincronizza e imposta traccia corrente per il rendering
        apiObject.sync();
        if (apiObject.TraceGroup != null && !apiObject.TraceGroup.isEmpty()) {
            int idx = Math.min(Math.max(apiObject.trace_index, 0), apiObject.TraceGroup.size() - 1);
            apiObject.fb = apiObject.TraceGroup.get(idx);
            apiObject.tr = apiObject.fb.tr;
            System.out.println("[OpenRefractLoader] Impostato fb e tr dalla TraceGroup all'indice: " + idx);
            System.out.println("[OpenRefractLoader] tr.length: " + (apiObject.tr != null ? apiObject.tr.length : 0));

            // Imposta in_file_l in base ai path delle tracce (come in OpenPrj)
            apiObject.in_file_l = new File[apiObject.TraceGroup.size()];
            for (int i = 0; i < apiObject.TraceGroup.size(); i++) {
                FirstBrakeList fbl = apiObject.TraceGroup.get(i);
                if (fbl.fbp != null && !fbl.fbp.isEmpty()) {
                    apiObject.in_file_l[i] = new File(fbl.fbp);
                } else {
                    apiObject.in_file_l[i] = null;
                }
            }
        } else {
            System.out.println("[OpenRefractLoader] TraceGroup vuoto dopo sync!");
        }

        return apiObject;
    }
    
    /**
     * Converts OpenRefractInvestigation to Indagine
     */
    private static Indagine convertInvestigation(OpenRefractInvestigation investigation) {
        Indagine indagine = new Indagine();
        indagine.index = investigation.index;
        indagine.xy = investigation.xy;
        indagine.xy2 = investigation.xy2;
        indagine.licenza = investigation.licenza;
        indagine.max3 = investigation.max3;
        indagine.maxR3 = investigation.maxR3;
        return indagine;
    }
    
    /**
     * Converts OpenRefractTraceGroup to FirstBrakeList
     */
    private static FirstBrakeList convertTraceGroup(OpenRefractTraceGroup traceGroup) {
        FirstBrakeList fbl = new FirstBrakeList();
        
        // Basic properties
        fbl.ch = traceGroup.channelCount;
        fbl.spaz = traceGroup.spacing;
        fbl.spaz_in = traceGroup.spacingIn;
        fbl.primo = traceGroup.primo;
    fbl.scoppio = traceGroup.shotLocation;
    fbl.shotElevation = Double.isNaN(traceGroup.shotElevation) ? 0.0 : traceGroup.shotElevation;
        fbl.AR = traceGroup.AR;
        fbl.xsc = traceGroup.xsc;
        fbl.tAB = traceGroup.tAB;
        // Normalizza i path per evitare backslash errati nei file .orefract
        if (traceGroup.filePath != null) {
            fbl.fbp = traceGroup.filePath.replace("\\", "/");
        } else {
            fbl.fbp = "";
        }
        
        // Layer information
        fbl.strato1 = (traceGroup.strato1 != null) ? traceGroup.strato1 : "0-0";
        fbl.strato2 = (traceGroup.strato2 != null) ? traceGroup.strato2 : "0-0";
        fbl.strato3 = (traceGroup.strato3 != null) ? traceGroup.strato3 : "0-0";
        fbl.strato1R = (traceGroup.strato1R != null) ? traceGroup.strato1R : "0-0";
        fbl.strato2R = (traceGroup.strato2R != null) ? traceGroup.strato2R : "0-0";
        fbl.strato3R = (traceGroup.strato3R != null) ? traceGroup.strato3R : "0-0";
        
        // Convert first breaks
        if (traceGroup.firstBreaks != null && traceGroup.firstBreaks.length > 0) {
            fbl.fb = new FirstBrake[traceGroup.firstBreaks.length];
            fbl.linea = new ArrayList<>();
            for (int i = 0; i < traceGroup.firstBreaks.length; i++) {
                fbl.fb[i] = convertFirstBreak(traceGroup.firstBreaks[i]);
                fbl.linea.add(fbl.fb[i]);
            }
        } else if (fbl.ch > 0) {
            // Create default first breaks if none exist but we have channels
            fbl.setChanel(fbl.ch);
        }
        
        // Convert dromo lines
        if (traceGroup.dromo != null && traceGroup.dromo.length > 0) {
            fbl.dromo = new JRetta[traceGroup.dromo.length];
            for (int i = 0; i < traceGroup.dromo.length; i++) {
                fbl.dromo[i] = new JRetta(traceGroup.dromo[i].a, traceGroup.dromo[i].b);
            }
        } else {
            // Create default dromo array
            fbl.dromo = new JRetta[3];
            for (int i = 0; i < 3; i++) {
                fbl.dromo[i] = new JRetta();
            }
        }
        
        // Convert dromoR lines
        if (traceGroup.dromoR != null && traceGroup.dromoR.length > 0) {
            fbl.dromoR = new JRetta[traceGroup.dromoR.length];
            for (int i = 0; i < traceGroup.dromoR.length; i++) {
                fbl.dromoR[i] = new JRetta(traceGroup.dromoR[i].a, traceGroup.dromoR[i].b);
            }
        } else {
            // Create default dromoR array
            fbl.dromoR = new JRetta[3];
            for (int i = 0; i < 3; i++) {
                fbl.dromoR[i] = new JRetta();
            }
        }
        
        // Convert traces with full data reconstruction
        if (traceGroup.traces != null && traceGroup.traces.length > 0) {
            fbl.tr = new org.myorg.myapi.Trace[traceGroup.traces.length];
            for (int i = 0; i < traceGroup.traces.length; i++) {
                fbl.tr[i] = convertTrace(traceGroup.traces[i]);
            }
            System.out.println("Loaded " + traceGroup.traces.length + " traces with full seismic data");
            // Inizializza zoomTr da JSON (zoomTraceUtils)
            fbl.zoomTr = new it.vs30.smartRefract.utils.ZoomTraceUtil[fbl.tr.length];
            for (int i = 0; i < fbl.tr.length; i++) {
                fbl.zoomTr[i] = new it.vs30.smartRefract.utils.ZoomTraceUtil();
                if (traceGroup.zoomTraceUtils != null && traceGroup.zoomTraceUtils.length == fbl.tr.length) {
                    fbl.zoomTr[i].zoom_factor = traceGroup.zoomTraceUtils[i].zoomFactor;
                    fbl.zoomTr[i].is_selected = traceGroup.zoomTraceUtils[i].isSelected;
                }
            }
        }
        
        return fbl;
    }
    
    /**
     * Converts OpenRefractFirstBreak to FirstBrake
     */
    private static FirstBrake convertFirstBreak(OpenRefractFirstBreak openFB) {
        FirstBrake fb = new FirstBrake();
        fb.chan = openFB.channel;
        fb.ar = openFB.ar;
        fb.layer = openFB.layer;
        fb.time = openFB.time;
        fb.posx = openFB.posX;
        fb.z = openFB.z;
        fb.offset = openFB.offset;
        fb.enabled = openFB.enabled;
        return fb;
    }
    
    /**
     * Converts OpenRefractTrace to org.myorg.myapi.Trace with full data reconstruction
     */
    private static org.myorg.myapi.Trace convertTrace(OpenRefractTrace openTrace) {
        // Crea una nuova Trace con la lunghezza corretta
        org.myorg.myapi.Trace trace = new org.myorg.myapi.Trace(openTrace.length);
        trace.number = openTrace.number;
        trace.sampleInterval = openTrace.sampleInterval;
        trace.media = openTrace.media;

        // Ripristina pick e isPicked status in modo semplice e robusto
        if (openTrace.isPicked) {
            trace.setPick(openTrace.pick);
        } else {
            trace.setPick(0.0); // Nessun pick
        }
        // Ricostruisci i dati sismici
        if (openTrace.valueData != null && !openTrace.valueData.isEmpty()) {
            try {
                byte[] decodedBytes = java.util.Base64.getDecoder().decode(openTrace.valueData);
                String valueString = new String(decodedBytes);
                String[] values = valueString.split(",");
                int actualLength = Math.min(values.length, trace.value.length);
                for (int i = 0; i < actualLength; i++) {
                    double v = 0.0;
                    try {
                        v = Double.parseDouble(values[i]);
                    } catch (Exception e) {
                        // fallback: zero
                    }
                    trace.set(i, v);
                }
                // Aggiorna la lunghezza se necessario
                trace.length = actualLength;
            } catch (Exception e) {
                // fallback: array a zero
                for (int i = 0; i < trace.value.length; i++) trace.set(i, 0.0);
            }
        } else {
            for (int i = 0; i < trace.value.length; i++) trace.set(i, 0.0);
        }
        // Calcola maxValue per il corretto ridimensionamento in TraceView
        double maxVal = 0;
        for (double v : trace.value) {
            maxVal = Math.max(maxVal, Math.abs(v));
        }
        trace.setMaxValue(maxVal);
        return trace;
    }
}
