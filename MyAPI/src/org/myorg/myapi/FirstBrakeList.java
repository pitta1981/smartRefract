/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myorg.myapi;

import it.vs30.smartRefract.utils.ZoomTraceUtil;
import java.io.Externalizable;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author pitta1981
 */
public class FirstBrakeList implements Externalizable {

    public ArrayList<FirstBrake> linea = new ArrayList<FirstBrake>();
    public FirstBrake[] fb;
    public ZoomTraceUtil[] zoomTr;
    public Trace[] tr;
    public double spaz;
    public double spaz_in;
    public double primo;
    public double scoppio;
    public int AR = 0;
    public double xsc = 999;
    public double tAB = 999;
    public String fbp = "";
    public int ch = 0;
    public JRetta[] dromo;
    public String strato1 = "0-0";
    public String strato2 = "0-0";
    public String strato3 = "0-0";
    public String strato1R = "0-0";
    public String strato2R = "0-0";
    public String strato3R = "0-0";
    public JRetta[] dromoR;
    public int VERSION;
    // Optional: shot elevation (not serialized in legacy format to preserve compatibility)
    public double shotElevation = 0.0;

    public FirstBrakeList() {
        //System.out.println("Init FIB");
        fb = new FirstBrake[0];
        tr = new Trace[1];
        zoomTr = new ZoomTraceUtil[1];
        dromo = new JRetta[3];
        dromoR = new JRetta[3];
        dromo[0] = new JRetta();
        dromo[1] = new JRetta();

        dromoR[0] = new JRetta();
        dromoR[1] = new JRetta();

    linea = new ArrayList<FirstBrake>();
        zoomTr[0] = new ZoomTraceUtil();
    }

    public FirstBrakeList(int nchannel) {
        //System.out.println("Init FIB");
        ch = nchannel;
        fb = new FirstBrake[nchannel];
        tr = new Trace[ch];
        zoomTr = new ZoomTraceUtil[nchannel];

        dromo = new JRetta[3];
        dromoR = new JRetta[3];

        dromo[0] = new JRetta();
        dromo[1] = new JRetta();
        dromo[2] = new JRetta();

        dromoR[0] = new JRetta();
        dromoR[1] = new JRetta();
        dromoR[2] = new JRetta();

    linea = new ArrayList<FirstBrake>();
        for (int i = 0; i < nchannel; i++) {
            tr[i] = new Trace(1);
            zoomTr[i] = new ZoomTraceUtil();
            fb[i] = new FirstBrake();
            fb[i].posx = spaz_in + (spaz * (i - 1));
        }
        setChanel(ch);
        fb[0] = new FirstBrake();
        fb[0].posx = scoppio;
        fb[0].time = -5.0;

    }

    public void setChanel(int n) {
        ch = n;
    linea = new ArrayList<FirstBrake>(n);
        //System.out.println("setChanel FIB");
        fb = new FirstBrake[n];
        zoomTr = new ZoomTraceUtil[n];
        //System.out.println("test");

        for (int i = 0; i < n; i++) {
            fb[i] = new FirstBrake();
            zoomTr[i] = new ZoomTraceUtil();
            //System.out.println("test"+i+" "+(n+1));
            fb[i].posx = spaz_in + spaz * (i);
            linea.add(i, fb[i]);
        }
    }

    public void setGeom() {
        for (int i = 0; i < ch; i++) {
            //fb[i] = new FirstBrake();
            //System.out.println("test"+i+" "+(n+1));
            fb[i].posx = spaz_in + spaz * (i);
            //linea.add(i, fb[i]);
        }
    }

    public void setFB(int ch, double t) {
        // System.out.println("test 3");
        FirstBrake fib = new FirstBrake();
        //   System.out.println("test 3.5");
        fib.setBrake(ch, t);
        //   System.out.println("test 4");
        fib.posx = spaz_in + spaz * (ch);
        //     System.out.println("test 5");
        linea.set(ch, fib);

        //  System.out.println("--- fb --- "+fb.length);
        fb[ch].setBrake(ch, t);
        fb[ch].posx = fib.posx;
        if (scoppio < fb[ch].posx) {
            fb[ch].ar = 0;
        } else //aggiungere controllo =
        {
            fb[ch].ar = 1;
        }

    }

