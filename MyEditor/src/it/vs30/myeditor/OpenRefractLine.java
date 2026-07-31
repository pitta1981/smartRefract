package it.vs30.myeditor;

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
