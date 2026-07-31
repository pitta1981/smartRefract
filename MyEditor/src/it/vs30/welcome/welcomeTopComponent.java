/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.vs30.welcome;
import it.vs30.myeditor.OpenRefractLoader;

import it.vs30.geometryView.geometryViewerTopComponent;
import it.vs30.myeditor.DocumentEditor;
import it.vs30.myeditor.OpenPrj;
import it.vs30.myeditor.Set_geometry;
import it.vs30.myeditor.folder_history;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import javax.swing.*;
import org.myorg.myapi.APIObject;
import org.myorg.myapi.FirstBrakeList;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.util.Lookup;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle;
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

    // Windows 11 Fluent-inspired palette
    private static final Color BG_WINDOW = new Color(0xF3, 0xF3, 0xF3);
    private static final Color CARD_BG = Color.WHITE;
    private static final Color CARD_BORDER = new Color(0xE1, 0xE1, 0xE1);
    private static final Color ROW_HOVER = new Color(0x00, 0x00, 0x00, 22);
    private static final Color SECONDARY_HOVER = new Color(0xF5, 0xF5, 0xF5);
    private static final Color ACCENT = new Color(0xE8, 0x87, 0x1A);
    private static final Color ACCENT_HOVER = new Color(0xCE, 0x76, 0x12);
    private static final Color TEXT_PRIMARY = new Color(0x20, 0x1F, 0x1E);
    private static final Color TEXT_SECONDARY = new Color(0x60, 0x5E, 0x5C);

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
        setName(org.openide.util.NbBundle.getMessage(welcomeTopComponent.class, "CTL_welcomeTopComponent"));
        setToolTipText(org.openide.util.NbBundle.getMessage(welcomeTopComponent.class, "HINT_welcomeTopComponent"));
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

    private void moveRecentFirst(String elemento_lista) {
        // Nuova logica: deduplica, ordina, aggiorna recenti
        String newPath = (elemento_lista != null) ? elemento_lista.replace("\\", "/") : null;
        ArrayList<String> recentList = new ArrayList<>();
        if (newPath != null && !newPath.isEmpty()) {
            recentList.add(newPath);
        }
        for (String s : lista) {
            if (s != null && !s.isEmpty()) {
                String normalized = s.replace("\\", "/");
                if (!recentList.contains(normalized)) {
                    recentList.add(normalized);
                }
            }
        }
        // Limita a 3 elementi
        while (recentList.size() < 3) recentList.add("");
        while (recentList.size() > 3) recentList.remove(recentList.size() - 1);
        // Aggiorna lista[]
        for (int i = 0; i < 3; i++) {
            lista[i] = recentList.get(i);
        }
        String userHome = "user.home";
        String path = System.getProperty(userHome);
        try (FileOutputStream fos = new FileOutputStream(path + "/smartRefract-data/" + recent_files);
             OutputStreamWriter os = new OutputStreamWriter(fos)) {
            for (int i = 0; i < 3; i++) {
                os.write((lista[i] != null ? lista[i] : "") + "\n");
            }
            os.flush();
        } catch(Exception ex){
            // Ignora errori di scrittura
        }
        updateRecentProject();
    }

    public void updateRecentProject() {
        String userHome = "user.home";
        String path = System.getProperty(userHome);
        try (FileInputStream fis = new FileInputStream(path + "/smartRefract-data/" + recent_files);
             InputStreamReader isr = new InputStreamReader(fis);
             BufferedReader br = new BufferedReader(isr)) {
            for (int i = 0; i < 3; i++) {
                String linea = br.readLine();
                lista[i] = (linea != null) ? linea : "";
            }
        } catch (Exception ex) {
            // Se il file non esiste o errore, azzera la lista
            for (int i = 0; i < 3; i++) lista[i] = "";
        }

        boolean anyRecent = false;
        for (int i = 0; i < 3; i++) {
            String entry = lista[i];
            RecentItemButton row = recentButtons[i];
            if (entry != null && !entry.isEmpty()) {
                anyRecent = true;
                File f = new File(entry);
                String folder = f.getParent() != null ? f.getParent() : entry;
                row.setRecentText(f.getName(), truncateMiddle(folder, 52));
                row.setVisible(true);
            } else {
                row.setVisible(false);
            }
        }
        emptyRecentLabel.setVisible(!anyRecent);
        recentCard.setVisible(anyRecent);
    }

    private static String truncateMiddle(String s, int maxLen) {
        if (s == null) {
            return "";
        }
        if (s.length() <= maxLen) {
            return s;
        }
        int keepEnd = Math.max(maxLen - 13, 4);
        return s.substring(0, 10) + "…" + s.substring(s.length() - keepEnd);
    }

    private static String escapeHtml(String s) {
        if (s == null) {
            return "";
        }
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }

    /**
     * Builds the "start page" UI: a Windows 11 style layout with a header,
     * two primary action cards (New / Open) and a card listing recent
     * projects.
     */
    private void initComponents() {

        setLayout(new BorderLayout());

        JPanel root = new JPanel();
        root.setOpaque(true);
        root.setBackground(BG_WINDOW);
        root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));

        // ---- Header -------------------------------------------------
        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.X_AXIS));
        header.setBorder(BorderFactory.createEmptyBorder(36, 40, 28, 40));
        header.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel logo = new JLabel(loadLogo(56));
        header.add(logo);
        header.add(Box.createHorizontalStrut(18));

        JPanel titleBox = new JPanel();
        titleBox.setOpaque(false);
        titleBox.setLayout(new BoxLayout(titleBox, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("smartRefract");
        title.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 26));
        title.setForeground(TEXT_PRIMARY);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("Start a new project or continue where you left off");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(TEXT_SECONDARY);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        titleBox.add(title);
        titleBox.add(Box.createVerticalStrut(4));
        titleBox.add(subtitle);
        header.add(titleBox);
        header.add(Box.createHorizontalGlue());

        root.add(header);

        // ---- Action cards (New / Open) ------------------------------
        JPanel actions = new JPanel(new GridLayout(1, 2, 16, 0));
        actions.setOpaque(false);
        actions.setBorder(BorderFactory.createEmptyBorder(0, 40, 28, 40));
        actions.setAlignmentX(Component.LEFT_ALIGNMENT);
        actions.setMaximumSize(new Dimension(Integer.MAX_VALUE, 118));

        new_Button1 = new FluentButton(
                "New", "Import trace files and start a project",
                new PlusIcon(24, Color.WHITE), true);
        new_Button1.addActionListener(evt -> new_Button1ActionPerformed(evt));

        jButton2 = new FluentButton(
                "Open", "Browse for an existing project file",
                new FolderIcon(24, ACCENT), false);
        jButton2.addActionListener(evt -> jButton2ActionPerformed(evt));

        actions.add(new_Button1);
        actions.add(jButton2);

        root.add(actions);

        // ---- Recent projects card ------------------------------------
        JPanel recentSection = new JPanel();
        recentSection.setOpaque(false);
        recentSection.setLayout(new BoxLayout(recentSection, BoxLayout.Y_AXIS));
        recentSection.setBorder(BorderFactory.createEmptyBorder(0, 40, 24, 40));
        recentSection.setAlignmentX(Component.LEFT_ALIGNMENT);

        jLabel2 = new JLabel("Recent projects");
        jLabel2.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 15));
        jLabel2.setForeground(TEXT_PRIMARY);
        jLabel2.setAlignmentX(Component.LEFT_ALIGNMENT);
        jLabel2.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        recentSection.add(jLabel2);

        recentCard = new RoundedPanel(8);
        recentCard.setLayout(new BoxLayout(recentCard, BoxLayout.Y_AXIS));
        recentCard.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        recentCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        recentCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 220));

        jButton3 = new RecentItemButton();
        jButton3.addActionListener(evt -> jButton3ActionPerformed(evt));
        jButton4 = new RecentItemButton();
        jButton4.addActionListener(evt -> jButton4ActionPerformed(evt));
        jButton5 = new RecentItemButton();
        jButton5.addActionListener(evt -> jButton5ActionPerformed(evt));

        recentButtons = new RecentItemButton[]{jButton3, jButton4, jButton5};

        recentCard.add(jButton3);
        recentCard.add(new Divider());
        recentCard.add(jButton4);
        recentCard.add(new Divider());
        recentCard.add(jButton5);

        recentSection.add(recentCard);

        emptyRecentLabel = new JLabel("No recent projects yet — open or create one to see it here.");
        emptyRecentLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        emptyRecentLabel.setForeground(TEXT_SECONDARY);
        emptyRecentLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        emptyRecentLabel.setBorder(BorderFactory.createEmptyBorder(6, 2, 0, 0));
        recentSection.add(emptyRecentLabel);

        root.add(recentSection);
        root.add(Box.createVerticalGlue());

        jScrollPane1 = new JScrollPane(root);
        jScrollPane1.setBorder(BorderFactory.createEmptyBorder());
        jScrollPane1.getViewport().setBackground(BG_WINDOW);
        jScrollPane1.getVerticalScrollBar().setUnitIncrement(16);

        add(jScrollPane1, BorderLayout.CENTER);
    }

    private static ImageIcon loadLogo(int size) {
        java.net.URL url = welcomeTopComponent.class.getResource("app_logo.png");
        if (url == null) {
            return new ImageIcon();
        }
        Image img = new ImageIcon(url).getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
        // Apri il progetto con OpenRefract
        OpenPrj opn = new OpenPrj();
        opn.actionPerformed(null);
        // Recupera il file appena aperto (assumendo che OpenPrj.lastOpenedFile sia stato impostato)
        if (OpenPrj.lastOpenedFile != null) {
            moveRecentFirst(OpenPrj.lastOpenedFile.getAbsolutePath());
        } else {
            updateRecentProject();
        }

    }

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {
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
        boolean mORefract = false;
        if (file.getPath().toLowerCase().endsWith(".txt")) {
            mTxt = true;
            opj.loadPrj(file, editor.obj, base);
        } else if (file.getPath().toLowerCase().endsWith(".srefract")) {
            opj.loadPrjSmartRefract(file, editor);
            editor.open();
            editor.requestActive();
            editor.setDisplayName((new File(lista[0])).getName());
            editor.invalidate();
        } else if (file.getPath().toLowerCase().endsWith(".orefract")) {
            mORefract = true;
            try {
                editor.obj = OpenRefractLoader.loadOpenRefractProject(file);
                editor.obj.sync();
            } catch (Exception ex) {
                javax.swing.JOptionPane.showMessageDialog(null,
                    "Errore caricamento OpenRefract: " + ex.getMessage(),
                    "Errore", javax.swing.JOptionPane.ERROR_MESSAGE);
                return;
            }
            editor.txV.setObj(editor.obj);
            editor.sv.setObj(editor.obj);
            editor.tv.setObj(editor.obj);
            editor.txV.repaint();
            editor.sv.repaint();
            editor.tv.repaint();
        }

        updateRecentProject();

        editor.setDisplayName((new File(lista[0])).getName());
        editor.obj.proj_file = (new File(lista[0]));
        if (mTxt) {
            opj.loadtrace(editor.obj, base);
            editor.obj.loadSism(0);
            editor.obj.LoadTrace_For_Open();
            editor.obj.tr = editor.obj.getTraces();
        }
        if (mTxt || mORefract) {
            try {
                editor.obj.fb =  editor.obj.TraceGroup.get(0);
            } catch (Exception ex) {
                // fallback: do nothing
            }
            editor.obj.prevTr();
            editor.tv.obj = editor.obj;
            editor.obj.sync();
            editor.tv.repaint();
            editor.txV.setProj(editor.obj.proj);
            editor.invalidate();
        }

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


    }

    private void new_Button1ActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        // TODO add your handling code here:
        System.getProperty("os.arch");
        System.getProperty("os.name");
        folder_history fh = new folder_history();
        DocumentEditor editor = new DocumentEditor();
        editor.open();
        editor.requestActive();
        JFileChooser fc = new JFileChooser();
        //set the default directory of the file chooser to the last opened folder
        fc.setCurrentDirectory(new File(fh.getLastOpenedFolder()));

        fc.setFileFilter(new Seg2FileFilter());
        fc.setMultiSelectionEnabled(true);

        int returnVal = fc.showOpenDialog(editor);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            //File file = fc.getSelectedFile();

            //save the last opened folder
            fh.saveLastOpenedFolder(fc.getSelectedFile().getParent());

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


    }

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {
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
        boolean mORefract = false;
        if (file.getPath().toLowerCase().endsWith(".txt")) {
            mTxt = true;
            opj.loadPrj(file, editor.obj, base);
        } else if (file.getPath().toLowerCase().endsWith(".srefract")) {
            opj.loadPrjSmartRefract(file, editor);
            editor.open();
            editor.requestActive();
            editor.setDisplayName((new File(lista[1])).getName());
            editor.invalidate();
        } else if (file.getPath().toLowerCase().endsWith(".orefract")) {
            mORefract = true;
            try {
                editor.obj = OpenRefractLoader.loadOpenRefractProject(file);
                editor.obj.sync();
            } catch (Exception ex) {
                javax.swing.JOptionPane.showMessageDialog(null,
                    "Errore caricamento OpenRefract: " + ex.getMessage(),
                    "Errore", javax.swing.JOptionPane.ERROR_MESSAGE);
                return;
            }
            editor.txV.setObj(editor.obj);
            editor.sv.setObj(editor.obj);
            editor.tv.setObj(editor.obj);
            editor.txV.repaint();
            editor.sv.repaint();
            editor.tv.repaint();
        }

        editor.setDisplayName((new File(lista[1])).getName());
        editor.obj.proj_file = (new File(lista[1]));
        if (mTxt) {
            opj.loadtrace(editor.obj, base);
            editor.obj.loadSism(0);
            editor.obj.LoadTrace_For_Open();
            editor.obj.tr = editor.obj.getTraces();
        }
        if (mTxt || mORefract) {
            try {
                editor.obj.fb = editor.obj.TraceGroup.get(editor.obj.trace_index);
            } catch (Exception ex) {
                // fallback: do nothing
            }
            editor.obj.prevTr();
            editor.obj.sync();
            editor.tv.repaint();
            editor.txV.setProj(editor.obj.proj);
            editor.invalidate();
        }

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

    }

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {

        DocumentEditor editor = new DocumentEditor();
        OpenPrj open = new OpenPrj();
        boolean mTxt = false;
        boolean mORefract = false;
        editor.open();
        editor.requestActive();
        String path = (new File(lista[2])).getAbsolutePath(); //fc.getSelectedFile().getAbsolutePath();
        String base = (new File(lista[2])).getParent();  // (fc.getSelectedFile().getParent());
        String relative = (new File(base)).toURI().relativize(new File(path).toURI()).getPath();// new File(base).toURI().relativize(new File(path).toURI()).getPath();

        File file = new File(lista[2]);    //File file = fc.getSelectedFile();

        editor.obj.TraceGroup = new ArrayList();
        if (file.getPath().toLowerCase().endsWith(".txt")) {
            open.loadPrj(file, editor.obj, base);
            mTxt = true;
        } else if (file.getPath().toLowerCase().endsWith(".srefract")) {
            open.loadPrjSmartRefract(file, editor);
            editor.open();
            editor.requestActive();
            editor.setDisplayName(file.getName());
            editor.invalidate();
        } else if (file.getPath().toLowerCase().endsWith(".orefract")) {
            mORefract = true;
            try {
                editor.obj = OpenRefractLoader.loadOpenRefractProject(file);
                editor.obj.sync();
            } catch (Exception ex) {
                javax.swing.JOptionPane.showMessageDialog(null,
                    "Errore caricamento OpenRefract: " + ex.getMessage(),
                    "Errore", javax.swing.JOptionPane.ERROR_MESSAGE);
                return;
            }
            editor.txV.setObj(editor.obj);
            editor.sv.setObj(editor.obj);
            editor.tv.setObj(editor.obj);
            editor.txV.repaint();
            editor.sv.repaint();
            editor.tv.repaint();
        }

        // La lista dei recenti viene letta da updateRecentProject(): lo stream aperto qui
        // non era usato né chiuso, e lasciava un descrittore aperto a ogni progetto aperto.
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
        if (mTxt || mORefract) {
            try {
                editor.obj.fb =  editor.obj.TraceGroup.get(0);
            } catch (Exception ex) {
                // fallback: do nothing
            }
            editor.obj.prevTr();
            editor.tv.obj = editor.obj;
            editor.obj.sync();
            editor.tv.repaint();
            editor.invalidate();
        }

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
    }

    // ---- UI components ------------------------------------------------
    private FluentButton new_Button1;
    private FluentButton jButton2;
    private RecentItemButton jButton3;
    private RecentItemButton jButton4;
    private RecentItemButton jButton5;
    private RecentItemButton[] recentButtons;
    private JLabel jLabel2;
    private JLabel emptyRecentLabel;
    private RoundedPanel recentCard;
    private JScrollPane jScrollPane1;

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

    // ==================================================================
    //  Fluent-style building blocks
    // ==================================================================

    /** A flat rounded-corner panel used as a card container. */
    private static class RoundedPanel extends JPanel {
        private final int radius;

        RoundedPanel(int radius) {
            this.radius = radius;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(CARD_BG);
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), radius, radius));
            g2.setColor(CARD_BORDER);
            g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, getWidth() - 1f, getHeight() - 1f, radius, radius));
            g2.dispose();
            super.paintComponent(g);
        }
    }

    /** Hairline divider used between rows inside the recent-projects card. */
    private static class Divider extends JComponent {
        Divider() {
            setOpaque(false);
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
            setPreferredSize(new Dimension(10, 1));
            setAlignmentX(Component.LEFT_ALIGNMENT);
        }

        @Override
        protected void paintComponent(Graphics g) {
            g.setColor(CARD_BORDER);
            g.fillRect(14, 0, getWidth() - 28, 1);
        }
    }

    /** Large primary/secondary action card, Windows 11 "Get started" style. */
    private static class FluentButton extends JButton {
        private final boolean primary;
        private boolean hover;

        FluentButton(String titleText, String descriptionText, Icon icon, boolean primary) {
            this.primary = primary;
            setIcon(icon);
            setText("<html><div style='width:170px'>"
                    + "<span style='font-size:14px;font-weight:bold;color:" + (primary ? "#FFFFFF" : "#201F1E") + "'>"
                    + escapeHtml(titleText) + "</span><br>"
                    + "<span style='font-size:11px;color:" + (primary ? "#FBE7D0" : "#605E5C") + "'>"
                    + escapeHtml(descriptionText) + "</span></div></html>");
            setHorizontalAlignment(SwingConstants.LEFT);
            setVerticalTextPosition(SwingConstants.BOTTOM);
            setHorizontalTextPosition(SwingConstants.LEFT);
            setIconTextGap(10);
            setFocusPainted(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(18, 20, 16, 16));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    hover = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    hover = false;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            RoundRectangle2D shape = new RoundRectangle2D.Float(0.5f, 0.5f, getWidth() - 1f, getHeight() - 1f, 8, 8);
            if (primary) {
                g2.setColor(hover ? ACCENT_HOVER : ACCENT);
                g2.fill(shape);
            } else {
                g2.setColor(hover ? SECONDARY_HOVER : CARD_BG);
                g2.fill(shape);
                g2.setColor(CARD_BORDER);
                g2.draw(shape);
            }
            g2.dispose();
            super.paintComponent(g);
        }
    }

    /** Flat, hoverable row used inside the recent-projects card. */
    private static class RecentItemButton extends JButton {
        private boolean hover;

        RecentItemButton() {
            setIcon(new DocumentIcon());
            setHorizontalAlignment(SwingConstants.LEFT);
            setIconTextGap(12);
            setFocusPainted(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setAlignmentX(Component.LEFT_ALIGNMENT);
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 56));
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    hover = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    hover = false;
                    repaint();
                }
            });
        }

        void setRecentText(String titleText, String subtitleText) {
            setText("<html><div>"
                    + "<span style='font-size:12px;color:#1A1A1A'>" + escapeHtml(titleText) + "</span><br>"
                    + "<span style='font-size:11px;color:#605E5C'>" + escapeHtml(subtitleText) + "</span></div></html>");
        }

        @Override
        protected void paintComponent(Graphics g) {
            if (hover) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(ROW_HOVER);
                g2.fill(new RoundRectangle2D.Float(2, 2, getWidth() - 4, getHeight() - 4, 6, 6));
                g2.dispose();
            }
            super.paintComponent(g);
        }
    }

    /** Simple flat "+" glyph used on the primary New button. */
    private static class PlusIcon implements Icon {
        private final int size;
        private final Color color;

        PlusIcon(int size, Color color) {
            this.size = size;
            this.color = color;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(2.4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            int cx = x + size / 2;
            int cy = y + size / 2;
            int r = size / 2 - 2;
            g2.drawLine(cx - r, cy, cx + r, cy);
            g2.drawLine(cx, cy - r, cx, cy + r);
            g2.dispose();
        }

        @Override
        public int getIconWidth() {
            return size;
        }

        @Override
        public int getIconHeight() {
            return size;
        }
    }

    /** Simple flat folder glyph used on the secondary Open button. */
    private static class FolderIcon implements Icon {
        private final int size;
        private final Color color;

        FolderIcon(int size, Color color) {
            this.size = size;
            this.color = color;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            float w = size;
            float h = size * 0.78f;
            float top = y + size - h;
            g2.fill(new RoundRectangle2D.Float(x, top, w * 0.42f, h * 0.26f, 3, 3));
            g2.fill(new RoundRectangle2D.Float(x, top + h * 0.16f, w, h * 0.84f, 4, 4));
            g2.dispose();
        }

        @Override
        public int getIconWidth() {
            return size;
        }

        @Override
        public int getIconHeight() {
            return size;
        }
    }

    /** Small flat document glyph used on recent-project rows. */
    private static class DocumentIcon implements Icon {
        private final int size = 24;

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(0xF3, 0xF3, 0xF3));
            g2.fillRoundRect(x + 3, y + 1, size - 8, size - 2, 3, 3);
            g2.setColor(new Color(0xC8, 0xC6, 0xC4));
            g2.drawRoundRect(x + 3, y + 1, size - 8, size - 2, 3, 3);
            g2.setColor(ACCENT);
            g2.fillRect(x + 6, y + 7, size - 14, 2);
            g2.fillRect(x + 6, y + 12, size - 14, 2);
            g2.fillRect(x + 6, y + 17, size - 18, 2);
            g2.dispose();
        }

        @Override
        public int getIconWidth() {
            return size;
        }

        @Override
        public int getIconHeight() {
            return size;
        }
    }

}
