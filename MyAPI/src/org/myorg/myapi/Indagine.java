/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myorg.myapi;

//import Key.KeyObj;
import it.vs30.smartRefract.refraction.GRM;
import it.vs30.smartRefract.utils.Bool;
import it.vs30.smartRefract.utils.FB_set;
import it.vs30.smartRefract.utils.JShot;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

//import org.openide.LifecycleManager;
/**
 *
 * @author pitta1981
 */
public class Indagine {

    public ArrayList<JShot> shots = new ArrayList<JShot>();
    public ArrayList<FirstBrakeList> stesa = null;

    public int index = 0;
    public FirstBrakeList fbA = null;
    public FirstBrakeList fbR = null;
    public FirstBrakeList[] VfB_A = new FirstBrakeList[4];
    public FirstBrakeList[] VfB_R = new FirstBrakeList[4];
    public FirstBrakeList[] man_Phant_and = new FirstBrakeList[2];
    public FirstBrakeList[] man_Phant_rit = new FirstBrakeList[2];
    public double xy = 0.0;
    public double xy2 = 0.0;
    public FileOutputStream logs;
    OutputStreamWriter os;
    public boolean licenza = true;
    public Bool[] useManPhant = new Bool[2];
    
    public int max3 = 0, maxR3 = 0;


