/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myorg.myapi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.DataFormatException;

/**
 *
 * @author user
 */
public final class APIObject implements java.io.Externalizable {

    private final Date date = new Date();
    public File in_file = null;
    public File[] in_file_l = null;
    private static int count = 0;
    private final int index;
    TraceSet ts = null;
    public Trace[] tr = null;
    public int trace_index = 0;
    public ArrayList<FirstBrakeList> TraceGroup = new ArrayList();
    public FirstBrakeList fb = new FirstBrakeList();
    public Indagine proj = new Indagine();
    public boolean is_white = false;
    public boolean proporz = false;
    public File proj_file = null;
    public int selected_Tab = 0;
    public int FORMAT;
    private static final long serialVersionUID = -9010041239531461143L;
    public APIObject() {
        index = count++;
        TraceGroup = new ArrayList();
    }

    public Date getDate() {
        return date;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public String toString() {
        return index + " - " + date;
    }

    public void sync() {
        proj.stesa = this.TraceGroup;
        proj.setdromo();
    }

    public void loadSism() {
        InputFileSg2 inf = null;
        InputFileSgy infY = null;


        try {
            try {
                // TODO code application logic here
                fb = new FirstBrakeList();
                if (in_file.getName().toLowerCase().endsWith(".dat")) {
                    inf = new InputFileSg2(in_file);
                    ts = inf.getTraceSet();
                } else if (in_file.getName().toLowerCase().endsWith(".sgy")) {
                    infY = new InputFileSgy(in_file);
                    ts = infY.getTraceSet();
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(APIObject.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (DataFormatException ex) {
            Logger.getLogger(APIObject.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Aperto...");
        System.out.println("Letto...");

        //  Trace t=ts.getTrace(0);

    }

    public void LoadTrace_For_Open() {
        for (int i = 0; i < in_file_l.length; i++) {
            fb = (FirstBrakeList) TraceGroup.get(i);
            loadSism(i);
            this.tr = this.getTraces();
            fb.tr = this.tr;

            TraceGroup.set(i, fb);
        }
        fb = new FirstBrakeList();
        proj.stesa = TraceGroup;
    }

    public void newFb() {
        TraceGroup = new ArrayList<FirstBrakeList>();
        for (int i = 0; i < in_file_l.length; i++) {
            fb = new FirstBrakeList();
            loadSism(i);
            this.tr = this.getTraces();
            fb.tr = this.tr;
            fb.setChanel(ts.traceArray.length);
            fb.scoppio = tr[0].getShotLocation();
            double geo_step = 0;
            boolean is_regular = false;
            if (tr.length > 1) {
                geo_step = tr[1].getPhoneLocation() - tr[0].getPhoneLocation();
            }
            for (int j = 0; j < tr.length - 1; j++) {
                is_regular = (geo_step == tr[j + 1].getPhoneLocation() - tr[j].getPhoneLocation());
            }
            if (is_regular) {
                fb.spaz = geo_step;
                fb.spaz_in = tr[0].getPhoneLocation();
            }
            fb.fbp = in_file_l[i].getPath();
            TraceGroup.add(fb);
        }
        fb = new FirstBrakeList();
        proj.stesa = TraceGroup;
    }

    public void loadSism(int indx) {
        NIOInputFile inf = null;
        InputFileSgy infY = null;
        InputFileSU infU = null;

        try {
            try {
                // TODO code application logic here
                if (in_file_l[indx].getName().toLowerCase().endsWith(".dat") || in_file_l[indx].getName().toLowerCase().endsWith(".sg2")) {
                    inf = new NIOInputFile(in_file_l[indx]);
                    ts = inf.getTraceSet();
                } else if (in_file_l[indx].getName().toLowerCase().endsWith(".sgy")) {
                    infY = new InputFileSgy(in_file_l[indx]);
                    ts = infY.getTraceSet();
                } else if (in_file_l[indx].getName().toLowerCase().endsWith(".su")) {
                    infU = new InputFileSU(in_file_l[indx]);
                    ts = infU.getTraceSet();
                }




                //inf = new InputFileSg2(in_file_l[indx]);
                trace_index = indx;
            } catch (FileNotFoundException ex) {
                Logger.getLogger(APIObject.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Errore file openprj " + ex);
            }
        } catch (DataFormatException ex) {
            Logger.getLogger(APIObject.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Errore data openprj " + ex);
        }
        System.out.println("Aperto...");
        try {
            //ts=inf.getTraceSet();
        } catch (Exception e) {
        }
        System.out.println("Letto...");

        //  Trace t=ts.getTrace(0);

    }

    public Trace[] getTraces() {
        try {
            return ts.traceArray;
        } catch (Exception e) {
            return null;
        }

    }

    public void prevTr() {
        if (trace_index > 0) {
            trace_index = trace_index - 1;
            //loadSism(trace_index);
            //tr = getTraces();
            fb =  TraceGroup.get(trace_index);
            tr = fb.tr;
        } else {
            trace_index = in_file_l.length - 1;
            //loadSism(trace_index);
            //tr = getTraces();
            fb =  TraceGroup.get(trace_index);
            tr = fb.tr;
        }
    }

    public void nextTr() {
        if (trace_index < in_file_l.length - 1) {
            trace_index = trace_index + 1;
            //loadSism(trace_index);
            //tr = getTraces();
            fb =  TraceGroup.get(trace_index);
            tr = fb.tr;
        } else {
            trace_index = 0;
            //loadSism(trace_index);
            //tr = getTraces();
            fb =  TraceGroup.get(trace_index);
            tr = fb.tr;
        }

    }

    public double find_first_X(FirstBrakeList FBlist, int layer, boolean direct) {
        FirstBrake[] list = FBlist.fb;
        double x = Double.MAX_VALUE;
        double act_offset = Double.MAX_VALUE;
        for (int i = 0; i < list.length; i++) {


            if (list[i].ar == 0 && direct) {
                double[] d = new double[2];
                if (list[i].layer == layer) {
                    if (act_offset > list[i].offset) {
                        act_offset = list[i].offset;

                        x = list[i].posx;
                    }
                }
            } else if (list[i].ar == 1 && !direct) {
                double[] d = new double[2];
                if (list[i].layer == layer) {
                    if (act_offset > list[i].offset) {
                        act_offset = list[i].offset;

                        x = list[i].posx;
                    }
                }
            }






        }
        return x;
    }

    public double find_last_X(FirstBrakeList FBlist, int layer, boolean direct) {
        FirstBrake[] list = FBlist.fb;
        double x = Double.MAX_VALUE;
        double act_offset = Double.MIN_VALUE;
        for (int i = 0; i < list.length; i++) {


            if (list[i].ar == 0 && direct) {
                double[] d = new double[2];
                if (list[i].layer == layer) {
                    if (act_offset < list[i].offset) {
                        act_offset = list[i].offset;

                        x = list[i].posx;
                    }
                }
            } else if (list[i].ar == 1 && !direct) {
                double[] d = new double[2];
                if (list[i].layer == layer) {
                    if (act_offset < list[i].offset) {
                        act_offset = list[i].offset;

                        x = list[i].posx;
                    }
                }


            }


        }
        return x;
    }

    @Override
    public void writeExternal(ObjectOutput oo) throws IOException {
        oo.writeDouble(2016.0);
        oo.writeInt(2);
        oo.writeInt(proj.stesa.size());
        for (FirstBrakeList fbl : proj.stesa) {
            fbl.writeExternal(oo);
        }
        oo.writeInt(in_file_l.length);
        for (File f : in_file_l) {
            oo.writeUTF(f.getAbsolutePath());
        }
                oo.writeInt(tr.length);
        for(Trace t:tr){
            oo.writeInt(t.number);
            oo.writeDouble(t.shotLocation);
            oo.writeDouble(t.sampleInterval);
            oo.writeDouble(t.windowLength);
            oo.writeInt(t.length);
            oo.writeInt(t.value.length);
            for(double v:t.value){
                oo.writeDouble(v);
            }
            
            
        }

        

        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void readExternal(ObjectInput oi) throws IOException, ClassNotFoundException {
        double version=oi.readDouble();
        int FormatVersion=oi.readInt();
        FORMAT=FormatVersion;
        int len=oi.readInt();
        if(proj==null)  
            proj=new Indagine();
        proj.stesa=new ArrayList<FirstBrakeList>();
        for(int i=0;i<len;i++){
            FirstBrakeList fb=new FirstBrakeList();
            fb.VERSION=FORMAT;
            fb.readExternal(oi);
            proj.stesa.add(fb);
            TraceGroup.add(fb);
        }
        len=oi.readInt();
        in_file_l=new File[len];
        for(int i=0;i<len;i++){
            String fname=oi.readUTF();
            in_file_l[i]=new File(fname);
        }
//        oo.writeInt(in_file_l.length);
//        for (File f : in_file_l) {
//            oo.writeUTF(f.getAbsolutePath());
//        }

        len=oi.readInt();
        
        tr=new Trace[len];
        for(int i=0;i<len;i++){
            int numnber=oi.readInt();
            double shotLocation=oi.readDouble();
            double sampleInterval=oi.readDouble();
            double windowLenght=oi.readDouble();
            int tmplen=oi.readInt();
            int vlen=oi.readInt();
            tr[i]=new Trace(len);
            tr[i].number=numnber;
            tr[i].shotLocation=shotLocation;
            tr[i].sampleInterval=sampleInterval;
            tr[i].windowLength=windowLenght;
            tr[i].value=new double[vlen];
            tr[i].length=vlen;
            double maxValue=0.0;
            double sommaAbsValue=0.0;
            int count=0;
            for (int k=0;k<vlen;k++) {
                double v = oi.readDouble();
                tr[i].value[k]=v;
                if(Math.abs(v)>maxValue)maxValue=v;
                sommaAbsValue = sommaAbsValue + Math.abs(v);
               // count=count+k;
            }
            tr[i].setMaxValue(maxValue);
            tr[i].setMediaAbs(sommaAbsValue, vlen);
            
            
        }
        len=proj.stesa.size();
        for (int i = 0; i < 4; i++) {
            proj.VfB_A[i] = new FirstBrakeList(len);
            proj.VfB_R[i] = new FirstBrakeList(len);
        }
        
        
        
        
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}