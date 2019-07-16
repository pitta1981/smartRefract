/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.vs30.myeditor;

import it.vs30.geometryView.geometryViewerTopComponent;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JFileChooser;
import org.openide.awt.ActionID;
import org.openide.awt.ActionRegistration;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

@ActionID(id = "it.vs30.myeditor.MyEditorAction1", category = "File")
@ActionRegistration(displayName = "New - Open traces")
public final class Open_Trace implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        System.getProperty("os.arch");
        System.getProperty("os.name");
        
       DocumentEditor editor = new DocumentEditor();
        editor.open();
        editor.requestActive();
        JFileChooser fc=new JFileChooser();

        fc.setFileFilter(new Seg2FileFilter());
        fc.setMultiSelectionEnabled(true);

        int returnVal = fc.showOpenDialog(editor);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            //File file = fc.getSelectedFile();

            editor.obj.in_file_l=fc.getSelectedFiles();
            editor.obj.newFb();
            //editor.obj.loadSism(0);
            editor.obj.tr=editor.obj.getTraces();
            //System.out.println("EUREKA - Shot location: "+editor.obj.tr[0].getShotLocation());
            
            editor.obj.fb=editor.obj.TraceGroup.get(0);
            
            
            editor.jLabel1.setText(editor.obj.fb.fbp);
            editor.tv.repaint();
            editor.invalidate();
            
            
            Set_geometry sg=new Set_geometry();
            sg.actionPerformed(e);
            
            TopComponent tc = WindowManager.getDefault().findTopComponent("geometryViewerTopComponent");
            geometryViewerTopComponent geomTC = (geometryViewerTopComponent) tc;
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

    class Seg2FileFilter extends javax.swing.filechooser.FileFilter {

        @Override
        public boolean accept(File f) {
            return f.isDirectory() || f.getName().toLowerCase().endsWith(".dat") || f.getName().toLowerCase().endsWith(".sgy") || f.getName().toLowerCase().endsWith(".sg2") || f.getName().toLowerCase().endsWith(".su");
        }

        @Override
        public String getDescription() {
            return "SEG2 file; SEGY file; SeismicUnix file";
        }
    }

}