    public void List() {
        // System.out.println("setChanel FIB");
        int n = linea.size();

        fb = new FirstBrake[n + 1];
        for (int i = 1; i < n + 1; i++) {
            fb[i] = new FirstBrake();
            fb[i] = linea.get(i - 1);
            fb[i].posx = spaz_in + spaz * (i - 1);
        }

    }

    public FirstBrake getFB(int ch) {

        //  System.out.println("Get FIB");
        return linea.get(ch - 1);
    }

    public double getXY(double x) {
        ch = fb.length;
        double x1 = 0, x2 = 0;
        double y1 = 0.0, y2 = 0.0;
        int index;
        if (x < spaz_in) {
            x1 = spaz_in;
            x2 = spaz_in + spaz;
            index = 0;
            y1 = fb[index].time;
            y2 = fb[index + 1].time;
        } else if (x > ((ch - 2) * spaz) + spaz_in) {
            index = ch - 2;
            x1 = ((ch - 3) * spaz) + spaz_in;
            x2 = ((ch - 2) * spaz) + spaz_in;
            y1 = fb[index - 1].time;
            y2 = fb[index].time;
        } else {
            index = (int) ((x - spaz_in) / spaz) + 1;
            x1 = ((index - 1) * spaz) + spaz_in;
            x2 = ((index) * spaz) + spaz_in;
            y1 = fb[index - 1].time;
            y2 = fb[index].time;
        }

        return (y2 - y1) / (x2 - x1) * (x - x1) + y1;
    }

    public double media(int in, int fi) {
        int n = 0;
        double sum = 0;
        if (in < fi) {
            for (int i = in; i <= fi; i++) {
                FirstBrake fib = linea.get(i);
                if (fib.time > 0) {
                    sum = sum + (fib.time);
                    n++;
                }
            }
        } else {
            for (int i = in; i >= fi; i--) {
                FirstBrake fib = linea.get(i);

                if (fib.time > 0) {
                    sum = sum + (fib.time);
                    n++;
                }

            }
        }

        return sum / n;
    }

    public void intersect() {
        //layer 1
        int in = 0;
        int fi = 0;
        boolean iniziato = false;
        for (int i = 0; i < this.fb.length - 1; i++) {

            if (this.fb[i].posx < this.scoppio) {
                if (this.fb[i].layer == 1) {
                    if (!iniziato) {
                        iniziato = true;
                        in = i;
                    } else {
                        fi = i;
                    }

                } else {
                    if (iniziato) {
                        iniziato = false;
                        fi = i - 1;
                    }
                }

            }
            this.strato1R = in + "-" + fi;
        }
        iniziato = false;
        in = fi = 0;
        for (int i = 0; i < this.fb.length - 1; i++) {

            if (this.fb[i].posx < this.scoppio) {
                if (this.fb[i].layer == 2) {
                    if (!iniziato) {
                        iniziato = true;
                        in = i;
                    } else {
                        fi = i;
                    }

                } else {
                    if (iniziato) {
                        iniziato = false;
                        fi = i - 1;
                    }
                }

            }
            this.strato2R = in + "-" + fi;
        }
        iniziato = false;
        in = fi = 0;
        for (int i = 0; i < this.fb.length - 1; i++) {

            if (this.fb[i].posx < this.scoppio) {
                if (this.fb[i].layer == 3) {
                    if (!iniziato) {
                        iniziato = true;
                        in = i;
                    } else {
                        fi = i;
                    }

                } else {
                    if (iniziato) {
                        iniziato = false;
                        fi = i - 1;
                    }
                }

            }
            this.strato3R = in + "-" + fi;
        }

        iniziato = false;
        in = fi = 0;
        for (int i = 0; i < this.fb.length - 1; i++) {

            if (this.fb[i].posx > this.scoppio) {
                if (this.fb[i].layer == 1) {
                    if (!iniziato) {
                        iniziato = true;
                        in = i;
                    } else {
                        fi = i;
                    }

                } else {
                    if (iniziato) {
                        iniziato = false;
                        fi = i - 1;
                    }
                }

            }
            this.strato1 = in + "-" + fi;
        }

        iniziato = false;
        in = fi = 0;
        for (int i = 0; i < this.fb.length - 1; i++) {

            if (this.fb[i].posx > this.scoppio) {
                if (this.fb[i].layer == 2) {
                    if (!iniziato) {
                        iniziato = true;
                        in = i;
                    } else {
                        fi = i;
                    }

                } else {
                    if (iniziato) {
                        iniziato = false;
                        fi = i - 1;
                    }
                }

            }
            this.strato2 = in + "-" + fi;
        }

        iniziato = false;
        in = fi = 0;
        for (int i = 0; i < this.fb.length - 1; i++) {

            if (this.fb[i].posx > this.scoppio) {
                if (this.fb[i].layer == 3) {
                    if (!iniziato) {
                        iniziato = true;
                        in = i;
                    } else {
                        fi = i;
                    }

                } else {
                    if (iniziato) {
                        iniziato = false;
                        fi = i;
                    }
                }

            }
            this.strato3 = in + "-" + fi;
        }

    }

