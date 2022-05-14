/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.vs30.smartRefract.utils;

/**
 *
 * @author Simone Pittaluga
 */
public class Bool {
    private boolean value;
    
    public Bool(){
        
    }

    public Bool(boolean b) {
        value=b;
    }
    
    public boolean getValue()
    {
        return value;
    }
    
    public void setValue(boolean b){
        value=b;
    }
}
