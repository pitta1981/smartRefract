/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.vs30.myeditor;

import it.vs30.geometryView.geometryViewerTopComponent;
import it.vs30.welcome.welcomeTopComponent;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.myorg.myapi.APIObject;
import org.myorg.myapi.FirstBrakeList;
//import org.myorg.myviewer.MyViewerTopComponent;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

public final class OpenPrj implements ActionListener {

    private boolean mTxt = false;
    private geometryViewerTopComponent geomTC;

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO implement action body
        DocumentEditor editor = new DocumentEditor();
        mTxt = false;
        editor.open();
        editor.requestActive();
        JFileChooser fc = new JFileChooser();

        // fc.setFileFilter(new Seg2FileFilter());
        FileNameExtensionFilter fileFilter1 = new FileNameExtensionFilter("smartRefract 2013", "txt");
        FileNameExtensionFilter fileFilter2 = new FileNameExtensionFilter("smartRefract 2016", "srefract");
        fc.addChoosableFileFilter(fileFilter1);
        fc.addChoosableFileFilter(fileFilter2);
        fc.setFileFilter(fileFilter2);

//     fc.setMultiSelectionEnabled(true);
        fc.setAcceptAllFileFilterUsed(true);

        int returnVal = fc.showOpenDialog(editor);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            //File file = fc.getSelectedFile();

            //editor.obj.in_file=fc.getSelectedFile();
            editor.obj.TraceGroup = new ArrayList();
            String path = fc.getSelectedFile().getAbsolutePath();
            String base = (fc.getSelectedFile().getParent());
            //String relative = new File(base).toURI().relativize(new File(path).toURI()).getPath();
            //editor.obj.newFb();
            //editor.obj.loadSism(0);
            //editor.obj.tr=editor.obj.getTraces();
            File file = fc.getSelectedFile();
            if (file.getPath().toLowerCase().endsWith(".txt")) {
                loadPrj(fc.getSelectedFile(), editor.obj, base);
                mTxt = true;
            }

            String[] lista = new String[3];
            String userHome = "user.home";

            // We get the path by getting the system property with the 
            // defined key above. path+"/smartRefract-data/"
            String path1 = System.getProperty(userHome);

            FileInputStream fis = null;
            try {
                fis = new FileInputStream(path1 + "/smartRefract-data/" + "recenti.list");
            } catch (FileNotFoundException ex) {
                //   Exceptions.printStackTrace(ex);
            }
            try {
                fis = new FileInputStream(path1 + "/smartRefract-data/" + "recenti.list");

                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader br = new BufferedReader(isr);
                String linea = br.readLine();
                int i = 0;
                while (linea != null && i < 3) {
                    lista[i] = linea;
                    i++;
                    linea = br.readLine();
                }

                isr.close();
                fis.close();
            } catch (Exception ex) {
            } finally {
                /*        try {
                 // fis.close();
                 } catch (IOException ex) {
                 Exceptions.printStackTrace(ex);
                 }*/
            }
            boolean pathPresent = false;
            for (int i = 0; i < lista.length; i++) {
                if (lista[i] == path) {
                    lista[2] = lista[1];
                    lista[1] = lista[0];
                    lista[0] = lista[i];
                    pathPresent = true;

                }

            }
            if (!pathPresent) {
                lista[2] = lista[1];
                lista[1] = lista[0];
                lista[0] = path;
            }
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(path1 + "/smartRefract-data/" + "recenti.list");

                OutputStreamWriter os = new OutputStreamWriter(fos);
                int i = 0;
                while (i < 3) {
                    os.write(lista[i] + "\n");
                    i++;
                }

