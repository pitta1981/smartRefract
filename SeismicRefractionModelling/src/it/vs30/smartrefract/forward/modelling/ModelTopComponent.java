/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.vs30.smartrefract.forward.modelling;

import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;

@TopComponent.Description(preferredID = "ModelTopComponent", persistenceType = TopComponent.PERSISTENCE_ONLY_OPENED)
@TopComponent.Registration(mode = "output", openAtStartup = true)
@ActionID(category = "Window", id = "it.vs30.smartrefract.forward.modelling.ModelTopComponent")
@ActionReferences({
    @ActionReference(path = "Menu/Model", position = 0)})
@TopComponent.OpenActionRegistration(displayName = "#CTL_MyMapAction")
@Messages("CTL_MyMapAction=Open Modeller") //- See more at: https://platform.netbeans.org/tutorials/nbm-geospatial.html#sthash.p9QdCiQx.dpuf
public class ModelTopComponent extends TopComponent {

    @Messages("CTL_MyMapName=Forward modelling")
    public ModelTopComponent() {
        setDisplayName(Bundle.CTL_MyMapName());
    }
} //- See more at: https://platform.netbeans.org/tutorials/nbm-geospatial.html#sthash.p9QdCiQx.dpuf