    public void setLayertoZero() {
        for (int i = 0; i < fb.length - 1; i++) {
            fb[i].setLayer(0);

        }
    }

    public void setLayer(int in, int fi, int l) {

        String userHome = "user.home";

        // We get the path by getting the system property with the
        // defined key above.
        String path = System.getProperty(userHome);
        try (FileOutputStream fos = new FileOutputStream(path + "/smartRefract-data/" + "dromo.txt", true);
             OutputStreamWriter os = new OutputStreamWriter(fos)) {

            os.write("layer " + l + "\n");
            os.write("" + spaz_in + " " + spaz + " " + scoppio + "\n");

            if (in < fi) {
                for (int i = in; i <= fi; i++) {
                    fb[i].setLayer(l);
                    fb[i].offset = Math.abs(fb[i].posx - this.scoppio);
                    os.write(i + " " + fb[i].time + "\n");
                }
            } else {
                for (int i = in; i >= fi; i--) {
                    if (in - fi == 0) {
                        os.write(i + " " + -5 + "\n");
                    } else {
                        fb[i].setLayer(l);
                        fb[i].offset = Math.abs(fb[i].posx - this.scoppio);
                        os.write(i + " " + fb[i].time + "\n");
                    }

                }
            }
            if (l == 3) {
                os.write("FineShot\n");
            }
            os.flush();
        } catch (IOException ex) {
            System.out.println("SetLayer IOEx  " + ex.getMessage());
        }

    }

    public double mediax(int in, int fi) {
        int n = 0;
        double sum = 0;
        if (in < fi) {
            for (int i = in; i <= fi; i++) {
                FirstBrake fib = linea.get(i);
                if (fib.time > 0) {
                    sum = sum + (fib.posx);
                    n++;
                }
            }
        } else {
            for (int i = in; i >= fi; i--) {
                FirstBrake fib = linea.get(i);
                if (fib.time > 0) {
                    sum = sum + (fib.posx);
                    n++;
                }
            }
        }

        return sum / n;

        //return (spaz_in+(spaz*(fi-in)))/(Math.abs(fi-in)+1);
    }

    public double mediax() {
        int fi = linea.size() - 1;

        return mediax(1, fi);
    }

    public double media() {
        int fi = linea.size() - 1;

        return media(1, fi);
    }

    public double varianza(int in, int fi) {
        double sum = 0;
        int n = 0;
        if (in < fi) {
            for (int i = in; i <= fi; i++) {
                FirstBrake fib = linea.get(i);
                if (fib.time > 0) {
                    sum = sum + Math.pow((fib.posx) - mediax(in, fi), 2);
                    n++;
                }

            }
        } else {
            for (int i = in; i >= fi; i--) {
                FirstBrake fib = linea.get(i - 1);
                if (fib.time > 0) {
                    sum = sum + Math.pow((fib.posx) - mediax(in, fi), 2);
                    n++;
                }
            }
        }

        /*    for(int i=in;i<=fi;i++){
         //sum=sum+Math.pow(fb[i].time-media(in,fi),2);


         }*/
        return sum / (n);
    }

    public double varianza() {
        int fi = linea.size() - 1;

        return varianza(0, fi);
    }

    public double covarianza() {
        int fi = linea.size() - 1;

        return covarianza1(0, fi);
    }

