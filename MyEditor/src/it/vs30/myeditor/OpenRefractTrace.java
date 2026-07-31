package it.vs30.myeditor;

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
