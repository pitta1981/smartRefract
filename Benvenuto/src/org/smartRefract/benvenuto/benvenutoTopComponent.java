/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.smartRefract.benvenuto;

//package org.myorg.myeditor;

import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
//import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;



//import modev.ModevObj;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import it.vs30.myeditor.DocumentEditor;
import it.vs30.myeditor.OpenPrj;
import org.myorg.myapi.FirstBrakeList;
import it.vs30.myeditor.Set_geometry;
import org.myorg.myviewer.MyViewerTopComponent;
import org.openide.util.Lookup;
import org.openide.windows.WindowManager;
/*import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;*/




/**
 * Top component which displays something.
 */
//@ConvertAsProperties(dtd = "-//org.smartRefract.benvenuto//benvenuto//EN",autostore = false)
@TopComponent.Description(preferredID = "benvenutoTopComponent",
//iconBase="SET/PATH/TO/ICON/HERE", 
persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(mode = "editor", openAtStartup = true)
@ActionID(category = "Window", id = "org.smartRefract.benvenuto.benvenutoTopComponent")
//@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(displayName = "#CTL_benvenutoAction",
preferredID = "benvenutoTopComponent")
public final class benvenutoTopComponent extends TopComponent{
    private boolean licenza=false;
    String[] lista=new String[3];
    
