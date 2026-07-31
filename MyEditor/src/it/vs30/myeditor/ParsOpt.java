/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.vs30.myeditor;
//import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.ServiceProvider;

import it.vs30.geometryView.geometryViewerTopComponent;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.myorg.myapi.APIObject;
import org.myorg.myapi.FirstBrakeList;
//import org.myorg.myviewer.MyViewerTopComponent;
import org.netbeans.api.sendopts.CommandException;
import org.netbeans.spi.sendopts.Env;
import org.netbeans.spi.sendopts.Option;
import org.netbeans.spi.sendopts.OptionProcessor;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 *
 * @author pitta1981
 */
@ServiceProvider(service = OptionProcessor.class)
public class ParsOpt extends OptionProcessor {

    private Option openOption = Option.defaultArguments();
    private Option openOption2 = Option.additionalArguments(
            'o', "open");
    //   private int lt;
    //   private int i;
    private geometryViewerTopComponent geomTC;

    @Override
    public Set getOptions() {
        HashSet set = new HashSet();
        set.add(openOption);
        set.add(openOption2);
        return set;
    }

    @Override
    public void process(Env env, Map values)
            throws CommandException {
        List<File> filenameList = new ArrayList<File>();
        List<String> projectnameList = new ArrayList<String>();

        Object obj = values.get(openOption);
        if (obj != null) {
            projectnameList.addAll(Arrays.asList((String[]) obj));
            System.out.println("EUREKA " + (projectnameList.get(0)));
            filenameList = getFileName(projectnameList);
            DocumentEditor editor = new DocumentEditor();
            editor.open();
            editor.requestActive();
            editor.obj.TraceGroup = new ArrayList<FirstBrakeList>();
            System.out.println("EUREKA LISTA LETTA");

            String path = ((File) filenameList.get(0)).getAbsolutePath();
            String base = (new File(path).getParent());
            //  String relative = new File(base).toURI().relativize(new File(path).toURI()).getPath();
            System.out.println("EUREKA " + path);

            try {
                editor.obj.in_file_l = new File[filenameList.size()];
                filenameList.toArray(editor.obj.in_file_l);
            } catch (Exception e) {
                System.out.println("- 1 " + e.getMessage() + " " + e.getLocalizedMessage());
                e.printStackTrace();
            }
            editor.obj.newFb();
            editor.obj.loadSism(0);
            editor.obj.tr = editor.obj.getTraces();
            editor.obj.fb = editor.obj.TraceGroup.get(0);
            editor.jTracePath.setText(editor.obj.fb.fbp);
            editor.tv.repaint();
            editor.invalidate();

            Set_geometry sg = new Set_geometry();
            sg.actionPerformed(null);

            //editor.obj.newFb();
            //editor.obj.loadSism(0);
            //editor.obj.tr=editor.obj.getTraces();
            //loadPrj(new File(path), editor.obj, base);
            editor.setDisplayName(new File(path).getName());
            base = "";
            loadtrace(editor.obj, base);

            editor.obj.loadSism(0);
            editor.obj.tr = editor.obj.getTraces();

            editor.obj.fb = editor.obj.TraceGroup.get(0);

            editor.tv.repaint();
            // editor.dv.setProj(editor.obj.proj);
            editor.invalidate();

            TopComponent tc = WindowManager.getDefault().findTopComponent("MyViewerTopComponent");
            Lookup tcLookup = tc.getLookup();

       /*     ((MyViewerTopComponent) tc).setActive(editor.obj);
            ((MyViewerTopComponent) tc).gmview.setBackground(Color.black);
            ((MyViewerTopComponent) tc).gmview.setGeom(editor.obj.fb.scoppio, editor.obj.fb.spaz, editor.obj.fb.spaz_in, editor.obj.tr.length);
            ((MyViewerTopComponent) tc).gmview.repaint();
            ((MyViewerTopComponent) tc).gmview.invalidate();*/

            tc = WindowManager.getDefault().findTopComponent("geometryViewerTopComponent");
            geomTC = (geometryViewerTopComponent) tc;
            geomTC.gmview.setGeom(editor.obj.fb.scoppio, editor.obj.fb.spaz, editor.obj.fb.spaz_in, editor.obj.tr.length);
            geomTC.gmview.repaint();
            geomTC.gmview.invalidate();

            editor.jTracePath.setText(editor.obj.fb.fbp);

            //System.err.println("Eureka");
        }
        obj = values.get(openOption2);
        /* if (obj != null) {

         filenameList.addAll(Arrays.asList((String[]) obj));
         }

         for (int i = 0; i < filenameList.size(); i++) {
         File file = new File(filenameList.get(i));
         if (!file.isAbsolute()) {
         file = new File(env.getCurrentDirectory(),
         filenameList.get(i));
         }

         //System.out.println(file.toString());

         try {
         } catch (OutOfMemoryError ex) {
         } catch (Exception ex) {
         }
         }*/

    }

