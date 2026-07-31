/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.vs30.myeditor;

import it.vs30.geometryView.geometryViewerTopComponent;
import it.vs30.myeditor.SaveProject;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import javax.swing.JFileChooser;
import org.myorg.myapi.APIObject;
import org.myorg.myapi.FirstBrake;
import org.myorg.myapi.FirstBrakeList;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

@ActionID(
        category = "File",
        id = "it.vs30.geometryView.ExportTravelTime"
)
@ActionRegistration(
        displayName = "#CTL_ExportTravelTime"
)
@ActionReference(path = "Menu/File", position = 1750, separatorBefore = 1725)
@Messages("CTL_ExportTravelTime=Export travel time to smartTomo...")
public final class ExportTravelTime implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO implement action body
        JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new ExportTravelTime.OTTEFilter());
        fc.setMultiSelectionEnabled(true);

        TopComponent tc = WindowManager.getDefault().findTopComponent("geometryViewerTopComponent");
        APIObject obj = ((geometryViewerTopComponent) tc).active;
        try {
            fc.setCurrentDirectory((obj.proj_file));
        } catch (Exception ex) {

        }
        int returnVal = fc.showSaveDialog(null);
        //  int returnVal = fc.showSaveDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                File file = fc.getSelectedFile();
                if (!file.getPath().toLowerCase().endsWith(".otte")) {
                    file = new File(file.getPath() + ".otte");
                }
                
                
                try (FileOutputStream fos2 = new FileOutputStream(file);
                     OutputStreamWriter os = new OutputStreamWriter(fos2)) {
                    os.write("otte\n");
                    os.write(""+1.0+"\n");
                    os.write(2+"\n");
                    os.write(1+"\n");
                     FirstBrakeList fl =  obj.TraceGroup.get(0);
                    String posx="";
                    String posz="";
                
                    for(FirstBrake fb:fl.fb){
                       posx=posx+fb.posx+";";
                       posz=posz+fb.z+";";
                    }
                    os.write(posx+"\n");
                    os.write(posz+"\n");
                    posx="";
                    posz="";
                
                    for(FirstBrakeList FBL:obj.TraceGroup){
                        posx=posx+FBL.scoppio+";";
                        posz=posz+"0.0"+";";
                    
                    }
                    os.write(posx+"\n");
                    os.write(posz+"\n");
                    int i=0;
                    for(FirstBrakeList FBL:obj.TraceGroup){
                        String tt="";
                        for(FirstBrake fb:FBL.fb){
                            tt=tt+fb.time+";";
                        }
                        os.write("0\n");
                        os.write(i+"\n");
                        os.write(tt+"\n");
                        i++;
                    }
                
                    os.flush();
                }

            } catch (FileNotFoundException ex) {
                Exceptions.printStackTrace(ex);
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        
        }
    }

    class OTTEFilter extends javax.swing.filechooser.FileFilter {

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
