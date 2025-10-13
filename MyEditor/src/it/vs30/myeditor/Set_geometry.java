/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.vs30.myeditor;

import it.vs30.myeditor.geometry.GeometryEditorDialog;
import it.vs30.myeditor.geometry.ShotPosition;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import org.myorg.myapi.APIObject;
import org.myorg.myapi.FirstBrakeList;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 * Action per impostare la geometria dello stendimento.
 * Utilizza il nuovo GeometryEditorDialog con supporto tabelle e CSV.
 * 
 * @author smartRefract Team
 */
public final class Set_geometry implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        TopComponent tc = WindowManager.getDefault().findTopComponent("DocumentEditor");
        
        if (tc == null || !(tc instanceof DocumentEditor)) {
            System.err.println("DocumentEditor non trovato");
            return;
        }
        
        DocumentEditor docEditor = (DocumentEditor) tc;
        APIObject obj = docEditor.obj;
        
        if (obj == null || obj.proj == null) {
            System.err.println("Progetto non inizializzato");
            return;
        }
        
        // Crea e mostra il nuovo dialog
        GeometryEditorDialog dialog = new GeometryEditorDialog(null);
        
        // Carica la geometria esistente
        dialog.loadFromProject(obj.proj);
        
        // Mostra il dialog
        dialog.setVisible(true);
        
        // Se l'utente ha confermato, applica le modifiche
        if (dialog.isApproved()) {
            // Applica i parametri base
            dialog.applyToProject(obj.proj);
            
            // Applica le posizioni degli shot
            List<ShotPosition> shots = dialog.getShots();
            for (int i = 0; i < Math.min(shots.size(), obj.proj.stesa.size()); i++) {
                FirstBrakeList fbl = obj.proj.stesa.get(i);
                fbl.scoppio = shots.get(i).getAbsolutePosition();
                // Non rigenerare la geometria: manterremo le posizioni personalizzate dei geofoni
            }
            
            // Sincronizza il progetto
            obj.sync();
            
            // Aggiorna la vista geometrica se disponibile
            TopComponent geomTC = WindowManager.getDefault().findTopComponent("geometryViewerTopComponent");
            if (geomTC != null) {
                geomTC.repaint();
            }
            
            // Aggiorna il DocumentEditor
            docEditor.tv.repaint();
            
            System.out.println("Geometria aggiornata con successo");
        }
    }
}
