/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.vs30.myeditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.myorg.myapi.APIObject;
import org.myorg.myapi.FirstBrakeList;
//import org.myorg.myviewer.MyViewerTopComponent;
import org.openide.util.Lookup;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

public final class Set_geometry implements ActionListener {

    public void actionPerformed(ActionEvent e) {
        JGeometryDlg geomDLG = new JGeometryDlg();
        geomDLG.setModal(true);
        TopComponent tc;// = WindowManager.getDefault().findTopComponent("MyViewerTopComponent");
        //Lookup tcLookup = tc.getLookup();
        //     ((MyViewerTopComponent) tc).jLabel1.setText("APIObject # save");
        tc = WindowManager.getDefault().findTopComponent("DocumentEditor");
        
        APIObject obj = ((DocumentEditor) tc).obj;

        geomDLG.setTable(obj.proj);

        geomDLG.draw_Geom_Preview();
        geomDLG.setVisible(true);

        if (geomDLG.RETVALUE == 1) {
            //       TopComponent tc = WindowManager.getDefault().findTopComponent("MyViewerTopComponent");
            //    Lookup tcLookup = tc.getLookup();
            //     ((MyViewerTopComponent) tc).jLabel1.setText("APIObject # save");
            //     APIObject obj = ((MyViewerTopComponent) tc).active;
            //     ((MyViewerTopComponent) tc).jLabel2.setText("APIObject # trace " + obj.tr.length);
            while (geomDLG.selezionato.startsWith(" ")) {
                geomDLG.selezionato = geomDLG.selezionato.replaceFirst(" ", "");
            }

            if (obj.proj.stesa.size() == geomDLG.selezionato.split(" ").length) {
                String[] geom = geomDLG.selezionato.split(" ");
                for (int i = 0; i < obj.proj.stesa.size(); i++) {
                    FirstBrakeList fbl = (FirstBrakeList) obj.proj.stesa.get(i);
                    fbl.spaz = geomDLG.spaz;
                    fbl.spaz_in = geomDLG.spaz_in;

                    if (geom[i].contains("m") && !geom[i].contains("+")) {
                        fbl.scoppio = Double.parseDouble(geom[i].replace('m', '\0')) + fbl.spaz_in;

                    } else if (geom[i].contains("m") && geom[i].contains("+")) {
                        fbl.scoppio = (fbl.ch - 1) * fbl.spaz + Double.parseDouble(geom[i].replace('m', '\0')) + fbl.spaz_in;

                    } else if (!geom[i].contains("m")) {
                        fbl.scoppio = (Double.parseDouble(geom[i].replace('m', '\0'))) * fbl.spaz + fbl.spaz_in;
                    }
                    fbl.setGeom();

                }
            }
            obj.sync();
        }
        // TODO implement action body
    }
}
