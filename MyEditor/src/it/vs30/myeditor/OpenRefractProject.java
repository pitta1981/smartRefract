/*
 * Data classes for OpenRefract JSON format
 */
package it.vs30.myeditor;

import java.util.Date;

/**
 * Root class representing an OpenRefract project file
 */
public class OpenRefractProject {
    public String version;
    public String formatName = "OpenRefract";
    public Date createdDate;
    public Date modifiedDate;
    public String description;
    
    // Project settings
    public OpenRefractDisplaySettings displaySettings;
    public int traceIndex;
    public int format;
    
    // Main data
    public OpenRefractTraceGroup[] traceGroups;
    public OpenRefractInvestigation investigation;
    
    public OpenRefractProject() {
        this.version = "1.0";
        this.createdDate = new Date();
        this.modifiedDate = new Date();
        this.displaySettings = new OpenRefractDisplaySettings();
    }
}
