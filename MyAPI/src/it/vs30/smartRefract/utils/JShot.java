/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.vs30.smartRefract.utils;

import java.io.Serializable;
import java.util.ArrayList;
import org.myorg.myapi.FirstBrake;

/**
 *
 * @author PC
 */
public class JShot implements Serializable{
    String fbpath="";
    boolean baseshot=false;
    public double xshot;
    double nshot;
    
   
    
    public JShot(){
        
    }
    
    public JShot(String file,double xshot){
        this.fbpath=file;
        this.xshot=xshot;
    }
    
    public ArrayList<FB_set> FB_sets=new ArrayList();

    public void AddFBSet(FB_set fbs) {
        FB_sets.add(fbs);
    }
    
}
