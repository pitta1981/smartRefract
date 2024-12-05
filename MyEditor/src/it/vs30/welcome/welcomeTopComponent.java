/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.vs30.welcome;

import it.vs30.geometryView.geometryViewerTopComponent;
import it.vs30.myeditor.DocumentEditor;
import it.vs30.myeditor.OpenPrj;
import it.vs30.myeditor.Set_geometry;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import org.myorg.myapi.APIObject;
import org.myorg.myapi.FirstBrakeList;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.util.Lookup;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;
import org.openide.util.lookup.InstanceContent;
import org.openide.windows.WindowManager;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(
        dtd = "-//it.vs30.welcome//welcome//EN",
        autostore = false)
@TopComponent.Description(
        preferredID = "welcomeTopComponent",
        //iconBase="SET/PATH/TO/ICON/HERE", 
        persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(mode = "editor", openAtStartup = true)
@ActionID(category = "Window", id = "it.vs30.welcome.welcomeTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_welcomeAction",
        preferredID = "welcomeTopComponent")
@Messages({
    "CTL_welcomeAction=welcome",
    "CTL_welcomeTopComponent=Start page",
    "HINT_welcomeTopComponent=This is the start page"
})
public final class welcomeTopComponent extends TopComponent {

    private boolean licenza = false;
    String[] lista = new String[3];
    
    public int lra = 20;
    
    public class STALTAPAR {
        public
        int lra ,sra;
        public
        double thrs;
        
        public STALTAPAR(){}
        
        public void set_par(int LRA, int SRA, double THRS)
        {
            lra = LRA;
            sra = SRA;
            thrs = THRS;
        }

        public void saveParametersToFile(String filePath) {
            try (FileOutputStream fos = new FileOutputStream(filePath);
                 OutputStreamWriter os = new OutputStreamWriter(fos)) {
                os.write("lra=" + lra + "\n");
                os.write("sra=" + sra + "\n");
                os.write("thrs=" + thrs + "\n");
                os.flush();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        public void loadParametersFromFile(String filePath) {
            try (FileInputStream fis = new FileInputStream(filePath);
                 BufferedReader br = new BufferedReader(new InputStreamReader(fis))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split("=");
                    if (parts.length == 2) {
                        switch (parts[0].trim()) {
                            case "lra":
                                lra = Integer.parseInt(parts[1].trim());
                                break;
                            case "sra":
                                sra = Integer.parseInt(parts[1].trim());
                                break;
                            case "thrs":
                                thrs = Double.parseDouble(parts[1].trim());
                                break;
                        }
                    }
                }
            } catch (Exception ex) {
                lra = 50;
                sra = 5;
                thrs = 3.0;
                ex.printStackTrace();
            }
        }
        
    };

    private final InstanceContent content = new InstanceContent();
    private Lookup.Result<APIObject> result;
    private geometryViewerTopComponent geomTC;
    public STALTAPAR ltasta_par;

    private String recent_files = "recenti.list";
    private String autopick_par = "autopick.par";
    private String tmpPath; 

    public welcomeTopComponent() {
        initComponents();
        setName(Bundle.CTL_welcomeTopComponent());
        setToolTipText(Bundle.HINT_welcomeTopComponent());
        putClientProperty(TopComponent.PROP_CLOSING_DISABLED, Boolean.TRUE);
        putClientProperty(TopComponent.PROP_UNDOCKING_DISABLED, Boolean.TRUE);

        if (getLookup().lookup(welcomeTopComponent.class) == null) {
            content.add(this);
        }


        String userHome = "user.home";

        // We get the path by getting the system property with the 
        // defined key above. path+"/smartRefract-data/"
        String path = System.getProperty(userHome);
        tmpPath = path + "/smartRefract-data/";

        ltasta_par = new STALTAPAR();

        ltasta_par.loadParametersFromFile(tmpPath + autopick_par);
        
        jButton1.setText("New...");
        jButton2.setText("Open...");
        boolean checkLic = true;

        /*    final JFXPanel jfxPanel = new JFXPanel();
         jPanel7.setLayout(new BorderLayout());
         jPanel7.add(jfxPanel, BorderLayout.CENTER);
         Platform.runLater(new Runnable(){@Override public void run(){initFX(jfxPanel);}});
         // initFX(jfxPanel);
         */
        //     jLabel3.setVisible(false);
        if (checkLic) {
            //        jLabel3.setForeground(Color.GREEN);
            //       jLabel3.setText("Licenza attiva");
        } else {
            //     jLabel3.setForeground(Color.red);
            //     jLabel3.setText("Licenza non attiva; inserisci la chiavetta e clicca qui; se prosegui saranno disabilitate delle funzionalità");
        }

        updateRecentProject();

    }

    public void updateRecentProject() {
        String userHome = "user.home";

        // We get the path by getting the system property with the 
        // defined key above. path+"/smartRefract-data/"
        String path = System.getProperty(userHome);

        try {
            FileInputStream fis = new FileInputStream(path + "/smartRefract-data/" + "recenti.list");

            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String linea = br.readLine();
            int i = 0;
            while (linea != null && i < 3) {
                lista[i] = linea;
                i++;
                linea = br.readLine();
            }
            String linea1 = lista[0];
            if (lista[0].length() > 45) {
                linea1 = lista[0].substring(0, 10) + "..." + lista[0].substring(lista[0].length() - 32, lista[0].length());
            }
            String linea2 = lista[1];
            if (lista[1].length() > 45) {
                linea2 = lista[1].substring(0, 10) + "..." + lista[1].substring(lista[1].length() - 32, lista[1].length());
            }
            String linea3 = "";
            if (lista[2].length() > 45) {
                linea3 = lista[2].substring(0, 10) + "..." + lista[2].substring(lista[2].length() - 32, lista[2].length());
            } else {
                linea3 = lista[2];
            }

            jButton3.setText(linea1);
            jButton4.setText(linea2);
            jButton5.setText(linea3);
        } catch (Exception ex) {

        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();

        setLayout(new java.awt.GridLayout(1, 0));

        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.LINE_AXIS));

        jButton1.setBackground(new java.awt.Color(153, 255, 153));
        jButton1.setFont(jButton1.getFont().deriveFont(jButton1.getFont().getSize()+2f));
        org.openide.awt.Mnemonics.setLocalizedText(jButton1, org.openide.util.NbBundle.getMessage(welcomeTopComponent.class, "welcomeTopComponent.jButton1.text")); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(255, 204, 102));
        jButton2.setFont(jButton2.getFont().deriveFont(jButton2.getFont().getSize()+2f));
        org.openide.awt.Mnemonics.setLocalizedText(jButton2, org.openide.util.NbBundle.getMessage(welcomeTopComponent.class, "welcomeTopComponent.jButton2.text")); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setFont(jButton3.getFont().deriveFont(jButton3.getFont().getSize()+1f));
        org.openide.awt.Mnemonics.setLocalizedText(jButton3, org.openide.util.NbBundle.getMessage(welcomeTopComponent.class, "welcomeTopComponent.jButton3.text")); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setFont(jButton4.getFont().deriveFont(jButton4.getFont().getSize()+1f));
        org.openide.awt.Mnemonics.setLocalizedText(jButton4, org.openide.util.NbBundle.getMessage(welcomeTopComponent.class, "welcomeTopComponent.jButton4.text")); // NOI18N
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setFont(jButton5.getFont().deriveFont(jButton5.getFont().getSize()+1f));
        org.openide.awt.Mnemonics.setLocalizedText(jButton5, org.openide.util.NbBundle.getMessage(welcomeTopComponent.class, "welcomeTopComponent.jButton5.text")); // NOI18N
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jLabel2.setFont(jLabel2.getFont().deriveFont(jLabel2.getFont().getStyle() | java.awt.Font.BOLD, jLabel2.getFont().getSize()+4));
        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(welcomeTopComponent.class, "welcomeTopComponent.jLabel2.text")); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(98, 98, 98)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(50, 50, 50)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                        .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabel2))
                .addContainerGap(54, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(80, 80, 80)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(107, 107, 107)
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(jButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton5)))
                .addContainerGap(333, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel3);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 517, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 593, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel2);

        jScrollPane1.setViewportView(jPanel1);

        add(jScrollPane1);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        OpenPrj opn = new OpenPrj();
        opn.actionPerformed(evt);
        updateRecentProject();

    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        DocumentEditor editor = new DocumentEditor();
        editor.open();
        editor.requestActive();
        OpenPrj opj = new OpenPrj();
        //  lista[1]
        editor.obj.TraceGroup = new ArrayList();

        String path = (new File(lista[0])).getAbsolutePath(); //fc.getSelectedFile().getAbsolutePath();
        String base = (new File(lista[0])).getParent();  // (fc.getSelectedFile().getParent());
        //String relative = (new File(base)).toURI().relativize(new File(path).toURI()).getPath();// new File(base).toURI().relativize(new File(path).toURI()).getPath();
        //editor.obj.newFb();
        //editor.obj.loadSism(0);
        //editor.obj.tr=editor.obj.getTraces();

        File file = new File(lista[0]);
        boolean mTxt = false;
        if (file.getPath().toLowerCase().endsWith(".txt")) {
            mTxt = true;
            opj.loadPrj(file, editor.obj, base);
        } else if (file.getPath().toLowerCase().endsWith(".srefract")) {
            opj.loadPrjSmartRefract(file, editor);
        }

        updateRecentProject();

        editor.setDisplayName((new File(lista[0])).getName());
        editor.obj.proj_file = (new File(lista[0]));
        if (mTxt) {
            //base = "";
            opj.loadtrace(editor.obj, base);
            editor.obj.loadSism(0);
            editor.obj.LoadTrace_For_Open();
            editor.obj.tr = editor.obj.getTraces();
        }

        editor.obj.fb =  editor.obj.TraceGroup.get(0);
        editor.obj.prevTr();
        editor.tv.obj = editor.obj;
        editor.obj.sync();
        editor.tv.repaint();
        editor.txV.setProj(editor.obj.proj);
        editor.invalidate();

        /*     tc = WindowManager.getDefault().findTopComponent("MyViewerTopComponent");
      //  Lookup tcLookup = tc.getLookup();

        ((MyViewerTopComponent) tc).setActive(editor.obj);
        ((MyViewerTopComponent) tc).gmview.setBackground(Color.black);
        ((MyViewerTopComponent) tc).gmview.setGeom(editor.obj.fb.scoppio, editor.obj.fb.spaz, editor.obj.fb.spaz_in, editor.obj.tr.length);
        ((MyViewerTopComponent) tc).gmview.repaint();
        ((MyViewerTopComponent) tc).gmview.invalidate();*/
        editor.jTracePath.setText(editor.obj.fb.fbp);

        TopComponent tc = WindowManager.getDefault().findTopComponent("geometryViewerTopComponent");
        geomTC = (geometryViewerTopComponent) tc;
        geomTC.setActive(editor.obj);
        geomTC.gmview.setGeom(editor.obj.fb.scoppio, editor.obj.fb.spaz, editor.obj.fb.spaz_in, editor.obj.tr.length);
        geomTC.gmview.repaint();
        geomTC.gmview.invalidate();

        moveRecentFirst(lista[1]);
        editor.jTracePath.setText(editor.obj.fb.fbp);


    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        // TODO add your handling code here:
        System.getProperty("os.arch");
        System.getProperty("os.name");

        DocumentEditor editor = new DocumentEditor();
        editor.open();
        editor.requestActive();
        JFileChooser fc = new JFileChooser();

        fc.setFileFilter(new Seg2FileFilter());
        fc.setMultiSelectionEnabled(true);

        int returnVal = fc.showOpenDialog(editor);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            //File file = fc.getSelectedFile();

            editor.obj.in_file_l = fc.getSelectedFiles();
            editor.obj.newFb();
            //editor.obj.loadSism(0);
            editor.obj.tr = editor.obj.getTraces();
            editor.obj.fb = editor.obj.TraceGroup.get(editor.obj.trace_index);
            editor.jTracePath.setText(editor.obj.fb.fbp);
            editor.tv.repaint();
            editor.invalidate();

            Set_geometry sg = new Set_geometry();
            sg.actionPerformed(evt);
            
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


    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        DocumentEditor editor = new DocumentEditor();
        editor.open();
        editor.requestActive();
        OpenPrj opj = new OpenPrj();
        //  lista[1]
        editor.obj.TraceGroup = new ArrayList();
        String path = (new File(lista[1])).getAbsolutePath(); //fc.getSelectedFile().getAbsolutePath();
        String base = (new File(lista[1])).getParent();  // (fc.getSelectedFile().getParent());
        String relative = (new File(base)).toURI().relativize(new File(path).toURI()).getPath();// new File(base).toURI().relativize(new File(path).toURI()).getPath();
        
        File file = new File(lista[1]);
        boolean mTxt = false;
        if (file.getPath().toLowerCase().endsWith(".txt")) {
            mTxt = true;
            opj.loadPrj(file, editor.obj, base);
        } else if (file.getPath().toLowerCase().endsWith(".srefract")) {
            opj.loadPrjSmartRefract(file, editor);
        }

        editor.setDisplayName((new File(lista[1])).getName());
        editor.obj.proj_file = (new File(lista[1]));
        if (mTxt) {
            //base = "";
            opj.loadtrace(editor.obj, base);
                editor.obj.loadSism(0);
                editor.obj.LoadTrace_For_Open();
                editor.obj.tr = editor.obj.getTraces();
    
        }
        editor.obj.fb = (FirstBrakeList) editor.obj.TraceGroup.get( editor.obj.trace_index );
        editor.obj.prevTr();
        editor.obj.sync();
        editor.tv.repaint();
        editor.txV.setProj(editor.obj.proj);
        editor.invalidate();

        //   tc = WindowManager.getDefault().findTopComponent("MyViewerTopComponent");
        //  Lookup tcLookup = tc.getLookup();

        /*((MyViewerTopComponent) tc).setActive(editor.obj);
        ((MyViewerTopComponent) tc).gmview.setBackground(Color.black);
        ((MyViewerTopComponent) tc).gmview.setGeom(editor.obj.fb.scoppio, editor.obj.fb.spaz, editor.obj.fb.spaz_in, editor.obj.tr.length);
        ((MyViewerTopComponent) tc).gmview.repaint();
        ((MyViewerTopComponent) tc).gmview.invalidate();*/
        editor.jTracePath.setText(editor.obj.fb.fbp);

        TopComponent tc = WindowManager.getDefault().findTopComponent("geometryViewerTopComponent");
        geomTC = (geometryViewerTopComponent) tc;
        geomTC.setActive(editor.obj);
        geomTC.gmview.setGeom(editor.obj.fb.scoppio, editor.obj.fb.spaz, editor.obj.fb.spaz_in, editor.obj.tr.length);
        geomTC.gmview.repaint();
        geomTC.gmview.invalidate();

        moveRecentFirst(lista[1]);

    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed

        DocumentEditor editor = new DocumentEditor();
        OpenPrj open = new OpenPrj();
        boolean mTxt = false;
        editor.open();
        editor.requestActive();
        String path = (new File(lista[2])).getAbsolutePath(); //fc.getSelectedFile().getAbsolutePath();
        String base = (new File(lista[2])).getParent();  // (fc.getSelectedFile().getParent());
        String relative = (new File(base)).toURI().relativize(new File(path).toURI()).getPath();// new File(base).toURI().relativize(new File(path).toURI()).getPath();
        //editor.obj.newFb();
        //editor.obj.loadSism(0);
        //editor.obj.tr=editor.obj.getTraces();

        File file = new File(lista[2]);    //File file = fc.getSelectedFile();

        //editor.obj.in_file=fc.getSelectedFile();
        editor.obj.TraceGroup = new ArrayList();
        if (file.getPath().toLowerCase().endsWith(".txt")) {
            open.loadPrj(file, editor.obj, base);
            mTxt = true;
        }

        String userHome = "user.home";

        // We get the path by getting the system property with the 
        // defined key above. path+"/smartRefract-data/"
        String path1 = System.getProperty(userHome);

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(path1 + "/smartRefract-data/" + "recenti.list");
        } catch (FileNotFoundException ex) {
            //   Exceptions.printStackTrace(ex);
        }
        boolean pathPresent = false;

        welcomeTopComponent wTopComponent = (welcomeTopComponent) WindowManager.getDefault().findTopComponent("welcomeTopComponent");
        updateRecentProject();
        if (!mTxt) {
            open.loadPrjSmartRefract(file, editor);
        }

        editor.setDisplayName(file.getName());
        editor.obj.proj_file = file;
        if (mTxt) {
            base = "";
            open.loadtrace(editor.obj, base);
                editor.obj.loadSism(0);
        editor.obj.LoadTrace_For_Open();
        editor.obj.tr = editor.obj.getTraces();
    
        }
        editor.obj.fb =  editor.obj.TraceGroup.get(0);
        editor.obj.prevTr();
        editor.tv.obj = editor.obj;

        editor.obj.sync();
        editor.tv.repaint();
        //editor.dv.setProj(editor.obj.proj);
        editor.invalidate();

        /*  tc = WindowManager.getDefault().findTopComponent("MyViewerTopComponent");
        Lookup tcLookup = tc.getLookup();

        ((MyViewerTopComponent) tc).setActive(editor.obj);
        ((MyViewerTopComponent) tc).gmview.setBackground(Color.black);
        ((MyViewerTopComponent) tc).gmview.setGeom(editor.obj.fb.scoppio, editor.obj.fb.spaz, editor.obj.fb.spaz_in, editor.obj.tr.length);
        ((MyViewerTopComponent) tc).gmview.repaint();
        ((MyViewerTopComponent) tc).gmview.invalidate();*/
        editor.jTracePath.setText(editor.obj.fb.fbp);
        //This is where a real application would open the file.
        //System.out.println("Opening: " + file.getName() + "." );

        TopComponent tc = WindowManager.getDefault().findTopComponent("geometryViewerTopComponent");
        geomTC = (geometryViewerTopComponent) tc;
        geomTC.setActive(editor.obj);
        geomTC.gmview.setGeom(editor.obj.fb.scoppio, editor.obj.fb.spaz, editor.obj.fb.spaz_in, editor.obj.tr.length);
        geomTC.gmview.repaint();
        geomTC.gmview.invalidate();
        moveRecentFirst(lista[2]);

// TODO add your handling code here:
    }//GEN-LAST:event_jButton5ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
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

    private void moveRecentFirst(String elemento_lista) {
        
        
        lista[2]=lista[1];
        lista[1]=lista[0];
        lista[0]=elemento_lista;
        
        String userHome = "user.home";

        // We get the path by getting the system property with the 
        // defined key above. path+"/smartRefract-data/"
        String path = System.getProperty(userHome);

        try {
            
            FileOutputStream fos=new FileOutputStream(path + "/smartRefract-data/" + "recenti.list");

            OutputStreamWriter os = new OutputStreamWriter(fos);

            os.write(lista[0] + "\n");
            os.write(lista[1] + "\n");
            os.write(lista[2] + "\n");
            os.flush();
            os.close();
            fos.close();
        }
        catch(Exception ex){
            
        }
        updateRecentProject();

// throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void set_lrasra_par(int lra, int sra, double thrs)
    {
        ltasta_par.set_par(lra, sra, thrs);
        ltasta_par.saveParametersToFile(tmpPath+autopick_par);
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