                os.flush();
                os.close();
            } catch (Exception ex) {
            } finally {
                try {
                    fos.close();
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }

            welcomeTopComponent wTopComponent = (welcomeTopComponent) WindowManager.getDefault().findTopComponent("welcomeTopComponent");
            wTopComponent.updateRecentProject();
            if (!mTxt) {
                loadPrjSmartRefract(file, editor);
            }
            try {
                fos.close();
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }

            editor.setDisplayName(fc.getSelectedFile().getName());
            editor.obj.proj_file = fc.getSelectedFile();
            if (mTxt) {
                base = "";
                loadtrace(editor.obj, base);
                editor.obj.loadSism(0);
                editor.obj.LoadTrace_For_Open();
                editor.obj.tr = editor.obj.getTraces();
            }
            //  editor.obj.loadSism(0);
            //  editor.obj.LoadTrace_For_Open();
            
            editor.obj.fb =  editor.obj.TraceGroup.get(0);
            editor.tv.obj = editor.obj;

            editor.obj.sync();
            editor.tv.repaint();
            //editor.dv.setProj(editor.obj.proj);
            editor.invalidate();

            TopComponent tc = WindowManager.getDefault().findTopComponent("MyViewerTopComponent");
          
            editor.jLabel1.setText(editor.obj.fb.fbp);

            tc = WindowManager.getDefault().findTopComponent("geometryViewerTopComponent");
            geomTC = (geometryViewerTopComponent) tc;
            geomTC.setActive(editor.obj);
            geomTC.gmview.setStesa(editor.obj.proj.stesa);
            geomTC.gmview.setBackground(Color.black);
            geomTC.gmview.setGeom(editor.obj.fb.scoppio, editor.obj.fb.spaz, editor.obj.fb.spaz_in, editor.obj.tr.length);
            geomTC.gmview.repaint();
            geomTC.gmview.invalidate();

            //This is where a real application would open the file.
            //System.out.println("Opening: " + file.getName() + "." );
        } else {
            //log.append("Open command cancelled by user." + newline);
        }

        // TODO implement action body
    }

    public void loadPrj(File f, APIObject obj, String base) {
        try {
            FileInputStream fis = new FileInputStream(f);

            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);

            String linea = br.readLine();
            
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
                    //System.out.println("* File :" + linea);

                    obj.fb = new FirstBrakeList();
                    obj.fb.setChanel(chanel);
                    obj.fb.fbp = new File(base, linea.replace("\\", "/")).getPath();
                    // obj.fb.fbp = base+linea;

                    obj.fb.spaz = spaz;
                    obj.fb.spaz_in = spaz_in;

                    linea = br.readLine();
                    double shot = Double.parseDouble(linea);
                    obj.fb.scoppio = shot;
                    if (obj.fb.scoppio < obj.fb.spaz_in) {
                        obj.fb.AR = 0;
                    } else {
                        obj.fb.AR = 1;
                    }

                    //System.out.println("#PosShot:" + linea);
                    linea = br.readLine();
                    String[] split = linea.split(" ");
                    //System.out.println("@ " + linea + "# " + split.length);
                    int delta = 0;
                    if (Double.parseDouble(split[split.length - 1]) < 0) {
                        delta = -1;
                    } else {
                        delta = 0;
                    }
                    for (int i = 0; i < split.length + delta; i++) {
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
                    System.out.println("linea " + linea + " #- test " + e1.getMessage());
                }
                //System.out.println("% in :"+Double.toString(fiB.getFB(ch).time));
                //linea=br.readLine();
                ch++;

            }
            // proj.setdromo();
            obj.fb = (FirstBrakeList) obj.TraceGroup.get(0);
            //   jdromo.setProj(proj);
            //     jdromo.setFB(fiB);
        } catch (IOException e1) {
            System.out.println("Error You chose to open this file: " + e1);
        }

        obj.sync();
        //obj.fb = (FirstBrakeList) obj.TraceGroup.get(obj.TraceGroup.size() - 1);
        for (int i = 0; i < obj.proj.stesa.size(); i++) {
            FirstBrakeList fbl = (FirstBrakeList) obj.proj.stesa.get(i);
            for (int j = 0; j < fbl.fb.length - 1; j++) {
                fbl.fb[j].z = (Double) obj.fb.fb[j].z;

            }
        }

        System.out.println("end load");

    }

    public void loadtrace(APIObject obj, String base) {
        obj.in_file_l = new File[obj.TraceGroup.size()];
        for (int i = 0; i < obj.TraceGroup.size(); i++) {
            obj.in_file_l[i] = new File(((FirstBrakeList) obj.TraceGroup.get(i)).fbp);
        }
    }

    public void loadPrjSmartRefract(File file, DocumentEditor editor) {
        try {
            if (file.getPath().toLowerCase().endsWith(".txt")) {
                file = new File(file.getPath().replace(".txt", "") + ".srefract");
            }

            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);

            APIObject loadingObj = (APIObject) ois.readObject();
            editor.obj = loadingObj;

//ois.writeObject(obj);
            //oos.flush();
            //oos.close();
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        } catch (ClassNotFoundException ex) {
            Exceptions.printStackTrace(ex);
        } finally {
            editor.obj.proj.stesa = editor.obj.TraceGroup;
// TODO:         
            editor.txV.setObj(editor.obj);
            editor.sv.setObj(editor.obj);
            editor.tv.obj = editor.obj;
            editor.tv.proj = editor.obj.proj;

        }

        //     throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    class Seg2FileFilter extends javax.swing.filechooser.FileFilter {

        @Override
        public boolean accept(File f) {
            return f.isDirectory() || f.getName().toLowerCase().endsWith(".txt") || f.getName().toLowerCase().endsWith(".srefract");
        }

        @Override
        public String getDescription() {
            return "progetto smartRefract 2013; progetto smartRefract 2016;";
        }
    }
}
