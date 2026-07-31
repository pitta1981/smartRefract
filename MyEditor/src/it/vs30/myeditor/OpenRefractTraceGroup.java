package it.vs30.myeditor;

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