    public void loadtrace(APIObject obj, String base) {
        obj.in_file_l = new File[obj.TraceGroup.size()];
        for (int i = 0; i < obj.TraceGroup.size(); i++) {
            obj.in_file_l[i] = new File((obj.TraceGroup.get(i)).fbp);
        }
    }

    public void loadPrj(File f, APIObject obj, String base) {
        try (FileInputStream fis = new FileInputStream(f);
             InputStreamReader isr = new InputStreamReader(fis);
             BufferedReader br = new BufferedReader(isr)) {

            String linea = br.readLine();
            //int n=Integer.parseInt(linea);

            //  jpWave.setStatus(statusMessageLabel);
            //jpWave.newSignal(16);
            //jpWave.sample[1]=new ArrayList();
            //linea = br.readLine();
            int chanel = Integer.parseInt(linea);

            linea = br.readLine();
            double spaz_in = Double.parseDouble(linea);

            linea = br.readLine();
            double spaz = Double.parseDouble(linea);
            linea = br.readLine();

            //double spaz = 2;
            //double spaz_in = 12;
            //int chanel = 16;
            /*
             * fiB.setChanel(chanel);
             */
            int ch = 1;

            //obj.fb.setChanel(chanel);
            //if((fiB.scoppio-fiB.spaz_in)/fiB.spaz>0 && (fiB.scoppio-fiB.spaz_in)/fiB.spaz<chanel){
            //    System.out.println("***** SPLIT ***** "+(fiB.scoppio-fiB.spaz_in)/fiB.spaz);
            while (linea != null) {
                try {
                    System.out.println("* File :" + linea);

                    obj.fb = new FirstBrakeList();
                    obj.fb.setChanel(chanel);
                    obj.fb.fbp = new File(base, linea.replace("\\", "/")).getPath();
                    // obj.fb.fbp = base+linea;

                    obj.fb.spaz = spaz;
                    obj.fb.spaz_in = spaz_in;
                    if (obj.fb.scoppio < obj.fb.spaz_in) {
                        obj.fb.AR = 0;
                    } else {
                        obj.fb.AR = 1;
                    }

                    linea = br.readLine();
                    double shot = Double.parseDouble(linea);
                    obj.fb.scoppio = shot;
                    if (obj.fb.scoppio < obj.fb.spaz_in) {
                        obj.fb.AR = 0;
                    } else {
                        obj.fb.AR = 1;
                    }

                    System.out.println("#PosShot:" + linea);
                    linea = br.readLine();
                    String[] split = linea.split(" ");
                    System.out.println("@ " + linea + "# " + split.length);
                    for (int i = 0; i < split.length; i++) {
                        if (((shot - spaz_in) / spaz) < i) {
                            System.out.print(" A ");
                        }
                        System.out.print(i + " # " + split[i] + " * ");
                        obj.fb.setFB(i, Double.parseDouble(split[i]));
                    }

                    obj.fb.strato1 = br.readLine();
                    obj.fb.strato2 = br.readLine();
                    obj.fb.strato3 = br.readLine();
                    obj.fb.strato1R = br.readLine();
                    obj.fb.strato2R = br.readLine();
                    obj.fb.strato3R = br.readLine();

                    //proj.AddFb(fiB);
                    //System.out.println(" # proj AddFB : " + proj.stesa.size());
                    linea = br.readLine();
                    if ("Elevation".equals(linea)) {
                        linea = br.readLine();
                        split = linea.split(" ");
                        for (int i = 0; i < split.length; i++) {
                            obj.fb.fb[i].z = Double.parseDouble(split[i]);
                            System.out.println(obj.fb.fb[i].z);
                        }
                    }
                    obj.TraceGroup.add(obj.fb);

                } catch (Exception e1) {
                    System.out.println("linea " + linea + " #- test" + e1.getMessage());
                }
                //System.out.println("% in :"+Double.toString(fiB.getFB(ch).time));
                //linea=br.readLine();
                ch++;

            }
            // proj.setdromo();
            obj.fb =  obj.TraceGroup.get(0);
            //   jdromo.setProj(proj);
            //     jdromo.setFB(fiB);
        } catch (IOException e1) {
            System.out.println("Error You chose to open this file: " + e1);
        }

        obj.sync();
        obj.fb = obj.TraceGroup.get(obj.TraceGroup.size() - 1);
        for (int i = 0; i < obj.proj.stesa.size(); i++) {
            FirstBrakeList fbl = obj.proj.stesa.get(i);
            for (int j = 0; j < fbl.fb.length - 1; j++) {
                fbl.fb[j].z = (Double) obj.fb.fb[j].z;

            }
        }

        System.out.println("end load");

    }

