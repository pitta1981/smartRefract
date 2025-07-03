/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

 /*
 * DocumentEditor.java
 *
 * Created on 28-mar-2011, 0.03.08
 */
package it.vs30.myeditor;

import it.vs30.geometryView.geometryViewerTopComponent;
import it.vs30.sidebar.sideTools_TopComponent;
import it.vs30.sidebar.trace_tools;
import it.vs30.smartRefract.drawing.DrawDromocrone;
import it.vs30.smartRefract.drawing.DrawHorizontalRule;
import it.vs30.smartRefract.drawing.DrawVerticalRule;
import it.vs30.smartUI.JTX_view;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.font.LineMetrics;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JScrollPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.myorg.myapi.APIObject;
import org.myorg.myapi.DrawingAPI;
import org.myorg.myapi.FirstBrakeList;
//import org.myorg.myviewer.MyViewerTopComponent;
import org.myorg.myapi.Trace;
import org.netbeans.api.print.PrintManager;
import org.netbeans.spi.actions.AbstractSavable;

import org.netbeans.spi.print.PrintPage;
import org.netbeans.spi.print.PrintProvider;

import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.util.Cancellable;
import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 *
 * @author user
 */
public class DocumentEditor extends TopComponent implements DocumentListener {
//public class DocumentEditor extends TopComponent implements  {

    private final InstanceContent content = new InstanceContent();
    public APIObject obj = null;
    public TraceView tv = null;
    public Trace[] tr = null;
    //   public JDromoView dv;
    public JSezioneView sv;
    public white wh = new white();
    public JScrollPane jScrollPane1;
    public Rule columnView = null;
    public Rule rowView = null;
    private sideTools_TopComponent sideTool = null;
    public static final Icon ICON = ImageUtilities.loadImageIcon("org/myorg/myeditor/instagram12.png", true);
    public JTX_view txV;
    private geometryViewerTopComponent geomTC;

    /**
     * Creates new form MyEditor
     */
    public DocumentEditor() {

        System.setProperty("sun.java2d.opengl", "True");
        //    associateLookup(new AbstractLookup(content));
        obj = new APIObject();
        //associateLookup(new AbstractLookup(ic));
        //associateLookup(Lookups.singleton(obj));
        setDisplayName("MyEditor " + obj.getIndex());
        content.set(Collections.singleton(obj), null);
        //   Lookup lu=getLookup();

   
        tv = new TraceView(obj);
        tv.docEd = this;
        
//        dv = new JDromoView(obj);
        txV = new JTX_view(obj);

        // dv.setBackground(Color.BLACK);
        sv = new JSezioneView(obj);
        TopComponent tc1 = WindowManager.getDefault().findTopComponent("sideTools_TopComponent");
        sideTool = (sideTools_TopComponent) tc1;
        
        // Check if sideTools_TopComponent is available before using it
        if (sideTool != null && sideTool.txt != null) {
            sideTool.txt.setTXView(txV.dromoSelected);
            sideTool.jtr_dlg = new trace_tools(tv);
        } else {
            System.err.println("Warning: sideTools_TopComponent not available or not fully initialized");
        }

//        tc1 = WindowManager.getDefault().findTopComponent("geometryViewerTopComponent");
//        geomTC = (geometryViewerTopComponent) tc1;
//        geomTC.gmview.setStesa(obj.proj.stesa);

        initComponents();
        sv.setBackground(Color.BLACK);
        //jPanel3.setLayout(new AbsoluteLayout());
        //jPanel3.add(tv);tv.setLocation(0, 0);
        tv.setPreferredSize(new Dimension(jPanel3.getWidth(), jPanel3.getHeight() * 2));
        //tv.setSize(300,2000);

        jScrollPane1 = new JScrollPane(tv);

        //modify();
//Where the GUI is initialized:
//Create the row and column headers.
        columnView = new Rule(Rule.HORIZONTAL, true);
        rowView = new Rule(Rule.VERTICAL, true);
        columnView.setPreferredWidth(jPanel3.getWidth());
        rowView.setPreferredHeight(jPanel3.getHeight());
        tv.setRuler(columnView, rowView);
        jScrollPane1.setColumnHeaderView(columnView);
        jScrollPane1.setRowHeaderView(rowView);

        jPanel3.add(jScrollPane1);

        //jScrollPane1.add(tv);
        //jPanel4.add(dv);
        jPanel4.add(txV);
        jPanel2.add(sv);
        this.updateUI();

        final MyPrint dataPrintProvider = new MyPrint();

        PrintProvider[] printProviders = new PrintProvider[]{dataPrintProvider};

        final Action printAction = PrintManager.printAction(printProviders);

        //    putClientProperty("print.printable", Boolean.TRUE);
        //     content.set(getActionMap(), dataPrintProvider);
        //   associateLookup(new AbstractLookup(content));
        //   associateLookup( Lookups.fixed( getActionMap(), dataPrintProvider ) );
        //   printButton.setAction( printAction );
        associateLookup(new ProxyLookup(new AbstractLookup(content), Lookups.fixed(getActionMap(), dataPrintProvider)));

        //     TopComponent tc = WindowManager.getDefault().findTopComponent("MyViewerTopComponent");
        //Lookup tcLookup = tc.getLookup();
        //    ((MyViewerTopComponent)tc).jLabel1.setText("APIObject #" + obj.getIndex());
        //    ((MyViewerTopComponent)tc).jLabel2.setText("Created: " + obj.getDate());
        //  ((MyViewerTopComponent) tc).setActive(obj);
    }