    public benvenutoTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(benvenutoTopComponent.class, "CTL_benvenutoTopComponent"));
        setToolTipText(NbBundle.getMessage(benvenutoTopComponent.class, "HINT_benvenutoTopComponent"));
        putClientProperty(TopComponent.PROP_CLOSING_DISABLED, Boolean.TRUE);
        putClientProperty(TopComponent.PROP_UNDOCKING_DISABLED, Boolean.TRUE);
        boolean checkLic = true;
        
    /*    final JFXPanel jfxPanel = new JFXPanel();
        jPanel7.setLayout(new BorderLayout());
        jPanel7.add(jfxPanel, BorderLayout.CENTER);
        Platform.runLater(new Runnable(){@Override public void run(){initFX(jfxPanel);}});
       // initFX(jfxPanel);
      */  
        jLabel3.setVisible(false);
        if (checkLic){
            jLabel3.setForeground(Color.GREEN);
            jLabel3.setText("Licenza attiva");
        }
        else{
            jLabel3.setForeground(Color.red);
            jLabel3.setText("Licenza non attiva; inserisci la chiavetta e clicca qui; se prosegui saranno disabilitate delle funzionalità");
        }
        
         //String[] lista;
        String userHome = "user.home";        
         
            // We get the path by getting the system property with the 
            // defined key above. path+"/smartRefract-data/"
        String path = System.getProperty(userHome);      
        
            try{
                FileInputStream fis = new FileInputStream(path+"/smartRefract-data/"+"recenti.list");

                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader br = new BufferedReader(isr);
                String linea = br.readLine();
                int i=0;
                while(linea!=null&&i<3){
                    lista[i]=linea;
                    i++;
                    linea = br.readLine();
                }
                String linea1=lista[0];
                if(lista[0].length()>45){
                    linea1=lista[0].substring(0, 10)+"..."+lista[0].substring(lista[0].length()-32,lista[0].length());
                }
                String linea2=lista[1];
                if(lista[1].length()>45){
                    linea2=lista[1].substring(0, 10)+"..."+lista[1].substring(lista[1].length()-32,lista[1].length());
                }
                String linea3="";
                if(lista[2].length()>45){
                    linea3=lista[2].substring(0, 10)+"..."+lista[2].substring(lista[2].length()-32,lista[2].length());
                }else
                linea3=lista[2];
                
                
                
                jButton1.setText(linea1);
                jButton2.setText(linea2);
                jButton3.setText(linea3);
            }
            catch(Exception ex){
                
            }
        
       
        

    }
    
    
    /*private void initFX(JFXPanel jfxPanel) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("FX_panel.fxml"));
            Scene scene = new Scene(root, 250, 150);
           // scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
            jfxPanel.setScene(scene);
        } catch (IOException exc) {
            exc.printStackTrace();
            System.exit(1);
        }
    }*/
    
    

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel8 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        filler3 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        filler4 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));

        setBackground(new java.awt.Color(0, 255, 255));
        setFont(new java.awt.Font("Open Sans", 0, 12)); // NOI18N
        setLayout(new java.awt.GridLayout(1, 0));

        jPanel8.setPreferredSize(new java.awt.Dimension(750, 450));
        jPanel8.setLayout(new javax.swing.BoxLayout(jPanel8, javax.swing.BoxLayout.PAGE_AXIS));

        jPanel2.setBackground(new java.awt.Color(255, 198, 78));
        jPanel2.setMaximumSize(new java.awt.Dimension(32767, 100));
        jPanel2.setPreferredSize(new java.awt.Dimension(1006, 100));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 750, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 94, Short.MAX_VALUE)
        );

        jPanel8.add(jPanel2);

        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.LINE_AXIS));

        jPanel3.setLayout(new javax.swing.BoxLayout(jPanel3, javax.swing.BoxLayout.PAGE_AXIS));

        jPanel4.setLayout(new javax.swing.BoxLayout(jPanel4, javax.swing.BoxLayout.X_AXIS));

        jPanel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(benvenutoTopComponent.class, "benvenutoTopComponent.jLabel2.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jButton1, org.openide.util.NbBundle.getMessage(benvenutoTopComponent.class, "benvenutoTopComponent.jButton1.text")); // NOI18N
        jButton1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jButton3, org.openide.util.NbBundle.getMessage(benvenutoTopComponent.class, "benvenutoTopComponent.jButton3.text")); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jButton2, org.openide.util.NbBundle.getMessage(benvenutoTopComponent.class, "benvenutoTopComponent.jButton2.text")); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton4.setBackground(new java.awt.Color(134, 253, 67));
        jButton4.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jButton4, org.openide.util.NbBundle.getMessage(benvenutoTopComponent.class, "benvenutoTopComponent.jButton4.text")); // NOI18N
        jButton4.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jButton4.setContentAreaFilled(false);
        jButton4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setBackground(new java.awt.Color(255, 255, 0));
        jButton5.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jButton5, org.openide.util.NbBundle.getMessage(benvenutoTopComponent.class, "benvenutoTopComponent.jButton5.text")); // NOI18N
        jButton5.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jButton5.setContentAreaFilled(false);
        jButton5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 192, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
                    .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 44, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(70, 70, 70))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(43, 43, 43)
                        .addComponent(jLabel2)
                        .addGap(24, 24, 24)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(43, 43, 43)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(34, 34, 34)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(115, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(108, 108, 108))
        );

        jPanel4.add(jPanel6);

        jPanel9.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setFont(new java.awt.Font("Open Sans", 0, 16)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 255));
        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(benvenutoTopComponent.class, "benvenutoTopComponent.jLabel1.text")); // NOI18N
        jLabel1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel1MouseClicked(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Open Sans", 0, 16)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 255));
        org.openide.awt.Mnemonics.setLocalizedText(jLabel4, org.openide.util.NbBundle.getMessage(benvenutoTopComponent.class, "benvenutoTopComponent.jLabel4.text")); // NOI18N
        jLabel4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel4MouseClicked(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Open Sans", 0, 16)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 255));
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/smartRefract/benvenuto/Facebook-Like-Button_reduced.jpg"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jLabel5, org.openide.util.NbBundle.getMessage(benvenutoTopComponent.class, "benvenutoTopComponent.jLabel5.text")); // NOI18N
        jLabel5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel5MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(69, 69, 69)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addComponent(jLabel1)
                .addGap(24, 24, 24)
                .addComponent(jLabel4)
                .addGap(25, 25, 25)
                .addComponent(jLabel5)
                .addContainerGap(159, Short.MAX_VALUE))
        );

        jPanel4.add(jPanel9);

        jPanel3.add(jPanel4);

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jLabel3, org.openide.util.NbBundle.getMessage(benvenutoTopComponent.class, "benvenutoTopComponent.jLabel3.text")); // NOI18N
        jLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel3MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(0, 25, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addGap(0, 674, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel3.add(jPanel5);
        jPanel3.add(filler3);
        jPanel3.add(filler4);

        jPanel1.add(jPanel3);

        jPanel8.add(jPanel1);

        jScrollPane1.setViewportView(jPanel8);

        add(jScrollPane1);
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseClicked
        // TODO add your handling code here:
        boolean checkLic = true;
        
        if (checkLic){
            jLabel3.setForeground(Color.GREEN);
            jLabel3.setText("Licenza attiva");
        }
        else{
            jLabel3.setForeground(Color.red);
            jLabel3.setText("Licenza non attiva; inserisci la Unikey(R) e clicca qui; se prosegui saranno disabilitate delle funzionalità");
        }
        
        
    }//GEN-LAST:event_jLabel3MouseClicked

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
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
            editor.obj.fb=(FirstBrakeList)editor.obj.TraceGroup.get(0);
            editor.jLabel1.setText(editor.obj.fb.fbp);
            editor.tv.repaint();
            editor.invalidate();

            Set_geometry sg=new Set_geometry();
            sg.actionPerformed(evt);

            //This is where a real application would open the file.
            //System.out.println("Opening: " + file.getName() + "." );
        } else {
            //log.append("Open command cancelled by user." + newline);
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        OpenPrj opn=new OpenPrj();
        opn.actionPerformed(evt);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        DocumentEditor editor = new DocumentEditor();
        editor.open();
        editor.requestActive();
        OpenPrj opj=new OpenPrj();
        //  lista[1]
        editor.obj.TraceGroup = new ArrayList<FirstBrakeList>();
        String path = (new File(lista[1])).getAbsolutePath() ; //fc.getSelectedFile().getAbsolutePath();
        String base = (new File(lista[1])).getParent();  // (fc.getSelectedFile().getParent());
        String relative = (new File(base)).toURI().relativize(new File(path).toURI()).getPath();// new File(base).toURI().relativize(new File(path).toURI()).getPath();
        //editor.obj.newFb();
        //editor.obj.loadSism(0);
        //editor.obj.tr=editor.obj.getTraces();
        opj.loadPrj(new File(lista[1]), editor.obj, base);
        
        editor.setDisplayName((new File(lista[1])).getName());
        editor.obj.proj_file=(new File(lista[1]));
        base = "";
        opj.loadtrace(editor.obj, base);
        
        editor.obj.loadSism(0);
        editor.obj.tr = editor.obj.getTraces();
        editor.obj.LoadTrace_For_Open();
        editor.obj.fb = (FirstBrakeList) editor.obj.TraceGroup.get(0);

        editor.tv.repaint();
        //editor.dv.setProj(editor.obj.proj);
        editor.invalidate();

        TopComponent tc = WindowManager.getDefault().findTopComponent("MyViewerTopComponent");
        Lookup tcLookup = tc.getLookup();

        ((MyViewerTopComponent) tc).setActive(editor.obj);
        ((MyViewerTopComponent) tc).gmview.setBackground(Color.black);
        ((MyViewerTopComponent) tc).gmview.setGeom(editor.obj.fb.scoppio, editor.obj.fb.spaz, editor.obj.fb.spaz_in, editor.obj.tr.length);
        ((MyViewerTopComponent) tc).gmview.repaint();
        ((MyViewerTopComponent) tc).gmview.invalidate();
        editor.jLabel1.setText(editor.obj.fb.fbp);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        DocumentEditor editor = new DocumentEditor();
        editor.open();
        editor.requestActive();
        OpenPrj opj=new OpenPrj();
        //  lista[1]
        editor.obj.TraceGroup = new ArrayList<FirstBrakeList>();
        String path = (new File(lista[2])).getAbsolutePath() ; //fc.getSelectedFile().getAbsolutePath();
        String base = (new File(lista[2])).getParent();  // (fc.getSelectedFile().getParent());
        String relative = (new File(base)).toURI().relativize(new File(path).toURI()).getPath();// new File(base).toURI().relativize(new File(path).toURI()).getPath();
        //editor.obj.newFb();
        //editor.obj.loadSism(0);
        //editor.obj.tr=editor.obj.getTraces();
        opj.loadPrj(new File(lista[2]), editor.obj, base);

        editor.setDisplayName((new File(lista[2])).getName());
        editor.obj.proj_file=(new File(lista[2]));
        base = "";
        opj.loadtrace(editor.obj, base);
        editor.obj.LoadTrace_For_Open();
        editor.obj.loadSism(0);
        editor.obj.tr = editor.obj.getTraces();

        editor.obj.fb = (FirstBrakeList) editor.obj.TraceGroup.get(0);

        editor.tv.repaint();
    //    editor.dv.setProj(editor.obj.proj);
        editor.invalidate();

        TopComponent tc = WindowManager.getDefault().findTopComponent("MyViewerTopComponent");
        Lookup tcLookup = tc.getLookup();

        ((MyViewerTopComponent) tc).setActive(editor.obj);
        ((MyViewerTopComponent) tc).gmview.setBackground(Color.black);
        ((MyViewerTopComponent) tc).gmview.setGeom(editor.obj.fb.scoppio, editor.obj.fb.spaz, editor.obj.fb.spaz_in, editor.obj.tr.length);
        ((MyViewerTopComponent) tc).gmview.repaint();
        ((MyViewerTopComponent) tc).gmview.invalidate();
        editor.jLabel1.setText(editor.obj.fb.fbp);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        DocumentEditor editor = new DocumentEditor();
        editor.open();
        editor.requestActive();
        OpenPrj opj=new OpenPrj();
        //  lista[1]
        editor.obj.TraceGroup = new ArrayList<FirstBrakeList>();
        String path = (new File(lista[0])).getAbsolutePath() ; //fc.getSelectedFile().getAbsolutePath();
        String base = (new File(lista[0])).getParent();  // (fc.getSelectedFile().getParent());
        String relative = (new File(base)).toURI().relativize(new File(path).toURI()).getPath();// new File(base).toURI().relativize(new File(path).toURI()).getPath();
        //editor.obj.newFb();
        //editor.obj.loadSism(0);
        //editor.obj.tr=editor.obj.getTraces();
        opj.loadPrj(new File(lista[0]), editor.obj, base);

        editor.setDisplayName((new File(lista[0])).getName());
        editor.obj.proj_file=(new File(lista[0]));
        base = "";
        opj.loadtrace(editor.obj, base);
        editor.obj.LoadTrace_For_Open();
        editor.obj.loadSism(0);
        editor.obj.tr = editor.obj.getTraces();

        editor.obj.fb = (FirstBrakeList) editor.obj.TraceGroup.get(0);

        editor.tv.repaint();
       // editor.dv.setProj(editor.obj.proj);
        editor.invalidate();

        TopComponent tc = WindowManager.getDefault().findTopComponent("MyViewerTopComponent");
        Lookup tcLookup = tc.getLookup();

        ((MyViewerTopComponent) tc).setActive(editor.obj);
        ((MyViewerTopComponent) tc).gmview.setBackground(Color.black);
        ((MyViewerTopComponent) tc).gmview.setGeom(editor.obj.fb.scoppio, editor.obj.fb.spaz, editor.obj.fb.spaz_in, editor.obj.tr.length);
        ((MyViewerTopComponent) tc).gmview.repaint();
        ((MyViewerTopComponent) tc).gmview.invalidate();
        editor.jLabel1.setText(editor.obj.fb.fbp);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jLabel4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel4MouseClicked
        java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
        try {

            java.net.URI uri = new java.net.URI( "http://www.vs30.it/en/smartrefract/3-smartrefract-tutorial-1" );
            desktop.browse( uri );
        }
        catch ( Exception e ) {

            System.err.println( e.getMessage() );
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel4MouseClicked

    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseClicked
        // TODO add your handling code here:

        java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
        try {

            java.net.URI uri = new java.net.URI( "http://www.vs30.it" );
            desktop.browse( uri );
        }
        catch ( Exception e ) {

            System.err.println( e.getMessage() );
        }
    }//GEN-LAST:event_jLabel1MouseClicked

    private void jLabel5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MouseClicked
        // TODO add your handling code here:
        java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
        try {

            java.net.URI uri = new java.net.URI( "https://www.facebook.com/smartrefract" );
            desktop.browse( uri );
        }
        catch ( Exception e ) {

            System.err.println( e.getMessage() );
        }
    }//GEN-LAST:event_jLabel5MouseClicked

    

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

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.Box.Filler filler3;
    private javax.swing.Box.Filler filler4;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
    @Override
    public void componentOpened() {
        // TODO add custom code on component opening
    }

    @Override
    public void componentClosed() {
        // TODO add custom code on component closing
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }
    
    
    
  


    
    
    
}
