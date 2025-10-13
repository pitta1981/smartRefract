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

/**
 * Display and UI settings
 */
class OpenRefractDisplaySettings {
    public boolean isWhite = false;
    public boolean proporz = false;
    public int selectedTab = 0;
}

/**
 * Represents a trace group (FirstBrakeList equivalent)
 */
class OpenRefractTraceGroup {
    public int channelCount;
    public double spacing;
    public double spacingIn;
    public double primo;
    public double shotLocation;
    public double shotElevation; // optional elevation of the shot location
    public int AR;
    public double xsc;
    public double tAB;
    public String filePath;
    
    // Layer information
    public String strato1 = "0-0";
    public String strato2 = "0-0";
    public String strato3 = "0-0";
    public String strato1R = "0-0";
    public String strato2R = "0-0";
    public String strato3R = "0-0";
    
    // Data arrays
    public OpenRefractFirstBreak[] firstBreaks;
    public OpenRefractLine[] dromo;
    public OpenRefractLine[] dromoR;
    public OpenRefractTrace[] traces;
    public OpenRefractZoomTraceUtil[] zoomTraceUtils;
}

/**
 * Represents a first break pick
 */
class OpenRefractFirstBreak {
    public int channel;
    public int ar;
    public int layer;
    public double time;
    public double posX;
    public double z;
    public double offset;
    public boolean enabled = true;
}

/**
 * Represents a line (JRetta equivalent) for dromochroni
 */
class OpenRefractLine {
    public double a = -999;
    public double b = -999;
    
    public OpenRefractLine() {}
    
    public OpenRefractLine(double a, double b) {
        this.a = a;
        this.b = b;
    }
}

/**
 * Represents trace data
 */
class OpenRefractTrace {
    public int number;
    public int length;
    public double sampleInterval;
    public double media;
    public double pick;
    public boolean isPicked;
    
    // Base64 encoded trace values to save space
    public String valueData;
}

/**
 * Represents zoom trace utility
 */
class OpenRefractZoomTraceUtil {
    public double zoomFactor = 1.0;
    public boolean isSelected = false;
}

/**
 * Represents investigation data (Indagine equivalent)
 */
class OpenRefractInvestigation {
    public int index;
    public double xy;
    public double xy2;
    public boolean licenza = true;
    public int max3;
    public int maxR3;
}