    public void modify() {
        
        if (getLookup().lookup(MySavable.class) == null) {
            content.add(new MySavable());
            this.setDisplayName(this.getDisplayName() + " (*)");
        }
    }

    @Override
    public boolean canClose() {
        MySavable savable = getLookup().lookup(MySavable.class);
        if (savable == null) {
            return true;
        }
        String msg = "Form '" + getDisplayName() + "' has unsaved data. Save it?";
        NotifyDescriptor nd = new NotifyDescriptor.Confirmation(msg, NotifyDescriptor.YES_NO_CANCEL_OPTION);
        Object result = DialogDisplayer.getDefault().notify(nd);
        if (result == NotifyDescriptor.CANCEL_OPTION || result == NotifyDescriptor.CLOSED_OPTION) {
            return false;
        }
        if (result == NotifyDescriptor.NO_OPTION) {
            Cancellable cancellable = getLookup().lookup(Cancellable.class);
            if (cancellable != null) {
                cancellable.cancel();
            }
            savable.unmodify();  //toglie il documento corrente dalla lista dei doc da salvare
            content.remove(savable);
            return true;
        }
        try {
            savable.save();
            content.remove(savable);
            return true;
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
            return false;
        }
    }

    @Override
    public int getPersistenceType() {
        return PERSISTENCE_NEVER;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSeparator1 = new javax.swing.JSeparator();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jToolBar1 = new javax.swing.JToolBar();
        jTracePath = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();

        setLayout(new java.awt.BorderLayout());
        add(jSeparator1, java.awt.BorderLayout.CENTER);

        jButton2.setText(org.openide.util.NbBundle.getMessage(DocumentEditor.class, "DocumentEditor.jButton2.text")); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        add(jButton2, java.awt.BorderLayout.LINE_START);

        jButton3.setText(org.openide.util.NbBundle.getMessage(DocumentEditor.class, "DocumentEditor.jButton3.text")); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        add(jButton3, java.awt.BorderLayout.LINE_END);

        jTabbedPane1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPane1StateChanged(evt);
            }
        });

        jPanel3.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                jPanel3ComponentResized(evt);
            }
        });
        jPanel3.setLayout(new java.awt.BorderLayout());

        jToolBar1.setRollover(true);

        jTracePath.setText(org.openide.util.NbBundle.getMessage(DocumentEditor.class, "DocumentEditor.jTracePath.text")); // NOI18N
        jToolBar1.add(jTracePath);

        jPanel3.add(jToolBar1, java.awt.BorderLayout.PAGE_START);

        jTabbedPane1.addTab(org.openide.util.NbBundle.getMessage(DocumentEditor.class, "DocumentEditor.jPanel3.TabConstraints.tabTitle"), jPanel3); // NOI18N

        jPanel4.setLayout(new java.awt.BorderLayout());
        jTabbedPane1.addTab(org.openide.util.NbBundle.getMessage(DocumentEditor.class, "DocumentEditor.jPanel4.TabConstraints.tabTitle"), jPanel4); // NOI18N

        jPanel2.setLayout(new java.awt.BorderLayout());
        jTabbedPane1.addTab(org.openide.util.NbBundle.getMessage(DocumentEditor.class, "DocumentEditor.jPanel2.TabConstraints.tabTitle"), jPanel2); // NOI18N

        add(jTabbedPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    @Override
    public void componentClosed() {
        System.out.println("-- Editor closed --");
        //   NotifyDescriptor d=new NotifyDescriptor("text","title",NotifyDescriptor.YES_NO_OPTION,NotifyDescriptor.QUESTION_MESSAGE,null,null);

        //  DialogDisplayer.getDefault().notify(d);
    }

    @Override
    public void componentShowing() {
        System.out.println("-- Editor showing --");

        TopComponent tc;// = WindowManager.getDefault().findTopComponent("MyViewerTopComponent");
//        Lookup tcLookup = tc.getLookup();
        //((MyViewerTopComponent) tc).active=obj;
/*        ((MyViewerTopComponent) tc).gmview.vis = jTabbedPane1.getSelectedIndex();
        ((MyViewerTopComponent) tc).gmview.invalidate();
        ((MyViewerTopComponent) tc).gmview.repaint();
*/
        tc = WindowManager.getDefault().findTopComponent("sideTools_TopComponent");
        ((sideTools_TopComponent) tc).jtr_dlg.setTraceView(tv);
        ((sideTools_TopComponent) tc).txt.setTTview(this.txV);
        ((sideTools_TopComponent) tc).setTabSelected(jTabbedPane1.getSelectedIndex());
        ((sideTools_TopComponent) tc).setAPIObject(obj);
        tc = WindowManager.getDefault().findTopComponent("geometryViewerTopComponent");
        geomTC = (geometryViewerTopComponent) tc;
        geomTC.gmview.setStesa(obj.proj.stesa);
        geomTC.gmview.vis = jTabbedPane1.getSelectedIndex();
        geomTC.gmview.invalidate();
        geomTC.gmview.repaint();
        //  ((sideTools_TopComponent) tc).set
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        obj.prevTr();
        TopComponent tc;

        tc = WindowManager.getDefault().findTopComponent("geometryViewerTopComponent");
        geomTC = (geometryViewerTopComponent) tc;
        geomTC.gmview.setGeom(obj.fb.scoppio, obj.fb.spaz, obj.fb.spaz_in, obj.tr.length);
        geomTC.gmview.repaint();
        geomTC.gmview.invalidate();

        jTracePath.setText(obj.fb.fbp);
        tv.repaint();
        tv.invalidate();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        obj.nextTr();
        jTracePath.setText(obj.fb.fbp);
        TopComponent tc;/* = WindowManager.getDefault().findTopComponent("MyViewerTopComponent");
        Lookup tcLookup = tc.getLookup();
        ((MyViewerTopComponent) tc).gmview.setBackground(Color.black);
        ((MyViewerTopComponent) tc).gmview.setGeom(obj.fb.scoppio, obj.fb.spaz, obj.fb.spaz_in, obj.tr.length);
        ((MyViewerTopComponent) tc).gmview.repaint();
        ((MyViewerTopComponent) tc).gmview.invalidate();*/

        tc = WindowManager.getDefault().findTopComponent("geometryViewerTopComponent");
        geomTC = (geometryViewerTopComponent) tc;
        geomTC.gmview.setGeom(obj.fb.scoppio, obj.fb.spaz, obj.fb.spaz_in, obj.tr.length);
        geomTC.gmview.repaint();
        geomTC.gmview.invalidate();

        tv.repaint();
        tv.invalidate();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jTabbedPane1StateChanged(javax.swing.event.ChangeEvent evt) {

        try {
//            dv.dlg_Sel.setVisible(false);

        } catch (Exception ex) {
        }
        try {
            sv.sezDlg.setVisible(false);
        } catch (Exception ex) {
        }
        System.out.println("-- cambio tab --- \nnew tab: " + jTabbedPane1.getSelectedIndex());
        obj.selected_Tab = jTabbedPane1.getSelectedIndex();
        if (jTabbedPane1.getSelectedIndex() == 2) {
            System.out.println(obj.proj.Zg1.length);
            obj.proj.Zg1 = new double[1];
            if (obj.proj.Zg1.length < 2) {
                // TODO add your handling code here:
                FileOutputStream fos = null;
                //this.hide();
                String userHome = "user.home";

                // We get the path by getting the system property with the
                // defined key above.
                String path = System.getProperty(userHome);

                try {
                    fos = new FileOutputStream(path + "/smartRefract-data/" + "dromo.txt", false);
                    OutputStreamWriter os = new OutputStreamWriter(fos);
                    os.flush();
                    os.close();

                } catch (Exception ex) {

                }
                obj.proj.writeDromo();
                //obj.proj.setdromo();
                // obj.sync();
                obj.proj.max3 = obj.proj.count_element_layer(3, 0);
                obj.proj.maxR3 = obj.proj.count_element_layer(3, 1);
                obj.proj.sezDT_ver2016(true);
                modify();
//      System.out.println(obj.proj.stesa);

            }

        }

        TopComponent tc;// = WindowManager.getDefault().findTopComponent("MyViewerTopComponent");
       // Lookup tcLookup = tc.getLookup();

/*        ((MyViewerTopComponent) tc).gmview.vis = jTabbedPane1.getSelectedIndex();
        //((MyViewerTopComponent) tc).active=obj;
        ((MyViewerTopComponent) tc).gmview.invalidate();
        ((MyViewerTopComponent) tc).gmview.repaint();*/

        tc = WindowManager.getDefault().findTopComponent("sideTools_TopComponent");
        ((sideTools_TopComponent) tc).setTabSelected(jTabbedPane1.getSelectedIndex());
        ((sideTools_TopComponent) tc).jtr_dlg.setTraceView(tv);
        ((sideTools_TopComponent) tc).txt.setTTview(this.txV);
        ((sideTools_TopComponent) tc).prfDlg.setSezView(this.sv);
        
        
        ((sideTools_TopComponent) tc).setAPIObject(obj);

        tc = WindowManager.getDefault().findTopComponent("geometryViewerTopComponent");
        geomTC = (geometryViewerTopComponent) tc;
        geomTC.gmview.setStesa(obj.proj.stesa);
        geomTC.gmview.vis = jTabbedPane1.getSelectedIndex();
    }

        private void jPanel3ComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jPanel3ComponentResized

        System.out.println("\tRESIZED --");          tv.resized(jPanel3.getWidth(), jPanel3.getHeight());         //tv.setPreferredSize(new Dimension(jPanel3.getWidth(),jPanel3.getHeight()*2));         //tv.setSize(new Dimension(jPanel3.getWidth(),jPanel3.getHeight()*2));         //tv.invalidate();         System.out.println("PANEL3 - " + jPanel3.getHeight());     }//GEN-LAST:event_jPanel3ComponentResized
        tv.resized(jPanel3.getWidth(), jPanel3.getHeight());
        //tv.setPreferredSize(new Dimension(jPanel3.getWidth(),jPanel3.getHeight()*2));
        //tv.setSize(new Dimension(jPanel3.getWidth(),jPanel3.getHeight()*2));
        //tv.invalidate();
        System.out.println("PANEL3 - " + jPanel3.getHeight());

    }

    public void registerObj() {

        TopComponent tc = WindowManager.getDefault().findTopComponent("MyViewerTopComponent");
        Lookup tcLookup = tc.getLookup();
         tc = WindowManager.getDefault().findTopComponent("geometryViewerTopComponent");
            geomTC = (geometryViewerTopComponent) tc;
            geomTC.gmview.setGeom(this.obj.fb.scoppio, this.obj.fb.spaz, this.obj.fb.spaz_in, this.obj.tr.length);
            geomTC.gmview.repaint();
            geomTC.gmview.invalidate();
        
/*
        ((MyViewerTopComponent) tc).setActive(obj);
        ((MyViewerTopComponent) tc).gmview.setBackground(Color.black);
        ((MyViewerTopComponent) tc).gmview.setStesa(obj.proj.stesa);
        ((MyViewerTopComponent) tc).gmview.setGeom(obj.fb.scoppio, obj.fb.spaz, obj.fb.spaz_in, obj.tr.length);
        ((MyViewerTopComponent) tc).gmview.repaint();
        ((MyViewerTopComponent) tc).gmview.invalidate();*/

        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private class MyPrint implements PrintProvider, PrintPage {

        private class PageSezione implements PrintPage {

            private int pageHeight;
            private int pageWidth;
            private DrawingAPI dA;

            public PageSezione(int pageWidth, int pageHight) {
                this.pageHeight = pageHight;
                this.pageWidth = pageWidth;
                this.dA = new DrawingAPI(obj.proj, obj);
            }

            @Override
            public void print(Graphics g) {

                boolean proporz = obj.proporz;
                boolean isWhite = obj.is_white;
                //  if (isWhite) {
                //       this.setBackground(Color.white);

                //   } else {
                //       this.setBackground(Color.BLACK);
                //   }
                //  dAPI=new DrawingAPI(proj, obj);
                //sezione.draw();
                // dAPI.drawSezAssi(g,this.getWidth(), this.getHeight());
                double[] mxmn = dA.maxmin();
                FirstBrakeList ArrFB =  obj.proj.stesa.get(0);
                //  double[] mxmn=dAPI.maxmin();
                //  FirstBrakeList ArrFB = (FirstBrakeList) proj.stesa.get(0);
                double ascismax = (ArrFB.ch - 1) * ArrFB.spaz;
                double x_step = (pageWidth / ArrFB.fb.length);
                double x = (pageWidth - (2 * dA.margine_dx) - (2 * dA.margine_sx)) / ascismax;
                double y = x;
                double profmx = mxmn[0] + mxmn[1];
                double rap = (profmx * y) + 120;
                double scala = 1;
                //margup=0.07
                //ystp = (ymax - (2.3 * margUp * ymax) - 30) / mxmn;

                if (rap > pageHeight) {
                    // scala=(this.getHeight()-120)/rap;
                    x = (pageHeight - 120) / profmx;
                    rap = (pageHeight);
                    y = x;

                }
                if (!obj.proporz) {

                    x = (pageWidth - (2 * dA.margine_dx) - (2 * dA.margine_sx)) / ascismax;
                    y = (pageHeight - 120) / profmx;
                    rap = (profmx * y) + 120;

                }

                //   drawAssi(g, this.getWidth(), this.getHeight());
                dA.drawSezAssi(g, pageWidth, (int) (rap), x, y);

                dA.drawSezione2(g,  (pageWidth), (int) (rap), x, y);

                //drawSezione(g, this.getWidth(), this.getHeight());
                //      PenetrometroGraph pg = new PenetrometroGraph(new int[]{4, 3, 6, 3, 6, 9, 12, 14, 11, 10, 16, 19, 40, 60}, 25, 0.5);
                //pg.draw(g, margUp,xshf, this.getWidth(), this.getHeight(), this.maxv,((FirstBrakeList) proj.stesa.get(0)).spaz_in,((FirstBrakeList) proj.stesa.get(0)).spaz*(((FirstBrakeList) proj.stesa.get(0)).ch-1));
                // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        }

        private class PageTX implements PrintPage {

            private int pageHeight;
            private int pageWidth;
            private DrawDromocrone dd;

            public PageTX(int pageWidth, int pageHight) {
                this.pageHeight = pageHight;
                this.pageWidth = pageWidth;
                this.dd = new DrawDromocrone((obj.proj), obj);

            }

            @Override
            public void print(Graphics g) {

                dd.drawDr(g, pageWidth, pageHeight, true);
                // g.drawString("TEST", 20, 150);
//throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

        }

        private class PageTrace implements PrintPage {

            private List<String> lines;
            private int fromLine;
            private int toLine;
            private int pageHeight;
            private int pageWidth;
            DrawingAPI dapi;
            private int shot_n;

            PageTrace(int pageWidth, int pageHight, int i) {
                this.dapi = new DrawingAPI(obj.proj, obj);
                this.shot_n = i;
                this.pageHeight = pageHight;
                this.pageWidth = pageWidth;
            }

            @Override
            public void print(Graphics g) {
                g.setColor(Color.BLACK);
                FontMetrics fm = g.getFontMetrics();
                LineMetrics lm = fm.getLineMetrics("txt", g);
                float fheight = lm.getHeight();
                int height = (int) Math.ceil(fheight);
                height += height / 2;

                int offset = height;
                BufferedImage bi = new BufferedImage(pageWidth - 30, pageHeight - 30, BufferedImage.TYPE_INT_RGB);
                Graphics offg = bi.getGraphics();
                offg.setColor(Color.WHITE);
                offg.fillRect(0, 0, pageWidth - 30, pageHeight - 30);
                dapi.setHeight(pageHeight - 30);
                dapi.setWidth(pageWidth - 30);
                dapi.drawSeism(offg, obj.proj.stesa.get(this.shot_n).tr);
                g.drawImage(bi, 30, 30, null);

                g.drawString("TEST", 20, 150);

                double margine_X = 0;
                //TODO: da portare in double
                double stepCh = (int) ((pageWidth - 30) - 2 * margine_X) / obj.tr.length;
                double stepT = (double) (pageHeight - 30) / (double) (obj.tr[0].length);
                DrawVerticalRule dvr = new DrawVerticalRule();
                dvr.setVertInterv(stepT * obj.tr[0].sampleInterval * 1000, obj.tr[0].length, obj.tr[0].sampleInterval);
                dvr.setHeight(pageHeight - 30);
                bi = new BufferedImage(30, pageHeight - 30, BufferedImage.TYPE_INT_RGB);
                offg = bi.getGraphics();
                offg.setColor(Color.WHITE);
                offg.fillRect(0, 0, pageWidth - 30, pageHeight - 30);
                dvr.drawAssi(offg);
                g.drawImage(bi, 0, 30, null);

                DrawHorizontalRule dhr = new DrawHorizontalRule();
                FirstBrakeList ArrFB = (FirstBrakeList) obj.proj.stesa.get(shot_n);
                bi = new BufferedImage(pageWidth - 30, 30, BufferedImage.TYPE_INT_RGB);
                offg = bi.getGraphics();
                dhr.setHorPar(stepCh, ArrFB.spaz_in, ArrFB.spaz, obj.tr.length);
                offg.setColor(Color.WHITE);
                offg.fillRect(0, 0, pageWidth - 30, pageHeight - 30);
                dhr.drawX(offg);
                g.drawImage(bi, 30, 0, null);
            }

            public int getToLine() {
                return toLine;
            }

        }

        @Override
        public PrintPage[][] getPages(int w, int h, double d) {

            List<PrintPage> pageslst = new ArrayList<PrintPage>();
            for (int i = 0; i < obj.proj.stesa.size(); i++) {

                pageslst.add(new PageTrace(w, h, i));

            }
            pageslst.add(new PageTX(w, h));
            pageslst.add(new PageSezione(w, h));
            PrintPage[][] pages = new PrintPage[pageslst.size()][1];

            int ind = 0;
            for (Iterator<PrintPage> it = pageslst.iterator(); it.hasNext();) {
                PrintPage printPage = it.next();
                pages[ind][0] = printPage;
                ind++;
            }
            return pages;
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public String getName() {
            return "Test";
//throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Date lastModified() {
            return new Date();
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void print(Graphics grphcs) {

            grphcs.drawRect(0, 0, 200, 200);
//throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

    }

    private class MySavable extends AbstractSavable /*implements Icon*/ {

        MySavable() {
            register();
        }

        @Override
        protected String findDisplayName() {
            try {
                //  final Document doc = jTextArea1.getDocument();
                
                String s = tc().getDisplayName();
                int indx = s.indexOf('\n');
                if (indx >= 0) {
                    s = s.substring(0, indx);
                }
                return "'" + s + "'";
            } catch (Exception ex) {
                return ex.getLocalizedMessage();
            }
        }

        @Override
        protected void handleSave() throws IOException {
            tc().content.remove(this);
            unregister();
            if (tc().obj.proj_file != null) {
                String name = tc().obj.proj_file.getName().toLowerCase();
                if (name.endsWith("srefract")) {
                    System.out.println(tc().obj.proj_file.getName());
                    SaveProject.saveSmartRefractProject(tc().obj.proj_file, tc().obj);
                    tc().setDisplayName(tc().obj.proj_file.getName());
                } else if (name.endsWith("orefract")) {
                    OpenRefractWriter.saveOpenRefractProject(tc().obj.proj_file, tc().obj);
                    tc().setDisplayName(tc().obj.proj_file.getName());
                } else {
                    // Default: salva in formato OpenRefract
                    OpenRefractWriter.saveOpenRefractProject(tc().obj.proj_file, tc().obj);
                    tc().setDisplayName(tc().obj.proj_file.getName());
                }
            }
        }

        DocumentEditor tc() {
            return DocumentEditor.this;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof MySavable) {
                MySavable m = (MySavable) obj;
                return tc() == m.tc();
            }
            return false;
        }

        @Override
        public int hashCode() {
            return tc().hashCode();
        }

        /*
        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            ICON.paintIcon(c, g, x, y);
        }

        @Override
        public int getIconWidth() {
            return ICON.getIconWidth();
        }

        @Override
        public int getIconHeight() {
            return ICON.getIconHeight();
        }*/

        private void unmodify() {
            tc().content.remove(this);
            unregister();
            // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JToolBar jToolBar1;
    public javax.swing.JLabel jTracePath;
    // End of variables declaration//GEN-END:variables
    InstanceContent ic = new InstanceContent();

    @Override
    public void insertUpdate(DocumentEvent de) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void removeUpdate(DocumentEvent de) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void changedUpdate(DocumentEvent de) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
