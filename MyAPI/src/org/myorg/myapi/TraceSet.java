/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.myorg.myapi;

/**
 *
 * @author user
 */
import org.jfree.data.*;

public class TraceSet {
    Trace[] traceArray;
    int number;
    /** Creates a new instance of PingSet */
    public TraceSet(int n) {
        number = n;
        traceArray = new Trace[n];
        //for(int i=0; i<n; i++){
        //  pingArray[i] = new Ping();
        //}
    }
    public void setTrace(int n, Trace t){
traceArray[n] = t;
    }
    public Trace getTrace(int n){
        return traceArray[n];
    }
public XYSeries getPickSeries(double o){
    XYSeries s = new XYSeries("");
    for (int i=0; i < number; i++){
        s.add(traceArray[i].getPick(),o*(i+1));
    }
    return s;
}
public XYSeries getTtSeries(){
    XYSeries s = new XYSeries("");
    for (int i=0; i < number; i++){
        double pl = traceArray[i].getPhoneLocation();
        //double sl = traceArray[i].getShotLocation();

        s.add(pl,traceArray[i].getPick());
    }
    return s;
}
}
