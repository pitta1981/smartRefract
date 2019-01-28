/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.myorg.myapi;

import java.io.Serializable;

/**
 *
 * @author pitta1981
 */
public class FirstBrake implements Serializable{
    public int chan=0;
    public int ar=0;
    public int layer=0;
    public double time;
    public double posx=0;
    public double z=0;
    public double offset= Double.POSITIVE_INFINITY;
    public boolean enabled=true;
    public FirstBrake(){
        ar=0;
        chan=1;
        time=-5.0;
    }
    public void setBrake(int ch, double t)
    {
        chan=ch;
        time=t;
    }
    public void setAR(int AndataRitorno)
    {
        ar=AndataRitorno;
    }

    public void setLayer(int l){
        layer=l;
    }
    public void setEnabled(boolean e){
        enabled=e;
    }
    
    
}
