/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.vs30.smartRefract.utils;

import java.io.Serializable;
import java.util.ArrayList;
import org.myorg.myapi.FirstBrake;


public class FB_set implements Serializable{
        public ArrayList<FirstBrake> fb=new ArrayList();
        public int layer=0;
        public int dir=0;
        public double xshot;
        double nshot;
        public double shift;
        public boolean used;
        public boolean visible=true;

    public void AddFB(FirstBrake firstBrake) {
        fb.add(firstBrake);
        dir=(int)((firstBrake.posx-xshot)/
                Math.abs(firstBrake.posx-xshot));
    }

    
}
