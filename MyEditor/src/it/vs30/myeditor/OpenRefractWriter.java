/*
 * OpenRefract Writer for SmartRefract
 * Writes APIObject projects to the new JSON-based .orefract format
 */
package it.vs30.myeditor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Base64;
import java.util.Date;
import org.myorg.myapi.APIObject;
import org.myorg.myapi.FirstBrake;
import org.myorg.myapi.FirstBrakeList;
import org.myorg.myapi.Indagine;

/**
 * Writer for OpenRefract JSON format
 * 
 * @author SmartRefract Team
 */
public class OpenRefractWriter {
    
    /**
     * Saves an APIObject project to OpenRefract JSON format
     */
    public static void saveOpenRefractProject(File file, APIObject apiObject) throws IOException {
        // Convert APIObject to OpenRefractProject
        OpenRefractProject project = convertToOpenRefractProject(apiObject);
        
        // Serialize to JSON
        String json = SimpleJsonParser.projectToJson(project);
        
        // Write to file
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(json);
        }
    }
    
    /**
     * Converts APIObject to OpenRefractProject
     */
    private static OpenRefractProject convertToOpenRefractProject(APIObject apiObject) {
        OpenRefractProject project = new OpenRefractProject();
        
        // Basic project information
        project.version = "1.0";
        project.formatName = "OpenRefract";
        project.description = "SmartRefract seismic refraction project";
        project.createdDate = new Date();
        project.modifiedDate = new Date();
        
        // Copy project settings
        project.traceIndex = apiObject.trace_index;
        project.format = apiObject.FORMAT;
        
        // Convert display settings
        project.displaySettings = new OpenRefractDisplaySettings();
        project.displaySettings.isWhite = apiObject.is_white;
        project.displaySettings.proporz = apiObject.proporz;
        project.displaySettings.selectedTab = 0; // Default value
        
        // Convert investigation data
        if (apiObject.proj != null) {
            project.investigation = convertInvestigation(apiObject.proj);
        }
        
        // Convert trace groups
        if (apiObject.TraceGroup != null && !apiObject.TraceGroup.isEmpty()) {
            project.traceGroups = new OpenRefractTraceGroup[apiObject.TraceGroup.size()];
            for (int i = 0; i < apiObject.TraceGroup.size(); i++) {
                project.traceGroups[i] = convertTraceGroup((FirstBrakeList) apiObject.TraceGroup.get(i));
            }
        }
        
        return project;
    }
    
    /**
     * Converts Indagine to OpenRefractInvestigation
     */
    private static OpenRefractInvestigation convertInvestigation(Indagine indagine) {
        OpenRefractInvestigation investigation = new OpenRefractInvestigation();
        investigation.index = indagine.index;
        investigation.xy = indagine.xy;
        investigation.xy2 = indagine.xy2;
        investigation.licenza = indagine.licenza;
        investigation.max3 = indagine.max3;
        investigation.maxR3 = indagine.maxR3;
        return investigation;
    }
    
    /**
     * Converts FirstBrakeList to OpenRefractTraceGroup
     */
    private static OpenRefractTraceGroup convertTraceGroup(FirstBrakeList fbl) {
        OpenRefractTraceGroup traceGroup = new OpenRefractTraceGroup();
        
        // Basic properties
        traceGroup.channelCount = fbl.ch;
        traceGroup.spacing = fbl.spaz;
        traceGroup.spacingIn = fbl.spaz_in;
        traceGroup.primo = fbl.primo;
    traceGroup.shotLocation = fbl.scoppio;
    traceGroup.shotElevation = fbl.shotElevation;
        traceGroup.AR = fbl.AR;
        traceGroup.xsc = fbl.xsc;
        traceGroup.tAB = fbl.tAB;
        traceGroup.filePath = (fbl.fbp != null) ? fbl.fbp : "";
        
        // Layer information
        traceGroup.strato1 = (fbl.strato1 != null) ? fbl.strato1 : "0-0";
        traceGroup.strato2 = (fbl.strato2 != null) ? fbl.strato2 : "0-0";
        traceGroup.strato3 = (fbl.strato3 != null) ? fbl.strato3 : "0-0";
        traceGroup.strato1R = (fbl.strato1R != null) ? fbl.strato1R : "0-0";
        traceGroup.strato2R = (fbl.strato2R != null) ? fbl.strato2R : "0-0";
        traceGroup.strato3R = (fbl.strato3R != null) ? fbl.strato3R : "0-0";
        
        // Convert first breaks
        if (fbl.fb != null) {
            traceGroup.firstBreaks = new OpenRefractFirstBreak[fbl.fb.length];
            for (int i = 0; i < fbl.fb.length; i++) {
                traceGroup.firstBreaks[i] = convertFirstBreak(fbl.fb[i]);
            }
        }
        
        // Convert dromo lines
        if (fbl.dromo != null) {
            traceGroup.dromo = new OpenRefractLine[fbl.dromo.length];
            for (int i = 0; i < fbl.dromo.length; i++) {
                if (fbl.dromo[i] != null) {
                    traceGroup.dromo[i] = new OpenRefractLine(fbl.dromo[i].a, fbl.dromo[i].b);
                } else {
                    traceGroup.dromo[i] = new OpenRefractLine(); // default a=-999, b=-999
                }
            }
        }
        
        // Convert dromoR lines
        if (fbl.dromoR != null) {
            traceGroup.dromoR = new OpenRefractLine[fbl.dromoR.length];
            for (int i = 0; i < fbl.dromoR.length; i++) {
                if (fbl.dromoR[i] != null) {
                    traceGroup.dromoR[i] = new OpenRefractLine(fbl.dromoR[i].a, fbl.dromoR[i].b);
                } else {
                    traceGroup.dromoR[i] = new OpenRefractLine(); // default a=-999, b=-999
                }
            }
        }
        
        // Convert traces if available
        if (fbl.tr != null) {
            traceGroup.traces = new OpenRefractTrace[fbl.tr.length];
            for (int i = 0; i < fbl.tr.length; i++) {
                traceGroup.traces[i] = convertTrace(fbl.tr[i], i);
            }
        }
        
        return traceGroup;
    }
    
    /**
     * Converts FirstBrake to OpenRefractFirstBreak
     */
    private static OpenRefractFirstBreak convertFirstBreak(FirstBrake fb) {
        OpenRefractFirstBreak openFB = new OpenRefractFirstBreak();
        openFB.channel = fb.chan;
        openFB.ar = fb.ar;
        openFB.layer = fb.layer;
        openFB.time = fb.time;
        openFB.posX = fb.posx;
        openFB.z = fb.z;
        // Ensure finite values to prevent JSON parsing issues
        openFB.offset = Double.isFinite(fb.offset) ? fb.offset : 0.0;
        openFB.enabled = fb.enabled;
        return openFB;
    }
    
    /**
     * Converts trace data to OpenRefractTrace with Base64 encoding
     */
    private static OpenRefractTrace convertTrace(Object traceObj, int number) {
        OpenRefractTrace openTrace = new OpenRefractTrace();
        openTrace.number = number;
        
        // Handle Trace objects from MyAPI
        try {
            if (traceObj instanceof org.myorg.myapi.Trace) {
                org.myorg.myapi.Trace trace = (org.myorg.myapi.Trace) traceObj;
                
                // Extract all trace properties
                openTrace.length = trace.length;
                openTrace.sampleInterval = trace.sampleInterval;
                openTrace.media = trace.media;
                openTrace.pick = trace.getPick();
                openTrace.isPicked = trace.isPicked();
                
                // Serialize the actual seismic data values
                if (trace.value != null && trace.value.length > 0) {
                    StringBuilder valueString = new StringBuilder();
                    for (int i = 0; i < trace.value.length; i++) {
                        if (i > 0) valueString.append(",");
                        valueString.append(Double.toString(trace.value[i]));
                    }
                    openTrace.valueData = Base64.getEncoder().encodeToString(valueString.toString().getBytes());
                } else {
                    openTrace.valueData = "";
                }
                
            } else {
                // Set default values for unknown trace types
                openTrace.length = 1000;
                openTrace.sampleInterval = 0.001;
                openTrace.media = 0.0;
                openTrace.pick = 0.0;
                openTrace.isPicked = false;
                openTrace.valueData = "";
            }
        } catch (Exception e) {
            // If conversion fails, create a minimal trace
            openTrace.length = 0;
            openTrace.sampleInterval = 0.001;
            openTrace.media = 0.0;
            openTrace.pick = 0.0;
            openTrace.isPicked = false;
            openTrace.valueData = "";
        }
        
        return openTrace;
    }
}
