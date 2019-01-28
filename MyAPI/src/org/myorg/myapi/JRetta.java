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
public class JRetta implements Serializable{
    public JRetta(){

    }
    public JRetta(double aa,double bb){
        a=aa;
        b=bb;

    }
    public double interc(){
        return a;
    }

    public double time(double x)
    {
        return b*x+a;
    }
    
    public double[] intersection(JRetta retta2){
        double[] result=new double[2];
        if(retta2.b!=-999&&retta2.b-this.b!=0){
            
            result[0]=(retta2.a-this.a)/(this.b-retta2.b);
            result[1]=this.time(result[0]);
            return result;
        }
        else return null;
        
    }

    public double a=-999,b=-999;
}
