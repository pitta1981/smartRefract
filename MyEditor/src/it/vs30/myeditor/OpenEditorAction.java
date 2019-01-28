/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.vs30.myeditor;

/**
 *
 * @author user
 */
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JFileChooser;
import org.myorg.myapi.Trace;

public class OpenEditorAction implements ActionListener{

    @Override
    public void actionPerformed(ActionEvent e) {
        DocumentEditor editor = new DocumentEditor();
        editor.open();
        editor.requestActive();
        JFileChooser fc=new JFileChooser();

        fc.setFileFilter(new Seg2FileFilter());

        int returnVal = fc.showOpenDialog(editor);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            //File file = fc.getSelectedFile();

            editor.obj.in_file=fc.getSelectedFile();
            editor.obj.loadSism();
            editor.obj.tr=editor.obj.getTraces();
            editor.tv.repaint();
            editor.invalidate();
            //This is where a real application would open the file.
            //System.out.println("Opening: " + file.getName() + "." );
        } else {
            //log.append("Open command cancelled by user." + newline);
        }

    }

class Seg2FileFilter extends javax.swing.filechooser.FileFilter {
        @Override
    public boolean accept(File f) {
        return f.isDirectory() || f.getName().toLowerCase().endsWith(".dat") ||f.getName().toLowerCase().endsWith(".sgy");
    }

        @Override
    public String getDescription() {
        return "SEG2 file; SEGY file";
    }
}

}