    public Indagine() {

        xy = 0.0;
        xy2 = 0.0;

        Tg = new double[1];
        Zg = new double[1];
        try {
            String userHome = "user.home";

            // We get the path by getting the system property with the 
            // defined key above.
            String path = System.getProperty(userHome);
            boolean success = (new File(path + "/smartRefract-data")).mkdir();

            logs = new FileOutputStream(path + "/smartRefract-data/" + "logs.txt");
            os = new OutputStreamWriter(logs);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Indagine.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (os == null) {
            // Il resto della classe scrive su os senza controlli: con la home non
            // scrivibile ogni elaborazione moriva di NullPointerException.
            os = new OutputStreamWriter(new java.io.OutputStream() {
                @Override
                public void write(int b) {
                }
            });
        }

        this.useManPhant[0] = new Bool(false);
        this.useManPhant[1] = new Bool(false);

        for (int i = 0; i < man_Phant_rit.length; i++) {
            man_Phant_rit[i] = new FirstBrakeList();
        }
        for (int i = 0; i < man_Phant_and.length; i++) {
            man_Phant_and[i] = new FirstBrakeList();
        }

        stesa = new ArrayList();
        for (int i = 0; i < 4; i++) {
            VfB_A[i] = new FirstBrakeList();
            VfB_R[i] = new FirstBrakeList();
        }

    }

    public void update(FirstBrakeList fbl) {
        //System.out.println("1 - index upd :"+index);
        stesa.set(index, fbl);
        //System.out.println("2 - index upd :"+index);
    }
    
    ///
    // Trasla le dromocrone ricostruite per ridurre la differenza nel tempo reciproco
    
    public void applyDeltaTAB(double dTAB, int layer)
    {
        if(dTAB >0)
        {
            for(int i= 0; i<this.VfB_A[layer].ch; i++)
                this.VfB_A[layer].fb[i].time = this.VfB_A[layer].fb[i].time - dTAB;
        }
        else
        {
            for(int i= 0; i<this.VfB_A[layer].ch; i++)
                this.VfB_R[layer].fb[i].time = this.VfB_R[layer].fb[i].time - dTAB;
        }
    }
    
    
    public void setdromo() {
        String[] split = null;
        max3 = 0;
        maxR3 = 0;
        int in, fi;
        // ObjCp ocp = new ObjCp();
        for (int i = 0; i < stesa.size(); i++) {

            if (!(stesa.get(i)).strato1.equals("0-0")) {
                (stesa.get(i)).dromo[0] = new JRetta();
                split = (stesa.get(i)).strato1.split("-");
                in = Integer.parseInt(split[0]);
                fi = Integer.parseInt(split[1]);
                int k, f;
                if (in < fi) {
                    k = in;
                    f = fi;
                } else {
                    k = fi;
                    f = in;
                }
                double[][] data = new double[f - k + 2][2];
                int index = 0;
                for (int j = k; j <= f; j++) {
                    data[index][0] = (stesa.get(i)).fb[j].posx;
                    data[index][1] = (stesa.get(i)).fb[j].time;
                    index++;
                }
                data[index][0] = (stesa.get(i)).scoppio;
                data[index][1] = 0.0;
                index++;
                try {
                    double[] regr = FirstBrakeList.getOLSRegression(data);
                    (stesa.get(i)).dromo[0].b = regr[1];
                    (stesa.get(i)).dromo[0].a = regr[0];
                } catch (Exception ex) {
                    System.out.println("getOLS Error");
                }

                //(stesa.get(i)).dromo[0].b = 1 / ((stesa.get(i)).varianza(in, fi) / (stesa.get(i)).covarianza(in, fi));
                //(stesa.get(i)).dromo[0].a = (stesa.get(i)).media(in, fi) - (stesa.get(i)).dromo[0].b * (stesa.get(i)).mediax(in, fi);
                // fiB.dromo[0]=(JRetta)ocp.copy(dromo[0]);
                (stesa.get(i)).setLayer(in, fi, 1);
                // fiB.strato1=strato1.getText();

            } else {
                (stesa.get(i)).dromo[0] = new JRetta();
                (stesa.get(i)).dromo[0].b = -999;
                (stesa.get(i)).dromo[0].a = -999;

                // fiB.dromo[0]=(JRetta)ocp.copy(dromo[0]);
                (stesa.get(i)).setLayer(0, 0, 1);
                //fiB.strato1=strato1.getText();

            }
            if (!(stesa.get(i)).strato2.equals("0-0")) {
                (stesa.get(i)).dromo[1] = new JRetta();
                split = (stesa.get(i)).strato2.split("-");
                in = Integer.parseInt(split[0]);
                fi = Integer.parseInt(split[1]);

                int k, f;
                if (in < fi) {
                    k = in;
                    f = fi;
                } else {
                    k = fi;
                    f = in;
                }
                double[][] data = new double[f - k + 1][2];
                int index = 0;
                for (int j = k; j <= f; j++) {
                    data[index][0] = (stesa.get(i)).fb[j].posx;
                    data[index][1] = (stesa.get(i)).fb[j].time;
                    index++;
                }

                try {
                    double[] regr = FirstBrakeList.getOLSRegression(data);
                    (stesa.get(i)).dromo[1].b = regr[1];
                    (stesa.get(i)).dromo[1].a = regr[0];
                    (stesa.get(i)).setLayer(in, fi, 2);
                    // fiB.strato1=strato1.getText();
                } catch (Exception ex) {
                    System.out.println("getOLS Error");
                }

            } else {
                (stesa.get(i)).dromo[1] = new JRetta();
                (stesa.get(i)).dromo[1].b = -999;
                (stesa.get(i)).dromo[1].a = -999;
                (stesa.get(i)).setLayer(0, 0, 2);

            }
            if (!(stesa.get(i)).strato3.equals("0-0")) {
                (stesa.get(i)).dromo[2] = new JRetta();
                split = (stesa.get(i)).strato3.split("-");
                in = Integer.parseInt(split[0]);
                fi = Integer.parseInt(split[1]);

                int k, f;
                if (in < fi) {
                    k = in;
                    f = fi;
                } else {
                    k = fi;
                    f = in;
                }
                max3 = max3 + Math.abs(in - fi);
                double[][] data = new double[f - k + 1][2];
                int index = 0;
                for (int j = k; j <= f; j++) {
                    data[index][0] = (stesa.get(i)).fb[j].posx;
                    data[index][1] = (stesa.get(i)).fb[j].time;
                    index++;
                }

                try {
                    double[] regr = FirstBrakeList.getOLSRegression(data);
                    (stesa.get(i)).dromo[2].b = regr[1];
                    (stesa.get(i)).dromo[2].a = regr[0];
                } catch (IllegalArgumentException ex) {
                    (stesa.get(i)).dromo[2].b = -999;
                    (stesa.get(i)).dromo[2].a = -999;
                    System.out.println("getOLS Error: " + ex.getMessage());
                }
                (stesa.get(i)).setLayer(in, fi, 3);
                // fiB.strato1=strato1.getText();

            } else {
                (stesa.get(i)).dromo[2] = new JRetta();
                (stesa.get(i)).dromo[2].b = -999;
                (stesa.get(i)).dromo[2].a = -999;

                (stesa.get(i)).setLayer(0, 0, 3);
                //fiB.strato1=strato1.getText();

            }

            if (!(stesa.get(i)).strato1R.equals("0-0")) {
                (stesa.get(i)).dromoR[0] = new JRetta();
                split = (stesa.get(i)).strato1R.split("-");
                in = Integer.parseInt(split[0]);
                fi = Integer.parseInt(split[1]);
                int k, f;
                if (in < fi) {
                    k = in;
                    f = fi;
                } else {
                    k = fi;
                    f = in;
                }
                double[][] data = new double[f - k + 2][2];
                int index = 0;
                for (int j = k; j <= f; j++) {
                    data[index][0] = (stesa.get(i)).fb[j].posx;
                    data[index][1] = (stesa.get(i)).fb[j].time;
                    index++;
                }
                data[index][0] = (stesa.get(i)).scoppio;
                data[index][1] = 0.0;
                index++;

                try {
                    double[] regr = FirstBrakeList.getOLSRegression(data);
                    (stesa.get(i)).dromoR[0].b = regr[1];
                    (stesa.get(i)).dromoR[0].a = regr[0];
                } catch (IllegalArgumentException ex) {
                    (stesa.get(i)).dromoR[0].b = -999;
                    (stesa.get(i)).dromoR[0].a = -999;
                    System.out.println("getOLS Error: " + ex.getMessage());
                }

                (stesa.get(i)).setLayer(in, fi, 1);
                // fiB.strato1=strato1.getText();

            } else {
                (stesa.get(i)).dromoR[0] = new JRetta();
                (stesa.get(i)).dromoR[0].b = -999;
                (stesa.get(i)).dromoR[0].a = -999;
                (stesa.get(i)).setLayer(0, 0, 1);
                //fiB.strato1=strato1.getText();

            }
            if (!(stesa.get(i)).strato2R.equals("0-0")) {
                (stesa.get(i)).dromoR[1] = new JRetta();
                split = (stesa.get(i)).strato2R.split("-");
                in = Integer.parseInt(split[0]);
                fi = Integer.parseInt(split[1]);

                int k, f;
                if (in < fi) {
                    k = in;
                    f = fi;
                } else {
                    k = fi;
                    f = in;
                }
                double[][] data = new double[f - k + 1][2];
                int index = 0;
                for (int j = k; j <= f; j++) {
                    data[index][0] = (stesa.get(i)).fb[j].posx;
                    data[index][1] = (stesa.get(i)).fb[j].time;
                    index++;
                }

                try {
                    double[] regr = FirstBrakeList.getOLSRegression(data);
                    (stesa.get(i)).dromoR[1].b = regr[1];
                    (stesa.get(i)).dromoR[1].a = regr[0];
                } catch (IllegalArgumentException ex) {
                    (stesa.get(i)).dromoR[1].b = -999;
                    (stesa.get(i)).dromoR[1].a = -999;
                    System.out.println("getOLS Error: " + ex.getMessage());
                }
                (stesa.get(i)).setLayer(in, fi, 2);
                
                
// fiB.strato1=strato1.getText();

            } else {
                (stesa.get(i)).dromoR[1] = new JRetta();
                (stesa.get(i)).dromoR[1].b = -999;
                (stesa.get(i)).dromoR[1].a = -999;

                (stesa.get(i)).setLayer(0, 0, 2);
                //fiB.strato1=strato1.getText();

            }
            if (!(stesa.get(i)).strato3R.equals("0-0")) {
                (stesa.get(i)).dromoR[2] = new JRetta();
                split = (stesa.get(i)).strato3R.split("-");
                in = Integer.parseInt(split[0]);
                fi = Integer.parseInt(split[1]);

                int k, f;
                if (in < fi) {
                    k = in;
                    f = fi;
                } else {
                    k = fi;
                    f = in;
                }
                maxR3 = maxR3 + Math.abs(in - fi);
                double[][] data = new double[f - k + 1][2];
                int index = 0;
                for (int j = k; j <= f; j++) {
                    data[index][0] = (stesa.get(i)).fb[j].posx;
                    data[index][1] = (stesa.get(i)).fb[j].time;
                    index++;
                }

                try {
                    double[] regr = FirstBrakeList.getOLSRegression(data);
                    (stesa.get(i)).dromoR[2].b = regr[1];
                    (stesa.get(i)).dromoR[2].a = regr[0];
                } catch (IllegalArgumentException ex) {
                    (stesa.get(i)).dromoR[2].b = -999;
                    (stesa.get(i)).dromoR[2].a = -999;
                    System.out.println("getOLS Error: " + ex.getMessage());
                }
                (stesa.get(i)).setLayer(in, fi, 3);

            } else {
                (stesa.get(i)).dromoR[2] = new JRetta();
                (stesa.get(i)).dromoR[2].b = -999;
                (stesa.get(i)).dromoR[2].a = -999;
                (stesa.get(i)).setLayer(0, 0, 3);

            }

            // (stesa.get(i)).intersect();
        }
    }

    public void init_man_phantoming(int chanel, ArrayList<FirstBrakeList> mStesa, boolean forceInit) {
        //Se la traccia ricostruita è già stata inizializata controlla che sia completa
        //altrimenti la inizializza

        if (forceInit || man_phant_is_Empty()) {
            init_man_phantoming(
                    chanel);
            int i = 0;
            for (FirstBrake fb : mStesa.get(0).fb) {
                for (FirstBrakeList fbl : man_Phant_and) {
                    fbl.fb[i].posx = fb.posx;
                    fbl.fb[i].z = fb.z;
                    fbl.fb[i].time = -5;
                    fbl.spaz = mStesa.get(0).spaz;
                    fbl.spaz_in = mStesa.get(0).spaz_in;
                }
                for (FirstBrakeList fbl : man_Phant_rit) {
                    fbl.fb[i].posx = fb.posx;
                    fbl.fb[i].z = fb.z;
                    fbl.fb[i].time = -5;
                    fbl.spaz = mStesa.get(0).spaz;
                    fbl.spaz_in = mStesa.get(0).spaz_in;
                }

                i++;
            }

        } else {
            //init_man_phantoming(chanel);
            int i = 0;
            for (FirstBrake fb : mStesa.get(0).fb) {
                for (int k = 0; k < man_Phant_and.length; k++) {
                    //if (fbl.fb[i].time < 0) {
                    // fbl.
                    if (man_phant_is_Empty(k)) {
                        FirstBrakeList fbl = man_Phant_and[k];

                        fbl.fb[i].posx = fb.posx;
                        fbl.fb[i].z = fb.z;

                        if (fbl.fb[i].time >= 0) {
                            fbl.fb[i].time = -5;
                        }
                        fbl.spaz = mStesa.get(0).spaz;
                        fbl.spaz_in = mStesa.get(0).spaz_in;
                    }
                }
                for (int k = 0; k < man_Phant_rit.length; k++) {
                    //for (FirstBrakeList fbl : man_Phant_rit) {
                    //if (fbl.fb[i].time < 0) {
                    if (man_phant_is_Empty(k)) {
                        FirstBrakeList fbl = man_Phant_rit[k];

                        fbl.fb[i].posx = fb.posx;
                        fbl.fb[i].z = fb.z;
                        if (fbl.fb[i].time >= 0) {
                            fbl.fb[i].time = -5;
                        }

                        fbl.spaz = mStesa.get(0).spaz;
                        fbl.spaz_in = mStesa.get(0).spaz_in;
                    }
                }

                i++;
            }
        }
    }

    public boolean man_phant_is_Empty() {

        for (FirstBrakeList fbl : man_Phant_and) {
            if (fbl.fb.length > 0) {
                return false;
            }
            for (FirstBrake fb : fbl.fb) {
                if (fb.time > 0) {
                    return false;
                }
            }
        }

        for (FirstBrakeList fbl : man_Phant_rit) {
            if (fbl.fb.length > 0) {
                return false;
            }
            for (FirstBrake fb : fbl.fb) {
                if (fb.time > 0) {
                    return false;
                }
            }
        }

        return true;
    }

    public boolean man_phant_is_Empty(int index_phant) {

        FirstBrakeList fbl = man_Phant_and[index_phant];

        if (fbl.fb.length == 0) {
            return true;
        }

        for (FirstBrake fb : fbl.fb) {
            if (fb.time > 0) {
                return false;
            }
        }

        fbl = man_Phant_rit[index_phant];
        if (fbl.fb.length == 0) {
            return true;
        }
        for (FirstBrake fb : fbl.fb) {
            if (fb.time > 0) {
                return false;
            }
        }

        return true;
    }

    public void init_man_phantoming(int chanel) {
        for (int i = 0; i < man_Phant_rit.length; i++) {
            if (man_Phant_rit[i] != null) {
                man_Phant_rit[i] = new FirstBrakeList(chanel);
            }
        }
        for (int i = 0; i < man_Phant_and.length; i++) {
            if (man_Phant_and[i] != null) {
                man_Phant_and[i] = new FirstBrakeList(chanel);
            }
        }
    }

    public void phantom1() {
        double xsc_A = -999;
        double xsc_R = 999;
        int i_A = 0, i_R = 0;
        try {
            for (int j = 0; j < stesa.size(); j++) {
                FirstBrakeList fbl = stesa.get(j);
                VfB_A[1] = new FirstBrakeList(fbl.fb.length);

                VfB_R[1] = new FirstBrakeList(fbl.fb.length);

                if (!fbl.strato3.equals("0-0")) {
                    VfB_A[2] = new FirstBrakeList(fbl.fb.length);
                    VfB_R[2] = new FirstBrakeList(fbl.fb.length);

                }

                if (fbl.scoppio < fbl.spaz_in && fbl.scoppio > xsc_A) {
                    xsc_A = fbl.scoppio;
                    i_A = j;
                } else if (fbl.scoppio > fbl.spaz_in + ((fbl.fb.length - 2) * fbl.spaz) && fbl.scoppio < xsc_R) {
                    xsc_R = fbl.scoppio;
                    i_R = j;
                }
                VfB_A[1].spaz = fbl.spaz;
                VfB_R[1].spaz = fbl.spaz;
                VfB_A[1].spaz_in = fbl.spaz_in;
                VfB_R[1].spaz_in = fbl.spaz_in;

            }
            VfB_A[1].scoppio = xsc_A;
            VfB_R[1].scoppio = xsc_R;
        } catch (Exception e1) {
            System.out.println("Eccezione - phantom1 1 " + e1.getMessage());
        }
        System.out.println("- Phantom 1 - Scoppio A : " + xsc_A + " Scoppio B :" + xsc_R);
        ObjCp ocp = new ObjCp();

        try {
            fbA = (FirstBrakeList) ocp.copy(stesa.get(i_A));
            fbR = (FirstBrakeList) ocp.copy(stesa.get(i_R));

            for (int i = 1; i < (stesa.get(i_A)).fb.length; i++) {
                if (fbA.fb[i].layer == 2) {
                    //System.out.println(i+" "+(fbA.fb[i].time)+(fbA.dromo[1].a-fbA.dromo[1].a));
                    VfB_A[1].setFB(i, (fbA.fb[i].time) + (fbA.dromo[1].a - fbA.dromo[1].a));
                    VfB_A[1].fb[i].layer = 2;
                } else {
                    VfB_A[1].fb[i].layer = -2;
                    VfB_A[1].setFB(i, fbA.dromo[1].time(((i - 1) * fbA.spaz + fbA.spaz_in)/*-fbA.scoppio*/));
                    //System.out.println(i+" "+fbA.dromo[1].time(((i-1)*fbA.spaz+fbA.spaz_in)-fbA.scoppio));
                }

                if (fbA.fb[i].layer == 3) {
                    //System.out.println(i+" "+(fbA.fb[i].time)+(fbA.dromo[1].a-fbA.dromo[1].a));
                    VfB_A[2].setFB(i, (fbA.fb[i].time) + (fbA.dromo[2].a - fbA.dromo[2].a));
                } else {
                    //VfB_A[2].setFB(i,fbA.dromo[2].time(((i-1)*fbA.spaz+fbA.spaz_in)));
                    System.out.println(i + " phant 3 " + fbA.dromo[2].time(((i - 1) * fbA.spaz + fbA.spaz_in) - fbA.scoppio));
                }

            }
            for (int i = 1; i < (stesa.get(i_R)).fb.length; i++) {
                if (fbR.fb[i].layer == 2) {
                    VfB_R[1].fb[i].layer = 2;
                    //System.out.println(i+" "+(fbA.fb[i].time)+(fbA.dromo[1].a-fbA.dromo[1].a));
                    VfB_R[1].setFB(i, (fbR.fb[i].time) + (fbR.dromo[1].a - fbR.dromo[1].a));
                } else {
                    VfB_R[1].fb[i].layer = -2;
                    VfB_R[1].setFB(i, fbR.dromo[1].time(((i - 1) * fbR.spaz + fbR.spaz_in)/*-fbR.scoppio*/));
                    //System.out.println(i+" "+fbA.dromo[1].time(((i-1)*fbA.spaz+fbA.spaz_in)-fbA.scoppio));
                }

                if (fbR.fb[i].layer == 3) {
                    //System.out.println(i+" "+(fbA.fb[i].time)+(fbA.dromo[1].a-fbA.dromo[1].a));
                    VfB_R[2].setFB(i, (fbR.fb[i].time) + (fbR.dromo[2].a - fbR.dromo[2].a));
                }
                /*     else
                 {
                 VfB_R[2].setFB(i,fbR.dromo[2].time(((i-1)*fbR.spaz+fbR.spaz_in)));
                 //System.out.println(i+" "+fbA.dromo[1].time(((i-1)*fbA.spaz+fbA.spaz_in)-fbA.scoppio));
                 }*/

            }

            phant(1, xsc_A, xsc_R);
            phant(2, xsc_A, xsc_R);

        } catch (Exception e1) {
            System.out.println("Eccezione - phantom 1 2 " + e1.getMessage());
        }

        for (int i = 1; i < VfB_A[1].fb.length; i++) {
            System.out.println("-1- " + i + " " + VfB_A[1].fb[i].time + " " + VfB_R[1].fb[i].time);
        }

        for (int i = 1; i < VfB_A[2].fb.length; i++) {
            System.out.println("-2- " + i + " " + VfB_A[2].fb[i].time + " " + VfB_R[2].fb[i].time);
        }

    }

    public void phant(int lay, double xsc_A, double xsc_R) {
        int ly = lay + 1;
        try {

            for (int j = 0; j < stesa.size(); j++) {
                boolean isfirst = true;
                int indx = 0;
                double dxA = -99999, dxR = -99999;
                //System.out.println("phantom : "+j);
                FirstBrakeList fbl = stesa.get(j);

                //System.out.println("phantom : "+fbl.fb.length);
                for (int i = 1; i < fbl.fb.length; i++) {
                    if (((fbl.scoppio - fbl.spaz_in) / fbl.spaz) < i) {
                        if (fbl.fb[i].layer == VfB_A[lay].fb[i].layer) {
                            dxA = fbl.fb[i].time - VfB_A[lay].fb[i].time;
                        }
                    } else {
                        if (fbl.fb[i].layer == VfB_R[lay].fb[i].layer) {
                            dxR = fbl.fb[i].time - VfB_R[lay].fb[i].time;
                        }
                    }

                }

                for (int i = 1; i < fbl.fb.length; i++) {

                    //ANDATA
                    if (((fbl.scoppio - fbl.spaz_in) / fbl.spaz) < i) {
                        if (fbl.fb[i].layer == ly) {
                            if (isfirst) {
                                isfirst = false;
                                indx = i;
                            }
                            if (dxA == -99999) { //System.out.println(i+" "+(fbl.fb[i].time)+(fbl.dromo[1].a-fbA.dromo[1].a));
                                VfB_A[lay].setFB(i, (fbl.fb[i].time) - (fbl.dromo[lay].time(((indx - 1) * fbl.spaz + fbl.spaz_in)) - fbA.dromo[lay].time(((indx - 1) * fbA.spaz + fbA.spaz_in)/*-fbA.scoppio*/)));
                            } else {
                                VfB_A[lay].setFB(i, (fbl.fb[i].time) - dxA);
                            }

                        }

                        VfB_A[lay].xsc = xsc_R;
                        VfB_A[lay].tAB = fbA.dromo[lay].time(VfB_A[lay].xsc/*-fbA.scoppio*/);

                        System.out.println("i: " + i + " j: " + j + " phantom layer " + lay + " A: " + VfB_A[lay].fb[i].time);
                        System.out.println("tAB : " + VfB_A[lay].tAB);
                    } else {
                        if (fbl.fb[i].layer == ly) {
                            if (isfirst) {
                                isfirst = false;
                                indx = i;
                            }
                            if (dxR == -99999) {
                                VfB_R[lay].setFB(i, (fbl.fb[i].time) - (fbl.dromo[lay].time(((indx - 1) * fbl.spaz + fbl.spaz_in)) - fbR.dromo[lay].time(((indx - 1) * fbR.spaz + fbR.spaz_in))));
                            } else {
                                VfB_R[lay].setFB(i, (fbl.fb[i].time) - dxR);
                            }
                        }

                        VfB_R[lay].xsc = xsc_A;
                        VfB_R[lay].tAB = fbR.dromo[lay].time(VfB_R[lay].xsc/*-fbl.scoppio*/);
                        System.out.println("tBA : " + VfB_R[lay].tAB);
                        System.out.println("i: " + i + " j: " + j + " phantom layer " + lay + " R: " + VfB_R[lay].fb[i].time);

                    }
                    System.out.println("i: " + i + " j: " + j + "");
                }
                int in = 1, fi = 16;
                fbR.dromo[lay].b = 1 / (VfB_R[lay].varianza(in, fi) / VfB_R[lay].covarianza(in, fi));
                fbR.dromo[lay].a = VfB_R[lay].media(in, fi) - fbR.dromo[lay].b * VfB_R[lay].mediax(in, fi);
                fbA.dromo[lay].b = 1 / (VfB_A[lay].varianza(in, fi) / VfB_A[lay].covarianza(in, fi));
                fbA.dromo[lay].a = VfB_A[lay].media(in, fi) - fbA.dromo[lay].b * VfB_A[lay].mediax(in, fi);

            }
        } catch (Exception e1) {
            System.out.println("Eccezione - phantom1 2 " + e1.getMessage());
        }
    }
    public double[] Zg1 = new double[1];
    public double[] Zg2 = new double[1];
    public double[] Tv1;
    public double[] Tv2;
    double[] Tg;
    double[] Zg;

    public void sezDT_ver2016(boolean media) {

     /*   for (int i = 0; i < 4; i++) {
            VfB_A[i] = new FirstBrakeList();
            VfB_R[i] = new FirstBrakeList();
        }*/

        try {
            //   phantom1();

            if (!this.useManPhant[0].getValue()) {
                phantom(1);
            }
        } catch (IOException ex) {
            Logger.getLogger(Indagine.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (max3 >= 2 && maxR3 >= 2) {

            try {
                //   phantom1();
                if (!this.useManPhant[1].getValue()) {
                    phantom(2);
                }
            } catch (IOException ex) {
                Logger.getLogger(Indagine.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        GRM grm = new GRM();
        //double[] TxAB = grm.FirstBrakeToDouble(VfB_A[1].fb);
        //double[] TxBA = grm.FirstBrakeToDouble(VfB_R[1].fb);
        Tv1 = grm.compute_tV(VfB_A[1], VfB_R[1], Math.max(VfB_A[1].tAB, VfB_R[1].tAB), xy);

        double v1 = grm.compute_v1(Tv1, VfB_A[1].spaz);
        double[] tG = grm.compute_tG(VfB_A[1], VfB_R[1], Math.max(VfB_A[1].tAB, VfB_R[1].tAB), xy, v1);

        double v0 = grm.compute_V0(stesa);
        double[] zz = grm.compute_zz(tG, v1, v0);
        double[] tG2;
        Zg2 = new double[1];
        if (VfB_A[2].fb.length == stesa.get(0).ch) {
            Tv2 = grm.compute_tV(VfB_A[2], VfB_R[2], Math.max(VfB_A[2].tAB, VfB_R[2].tAB), xy2);
            double v2 = grm.compute_v1(Tv2, VfB_A[1].spaz);
            tG2 = grm.compute_tG(tG, VfB_A[2], VfB_R[2], Math.max(VfB_A[2].tAB, VfB_R[2].tAB), xy, v2);
            // for(int i = 0;i<tG.length; i++){
            //     tG2[i]=tG2[i]-tG[i];
            // }

            double[] zz1 = grm.compute_zz(tG2, v2, v1);

            Zg2 = zz1;
            V3 = v2;
        }
        V1 = v0;
        V2 = v1;
        Zg1 = zz;

    }

    public void sezDT(boolean media) {
        try {
            try {
                //   phantom1();

                if (!this.useManPhant[0].getValue()) {
                    phantom(1);
                }
            } catch (IOException ex) {
                Logger.getLogger(Indagine.class.getName()).log(Level.SEVERE, null, ex);
            }

            ObjCp ocp = new ObjCp();
            double[] Tg1 = DTTg(1);
            Zg1 = new double[Tg1.length];
            Zg1 = (double[]) ocp.copy(DTZg(Tg1, media));
            System.out.println("terminato zg1 " + Zg1.length + " " + Tg1.length);
            try {
                os.write("\nterminato zg1 regolarmente " + Zg1.length + " " + Tg1.length);
            } catch (IOException ex) {
                Logger.getLogger(Indagine.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (max3 > 3 && maxR3 > 3) {

                try {
                    //   phantom1();
                    phantom(2);
                    double[] Tg2 = DTTg(2);
                    for (int i = 0; i < Tg1.length; i++) {
                        Tg2[i] = Tg2[i] - Tg1[i];
                    }

                    Zg2 = new double[Tg1.length];
                    Zg2 = (double[]) ocp.copy(DTZg(2, Tg2, media));
                    for (int i = 0; i < Zg1.length - 1; i++) {
                        System.out.println("ZG 1 \t" + Zg1[i] + "\tZG 2 \t" + Zg2[i] + "\t" + (Zg1[i] + Zg2[i]));
                        os.write(" ZG 1 \t" + Zg1[i] + "\tZG 2 \t" + Zg2[i] + "\t" + (Zg1[i] + Zg2[i]) + "\n");
                        Zg2[i] = Zg1[i] + Zg2[i];
                        //Zg2[i] = 0.0;
                    }

                } catch (IOException ex) {
                    Logger.getLogger(Indagine.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            //Zg2 = new double[Tg1.length];
            //Zg2=DTZg(Tg2);
            for (int i = 1; i < Zg1.length - 1; i++) {
                System.out.println("ZG 1 \t" + Zg1[i] + "\tZG 2 \n");
                // os.write("ZG 1 \t" + Zg1[i] + "\tZG 2 \n");
                //Zg2[i] = 0.0;
            }
            os.flush();
        } catch (IOException ex) {
            Logger.getLogger(Indagine.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public double[] DTTv(int lay, double xy) {
        double[] Tv = new double[VfB_A[lay].fb.length];
        try {

            //double TAB=VfB_A[lay].tAB;
            double TAB = Math.max(VfB_A[lay].tAB, VfB_R[lay].tAB);
            System.out.println("TAB A/R " + VfB_A[lay].tAB + " / " + VfB_R[lay].tAB);
            os.write("\n\nTAB A/R " + VfB_A[lay].tAB + " / " + VfB_R[lay].tAB);
            for (int j = 0; j < VfB_A[lay].fb.length; j++) {
                Tv[j] = 0.5 * (VfB_A[lay].getXY(VfB_A[lay].spaz_in + ((j) * VfB_A[lay].spaz) + (xy / 2)) - VfB_R[lay].getXY(VfB_A[lay].spaz_in + ((j) * VfB_A[lay].spaz) - (xy / 2)) + TAB);
                os.write("\n " + j + " TV " + Tv[j]);
                System.out.println(j + " Tv : " + (VfB_A[lay].spaz_in + ((j - 1) * VfB_A[lay].spaz)) + " " + Tv[j] + " " + xy);
            }

        } catch (IOException ex) {
            Logger.getLogger(Indagine.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Tv;
    }

    public double[] DTTg(int lay) {
        try {
            Tg = new double[VfB_A[lay].fb.length];
            //  double TAB = (VfB_A[lay].tAB + VfB_R[lay].tAB) / 2.0;
            double TAB = Math.max(VfB_A[lay].tAB, VfB_R[lay].tAB);

            //System.out.println("TAB A/R " + VfB_A[lay].tAB + " / " + VfB_R[lay].tAB);
            for (int j = 0; j < VfB_A[lay].fb.length; j++) {
                Tg[j] = (VfB_A[lay].fb[j].time + VfB_R[lay].fb[j].time - TAB) / 2;
                System.out.println(j + " Tg : " + VfB_A[lay].fb[j].posx + " " + Tg[j] + " " + TAB);

                os.write("\n" + j + " Tg : " + VfB_A[lay].fb[j].posx + " " + Tg[j] + " " + TAB);
            }
            os.flush();

            return Tg;
        } catch (IOException ex) {

            Logger.getLogger(Indagine.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public double[] getArrayV1() {
        double V1 = 0.0;
        FirstBrakeList fbl = stesa.get(0);
        double[] V1A = new double[fbl.fb.length];
        for (int j = 0; j < stesa.size(); j++) {
            fbl = stesa.get(j);
            if (fbl.dromo[0].b > -999) {
                V1 = Math.abs(1 / fbl.dromo[0].b);
                for (int i = 0; i < fbl.fb.length; i++) {
                    if (fbl.fb[i].layer == 1) {
                        if (V1A[i] > 0) {
                            V1A[i] = (V1A[i] + V1) / 2;
                        } else {
                            V1A[i] = V1;
                        }
                    }
                }

            }
            if (fbl.dromoR[0].b > -999) {
                V1 = Math.abs(1 / fbl.dromoR[0].b);
                for (int i = 0; i < fbl.fb.length; i++) {
                    if (fbl.fb[i].layer == 1) {
                        if (V1A[i] > 0) {
                            V1A[i] = (V1A[i] + V1) / 2;
                        } else {
                            V1A[i] = V1;
                        }
                    }

                }

            }

        }
        V1 = 0;
        double V2 = 0;
        int step = 0;
        double stepV = 0;
        for (int j = 0; j < V1A.length; j++) {
            if (V1A[j] == 0) {
                V2 = 0;
                step = 0;
                for (int i = j + 1; i < V1A.length; i++) {
                    if (V1A[i] != 0) {
                        V2 = V1A[i];

                        i = V1A.length + 1;
                    }
                    step++;
                }
                if (V1 != 0 && V2 != 0) {
                    stepV = (V2 - V1) / step;
                    V1A[j] = V1 + stepV;
                } else if (V1 != 0) {
                    V1A[j] = V1;
                } else if (V2 != 0) {
                    V1A[j] = V2;
                }

            } else {
                V1 = V1A[j];
            }

            V1 = V1A[j];
        }

        return V1A;
    }
    double[] TgGRM, TgGRM2;
    public double V1 = 0.0;
    public double V2 = 0.0;
    public double V3 = 0.0;

    public double[] DTZg(double[] Tg, boolean media) {

        V1 = 0;
        V2 = 0;
        V3 = 0;
        double vjn = 0.0;
        double[] Zg = new double[Tg.length];
        TgGRM = new double[Tg.length];
        double mZg = 0;
        int lay = 1;
        try {

            int c1 = 0;
            int c2 = 0;

            for (int j = 0; j < stesa.size(); j++) {
                FirstBrakeList fbl = stesa.get(j);
                if (fbl.dromo[0].b > -999) {
                    V1 = V1 + Math.abs(1 / fbl.dromo[0].b);
                    c1++;
                }
                if (fbl.dromo[1].b > -999) {
                    V2 = V2 + Math.abs(1 / fbl.dromo[1].b);
                    c2++;
                }

                //if(VfB_A[2]!=null) V3=Math.abs(1/fbl.dromo[2].b);
            }
            V1A = this.getArrayV1();
            V2A = new double[V1A.length];
            V1 = V1 / c1;
            V2 = V2 / c2;
            System.out.println(" V1 " + V1 + " V2 " + V2);
            os.write("\n\n" + "V1 " + V1 + " V2 " + V2 + "\n");
            int count = 0;
            for (int i = 0; i < Tg.length; i++) {
                if (!media) {
                    vjn = (V2 * V1A[i]) / Math.sqrt((V2 * V2) - (V1A[i] * V1A[i]));
                } else {
                    vjn = (V2 * V1) / Math.sqrt((V2 * V2) - (V1 * V1));
                }
                Zg[i] = 0.5 * Tg[i] * vjn;
                //os.write("Zg "+Zg[i]+"\n");
                mZg = mZg + Zg[i];
                count++;
                System.out.println(i + " Zg : " + Zg[i] + " L " + Zg.length);
                os.write("\n" + i + " Zg : " + Zg[i] + " L " + Zg.length);
            }
            if (xy == 0.0) {
                xy = 2 * ((mZg / count) * Math.tan(Math.asin(V1 / V2)));
            }

            Tv1 = DTTv(1, xy);

            TgGRM = TgGRM(Tv1, xy, 1);

            count = 0;
            double Vn = 0;
            double[] x = new double[Tv1.length];
            double[] x1 = new double[3];

            double[][] data = new double[Tv1.length][2];
            double[][] data1 = new double[3][2];
            for (int j = 0; j < VfB_A[lay].fb.length /*- 2*/; j++) {

                x[j] = VfB_A[lay].spaz_in + ((j) * VfB_A[lay].spaz);
                if (j == 0) {
                    Vn = Vn + (VfB_A[lay].spaz) / (Tv1[1] - Tv1[0]);
                } else if (j > 1 && j < VfB_A[lay].fb.length - 3) {
                    Vn = Vn + (VfB_A[lay].spaz) / (Tv1[j + 1] - Tv1[j]);
                } else {
                    Vn = Vn + (VfB_A[lay].spaz) / (Tv1[j] - Tv1[j - 1]);
                }
                count++;
                data[j][0] = x[j];
                data[j][1] = Tv1[j];

            }
            //V2 = 1/FirstBrakeList.getOLSRegression(data)[1];
            //V2 = Vn / count;
            //V2 = (14 * VfB_A[lay].spaz) / (Tv[Tv.length - 2] - Tv[1]);
            //V2 = (covarianza(Tv, x, 1, Tv.length - 2) / varianza(Tv, 1, Tv.length - 2));
            //V2 = 1 / FirstBrakeList.getOLSRegression(data)[1];
            for (int i = 0; i < TgGRM.length /*- 2*/; i++) {
                int j = i;
                if (j == 0) {
                    Vn = (VfB_A[lay].spaz) / (Tv1[1] - Tv1[0]);
                    data1[0][1] = data[0][1];
                    data1[1][1] = data[1][1];
                    data1[2][1] = data[2][1];

                    data1[0][0] = data[0][0];
                    data1[1][0] = data[1][0];
                    data1[2][0] = data[2][0];

                } else if (j > 0 && j < VfB_A[lay].fb.length - 3) {
                    Vn = (VfB_A[lay].spaz) / (Tv1[j + 1] - Tv1[j]);
                    data1[0][1] = data[j - 1][1];
                    data1[1][1] = data[j][1];
                    data1[2][1] = data[j + 1][1];

                    data1[0][0] = data[j - 1][0];
                    data1[1][0] = data[j][0];
                    data1[2][0] = data[j + 1][0];

                } else {
                    Vn = (VfB_A[lay].spaz) / (Tv1[j] - Tv1[j - 1]);

                    data1[0][1] = data[j - 2][1];
                    data1[1][1] = data[j - 1][1];
                    data1[2][1] = data[j][1];

                    data1[0][0] = data[j - 2][0];
                    data1[1][0] = data[j - 1][0];
                    data1[2][0] = data[j][0];

                }
                //Vn = V2;
                //V2 = Vn;
                //vjn = (V2 * V1) / Math.sqrt((V2 * V2) - (V1 * V1));
                double V2l = 1 / FirstBrakeList.getOLSRegression(data1)[1];
                V2 = 1 / FirstBrakeList.getOLSRegression(data)[1];
                V2A[j] = (0 * V2 + V2l) / 1;
                if (V2A[j] < 0) {
                    V2A[j] = V2;
                }

                if (!media) {
                    vjn = (V2A[j] * V1A[j]) / Math.sqrt((V2A[j] * V2A[j]) - (V1A[j] * V1A[j]));
                } else {
                    vjn = (V2 * V1) / Math.sqrt((V2 * V2) - (V1 * V1));
                }

                Zg[i] = TgGRM[i] * vjn;
                mZg = mZg + Zg[i];
                count++;
                System.out.println(i + "GRM Zg : " + Zg[i] + " L " + Zg.length + "\tv1 " + V1A[i] + " \tV2 " + V2A[j]);
                os.write("\n" + i + "GRM Zg : " + Zg[i] + "\t L " + Zg.length + "\t" + V1A[i] + "\t" + V2A[j]);
            }

            System.out.println("V1 " + V1 + " V2 " + V2 + " Vn " + Vn + " vjn " + (V2 * V1) / Math.sqrt((V2 * V2) - (V1 * V1)));
            // System.out.println(" XY "+2*((mZg/count)*Math.tan(Math.asin(V1/V2)))+" "+Math.asin(V1/V2)+" "+mZg/count);
            /*if(VfB_A[2]!=null){
             double TAB=(VfB_A[2].tAB+VfB_R[2].tAB)/2.0;
            
             for(int j=1;j<VfB_A[2].fb.length-1 ;j++)
             {
             double vjn=(V3*V2)/Math.sqrt((V3*V3)-(V2*V2));
            
             System.out.println(j+" Tg2 : "+ 0.5*vjn*((0-Tg[j])+VfB_A[2].fb[j].time+VfB_R[2].fb[j].time-TAB));
             }
             }*/
        } catch (Exception e1) {
            try {
                os.write("\n\nEccezione - dzg " + e1 + " " + VfB_A.length);
            } catch (IOException ex) {
                Logger.getLogger(Indagine.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Eccezione - dzg " + e1.getMessage() + " " + VfB_A.length);
        }

        // wvfront wv = new wvfront(V1, VfB_A[lay].getTArray(), VfB_R[lay].getTArray(), VfB_A[lay].spaz);
        //System.out.println(" l "+Zg.length);
        return Zg;
    }
    public double[] V1A, V2A, V3A;

    public double[] DTZg(int rifr, double[] Tg, boolean media) {

        double vjn = 0.0;
        double[] Zg = new double[Tg.length];
        V3A = new double[Tg.length];
        double mZg = 0;
        int lay = rifr;

        try {

            int c1 = 0;
            int c2 = 0;

            for (int j = 0; j < stesa.size(); j++) {
                FirstBrakeList fbl = stesa.get(j);
                if (fbl.dromo[rifr - 1].b > -999) {
                    // V2 = V2 + Math.abs(1 / fbl.dromo[rifr - 1].b);
                    c1++;
                }
                if (fbl.dromo[rifr].b > -999) {
                    V3 = V3 + Math.abs(1 / fbl.dromo[rifr].b);
                    c2++;
                }

                //if(VfB_A[2]!=null) V3=Math.abs(1/fbl.dromo[2].b);
            }
            V1A = this.getArrayV1();
            //   V2 = V2 / c1;
            V3 = V3 / c2;
            System.out.println(" V2 " + V2 + " V3 " + V3);
            os.write("\n\n -- LAYER 2 --\n" + "V2 " + V2 + " V3 " + V3 + "\n");
            int count = 0;
            for (int i = 0; i < Tg.length; i++) {
                vjn = (V3 * V2) / Math.sqrt((V3 * V3) - (V2 * V2));
                Zg[i] = 0.5 * Tg[i] * vjn;
                //os.write("Zg "+Zg[i]+"\n");
                mZg = mZg + Zg[i];
                count++;
                System.out.println(i + "NoGRM Zg : \t" + Zg[i] + " Tg \t" + Tg[i] + "\tVjn :" + vjn);
                os.write("\n" + i + " Zg : " + Zg[i] + " L " + Zg.length);
            }
            if (xy2 == 0.0) {
                xy2 = 2 * ((mZg / count) * Math.tan(Math.asin(V1 / V2)));
            }

            Tv2 = DTTv(rifr, xy2);
            TgGRM2 = TgGRM(Tv2, xy2, 2);
            count = 0;
            double Vn = 0;
            double[] x = new double[Tv2.length];
            double[] x1 = new double[3];

            double[][] data = new double[Tv2.length - 2][2];
            double[][] data1 = new double[3][2];
            for (int j = 0; j < VfB_A[lay].fb.length; j++) {

                x[j] = VfB_A[lay].spaz_in + ((j) * VfB_A[lay].spaz);
                if (j == 0) {
                    Vn = Vn + (VfB_A[lay].spaz) / (Tv2[1] - Tv2[0]);
                } else if (j > 1 && j < VfB_A[lay].fb.length - 3) {
                    Vn = Vn + (VfB_A[lay].spaz) / (Tv2[j + 1] - Tv2[j]);
                } else {
                    Vn = Vn + (VfB_A[lay].spaz) / (Tv2[j] - Tv2[j - 1]);
                }
                count++;
                data[j][0] = x[j];
                data[j][1] = Tv2[j];

            }
            //V2 = 1/FirstBrakeList.getOLSRegression(data)[1];
            //V2 = Vn / count;
            //V2 = (14 * VfB_A[lay].spaz) / (Tv[Tv.length - 2] - Tv[1]);
            //V2 = (covarianza(Tv, x, 1, Tv.length - 2) / varianza(Tv, 1, Tv.length - 2));
            //V2 = 1 / FirstBrakeList.getOLSRegression(data)[1];
            for (int i = 0; i < TgGRM2.length - 2; i++) {
                int j = i;
                if (j == 0) {
                    Vn = (VfB_A[lay].spaz) / (Tv2[1] - Tv2[0]);
                    data1[0][1] = data[0][1];
                    data1[1][1] = data[1][1];
                    data1[2][1] = data[2][1];

                    data1[0][0] = data[0][0];
                    data1[1][0] = data[1][0];
                    data1[2][0] = data[2][0];

                } else if (j > 0 && j < VfB_A[lay].fb.length - 3) {
                    Vn = (VfB_A[lay].spaz) / (Tv2[j + 1] - Tv2[j]);
                    data1[0][1] = data[j - 1][1];
                    data1[1][1] = data[j][1];
                    data1[2][1] = data[j + 1][1];

                    data1[0][0] = data[j - 1][0];
                    data1[1][0] = data[j][0];
                    data1[2][0] = data[j + 1][0];

                } else {
                    Vn = (VfB_A[lay].spaz) / (Tv2[j] - Tv2[j - 1]);

                    data1[0][1] = data[j - 2][1];
                    data1[1][1] = data[j - 1][1];
                    data1[2][1] = data[j][1];

                    data1[0][0] = data[j - 2][0];
                    data1[1][0] = data[j - 1][0];
                    data1[2][0] = data[j][0];

                }
                //Vn = V2;
                //V2 = Vn;
                //vjn = (V2 * V1) / Math.sqrt((V2 * V2) - (V1 * V1));
                double V3l = 1 / FirstBrakeList.getOLSRegression(data1)[1];
                V3 = 1 / FirstBrakeList.getOLSRegression(data)[1];

                V3A[i] = ((0.5 * V3) + (0.5 * V3l)) / 1.0;
                if (V3A[i] < 0) {
                    V3A[i] = V3;
                }

                //vjn = (V3 * V1) / Math.sqrt((V3 * V3) - (V1 * V1));
                if (!media) {
                    vjn = (V3A[j] * V2A[j]) / Math.sqrt((V3A[j] * V3A[j]) - (V2A[j] * V2A[j]));
                } else {
                    vjn = (V3 * V2) / Math.sqrt((V3 * V3) - (V2 * V2));
                }
                Zg[i] = (TgGRM2[i] - TgGRM[i]) * vjn;
                mZg = mZg + Zg[i];
                count++;
                System.out.println(i + " GRM 2 Tg:" + TgGRM2[i] + "\t Zg : " + Zg[i] + " Vjn " + vjn);
                os.write("\n" + i + "GRM 2 Zg : " + Zg[i] + "\t L " + Zg.length + "\tV2A " + V2A[i] + "\tV3A " + V3A[j]);
            }

            System.out.println("V2 " + V2 + " V3 " + V3 + " Vn " + Vn + " vjn " + (V2 * V3) / Math.sqrt((V3 * V3) - (V2 * V2)));
            // System.out.println(" XY "+2*((mZg/count)*Math.tan(Math.asin(V1/V2)))+" "+Math.asin(V1/V2)+" "+mZg/count);
            /*if(VfB_A[2]!=null){
             double TAB=(VfB_A[2].tAB+VfB_R[2].tAB)/2.0;
            
             for(int j=1;j<VfB_A[2].fb.length-1 ;j++)
             {
             double vjn=(V3*V2)/Math.sqrt((V3*V3)-(V2*V2));
            
             System.out.println(j+" Tg2 : "+ 0.5*vjn*((0-Tg[j])+VfB_A[2].fb[j].time+VfB_R[2].fb[j].time-TAB));
             }
             }*/
        } catch (Exception e1) {
            try {
                os.write("\n\nEccezione - dzg " + e1 + " " + VfB_A.length);
            } catch (IOException ex) {
                Logger.getLogger(Indagine.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Eccezione - dzg " + e1.getMessage() + " " + VfB_A.length);
        }

        //       wvfront wv = new wvfront(V1, VfB_A[lay].getTArray(), VfB_R[lay].getTArray(), VfB_A[lay].spaz);
        //System.out.println(" l "+Zg.length);
        return Zg;
    }

    public FirstBrakeList prvFb() {
        if (stesa.size() == 0) {
            System.out.println("index " + index);
            return null;
        }
        if (index > 0) {
            index--;
            return stesa.get(index);
        } else {
            return stesa.get(index);
        }

    }

    public void setFb(int ind) {
        index = ind;
        //return (FirstBrakeList)stesa.get(index);

    }

    public FirstBrakeList getFb() {
        return stesa.get(index);
    }

    public FirstBrakeList nxtFb() {
        if (index < stesa.size() - 1) {
            index++;
            return stesa.get(index);
        } else {
            return stesa.get(index);
        }

    }

    public void buildShots() {
        ObjCp cp = new ObjCp();
        if (stesa == null) {
            return;
        }
        ArrayList<JShot> oldshotsAL = null;
        if (this.shots != null) {

            oldshotsAL = (ArrayList<JShot>) cp.copy(shots);
        }
        this.shots = new ArrayList<JShot>();

        for (FirstBrakeList fbl_el : stesa) {
            JShot shot = new JShot(fbl_el.fbp, fbl_el.scoppio);
            int lay = fbl_el.fb[0].layer;
            FB_set fbs = new FB_set();
            int dir = (int) ((fbl_el.fb[0].posx - shot.xshot)
                    / Math.abs(fbl_el.fb[0].posx - shot.xshot));
            for (int i = 0; i < fbl_el.fb.length; i++) {

                if (fbl_el.fb[i].layer != lay
                        || dir != (int) ((fbl_el.fb[i].posx - shot.xshot) / Math.abs(fbl_el.fb[i].posx - shot.xshot))) {
                    shot.AddFBSet(fbs);
                    fbs = new FB_set();
                    dir = (int) ((fbl_el.fb[i].posx - shot.xshot) / Math.abs(fbl_el.fb[i].posx - shot.xshot));
                    lay = fbl_el.fb[i].layer;
                    fbs.AddFB(fbl_el.fb[i]);
                } else {
                    if (fbl_el.fb[i].enabled) {
                        fbs.AddFB(fbl_el.fb[i]);
                        fbs.layer = lay;
                        fbs.xshot = fbl_el.scoppio;
                        fbs.dir = dir;
                    }
                }

            }
            shot.AddFBSet(fbs);

            shots.add(shot);

        }
        if (oldshotsAL != null) {
            if (shots.size() == oldshotsAL.size()) {
                for (int i = 0; i < shots.size(); i++) {
                    if (shots.get(i).FB_sets.size() == oldshotsAL.get(i).FB_sets.size()) {
                        for (int j = 0; j < shots.get(i).FB_sets.size(); j++) {
                            shots.get(i).FB_sets.get(j).shift = oldshotsAL.get(i).FB_sets.get(j).shift;
                            shots.get(i).FB_sets.get(j).used = oldshotsAL.get(i).FB_sets.get(j).used;
                        }
                    }
                }
            }
        }

        return;
    }

    public void AddFb(FirstBrakeList fbl) {
        for (int i = 1; i < fbl.fb.length; i++) {
            if (fbl.scoppio < fbl.fb[i].posx) {
                fbl.fb[i].ar = 0;
            } else //aggiungere controllo =
            {
                fbl.fb[i].ar = 1;
            }
        }
        stesa.add(fbl);

    }

    private double[] TgGRM(double[] Tv, double xy, int lay) {

        Tg = new double[Tv.length];

        //double TAB = (VfB_A[lay].tAB + VfB_R[lay].tAB) / 2.0;
        double TAB = Math.max(VfB_A[lay].tAB, VfB_R[lay].tAB);
        // double TAB=VfB_A[lay].tAB;
        //System.out.println("TAB A/R "+VfB_A[lay].tAB+" / "+VfB_R[lay].tAB);
        /*   for (int j = 0; j < VfB_A[lay].fb.length - 2; j++) {
        
         double Vn = 0;
         if (j == 0) {
         Vn = (VfB_A[lay].spaz) / (Tv[1] - Tv[0]);
         } else if (j > 0 && j < VfB_A[lay].fb.length - 3) {
         Vn = (VfB_A[lay].spaz) / (Tv[j + 1] - Tv[j]);
         } else {
         Vn = (VfB_A[lay].spaz) / (Tv[j] - Tv[j - 1]);
         }
        
         } 
        
         */

        int count = 0;
        double Vn = 0;
        double[] x = new double[Tv.length /*- 2*/];
        double[] x1 = new double[3];

        double[][] data = new double[Tv.length /*- 2*/][2];
        double[][] data1 = new double[3][2];
        for (int j = 0; j < VfB_A[lay].fb.length /*- 2*/; j++) {

            x[j] = VfB_A[lay].spaz_in + ((j) * VfB_A[lay].spaz);
            if (j == 0) {
                Vn = Vn + (VfB_A[lay].spaz) / (Tv[1] - Tv[0]);
            } else if (j > 1 && j < VfB_A[lay].fb.length - 3) {
                Vn = Vn + (VfB_A[lay].spaz) / (Tv[j + 1] - Tv[j]);
            } else {
                Vn = Vn + (VfB_A[lay].spaz) / (Tv[j] - Tv[j - 1]);
            }
            count++;
            data[j][0] = x[j];
            data[j][1] = Tv[j];

        }
        try {
            for (int i = 0; i < VfB_A[lay].fb.length; i++) { //-2
                int j = i;
                if (j == 0) {
                    Vn = (VfB_A[lay].spaz) / (Tv[1] - Tv1[0]);
                    data1[0][1] = data[0][1];
                    data1[1][1] = data[1][1];
                    data1[2][1] = data[2][1];

                    data1[0][0] = data[0][0];
                    data1[1][0] = data[1][0];
                    data1[2][0] = data[2][0];

                } else if (j > 0 && j < VfB_A[lay].fb.length - 3) {
                    Vn = (VfB_A[lay].spaz) / (Tv[j + 1] - Tv[j]);
                    data1[0][1] = data[j - 1][1];
                    data1[1][1] = data[j][1];
                    data1[2][1] = data[j + 1][1];

                    data1[0][0] = data[j - 1][0];
                    data1[1][0] = data[j][0];
                    data1[2][0] = data[j + 1][0];

                } else {
                    Vn = (VfB_A[lay].spaz) / (Tv[j] - Tv[j - 1]);

                    data1[0][1] = data[j - 2][1];
                    data1[1][1] = data[j - 1][1];
                    data1[2][1] = data[j][1];

                    data1[0][0] = data[j - 2][0];
                    data1[1][0] = data[j - 1][0];
                    data1[2][0] = data[j][0];

                }
                Vn = 1 / FirstBrakeList.getOLSRegression(data1)[1];

                Tg[j] = 0.5 * (VfB_A[lay].getXY(VfB_A[lay].spaz_in + ((j) * VfB_A[lay].spaz) + (xy / 2)) + VfB_R[lay].getXY(VfB_A[lay].spaz_in + ((j) * VfB_A[lay].spaz) - (xy / 2)) - (TAB + (xy / Vn)));

                try {
                    os.write("\n- " + j + " Tg : " + (VfB_A[lay].spaz_in + ((j) * VfB_A[lay].spaz)) + " " + Tg[j] + " " + xy);
                    System.out.println(j + " Tg : " + (VfB_A[lay].spaz_in + ((j) * VfB_A[lay].spaz)) + " " + Tg[j] + " " + xy);
                } catch (IOException ex) {
                    Logger.getLogger(Indagine.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(Indagine.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Exception -- TgGRM " + ex.getMessage());
        }

        //throw new UnsupportedOperationException("Not yet implemented");
        return Tg;
    }

    public double media(double[] list, int in, int fi) {
        double sum = 0;
        int conta = 0;
        if (in < fi) {
            for (int i = in; i <= fi; i++) {
                //FirstBrake fib=(FirstBrake)linea.get(i-1);
                sum = sum + list[i];
                conta++;
            }
        } else {
            for (int i = in; i >= fi; i--) {

                sum = sum + list[i];
                conta++;

            }
        }

        return sum / conta;
    }

    public double varianza(double[] list, int in, int fi) {
        double sum = 0;

        if (in < fi) {
            for (int i = in; i <= fi; i++) {

                sum = sum + Math.pow((list[i]) - media(list, in, fi), 2);

            }
        } else {
            for (int i = in; i >= fi; i--) {
                sum = sum + Math.pow((list[i]) - media(list, in, fi), 2);
            }
        }

        return sum / (Math.abs(fi - in) + 1);
    }

    public double covarianza(double[] list, double[] listx, int in, int fi) {
        double sum = 0;
        int conto = 0;
        if (in < fi) {
            for (int i = in; i <= fi; i++) {
                conto++;

                sum = sum + ((list[i] - media(list, in, fi)) * ((listx[i]) - media(listx, in, fi)));
            }
        } else {
            for (int i = in; i >= fi; i--) {
                conto++;
                sum = sum + ((list[i] - media(list, in, fi)) * ((listx[i]) - media(listx, in, fi)));
            }
        }

        return sum / (Math.abs(fi - in) + 1);
    }

    private int rif(ArrayList linea) {
        int ind = 99;
        double shot = 0;
        double sx = ((FirstBrakeList) linea.get(0)).spaz_in;
        double dx = ((FirstBrakeList) linea.get(0)).spaz_in + (((FirstBrakeList) linea.get(0)).spaz * (((FirstBrakeList) linea.get(0)).ch - 2));
        double dist = 999;

        for (int n = 0; n < linea.size(); n++) {
            FirstBrakeList i = (FirstBrakeList) linea.get(n);
            if (i.scoppio <= sx) {
                if (sx - i.scoppio < dist) {
                    dist = dist = sx - i.scoppio;
                    ind = n;
                }
            }
        }
        /*
         if i.shot<sx:
         if sx-i.shot<dist:
         dist=sx-i.shot
         ind=n
         */
        return ind;
    }

    private int rif_rit(ArrayList linea) {
        int ind = 99;
        double shot = 0;
        double sx = ((FirstBrakeList) linea.get(0)).spaz_in;
        double dx = ((FirstBrakeList) linea.get(0)).spaz_in + (((FirstBrakeList) linea.get(0)).spaz * (((FirstBrakeList) linea.get(0)).ch - 2));
        double dist = 99999;

        for (int n = 0; n < linea.size(); n++) {
            FirstBrakeList i = (FirstBrakeList) linea.get(n);
            if (i.scoppio >= dx) {
                if (-dx + i.scoppio < dist) {
                    dist = -dx + i.scoppio;
                    ind = n;
                }
            }
        }

        return ind;
    }

    private FirstBrakeList phnt(ArrayList list) {
        ObjCp obj = new ObjCp();
        int ind = rif(list);
        System.out.println("TIRO ESTERNO " + rif(list));
        try {
            os.write("\nTIRO ESTERNO " + rif(list));
        } catch (IOException ex) {
            Logger.getLogger(Indagine.class.getName()).log(Level.SEVERE, null, ex);
        }

        FirstBrakeList ph;// = new FirstBrakeList(((FirstBrakeList) list.get(ind)).ch);
        ph = (FirstBrakeList) obj.copy(list.get(ind));
        ph.xsc = ph.scoppio;
        System.out.println("PH " + ph.fb.length);
        double dst = 0.0;
        for (int m = 0; m < list.size(); m++) {
            int n = 0;
            dst = 0;
            boolean primo = true;
            FirstBrakeList i = (FirstBrakeList) list.get(m);
            for (int o = 0; o < i.fb.length; o++) {
                ph.fb[o].posx = ph.spaz_in + (o * ph.spaz);
                if (i.fb[o].time > -5) {
                    if (ph.fb[o].time > -5) {
                        if (primo) {
                            primo = false;
                            dst = (ph.fb[o].time - i.fb[o].time) + dst;
                            n = n + 1 - 1;
                        }
                    }
                }
            }

            if (n > 0) {
                dst = dst / n;
                for (int o = 0; o < i.fb.length; o++) {

                    if (i.fb[o].time > -5) {
                        //#print "n",n,dsv
                        if (ph.fb[o].time <= -5.0) {
                            ph.setFB(o, i.fb[o].time + dst);
                        }
                    }
                }
            } else {
                dst = 0;
                primo = true;
                for (int o = 0; o < i.fb.length; o++) {
                    if (i.fb[o].time > -5) {
                        //#print "y",phant.getY(((o-1) * phant.spaz) + phant.spaz_in),i.fbla2[o],phant.getY(((5-1) * phant.spaz) + phant.spaz_in)
                        if (primo) {
                            primo = false;
                            dst = (ph.getT(((o) * ph.spaz) + ph.spaz_in) - i.fb[o].time) + dst;
                            n = n + 1;
                        }
                    }
                }
                dst = dst / n;
                //#print "d",dsv,n
                for (int o = 0; o < i.fb.length; o++) {

                    if (i.fb[o].time > -5) {
                        if (ph.fb[o].time <= -5.0) {
                            ph.setFB(o, i.fb[o].time + dst);
                        }
                    }

                }
            }
        }

        for (int o = 0; o < ph.fb.length; o++) {
            System.out.println(">>>>>> " + o + " " + ph.fb[o].time);
            try {
                if (ph.fb[o].time > -1) {
                    os.write("\n>>>PH>>>  \t" + ph.fb[o].posx + "\t " + ph.fb[o].time);
                } else {
                    ph.fb[o].time = ph.getT(ph.fb[o].posx);
                    os.write("\n>>>PHi>>  \t" + ph.fb[o].posx + "\t " + ph.getT(ph.fb[o].posx));
                }
            } catch (IOException ex) {
                Logger.getLogger(Indagine.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return ph;
    }

    private FirstBrakeList phnt_rit(ArrayList list) {
        ObjCp obj = new ObjCp();
        int ind = rif_rit(list);
        System.out.println("TIRO ESTERNO " + rif_rit(list));

        try {
            os.write("\nTIRO ESTERNO <" + rif_rit(list));
        } catch (IOException ex) {
            Logger.getLogger(Indagine.class.getName()).log(Level.SEVERE, null, ex);
        }

        FirstBrakeList ph = new FirstBrakeList(((FirstBrakeList) list.get(ind)).ch);
        ph = (FirstBrakeList) obj.copy(list.get(ind));
        ph.xsc = ph.scoppio;
        System.out.println("PH " + ph.fb.length);
        double dst = 0.0;
        for (int m = list.size() - 1; m >= 0; m--) {
            int n = 0;
            dst = 0;
            boolean primo = true;
            FirstBrakeList i = (FirstBrakeList) list.get(m);
            for (int o = i.fb.length - 1; o >= 0; o--) {
                ph.fb[o].posx = ph.spaz_in + (o * ph.spaz);
                if (i.fb[o].time > -5) {
                    if (ph.fb[o].time > -5) {
                        if (primo) {
                            primo = false;
                            dst = (ph.fb[o].time - i.fb[o].time) + dst;
                            n = n + 1 - 1;

                        }

                    }
                }
            }

            if (n > 0) {
                dst = dst / n;
                for (int o = 0; o < i.fb.length; o++) {

                    if (i.fb[o].time > -5) {
                        //#print "n",n,dsv
                        if (ph.fb[o].time <= -5.0) {
                            ph.setFB(o, i.fb[o].time + dst);
                        }
                    }
                }
            } else {
                dst = 0;
                n = 0;
                primo = true;
                for (int o = i.fb.length - 1; o >= 0; o--) {
                    if (i.fb[o].time > -5) {
                        //#print "y",phant.getY(((o-1) * phant.spaz) + phant.spaz_in),i.fbla2[o],phant.getY(((5-1) * phant.spaz) + phant.spaz_in)
                        if (primo) {
                            primo = false;
                            dst = (ph.getT(((o) * ph.spaz) + ph.spaz_in) - i.fb[o].time) + dst;
                            n = n + 1;
                        }
                    }
                }
                dst = dst / n;
                //#print "d",dsv,n
                for (int o = 0; o < i.fb.length; o++) {

                    if (i.fb[o].time > -5) {
                        if (ph.fb[o].time <= -5.0) {
                            ph.setFB(o, i.fb[o].time + dst);
                        }
                    }

                }
            }
        }

        for (int o = 0; o < ph.fb.length; o++) {

            try {
                if (ph.fb[o].time > -1) {
                    os.write("\n<<<PH<<<  \t" + ph.fb[o].posx + "\t " + ph.fb[o].time);
                } else {
                    ph.fb[o].time = ph.getT(ph.fb[o].posx);
                    os.write("\n<<<PHi<<  \t" + ph.fb[o].posx + "\t " + ph.getT(ph.fb[o].posx));
                }

                os.flush();
            } catch (IOException ex) {
                Logger.getLogger(Indagine.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("<<<<<< " + o + " " + ph.fb[o].time);
        }

        return ph;
    }

    public void phantom(int livello) throws IOException {
        int layer = livello + 1;

        os.write("--- Phantom - ");
        os.write("layer " + layer);
        int lay = 0;
        char dir = 'N';
        ArrayList and = new ArrayList();
        boolean bool_l = false;
        FirstBrakeList fbl = null;
        System.out.println("1--");
        String userHome = "user.home";

        // We get the path by getting the system property with the 
        // defined key above. 
        String path = System.getProperty(userHome);
        try (FileInputStream fis = new FileInputStream(path + "/smartRefract-data/" + "dromo.txt");
             InputStreamReader isr = new InputStreamReader(fis);
             BufferedReader br = new BufferedReader(isr)) {
            String linea;
            linea = br.readLine();
            int tiro = 0;
            while (linea != null) {
                if (linea.substring(0, 1).equals("l")) {
                    bool_l = true;

                    String[] spl = linea.split(" ");
                    lay = Integer.parseInt(spl[1]);

                    try {
                        if (lay == 1) {
                            fbl = new FirstBrakeList((stesa.get(0)).fb.length);
                            tiro++;
                            dir = 'N';
                        }
                    } catch (Exception ex) {
                        System.out.println(linea + " " + spl[1]);
                    }

                } else if (linea.substring(0, 1).equals("F")) {
                    if (dir == 'A') {
                        and.add(fbl);
                        System.out.println("shot " + fbl.scoppio);
                        os.write("\nA shot " + fbl.scoppio);
                    }
                } else {
                    if (bool_l) {
                        //System.out.println("<<< " + linea);
                        String[] spl = linea.split(" ");
                        fbl.spaz_in = Double.parseDouble(spl[0]);
                        fbl.spaz = Double.parseDouble(spl[1]);
                        fbl.scoppio = Double.parseDouble(spl[2]);
                        bool_l = false;
                    } else {
                        if (lay == layer) {
                            String[] spl = linea.split(" ");

                            int ch = Integer.parseInt(spl[0]);
                            if (((ch - 1) * fbl.spaz) + fbl.spaz_in > fbl.scoppio) {
                                // System.out.println(" linea " + spl[1]);
                                if (Double.parseDouble(spl[1]) > -5.0) {
                                    System.out.println(" PH " + ch + " " + Double.parseDouble(spl[1]));
                                    os.write("\n PH " + ch + " " + Double.parseDouble(spl[1]));
                                    fbl.setFB(ch, Double.parseDouble(spl[1]));
                                    dir = 'A';
                                }
                            }

                        }
                    }
                }

                linea = br.readLine();

            }
            // br.close();

        } catch (Exception ex) {
            Logger.getLogger(Indagine.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("2--" + ex.toString());
        }
        VfB_A[livello] = phnt(and);
        //VfB_A[livello].fb.length;

        System.out.println("AND : " + and.size());
        os.write("\n And " + and.size());
        dir = 'N';
        and = new ArrayList();
        bool_l = false;
        fbl = null;
        System.out.println("3--");

        try (FileInputStream fis = new FileInputStream(path + "/smartRefract-data/" + "dromo.txt");
             InputStreamReader isr = new InputStreamReader(fis);
             BufferedReader br = new BufferedReader(isr)) {
            String linea;
            linea = br.readLine();
            int tiro = 0;
            while (linea != null) {
                if (linea.substring(0, 1).equals("l")) {
                    bool_l = true;

                    String[] spl = linea.split(" ");
                    lay = Integer.parseInt(spl[1]);

                    try {
                        if (lay == 1) {
                            fbl = new FirstBrakeList((stesa.get(0)).fb.length);
                            tiro++;
                            dir = 'N';
                        }
                    } catch (Exception ex) {
                        System.out.println(linea + " " + spl[1]);
                    }

                } else if (linea.substring(0, 1).equals("F")) {
                    if (dir == 'R') {
                        and.add(fbl);
                        System.out.println("shot " + fbl.scoppio);
                        os.write("\n R Shot " + fbl.scoppio);
                    }
                } else {
                    if (bool_l) {
                        //System.out.println("<<< " + linea);
                        String[] spl = linea.split(" ");
                        fbl.spaz_in = Double.parseDouble(spl[0]);
                        fbl.spaz = Double.parseDouble(spl[1]);
                        fbl.scoppio = Double.parseDouble(spl[2]);
                        bool_l = false;
                    } else {
                        if (lay == layer) {
                            String[] spl = linea.split(" ");

                            int ch = Integer.parseInt(spl[0]);
                            if (((ch - 1) * fbl.spaz) + fbl.spaz_in > fbl.scoppio) {
                                // System.out.println(" linea " + spl[1]);
                                if (Double.parseDouble(spl[1]) > -5.0) {
                                    //System.out.println(" PH "+ ch+ " "+Double.parseDouble(spl[1]));
                                    //fbl.setFB(ch, Double.parseDouble(spl[1]));
                                    dir = 'A';
                                }
                            } else {
                                if (Double.parseDouble(spl[1]) > -5.0) {

                                    System.out.println(" PH " + ch + " " + Double.parseDouble(spl[1]));
                                    os.write("\n PH " + ch + " " + Double.parseDouble(spl[1]));

                                    fbl.setFB(ch, Double.parseDouble(spl[1]));
                                    dir = 'R';

                                }
                            }
                        }

                    }
                }
                linea = br.readLine();
            }

        } catch (IOException ex) {
            Logger.getLogger(Indagine.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("2--" + ex.toString());
        }

        VfB_R[livello] = phnt_rit(and);
        VfB_R[livello].tAB = VfB_A[livello].getT(VfB_R[livello].scoppio);
        System.out.println("<<<<<<<<<< TAB " + VfB_R[livello].tAB);
        os.write("\n<<<<<<<<<< TAB " + VfB_R[livello].tAB);
        VfB_A[livello].tAB = VfB_R[livello].getT(VfB_A[livello].scoppio);

        System.out.println("RIT : " + and.size());
        os.write("\nRIT : " + and.size());
        os.flush();
    }

    /*
     public double mediax(int in,int fi){
    
     double sum=0;
     if(in<fi){
     for(int i=in;i<=fi;i++){
     FirstBrake fib=(FirstBrake)linea.get(i-1);
     sum=sum+(fib.posx);
     }
     }
     else
     {
     for(int i=in;i>=fi;i--){
     FirstBrake fib=(FirstBrake)linea.get(i-1);
    
     sum=sum+(fib.posx);
     }
     }
    
     return sum/(Math.abs(fi-in)+1);
    
    
     //return (spaz_in+(spaz*(fi-in)))/(Math.abs(fi-in)+1);
     }
     */
    public void buildDromocrone() {
        this.shots.size();
        int i = 0;
        for (JShot shs : this.shots) {
            for (FB_set fbs : shs.FB_sets) {
                if (fbs.dir > 0) {
                    if (fbs.layer == 1) {
                        stesa.get(i).strato1 = fbs.fb.get(0).chan + "-" + fbs.fb.get(fbs.fb.size() - 1).chan;
                    }
                    if (fbs.layer == 2) {
                        stesa.get(i).strato2 = fbs.fb.get(0).chan + "-" + fbs.fb.get(fbs.fb.size() - 1).chan;
                    }
                    if (fbs.layer == 3) {
                        stesa.get(i).strato3 = fbs.fb.get(0).chan + "-" + fbs.fb.get(fbs.fb.size() - 1).chan;
                    }
                } else {
                    if (fbs.layer == 1) {
                        stesa.get(i).strato1R = fbs.fb.get(0).chan + "-" + fbs.fb.get(fbs.fb.size() - 1).chan;
                    }
                    if (fbs.layer == 2) {
                        stesa.get(i).strato2R = fbs.fb.get(0).chan + "-" + fbs.fb.get(fbs.fb.size() - 1).chan;
                    }
                    if (fbs.layer == 3) {
                        stesa.get(i).strato3R = fbs.fb.get(0).chan + "-" + fbs.fb.get(fbs.fb.size() - 1).chan;
                    }

                }
            }
            i++;
        }

        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void writeDromo() {

        System.out.println("saving... 0");

        String userHome = "user.home";

        // We get the path by getting the system property with the 
        // defined key above. 
        String path = System.getProperty(userHome);
        File file = new File(path + "/smartRefract-data/" + "dromo.txt");
        try (FileOutputStream fos = new FileOutputStream(file, false);
             OutputStreamWriter os = new OutputStreamWriter(fos)) {

            for (FirstBrakeList fbl : stesa) {
                int[] direzioni = {0, 1};
                for (int direzione : direzioni) {

                    for (int l = 1; l <= 3; l++) {
                        os.write("layer " + l + "\n");
                        os.write("" + fbl.spaz_in + " " + fbl.spaz + " " + fbl.scoppio + "\n");
                        boolean have_data = false;

                        int in = -1, fi = 0;
                        for (int i = 0; i < fbl.fb.length; i++) {
                            //fbl.fb[i].setLayer(l);
                            if (fbl.fb[i].layer == l && fbl.fb[i].ar == direzione) {
                                fbl.fb[i].offset = Math.abs(fbl.fb[i].posx - fbl.scoppio);
                                if (fbl.fb[i].enabled) {
                                    os.write(i + " " + fbl.fb[i].time + "\n");
                                    have_data = true;
                                }
                                if (in < 0) {
                                    in = i;
                                }
                                fi = i;
                            }

                        }

                        if (!have_data) {
                            fbl.updateLayer(0, 0, l, direzione);
                            os.write(0 + " " + -5 + "\n");
                        } else {
                            fbl.updateLayer(in, fi, l, direzione);
                        }

                    }
                    os.write("FineShot\n");

                }

            }

            os.flush();
        } catch (IOException ex) {
            System.out.println("writeDromo IOEx  " + ex.getMessage());
        }

    }

    public int count_element_layer(int layer, int direzione) {
        int count = 0;
        for (FirstBrakeList fbl : stesa) {
            for (int i = 0; i < fbl.fb.length; i++) {
                //fbl.fb[i].setLayer(l);
                if (fbl.fb[i].layer == layer && fbl.fb[i].ar == direzione) {
                    if (fbl.fb[i].enabled) {
                        count++;
                    }

                }

            }

        }
        return count;
    }

    public final class wvfront {

        double[] tA, tR;
        double v1;
        double DXG;
        double[] dtA, dtR, alphaA, alphaR;

        public wvfront() {
        }

        public wvfront(double v1, double[] t1, double[] t2, double spaziatura) {
            try {
                this.v1 = v1;
                tA = t1;
                tR = t2;
                DXG = spaziatura;
                calcDT();
                calcAlpha();
                stepizeAlpha();
            } catch (IOException ex) {
                Logger.getLogger(Indagine.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        public void calcDT() throws IOException {
            dtA = new double[tA.length - 2];
            dtR = new double[tA.length - 2];
            os.write("\n----------------------------------------------------\n");

            for (int i = 0; i < tA.length - 3; i++) {
                dtA[i] = -tA[i] + tA[i + 1];
                os.write("\n" + tA[i + 1] + "\t" + tA[i]);
            }
            os.write("\n::::::::::::::::::::::::::::::::::::::::::::::::::::\n");

            for (int i = tA.length - 3; i > 1; i--) {
                dtR[i - 1] = -tR[i] + tR[i - 1];
                os.write("\n" + tR[i - 1] + "\t" + tR[i]);

            }

        }

        public void calcAlpha() throws IOException {
            os.write("\n-----------------------------------\nALPHA\n");
            alphaA = new double[dtA.length];
            alphaR = new double[dtA.length];
            for (int i = 0; i < dtA.length - 1; i++) {
                alphaA[i] = Math.acos((dtA[i] * v1) / DXG);
                os.write("" + i + "\t" + alphaA[i] + "\t" + dtA[i] + "\n");
            }
            os.write("\n::::::::::::::::::::::::::::::::::::::::::::::::::::\n");
            for (int i = dtA.length - 1; i > 0; i--) {
                alphaR[i] = Math.acos(dtR[i] * v1 / DXG);
                os.write("" + i + "\t" + alphaR[i] + "\t" + dtR[i] + "\n");
            }

        }

        public void stepizeAlpha() throws IOException {
            int nstep = 10;
            double[] hr_a_A = new double[nstep * alphaA.length];
            double[] hr_a_R = new double[nstep * alphaA.length];
            int index = 0;
            for (int i = 0; i < alphaA.length - 1; i++) {
                double stepa = (alphaA[i + 1] - alphaA[i]) / nstep;
                for (int j = 0; j < nstep; j++) {
                    hr_a_A[index] = j * stepa + alphaA[i];
                    os.write("\n" + i + " " + j + "\t " + hr_a_A[index]);
                    index++;
                }
            }
            os.write("\n::::::::::::::::::::::::::::::::::::::::::::::::::::");
            for (int i = alphaR.length - 1; i > 1; i--) {
                double stepa = (alphaR[i - 1] - alphaR[i]) / nstep;
                for (int j = 0; j < nstep; j++) {
                    hr_a_R[index] = j * stepa + alphaA[i];
                    os.write("\n" + i + " " + j + "\t " + hr_a_R[index] + "\t" + index);
                    index--;
                }
            }

        }
    }
    double[] z;
}
