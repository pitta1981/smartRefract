/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/NetBeansModuleDevelopment-files/actionListener.java to edit this template
 */
package it.vs30.myeditor;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
import it.vs30.welcome.welcomeTopComponent;
//import it.vs30.myeditor.set_autopick_par_dialog;

@ActionID(category = "Aggiungi", id = "it.vs30.myeditor.set_autopick_parameter")
@ActionRegistration(displayName = "#CTL_set_autopick_parameter")
@ActionReference(path = "Menu/Setting", position = 200)
@Messages("CTL_set_autopick_parameter=Autopick setting...")
public final class set_autopick_parameter implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

                welcomeTopComponent welcomeTC = (welcomeTopComponent) WindowManager.getDefault()
                                .findTopComponent("welcomeTopComponent");
                if (welcomeTC != null) {
                        Frame mainFrame = WindowManager.getDefault().getMainWindow();
                        set_autopick_par_dialog ap_par_dlg = new set_autopick_par_dialog(mainFrame, true);
                        ap_par_dlg.setParameters(welcomeTC.ltasta_par.lra, welcomeTC.ltasta_par.sra,
                                welcomeTC.ltasta_par.thrs);
                        ap_par_dlg.setModal(true);
                        ap_par_dlg.setVisible(true);
                        if (ap_par_dlg.getReturnStatus() == 1) {

                                welcomeTC.set_lrasra_par(ap_par_dlg.lra, ap_par_dlg.sra, ap_par_dlg.thrs);

                                // welcomeTopComponent.open();
                                // welcomeTopComponent.requestActive();
                        }
                }

        }
}