    public double covarianza1(int in, int fi) {
        double sum = 0;
        int n = 0;
        if (in < fi) {
            for (int i = in; i <= fi; i++) {
                FirstBrake fib = linea.get(i);
                if (fib.time > 0) {
                    sum = sum + ((fib.time - media(in, fi)) * ((fib.posx) - mediax(in, fi)));
                    n = n + 1;
                }
            }
        } else {
            for (int i = in; i >= fi; i--) {
                FirstBrake fib = linea.get(i);
                if (fib.time > 0) {
                    sum = sum + ((fib.time - media(in, fi)) * ((fib.posx) - mediax(in, fi)));
                    n++;
                }

            }
        }

        return sum / (n);
    }

    public static double[][] getList(FirstBrakeList FBlist, int layer, boolean direct, double sc, double scm) {
        List<double[]> lista = new ArrayList<double[]>();
        FirstBrake[] list = FBlist.fb;

        for (int i = 0; i < list.length; i++) {

            list[i].offset = Math.abs(list[i].posx - FBlist.scoppio);
            if (list[i].ar == 0 && direct) {
                double[] d = new double[2];
                if (list[i].layer == layer) {
                    d[0] = list[i].posx;
                    d[1] = list[i].time;
                    lista.add(d);
                }

            } else if (list[i].ar == 1 && !direct) {
                double[] d = new double[2];
                if (list[i].layer == layer) {
                    d[0] = list[i].posx;
                    d[1] = list[i].time;
                    lista.add(d);
                }
            }

        }
        double[] d = new double[2];
        if (layer == 1 && lista.size() > 0) {
            if (FBlist.isInLine()) {
                d[0] = FBlist.scoppio;
                d[1] = 0.0;
                lista.add(d);
            }
        }

        double[][] returned = new double[lista.size()][2];
        return lista.toArray(returned);
    }

    public static double[][] getList(FirstBrake[] list, int in, int fi, double sc, double scm) {

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
        if (Math.abs(in - sc) < 1 || Math.abs(fi - sc) < 1) {
            data = new double[f - k + 2][2];
            data[index][0] = scm;
            data[index][1] = 0.0;
            index++;
        }

        for (int j = k; j <= f; j++) {
            data[index][0] = list[j].posx;
            data[index][1] = list[j].time;
            index++;
        }

        if (f - k > 3) {
            System.out.println("" + f);
        }
        return data;
    }

    public static double[][] getList(FirstBrake[] list, int in, int fi) {

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
            data[index][0] = list[j].posx;
            data[index][1] = list[j].time;
            index++;
        }

