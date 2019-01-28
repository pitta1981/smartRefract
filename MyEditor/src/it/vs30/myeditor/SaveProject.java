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
        
        
        
        fc.setFileFilter(new Seg2FileFilter());
        fc.setMultiSelectionEnabled(true);

        TopComponent tc = WindowManager.getDefault().findTopComponent("geometryViewerTopComponent");
        
        
       // TopComponent tc = WindowManager.getDefault().findTopComponent("DocumentEditor");
        //Lookup tcLookup = tc.getLookup();
        //   ((MyViewerTopComponent) tc).jLabel1.setText("APIObject # save");
        //DocumentEditor de=(DocumentEditor)tc;
        //APIObject obj=de.obj;
        APIObject obj = ((geometryViewerTopComponent) tc).active;
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

                    if (!file.getPath().toLowerCase().endsWith(".txt")) {
                        file = new File(file.getPath() + ".txt");
                    }

                    fos = new FileOutputStream(file);

                    OutputStreamWriter os = new OutputStreamWriter(fos);

                    FirstBrakeList fl =  obj.TraceGroup.get(0);
                    os.write(fl.ch + "\n");
                    os.write(fl.spaz_in + "\n");
                    os.write(fl.spaz + "\n");

                    for (int i = 0; i < obj.TraceGroup.size(); i++) {
                        fl = (FirstBrakeList) obj.TraceGroup.get(i);

                        //os.write(fl.fbp + "\n");
                        String path = fl.fbp;
                        String base = fc.getSelectedFile().getParent();
                        String relative = new File(base).toURI().relativize(new File(path).toURI()).getPath();
                        relative = ResourceUtils.getRelativePath(path, base, File.separator);
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
                    for (int i = 0; i < fl.fb.length - 1; i++) {
                        os.write(fl.fb[i].z + " ");

                    }

                    os.flush();
                    os.close();
                } catch (IOException ex) {
                    //Logger.getLogger(JRefractionView.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Exception ex) {
                    System.out.println("Error in saving project. Try again");

                } finally {
                    try {
                        fos.close();
                    } catch (IOException ex) {
                        //  Logger.getLogger(JRefractionView.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                try {
                    if (file.getPath().toLowerCase().endsWith(".txt")) {
                        file = new File(file.getPath().replace(".txt", "") + ".srefract");
                    }
                    saveSmartRefractProject(file, obj);
                    //fos = new FileOutputStream(file);
                    //ObjectOutputStream oos = new ObjectOutputStream(fos);
                    //oos.writeObject(obj);
                    //oos.flush();
                    //oos.close();
                } catch (Exception ex) {

                } finally {
                    /*
                    try {
                        fos.close();
                    } catch (IOException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                            */
                }

                //This is where a real application would open the file.
                //System.out.println("Opening: " + file.getName() + "." );
            } else {
                //log.append("Open command cancelled by user." + newline);
            }
        } else {
        }
        // TODO implement action body
    }

    public static void saveSmartRefractProject(File file,APIObject obj) {
       FileOutputStream fos=null;
        try {
            fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(obj);
            oos.flush();
            oos.close();
        } catch (IOException ex) {

        } finally {
            try {
                fos.close();
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }

    }

    class Seg2FileFilter extends javax.swing.filechooser.FileFilter {

        @Override
        public boolean accept(File f) {
            return f.isDirectory() || f.getName().toLowerCase().endsWith(".txt");
        }

        @Override
        public String getDescription() {
            return "smartRefract project file";
        }
    }

}
