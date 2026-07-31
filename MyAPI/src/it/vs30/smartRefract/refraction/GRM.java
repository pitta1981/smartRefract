/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.vs30.smartRefract.refraction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.myorg.myapi.FirstBrake;
import org.myorg.myapi.FirstBrakeList;
import org.myorg.myapi.Indagine;

/**
 *
 * @author PC
 */
public class GRM {

    public GRM() {
    }

    public double compute_v1(double tV[],double dx){
        double slope=0.0,dslope=0.0;
        int tV_size=tV.length;
        for(int i = 0;i<tV_size-1;i++){
            dslope=(tV[i+1]-tV[i])/dx;
            slope=slope+dslope;
            
        }
        
        
        return tV_size/slope;
    }
    
    public double compute_V0(ArrayList<FirstBrakeList> firstbrakelist){
        double V0=0.0;
        int c0=0;
        for (FirstBrakeList fbl:firstbrakelist) {
                //FirstBrakeList fbl = (FirstBrakeList) stesa.get(j);
                if (fbl.dromo[0].b > -999) {
                    V0 = V0 + Math.abs(1 / fbl.dromo[0].b);
                    c0++;
                }
               

                //if(VfB_A[2]!=null) V3=Math.abs(1/fbl.dromo[2].b);
            }
            V0 = V0 / c0;
            return V0;
    }
    
    
    public double[] compute_zz(double[] tG,double v1,double v0){
        
        double[] zz=new double[tG.length];
        for(int i =0;i<tG.length;i++){
            zz[i]=tG[i]*(v0*v1/Math.sqrt((v1*v1)-(v0*v0)));
            
        }
        
        return zz;
    }
    
    
    public double[] compute_tG(FirstBrakeList tAB, FirstBrakeList tBA,double TAB, double xy, double v1) {
        double[] Tg = new double[tAB.fb.length];
        for (int j = 0; j < tAB.fb.length; j++) {
                Tg[j] = 0.5 * (tAB.getXY(tAB.spaz_in + ((j) * tAB.spaz) + (xy / 2)) + tBA.getXY(tBA.spaz_in + ((j) * tBA.spaz) - (xy / 2)) - TAB-xy/v1);
   //             os.write("\n " + j + " TV " + Tv[j]);
 //               System.out.println(j + " Tv : " + (VfB_A[lay].spaz_in + ((j - 1) * VfB_A[lay].spaz)) + " " + Tv[j] + " " + xy);
            }
        
        
        return Tg;
    }
    
    
    public double[] compute_tV(FirstBrakeList tAB, FirstBrakeList tBA,double TAB, double xy) {
        double[] Tv = new double[tAB.fb.length];
        try {

            //double TAB=VfB_A[lay].tAB;
            //double TAB = Math.max(VfB_A[lay].tAB, VfB_R[lay].tAB);
            for (int j = 0; j < tAB.fb.length; j++) {
                Tv[j] = 0.5 * (tAB.getXY(tAB.spaz_in + ((j) * tAB.spaz) + (xy / 2)) - tBA.getXY(tBA.spaz_in + ((j) * tBA.spaz) - (xy / 2)) + TAB);
   //             os.write("\n " + j + " TV " + Tv[j]);
 //               System.out.println(j + " Tv : " + (VfB_A[lay].spaz_in + ((j - 1) * VfB_A[lay].spaz)) + " " + Tv[j] + " " + xy);
            }

        } catch (Exception ex) {
            Logger.getLogger(Indagine.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Tv;
        
    }

    public Integer getBestXYTv(double[][] tV1A,double dx) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        Integer best=new Integer(0);
        int maxXY=tV1A[0].length/4;
        double[] lap=new double[tV1A.length];
        double dlap=0.0;
        for(int j=0;j<tV1A.length;j++){
            for(int i=maxXY;i<tV1A[j].length-maxXY;i++){
                dlap=1000.*(tV1A[j][i-1]-2*tV1A[j][i]+tV1A[j][i+1])/(dx*dx);
                lap[j]=lap[j]+Math.abs(dlap);
            }
        }
        double min= Double.MAX_VALUE;
        for(int j=0;j<lap.length;j++){
            if(lap[j]<min){
                min=lap[j];
                best=j;
            }
        }
        return best;
        
    }

    public double[] FirstBrakeToDouble(FirstBrake[] firstBrake) {
       double[] tt=new double[firstBrake.length];
       for (int i = 0; i<firstBrake.length; i++){
           tt[i]=firstBrake[i].time;
       }
       
       
       return tt;
        //To change body of generated methods, choose Tools | Templates.
    }

    public double[] compute_tG(double[] tG, FirstBrakeList tAB, FirstBrakeList tBA, double TAB, double xy, double v1) {
         //To change body of generated methods, choose Tools | Templates.
        double[] Tg = new double[tAB.fb.length];
        for (int j = 0; j < tAB.fb.length; j++) {
                Tg[j] = 0.5 * (tAB.getXY(tAB.spaz_in + ((j) * tAB.spaz) + (xy / 2))-tG[j] + tBA.getXY(tBA.spaz_in + ((j) * tBA.spaz) - (xy / 2))-tG[j] - TAB -xy/v1);
   //             os.write("\n " + j + " TV " + Tv[j]);
 //               System.out.println(j + " Tv : " + (VfB_A[lay].spaz_in + ((j - 1) * VfB_A[lay].spaz)) + " " + Tv[j] + " " + xy);
            }
        
        
        return Tg;
    }

    public Integer getBestXYTg(double[][] tV1A, double dx) {
        Integer best=new Integer(0);
        int maxXY=tV1A[0].length/4;
        double[] lap=new double[tV1A.length];
        double dlap=0.0;
        for(int j=0;j<tV1A.length;j++){
            for(int i=maxXY;i<tV1A[j].length-maxXY;i++){
                dlap=1000.*(tV1A[j][i-1]-2*tV1A[j][i]+tV1A[j][i+1])/(dx*dx);
                lap[j]=lap[j]+Math.abs(dlap);
            }
        }
        double max= Double.MIN_VALUE;
        for(int j=0;j<lap.length;j++){
            if(lap[j]>max){
                max=lap[j];
                best=j;
            }
        }
        return best;
        
    

        
    }

  
}