    private List<File> getFileName(List<String> projectnameList) {
        List<File> lista = new ArrayList<File>();

        String base = (new File(projectnameList.get(0)).getParent());
        //String relative = new File(base).toURI().relativize(new File(path).toURI()).getPath();

        base = base.replace("\\", "/");
        File f = new File(projectnameList.get(0));
        String path = new File(base, (String) (projectnameList.get(0)).replace("\\", "/")).getPath();
        try (FileInputStream fis = new FileInputStream(f);
             InputStreamReader isr = new InputStreamReader(fis);
             BufferedReader br = new BufferedReader(isr)) {
            System.out.println("*** APERTO ***");

            String linea = br.readLine();
            while (linea != null) {

                //base = (new File(linea).getParent());
                //String relative = new File(base).toURI().relativize(new File(path).toURI()).getPath();
                //base = linea.replace("\\", "/");
                //String s="e:\\Programmi VB6\\JEA247ESAC V2 grafica\\Data\\uno.sg2";
                // Path p=Paths.get(s.replace("\\", "/"));
                // String path=p.getFileName().toString();
                Path p = Paths.get(linea.replace("\\", "/"));
                path = p.getFileName().toString();
                //path = new File(base, (String) (linea).replace("\\", "/")).getPath();

                System.out.println("*** APERTO ***\t" + linea + "*\t*" + path);
                if (new File(linea).exists()) {
                    lista.add(new File(linea));
                    System.out.println("*** " + linea);
                } else {
                    String filename = path;
                    System.out.println("*** APERTO ***\t" + filename);
                    if (new File(filename).exists()) {
                        lista.add(new File(filename));
                        System.out.println("*#* " + filename);
                    } else {
                        String separator = File.separator;
                        System.out.println("*** APERTO ***\t" + base + separator + filename);

                        if (new File(base + separator + filename).exists()) {
                            lista.add(new File(base + separator + filename));
                            System.out.println("### " + base + separator + filename);
                        } else {
                            throw new UnsupportedOperationException("Not supported yet.");
                        }
                    }
                }

                linea = br.readLine();
            }

        } catch (FileNotFoundException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        return lista;

        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
