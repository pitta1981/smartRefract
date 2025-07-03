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
import it.vs30.myeditor.folder_history;

public final class OpenPrj implements ActionListener {
    // File progetto aperto più recente (per welcomeTopComponent)
    public static File lastOpenedFile = null;
    private boolean mTxt = false;
    private geometryViewerTopComponent geomTC;

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO implement action body
        // Crea document editor per il progetto
        DocumentEditor editor = new DocumentEditor();
        mTxt = false;  // L'apertura avverrà dopo il caricamento

        folder_history fh = new folder_history();
        JFileChooser fc = new JFileChooser();

        // fc.setFileFilter(new Seg2FileFilter());
        FileNameExtensionFilter fileFilter1 = new FileNameExtensionFilter("smartRefract 2013", "txt");
        FileNameExtensionFilter fileFilter2 = new FileNameExtensionFilter("smartRefract 2016", "srefract");
        FileNameExtensionFilter fileFilter3 = new FileNameExtensionFilter("OpenRefract format", "orefract");
        fc.addChoosableFileFilter(fileFilter1);
        fc.addChoosableFileFilter(fileFilter2);
        fc.addChoosableFileFilter(fileFilter3);
        fc.setFileFilter(fileFilter3); // Default to OpenRefract format
        //setthe default directory of the file chooser to the last opened folder   
        fc.setCurrentDirectory(new File(folder_history.getLastOpenedFolder()));

        
        fc.setAcceptAllFileFilterUsed(true);

        int returnVal = fc.showOpenDialog(null);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            //File file = fc.getSelectedFile();

            //editor.obj.in_file=fc.getSelectedFile();
            editor.obj.TraceGroup = new ArrayList<FirstBrakeList>();
            String path = fc.getSelectedFile().getAbsolutePath();
            String base = (fc.getSelectedFile().getParent());
            //String relative = new File(base).toURI().relativize(new File(path).toURI()).getPath();
            //editor.obj.newFb();
            //editor.obj.loadSism(0);
            //editor.obj.tr=editor.obj.getTraces();
            File file = fc.getSelectedFile();
            // Aggiorna la variabile statica per i recenti
            OpenPrj.lastOpenedFile = file;
            if (file.getPath().toLowerCase().endsWith(".txt")) {
                loadPrj(fc.getSelectedFile(), editor.obj, base);
                mTxt = true;
                editor.open();
                editor.requestActive();
                editor.setDisplayName(fc.getSelectedFile().getName());
                editor.invalidate();
            } else if (file.getPath().toLowerCase().endsWith(".orefract")) {
                // Caricamento progetto OpenRefract (.orefract)
                try {
                    editor.obj = OpenRefractLoader.loadOpenRefractProject(file);
                    // Sincronizza modelli e tracce
                    editor.obj.sync();
                } catch (IOException ex) {
                    javax.swing.JOptionPane.showMessageDialog(null,
                        "Errore caricamento OpenRefract: " + ex.getMessage(),
                        "Errore", javax.swing.JOptionPane.ERROR_MESSAGE);
                    return;
                }
                // Aggiorna le view con il nuovo modello
                editor.txV.setObj(editor.obj);
                editor.sv.setObj(editor.obj);
                editor.tv.setObj(editor.obj);
                // Forza repaint delle view
                editor.txV.repaint();
                editor.sv.repaint();
                editor.tv.repaint();
                editor.open();
                editor.requestActive();
                editor.setDisplayName(fc.getSelectedFile().getName());
                editor.invalidate();
            } else if (file.getPath().toLowerCase().endsWith(".srefract")) {
                // Load binary format
                loadPrjSmartRefract(file, editor);
                editor.open();
                editor.requestActive();
                editor.setDisplayName(fc.getSelectedFile().getName());
                editor.invalidate();
            }
            folder_history.saveLastOpenedFolder(file.getParent());
            // Dopo il caricamento, apri l'editor e aggiorna UI
            // editor.open();
            // editor.requestActive();
            // editor.setDisplayName(fc.getSelectedFile().getName());
            // editor.invalidate();
            // Aggiorna lista recenti
            try {
                it.vs30.welcome.welcomeTopComponent wTopComponent =
                    (it.vs30.welcome.welcomeTopComponent)
                    org.openide.windows.WindowManager.getDefault().findTopComponent("welcomeTopComponent");
                if (wTopComponent != null) {
                    wTopComponent.updateRecentProject();
                }
            } catch (Exception ex) {
                System.err.println("Impossibile aggiornare la lista recenti: " + ex.getMessage());
            }

            editor.obj.proj_file = file;
            if (mTxt) {
                loadtrace(editor.obj, base);
                editor.obj.loadSism(0);
                editor.obj.LoadTrace_For_Open();
            }
            // TODO: Altri aggiornamenti UI se necessari

         } else {
             // log.cancelled
         }

        // TODO implement action body
    }

    public void loadPrj(File f, APIObject obj, String base) {
        try {
            FileInputStream fis = new FileInputStream(f);

            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);

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
            obj.fb = obj.TraceGroup.get(0);
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
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        boolean loadingSuccess = false;
        
        try {
            if (file.getPath().toLowerCase().endsWith(".txt")) {
                file = new File(file.getPath().replace(".txt", "") + ".srefract");
            }

            fis = new FileInputStream(file);
            ois = new ObjectInputStream(fis);

            APIObject loadingObj = (APIObject) ois.readObject();
            editor.obj = loadingObj;
            loadingSuccess = true;

        } catch (IOException ex) {
            System.err.println("Errore durante il caricamento del file: " + file.getAbsolutePath());
            System.err.println("Dettagli errore: " + ex.getMessage());
            Exceptions.printStackTrace(ex);
        } catch (ClassNotFoundException ex) {
            System.err.println("Classe non trovata durante la deserializzazione: " + ex.getMessage());
            Exceptions.printStackTrace(ex);
        } finally {
            // Chiudi i flussi
            try {
                if (ois != null) ois.close();
                if (fis != null) fis.close();
            } catch (IOException ex) {
                System.err.println("Errore durante la chiusura dei flussi: " + ex.getMessage());
            }
            
            // Solo se il caricamento è riuscito, procedi con l'inizializzazione
            if (loadingSuccess && editor.obj != null && editor.obj.TraceGroup != null && !editor.obj.TraceGroup.isEmpty()) {
                editor.obj.proj.stesa = editor.obj.TraceGroup;
                editor.txV.setObj(editor.obj);
                editor.sv.setObj(editor.obj);
                editor.tv.obj = editor.obj;
                editor.tv.proj = editor.obj.proj;
            } else {
                System.err.println("Caricamento fallito o TraceGroup vuoto/null. Inizializzazione saltata.");
            }
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
