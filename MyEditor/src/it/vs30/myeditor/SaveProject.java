/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.vs30.myeditor;

import it.vs30.geometryView.geometryViewerTopComponent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import javax.swing.JFileChooser;
import org.myorg.myapi.APIObject;
import org.myorg.myapi.FirstBrakeList;
import org.openide.util.Exceptions;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

public final class SaveProject implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser fc = new JFileChooser();
        /*MyEditor editor = new MyEditor();
         editor.open();
         editor.requestActive();*/
        
        
        
        fc.setFileFilter(new OpenRefractFileFilter());
        fc.addChoosableFileFilter(new Seg2FileFilter());
        fc.setMultiSelectionEnabled(false);

        TopComponent tc = WindowManager.getDefault().findTopComponent("DocumentEditor");
        
        
       // TopComponent tc = WindowManager.getDefault().findTopComponent("DocumentEditor");
        //Lookup tcLookup = tc.getLookup();
        //   ((MyViewerTopComponent) tc).jLabel1.setText("APIObject # save");
        DocumentEditor de=(DocumentEditor)tc;
        APIObject obj=de.obj;
        //APIObject obj = ((geometryViewerTopComponent) tc).active;
        //   ((MyViewerTopComponent) tc).jLabel2.setText("APIObject # trace " + obj.tr.length);
        try{
        fc.setCurrentDirectory((obj.proj_file));
        }
        catch(Exception ex){
            
        }
        
        if (obj.proj.licenza) {

            int returnVal = fc.showSaveDialog(null);
            //  int returnVal = fc.showSaveDialog(null);
            FileOutputStream fos = null;
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                try {
                    
                    // Check file extension to determine format
                    boolean useOpenRefractFormat = false;
                    if (file.getPath().toLowerCase().endsWith(".orefract")) {
                        useOpenRefractFormat = true;
                    } else if (!file.getPath().toLowerCase().endsWith(".txt") && 
                              !file.getPath().toLowerCase().endsWith(".orefract")) {
                        // Default to OpenRefract format if no extension specified
                        file = new File(file.getPath() + ".orefract");
                        useOpenRefractFormat = true;
                    }
                    
                    if (useOpenRefractFormat) {
                        // Save in new OpenRefract JSON format
                        OpenRefractWriter.saveOpenRefractProject(file, obj);
                        System.out.println("Project saved in OpenRefract format: " + file.getAbsolutePath());
                    } else {
                        // Save in legacy text format
                        saveLegacyFormat(file, obj);
                    }
                    
                } catch (Exception ex) {
                    // Senza notifica l'utente credeva di aver salvato un progetto perso.
                    System.out.println("Error saving project: " + ex.getMessage());
                    ex.printStackTrace();
                    javax.swing.JOptionPane.showMessageDialog(null,
                            "Impossibile salvare il progetto:\n" + ex.getMessage(),
                            "Errore di salvataggio", javax.swing.JOptionPane.ERROR_MESSAGE);
                }
            } else {
                //log.append("Open command cancelled by user." + newline);
            }
        } else {
        }
        // TODO implement action body
    }

    public static void saveSmartRefractProject(File file, APIObject obj) throws IOException {
        // L'eccezione va propagata: inghiottirla lasciava all'utente un file
        // troncato o vuoto con la conferma di salvataggio avvenuto.
        try (FileOutputStream fos = new FileOutputStream(file);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(obj);
            oos.flush();
        }
    }
    
    /**
     * Saves project in legacy text format for backward compatibility
     */
    private void saveLegacyFormat(File file, APIObject obj) throws IOException {
        if (obj.TraceGroup == null || obj.TraceGroup.isEmpty()) {
            throw new IOException("Nessuno scoppio da salvare: il progetto è vuoto.");
        }
        if (!file.getPath().toLowerCase().endsWith(".txt")) {
            file = new File(file.getPath() + ".txt");
        }

        try (FileOutputStream fos = new FileOutputStream(file);
             OutputStreamWriter os = new OutputStreamWriter(fos)) {

            FirstBrakeList fl = obj.TraceGroup.get(0);
            os.write(fl.ch + "\n");
            os.write(fl.spaz_in + "\n");
            os.write(fl.spaz + "\n");

            for (int i = 0; i < obj.TraceGroup.size(); i++) {
                fl = obj.TraceGroup.get(i);

                String path = fl.fbp;
                String base = file.getParent();
                String relative = path; // Simplified relative path handling
                try {
                    relative = new File(base).toURI().relativize(new File(path).toURI()).getPath();
                } catch (Exception e) {
                    // Use absolute path if relative path calculation fails
                }
                
                os.write(relative + "\n");
                os.write("" + fl.scoppio + "\n");
                for (int j = 0; j < fl.fb.length; j++) {
                    System.out.println("saving... " + j);
                    os.write(fl.fb[j].time + " ");
                }
                os.write("\n");
                os.write(fl.strato1 + "\n");
                os.write(fl.strato2 + "\n");
                os.write(fl.strato3 + "\n");
                os.write(fl.strato1R + "\n");
                os.write(fl.strato2R + "\n");
                os.write(fl.strato3R + "\n");
            }
            
            os.write("Elevation\n");
            for (int i = 0; i < fl.fb.length; i++) {
                os.write(fl.fb[i].z + " ");
            }

            os.flush();
        }

        // Also save binary format for compatibility
        File binaryFile = new File(file.getPath().replace(".txt", "") + ".srefract");
        saveSmartRefractProject(binaryFile, obj);
    }

    class Seg2FileFilter extends javax.swing.filechooser.FileFilter {

        @Override
        public boolean accept(File f) {
            return f.isDirectory() || 
                   f.getName().toLowerCase().endsWith(".txt") ||
                   f.getName().toLowerCase().endsWith(".orefract");
        }

        @Override
        public String getDescription() {
            return "smartRefract project files (*.txt, *.orefract)";
        }
    }
    
    class OpenRefractFileFilter extends javax.swing.filechooser.FileFilter {

        @Override
        public boolean accept(File f) {
            return f.isDirectory() || f.getName().toLowerCase().endsWith(".orefract");
        }

        @Override
        public String getDescription() {
            return "OpenRefract format (*.orefract)";
        }
    }

}
