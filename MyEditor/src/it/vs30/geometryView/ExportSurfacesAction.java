/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/NetBeansModuleDevelopment-files/actionListener.java to edit this template
 */
package it.vs30.geometryView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "File",
        id = "it.vs30.geometryView.ExportSurfacesAction"
)
@ActionRegistration(
 //       iconBase = "it/vs30/geometryView/frame48.gif",
        displayName = "#CTL_ExportSurfacesAction"
)
@ActionReferences({
 //   @ActionReference(path = "Menu/File", position = 2350, separatorBefore = 2275),
 //   @ActionReference(path = "Toolbars/File", position = 0)
})
@Messages("CTL_ExportSurfacesAction=Export Surfaces")
public final class ExportSurfacesAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO implement action body
    }
}