        return data;
    }

    public static double[][] getList(FirstBrake[] list) {

        int k = 0;
        for (FirstBrake fb : list) {
            if (fb.time > -1) {
                k++;
            }
        }

        double[][] data = new double[k ][2];
        int index = 0;

        for (FirstBrake fb : list) {
            if (fb.time > -1) {
                data[index][0] = fb.posx;
                data[index][1] = fb.time;
                index++;

            }
        }

        return data;
    }

    public static double[] getOLSRegression(double[][] data) {

        int n = data.length;
        if (n < 2) {
            throw new IllegalArgumentException("Not enough data.");
            
        }

        double sumX = 0;
        double sumY = 0;
        double sumXX = 0;
        double sumXY = 0;
        for (int i = 0; i < n; i++) {
            double x = data[i][0];
            double y = data[i][1];
            sumX += x;
            sumY += y;
            double xx = x * x;
            sumXX += xx;
            double xy = x * y;
            sumXY += xy;
        }
        double sxx = sumXX - (sumX * sumX) / n;
        double sxy = sumXY - (sumX * sumY) / n;
        double xbar = sumX / n;
        double ybar = sumY / n;

        // Con tutte le ascisse coincidenti la pendenza non è definita: senza questo
        // controllo sxy/sxx restituiva NaN/Infinity che si propagava nelle velocità.
        if (sxx == 0) {
            throw new IllegalArgumentException("Ascisse coincidenti: regressione non definita.");
        }

        double[] result = new double[2];
        result[1] = sxy / sxx;
        result[0] = ybar - result[1] * xbar;

        return result;

    }

    public double getT(double x) {

        double[][] data =getList(this.fb);
        double[] result=getOLSRegression(data);
        
        double a = result[1];
        double b = result[0];
        System.out.println(">>> a: " + a + "x: " + x + " + b :" + b);
        return a * x + b;
    }

    public double[] getTArray() {
        double[] Time = new double[fb.length];
        for (int i = 0; i < fb.length; i++) {
            Time[i] = fb[i].time;
        }

        return Time;
    }

    public double covarianza(int in, int fi) {
        double sum = 0;
        int n = 0;
        if (in < fi) {
            for (int i = in; i <= fi; i++) {
                FirstBrake fib = linea.get(i);
                if (fib.time > 0) {
                    sum = sum + ((fib.time - media(in, fi)) * ((fib.posx) - mediax(in, fi)));
                    n = n + 1;
                }
            }
        } else {
            for (int i = in; i >= fi; i--) {
                FirstBrake fib = linea.get(i);
                if (fib.time > 0) {
                    sum = sum + ((fib.time - media(in, fi)) * ((fib.posx) - mediax(in, fi)));
                    n++;
                }

            }
        }

        /*for(int i=in;i<=fi;i++){
         sum=sum+((fb[i].time-media(in,fi))*((spaz_in+spaz*i)-(spaz_in+(spaz*(fi-in)))));
         }*/
        return sum / (n);
    }

    private boolean isInLine() {

        if ((spaz_in - spaz) < scoppio && scoppio < (spaz_in + ch * spaz)) {
            return true;
        } else {
            return false;
        }
        //   throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void updateLayer(int in, int fi, int selected, int direzione) {
        if (direzione == 0) {
            switch (selected) {
                case 1:
                    strato1 = in + "-" + fi;

                    break;
                case 2:
                    strato2 = in + "-" + fi;

                    break;
                case 3:
                    strato3 = in + "-" + fi;

                    break;

            }
        } else {
            switch (selected) {
                case 1:
                    strato1R = in + "-" + fi;

                    break;
                case 2:
                    strato2R = in + "-" + fi;

                    break;
                case 3:
                    strato3R = in + "-" + fi;

                    break;

            }

        }

    }

    public void updateLayer(int in, int fi, int selected) {
        if (fb[in].ar == 0) {
            switch (selected) {
                case 1:
                    strato1 = in + "-" + fi;

                    break;
                case 2:
                    strato2 = in + "-" + fi;

                    break;
                case 3:
                    strato3 = in + "-" + fi;

                    break;

            }
        } else {
            switch (selected) {
                case 1:
                    strato1R = in + "-" + fi;

                    break;
                case 2:
                    strato2R = in + "-" + fi;

                    break;
                case 3:
                    strato3R = in + "-" + fi;

                    break;

            }

        }

    }

    @Override
    public void writeExternal(ObjectOutput oo) throws IOException {
        oo.writeDouble(primo);
        oo.writeDouble(scoppio);
        oo.writeDouble(spaz);
        oo.writeDouble(spaz_in);
        oo.writeDouble(tAB);
        oo.writeDouble(xsc);
        oo.writeUTF(fbp);
        oo.writeUTF(strato1);
        oo.writeUTF(strato1R);
        oo.writeUTF(strato2);
        oo.writeUTF(strato2R);
        oo.writeUTF(strato3);
        oo.writeUTF(strato3R);
        oo.writeInt(dromo.length);
        for (JRetta dr : dromo) {
            oo.writeDouble(dr.a);
            oo.writeDouble(dr.b);

        }
        oo.writeInt(dromoR.length);
        for (JRetta dr : dromoR) {
            oo.writeDouble(dr.a);
            oo.writeDouble(dr.b);
        }
        oo.writeInt(fb.length);
        for (FirstBrake brake : fb) {
            oo.writeDouble(brake.time);
            oo.writeDouble(brake.posx);
            oo.writeDouble(brake.offset);
            oo.writeDouble(brake.z);
            oo.writeInt(brake.chan);
            oo.writeInt(brake.ar);
            oo.writeInt(brake.layer);
        }
        oo.writeInt(tr.length);
        for (Trace t : tr) {
            oo.writeInt(t.number);
            oo.writeDouble(t.shotLocation);
            oo.writeDouble(t.sampleInterval);
            oo.writeDouble(t.windowLength);
            oo.writeInt(t.length);
            oo.writeInt(t.value.length);
            for (double v : t.value) {
                oo.writeDouble(v);
            }
        }
        oo.writeInt(linea.size());
        linea=new ArrayList<FirstBrake>(Arrays.asList(fb));
        for (FirstBrake brake : linea) {
            oo.writeDouble(brake.time);
            oo.writeDouble(brake.posx);
            oo.writeDouble(brake.offset);
            oo.writeDouble(brake.z);
            oo.writeInt(brake.chan);
            oo.writeInt(brake.ar);
            oo.writeInt(brake.layer);
            oo.writeBoolean(brake.enabled);
        }

    }

    @Override
    public void readExternal(ObjectInput oi) throws IOException, ClassNotFoundException {

        primo = oi.readDouble();
        scoppio = oi.readDouble();
        spaz = oi.readDouble();
        spaz_in = oi.readDouble();
        tAB = oi.readDouble();
        xsc = oi.readDouble();
        fbp = oi.readUTF();
        strato1 = oi.readUTF();
        strato1R = oi.readUTF();
        strato2 = oi.readUTF();
        strato2R = oi.readUTF();
        strato3 = oi.readUTF();
        strato3R = oi.readUTF();

        int len = oi.readInt();
        dromo = new JRetta[len];
        for (JRetta dr : dromo) {
            dr = new JRetta();
            dr.a = oi.readDouble();
            dr.b = oi.readDouble();

        }
        len = oi.readInt();
        dromoR = new JRetta[len];
        for (JRetta dr : dromoR) {
            dr = new JRetta();
            dr.a = oi.readDouble();
            dr.b = oi.readDouble();
        }
        len = oi.readInt();
        fb = new FirstBrake[len];
        zoomTr = new ZoomTraceUtil[len];
        for (int i = 0; i < len; i++) {
            zoomTr[i] = new ZoomTraceUtil();
            fb[i] = new FirstBrake();
            fb[i].time = oi.readDouble();
            fb[i].posx = oi.readDouble();
            fb[i].offset = oi.readDouble();
            fb[i].z = oi.readDouble();
            fb[i].chan = oi.readInt();
            fb[i].ar = oi.readInt();
            fb[i].layer = oi.readInt();

        }
        len = oi.readInt();

        tr = new Trace[len];
        for (int i = 0; i < len; i++) {
            int numnber = oi.readInt();
            double shotLocation = oi.readDouble();
            double sampleInterval = oi.readDouble();
            double windowLenght = oi.readDouble();
            /* int tmplen = */ oi.readInt();
            int vlen = oi.readInt();
            tr[i] = new Trace(len);
            tr[i].number = numnber;
            tr[i].shotLocation = shotLocation;
            tr[i].sampleInterval = sampleInterval;
            tr[i].windowLength = windowLenght;
            tr[i].value = new double[vlen];
            tr[i].length=vlen;
            double maxValue=0.0;
            double sommaAbsValue=0;
            for (int k=0;k<vlen;k++) {
                double v = oi.readDouble();
                tr[i].value[k]=v;
                if(Math.abs(v)>maxValue){
                    maxValue=Math.abs(v);
                }
                
                sommaAbsValue=sommaAbsValue+Math.abs(v);
            }
            tr[i].setMaxValue(maxValue);
            tr[i].setMediaAbs(sommaAbsValue, vlen);
            

        }
        len = oi.readInt();
        FirstBrake brake = new FirstBrake();
        linea = new ArrayList<FirstBrake>();
        for (int i = 0; i < len; i++) {
            brake = new FirstBrake();
            brake.time = oi.readDouble();
            brake.posx = oi.readDouble();
            brake.offset = oi.readDouble();
            brake.z = oi.readDouble();
            brake.chan = oi.readInt();
            brake.ar = oi.readInt();
            brake.layer = oi.readInt();
            if (VERSION > 1) {
                
                fb[i].enabled = oi.readBoolean();
                brake.enabled= fb[i].enabled;
            }
            linea.add(brake);
        }

        ch = fb.length;
        
       // fb=linea.toArray(new FirstBrake[linea.size()]);
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
