/*
 * Copyright (c) 2009, 2024, Simone Pittaluga and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Simone Pittaluga or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/*
* TraceView.java
*
* Created on 28-mar-2011, 17.03.51
*/
package it.vs30.myeditor;

import it.vs30.geometryView.geometryViewerTopComponent;
import it.vs30.smartRefract.utils.ZoomTraceUtil;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.TexturePaint;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import org.myorg.myapi.APIObject;
import org.myorg.myapi.FirstBrakeList;
import org.myorg.myapi.Indagine;
//import org.myorg.myviewer.MyViewerTopComponent;
import org.openide.util.Lookup;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 *
 * @author user
 */
public class TraceView extends javax.swing.JPanel {

    public APIObject obj = null;
    // public JTraceDlg dlg_Trc;
    public int scaleY = 1;
    public double scaleX = 1.0;
    private boolean unlocked = false;
    public boolean avgMAX = false;
    private int margine_X = 0;
    public boolean drawDromo = false;
    private Rule cView;
    private Rule rView;
    private boolean selectionMode;
    private int selected_trace = 0;

    /**
     * Reference to the documentEditor object
     */
    public DocumentEditor docEd;

    /**
     * Creates new form TraceView
     */
    public TraceView() {
        initComponents();
        // dlg_Trc = new JTraceDlg(this);
        setBackground(Color.BLACK);
    }

    public TraceView(APIObject Seismobj) {

        initComponents();
        obj = Seismobj;
        proj = obj.proj;
        // dlg_Trc = new JTraceDlg(this);
        setBackground(Color.BLACK);
        setForeground(Color.WHITE);
    }

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);

        BufferedImage im = (BufferedImage) this.createImage(this.getWidth(), this.getHeight());
        try {
            obj.fb = obj.TraceGroup.get(obj.trace_index);
        } catch (Exception ex) {
            System.err.println("LOG - No seismic data loaded " + ex.getMessage());
        }
        Graphics offg = im.getGraphics();
        this.is_white = obj.is_white;
        if (obj.is_white) {
            this.setBackground(Color.white);
            offg.setColor(Color.white);
        } else {
            this.setBackground(Color.black);

            offg.setColor(Color.black);
        }
        // offg.clearRect(0, 0, this.getWidth(), this.getHeight());

        offg.fillRect(0, 0, this.getWidth(), this.getHeight());
        if (selectionMode) {
            this.drawMousePosition(offg, selected_trace);
            this.drawSelected(offg);
        }

        // drawSelection(offg);
        drawSeism(offg);
        drawPick(offg, this.getWidth(), this.getHeight());
        if (this.drawDromo) {
            draw_all_Pick(offg);
        }
        drawAssi(offg);
        // disegnaSegnale(g);
        // disegnaPrimoA(g);
        g.drawImage(im, 0, 0, this);

        // TODO fix open project issue. The following code is a workaround
        TopComponent tc = WindowManager.getDefault().findTopComponent("MyViewerTopComponent");
        // Lookup tcLookup = tc.getLookup();
        /*
         * try {
         * ((MyViewerTopComponent) tc).gmview.setGeom(obj.fb.scoppio, obj.fb.spaz,
         * obj.fb.spaz_in, obj.tr.length);
         * ((MyViewerTopComponent) tc).active = obj;
         * ((MyViewerTopComponent) tc).gmview.invalidate();
         * ((MyViewerTopComponent) tc).gmview.repaint();
         * } catch (Exception ex) {
         * System.err.println("Errore in paintComponent: " + ex.getMessage());
         * }
         */
        geometryViewerTopComponent geomTC;
        tc = WindowManager.getDefault().findTopComponent("geometryViewerTopComponent");
        try {
            geomTC = (geometryViewerTopComponent) tc;
            geomTC.gmview.setGeom(this.obj.fb.scoppio, this.obj.fb.spaz, this.obj.fb.spaz_in, this.obj.tr.length);
            geomTC.gmview.repaint();
            geomTC.gmview.invalidate();
        } catch (Exception ex) {
            System.err.println("Errore in paintComponent: " + ex.getMessage());
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }

            public void mousePressed(java.awt.event.MouseEvent evt) {
                formMousePressed(evt);
            }

            public void mouseReleased(java.awt.event.MouseEvent evt) {
                formMouseReleased(evt);
            }
        });
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                formMouseDragged(evt);
            }

            public void mouseMoved(java.awt.event.MouseEvent evt) {
                formMouseMoved(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 413, Short.MAX_VALUE));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 300, Short.MAX_VALUE));
    }// </editor-fold>//GEN-END:initComponents

    private void formMouseMoved(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_formMouseMoved
        try {
            double stepCh = (this.getWidth() - (2 * margine_X)) / obj.tr.length;
            double stepT = (double) (this.getHeight()) / (double) (obj.tr[0].length);
            if (evt.getX() > margine_X && evt.getX() < (margine_X + (this.getWidth() - (2 * margine_X)))) {
                this.setToolTipText("Canale " + (int) ((evt.getX() - margine_X) / stepCh) + " tempo: "
                        + (int) (evt.getY() * (1 / stepT)) * obj.tr[0].sampleInterval * 1000);
            }

            // this.setToolTipText(" " + (int) (evt.getX() / stepCh) + " " + (int)
            // (evt.getY() * (1 / stepT)) /** obj.tr[0].sampleInterval*/
            // + " v: " + obj.tr[(int) (evt.getX() / stepCh)].value[(int) (evt.getY() * (1 /
            // stepT))] + " dv: " + (obj.tr[(int) (evt.getX() / stepCh)].value[(int)
            // (evt.getY() * (1 / stepT)) + 1] - obj.tr[(int) (evt.getX() /
            // stepCh)].value[(int) (evt.getY() * (1 / stepT))]));
            if (selectionMode) {
                if (evt.getX() > margine_X && evt.getX() < (margine_X + (this.getWidth() - (2 * margine_X)))) {

                    // this.setToolTipText("Canale " + (int) ((evt.getX() - margine_X) / stepCh) + "
                    // tempo: " + (int) (evt.getY() * (1 / stepT)) * obj.tr[0].sampleInterval *
                    // 1000);
                    // super.paintComponent(g);
                    selected_trace = (int) ((evt.getX() - margine_X) / stepCh);
                    this.repaint();
                    invalidate();

                }

            }

        } catch (Exception ex) {
        }
        /*
         * try {
         * AWTUtilities.setWindowOpacity(dlg_Trc, 0.5f);
         * } catch (Exception ex) {
         * }
         */

        // TODO add your handling code here:
    }// GEN-LAST:event_formMouseMoved

    public double undoFB = 0.0;
    public int undoCh = 0;
    public boolean is_white = false;

    private void formMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_formMouseClicked
        /*
         * try { // System.currentTimeMillis(); double stepCh = this.getWidth()
         * / obj.tr.length; double stepT = (double) (this.getHeight()) /
         * (double) (obj.tr[0].length); this.setToolTipText(" " + (int)
         * (evt.getX() / stepCh) + " " + (int) (evt.getY() * (1 / stepT)) *
         * obj.tr[0].sampleInterval);
         *
         * undoFB = obj.fb.fb[(int) (evt.getX() / stepCh)].time / 1000; undoCh =
         * (int) (evt.getX() / stepCh); obj.tr[(int) (evt.getX() /
         * stepCh)].setPick((evt.getY() * (1 / stepT)) *
         * obj.tr[0].sampleInterval); obj.fb.setFB((int) (evt.getX() / stepCh),
         * (evt.getY() * (1 / stepT)) * obj.tr[0].sampleInterval * 1000);
         * //obj.TraceGroup.remove(obj.trace_index);
         * obj.TraceGroup.set(obj.trace_index, obj.fb); obj.proj.stesa =
         * obj.TraceGroup; } catch (Exception ex) { System.out.println("" + ex);
         * } //dlg_Trc.setProj(proj); this.repaint(); this.invalidate(); double
         * screenH =
         * java.awt.Toolkit.getDefaultToolkit().getScreenSize().getHeight(); int
         * delta = (int) ((screenH - evt.getYOnScreen()) - dlg_Trc.getHeight())
         * / 3; dlg_Trc.setLocation(evt.getXOnScreen() - (dlg_Trc.getWidth() /
         * 2), evt.getYOnScreen() + delta); //dlg_Trc.setOpacity(0.5f); try {
         * AWTUtilities.setWindowOpacity(dlg_Trc, 0.5f); } catch (Exception ex)
         * { } dlg_Trc.setVisible(true);
         */
        // TODO add your handling code here:
        double stepCh = (this.getWidth() - (2 * margine_X)) / obj.tr.length;

        if (selectionMode) {
            if (evt.getX() > margine_X && evt.getX() < (margine_X + (this.getWidth() - (2 * margine_X)))) {

                // this.setToolTipText("Canale " + (int) ((evt.getX() - margine_X) / stepCh) + "
                // tempo: " + (int) (evt.getY() * (1 / stepT)) * obj.tr[0].sampleInterval *
                // 1000);
                // super.paintComponent(g);
                selected_trace = (int) ((evt.getX() - margine_X) / stepCh);
                obj.TraceGroup.get(obj.trace_index).zoomTr[selected_trace].is_selected = !obj.TraceGroup
                        .get(obj.trace_index).zoomTr[selected_trace].is_selected;
                this.repaint();
                invalidate();

            }

        }

    }// GEN-LAST:event_formMouseClicked

    long time = 0;

    private void formMousePressed(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_formMousePressed
        // TODO add your handling code here:
        time = 0;
        time = System.currentTimeMillis();
    }// GEN-LAST:event_formMousePressed

    private void formMouseReleased(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_formMouseReleased
        // TODO add your handling code here:
        long dt = System.currentTimeMillis() - time;
        if (!this.unlocked) {
            if (dt < 500) {

                double screenH = java.awt.Toolkit.getDefaultToolkit().getScreenSize().getHeight();
                // int delta = (int) ((screenH - evt.getYOnScreen()) - dlg_Trc.getHeight()) / 3;
                // dlg_Trc.setLocation(evt.getXOnScreen() - (dlg_Trc.getWidth() / 2),
                // evt.getYOnScreen() + delta);
                // dlg_Trc.setOpacity(0.5f);
                /*
                 * try {
                 * AWTUtilities.setWindowOpacity(dlg_Trc, 0.5f);
                 * } catch (Exception ex) {
                 * }
                 */
                // dlg_Trc.setVisible(true);

            } else {
                try {
                    // System.currentTimeMillis();
                    double stepCh = (this.getWidth() - (2 * margine_X)) / obj.tr.length;
                    double stepT = (double) (this.getHeight()) / (double) (obj.tr[0].length);
                    if (evt.getX() > margine_X && evt.getX() < (margine_X + (this.getWidth() - (2 * margine_X)))) {
                        int ch = (int) ((evt.getX() - margine_X) / stepCh);

                        this.setToolTipText(
                                " " + ch + " " + (int) (evt.getY() * (1 / stepT)) * obj.tr[0].sampleInterval);

                        undoFB = obj.fb.fb[ch].time / 1000;
                        undoCh = ch;
                        obj.tr[ch].setPick((evt.getY() * (1 / stepT)) * obj.tr[0].sampleInterval);
                        obj.fb.setFB(ch, (evt.getY() * (1 / stepT)) * obj.tr[0].sampleInterval * 1000);
                    }
                    // obj.TraceGroup.remove(obj.trace_index);
                    obj.TraceGroup.set(obj.trace_index, obj.fb);
                    obj.proj.stesa = obj.TraceGroup;

                    docEd.modify();

                } catch (Exception ex) {
                    System.out.println("" + ex);
                }

            }
        } else {
            try {
                // System.currentTimeMillis();
                double stepCh = (this.getWidth() - (2 * margine_X)) / obj.tr.length;
                double stepT = (double) (this.getHeight()) / (double) (obj.tr[0].length);
                this.setToolTipText(" " + (int) (evt.getX() / stepCh) + " "
                        + (int) (evt.getY() * (1 / stepT)) * obj.tr[0].sampleInterval);

                if (evt.getX() > margine_X && evt.getX() < (margine_X + (this.getWidth() - (2 * margine_X)))) {
                    int ch = (int) ((evt.getX() - margine_X) / stepCh);

                    this.setToolTipText(" " + ch + " " + (int) (evt.getY() * (1 / stepT)) * obj.tr[0].sampleInterval);

                    undoFB = obj.fb.fb[ch].time / 1000;
                    undoCh = ch;
                    obj.tr[ch].setPick((evt.getY() * (1 / stepT)) * obj.tr[0].sampleInterval);
                    obj.fb.setFB(ch, (evt.getY() * (1 / stepT)) * obj.tr[0].sampleInterval * 1000);
                }

                // obj.TraceGroup.remove(obj.trace_index);
                obj.TraceGroup.set(obj.trace_index, obj.fb);
                obj.proj.stesa = obj.TraceGroup;
            } catch (Exception ex) {
                System.out.println("" + ex);
            }

        }

        // dlg_Trc.setProj(proj);
        this.repaint();
        this.invalidate();
        double screenH = java.awt.Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        // int delta = (int) ((screenH - evt.getYOnScreen()) - dlg_Trc.getHeight()) / 3;
        // dlg_Trc.setLocation(evt.getXOnScreen() - (dlg_Trc.getWidth() / 2),
        // evt.getYOnScreen() + delta);
        // dlg_Trc.setOpacity(0.5f);
        /*
         * try {
         * AWTUtilities.setWindowOpacity(dlg_Trc, 0.5f);
         * } catch (Exception ex) {
         * }
         */
        // dlg_Trc.setVisible(true);

    }// GEN-LAST:event_formMouseReleased

    private void formMouseDragged(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_formMouseDragged
        // TODO add your handling code here:

        if (this.unlocked) {

            try {
                // System.currentTimeMillis();
                double stepCh = (this.getWidth() - (2 * margine_X)) / obj.tr.length;
                double stepT = (double) (this.getHeight()) / (double) (obj.tr[0].length);
                this.setToolTipText(" " + (int) (evt.getX() / stepCh) + " "
                        + (int) (evt.getY() * (1 / stepT)) * obj.tr[0].sampleInterval);

                if (evt.getX() > margine_X && evt.getX() < (margine_X + (this.getWidth() - (2 * margine_X)))) {
                    int ch = (int) ((evt.getX() - margine_X) / stepCh);

                    this.setToolTipText(" " + ch + " " + (int) (evt.getY() * (1 / stepT)) * obj.tr[0].sampleInterval);

                    undoFB = obj.fb.fb[ch].time / 1000;
                    undoCh = ch;
                    obj.tr[ch].setPick((evt.getY() * (1 / stepT)) * obj.tr[0].sampleInterval);
                    obj.fb.setFB(ch, (evt.getY() * (1 / stepT)) * obj.tr[0].sampleInterval * 1000);
                    docEd.modify();
                }

                // obj.TraceGroup.remove(obj.trace_index);
                obj.TraceGroup.set(obj.trace_index, obj.fb);
                obj.proj.stesa = obj.TraceGroup;
            } catch (Exception ex) {
                System.out.println("" + ex);
            }

        }

        // dlg_Trc.setProj(proj);
        this.repaint();
        this.invalidate();
        double screenH = java.awt.Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        // int delta = (int) ((screenH - evt.getYOnScreen()) - dlg_Trc.getHeight()) / 3;
        /*
         * dlg_Trc.setLocation(evt.getXOnScreen() - (dlg_Trc.getWidth() / 2),
         * evt.getYOnScreen() + delta);
         * //dlg_Trc.setOpacity(0.5f);
         * try {
         * AWTUtilities.setWindowOpacity(dlg_Trc, 0.5f);
         * } catch (Exception ex) {
         * }
         * dlg_Trc.setVisible(true);
         */

    }// GEN-LAST:event_formMouseDragged

    private void drawAssi(Graphics g) {
        try {
            if (obj.tr.length > 0) {

                if (!is_white) {
                    this.setForeground(Color.white);
                } else {
                    this.setForeground(Color.black);
                }

                Graphics2D g2 = (Graphics2D) g;
                final BasicStroke asse = new BasicStroke(0.0f);
                g2.setStroke(asse);

                if (!is_white) {
                    g2.setColor(Color.WHITE);
                } else {
                    g2.setColor(Color.BLACK);
                }
                int j = 0;
                double maxV = obj.tr[j].getMaxValue();
                double stepT = (double) (this.getHeight()) / (double) (obj.tr[0].length);

                double max = obj.tr[0].sampleInterval * 1000 * obj.tr[0].length;
                this.rView.isWhite(is_white);
                this.rView.setVertInterv(stepT * obj.tr[0].sampleInterval * 1000, obj.tr[0].length,
                        obj.tr[0].sampleInterval);

                this.rView.invalidate();
                this.rView.repaint();
                // this.rView.invalidate();
                double stepCh = (this.getWidth() - 2 * margine_X) / obj.tr.length;
                double stepV = (double) stepCh / (double) maxV;

                ArrFB = (FirstBrakeList) obj.TraceGroup.get(0);

                this.cView.setHorPar(stepCh, ArrFB.spaz_in, ArrFB.spaz, obj.tr.length);
                this.cView.isWhite(is_white);
                this.cView.invalidate();
                this.cView.repaint();
                // int xshf = (int) (margine_X * 0.75);

                // g2.drawLine(0, 20, this.getWidth(), 20);
                // g2.drawLine((int) (margine_X * 0.75), 0, (int) (margine_X * 0.75),
                // this.getHeight());
                // " tempo: " + (int) (evt.getY() * (1 / stepT)) * obj.tr[0].sampleInterval *
                // 1000
                int majTic = 0, minTic = 0;

                // ystp = (ymax - (2 * 45) - 30) / max;
                double ystp = stepT;

                if (max / 10.0 < 1) {
                    majTic = 2;
                    minTic = 1;

                } else if (max / 10.0 < 2) {
                    majTic = 3;
                    minTic = 1;
                } else if (max / 10.0 < 4) {
                    majTic = 5;
                    minTic = 2;
                } else if (max / 10.0 < 10) {
                    majTic = 10;
                    minTic = 2;
                } else if (max / 10.0 < 30) {
                    majTic = 20;
                    minTic = 5;
                } else if (max / 10.0 < 50) {
                    majTic = 25;
                    minTic = 5;
                } else if (max / 10.0 >= 50) {
                    majTic = 30;
                    minTic = 10;
                }

                // g2.setColor(Color.white);
                if (obj.is_white) {
                    g2.setColor(Color.black);
                } else {
                    g2.setColor(Color.white);
                }

                g2.setFont(new Font("Dialog", Font.PLAIN, 12));
                // new Font("Dialog", Font.PLAIN, 12);

                FontMetrics fontMetrics = g2.getFontMetrics();

                int height = fontMetrics.getHeight();

                /*
                 * for (int i = 0; i < max; i = i + minTic) {
                 * if (i % majTic == 0) {
                 * int x1 = (int) xshf;
                 * 
                 * int y1 = (int) (i * stepT / obj.tr[0].sampleInterval) / 1000;
                 * 
                 * int x2 = (int) xshf - 8;
                 * int y2 = (int) (i * stepT / obj.tr[0].sampleInterval) / 1000;
                 * 
                 * g2.drawLine(x1, y1, x2, y2);
                 * 
                 * 
                 * int width = fontMetrics.stringWidth("" + (i));
                 * 
                 * g2.drawString("" + (i), (int) x2 - width, y2 + (height / 3));
                 * 
                 * } else {
                 * 
                 * int x1 = (int) xshf;
                 * 
                 * int y1 = (int) (i * stepT / obj.tr[0].sampleInterval) / 1000;
                 * 
                 * int x2 = (int) xshf - 5;
                 * int y2 = (int) (i * stepT / obj.tr[0].sampleInterval) / 1000;
                 * 
                 * g2.drawLine(x1, y1, x2, y2);
                 * 
                 * // g2.drawLine((int) (xshf * 0.75), ymax - (int) (i * ystp + (margUp *
                 * ymax)), (int) (xshf * 0.65), ymax - (int) (i * ystp + (margUp * ymax)));
                 * // g2.drawLine((int) (xmax - xshf * 0.75), ymax - (int) (i * ystp + (margUp *
                 * ymax)), (int) (xmax - xshf * 0.65), ymax - (int) (i * ystp + (margUp *
                 * ymax)));
                 * }
                 * }
                 */
            }
        } catch (Exception ex) {
            System.err.println("" + ex.getMessage());
        }

    }

    private void drawPick(Graphics g, int w, int h) {
        try {
            if (obj.tr.length > 0) {

                Graphics2D g2 = (Graphics2D) g;

                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                for (int j = 0; j < obj.tr.length; j++) {
                    double maxV = obj.tr[j].getMaxValue();
                    double stepT = (double) (h) / (double) (obj.tr[0].length);
                    double stepCh = (w - 2 * margine_X) / obj.tr.length;
                    double stepV = (double) stepCh / (double) maxV;
                    int x1 = (int) ((((j) * stepCh) + stepCh / 2) - stepCh / 3) + margine_X;
                    int y1 = (int) (obj.fb.fb[j].time * stepT / obj.tr[0].sampleInterval) / 1000;

                    int x2 = (int) ((((j) * stepCh) + stepCh / 2) + stepCh / 3) + margine_X;
                    int y2 = (int) (obj.fb.fb[j].time * stepT / obj.tr[0].sampleInterval) / 1000;
                    if (obj.fb.fb[j].enabled) {
                        g2.setColor(Color.red);

                        g2.drawLine(x1, y1, x2, y2);
                    } else {
                        g2.setColor(Color.DARK_GRAY);

                        g2.drawLine(x1, y1, x2, y2);
                        drawDisabled(g2, w, h, x1, x2);
                    }

                }
            }
        } catch (Exception e) {
        }
    }

    private void drawSeism(Graphics g) {
        try {

            Path2D p = new Path2D.Double();
            int x1i = 0, y1i = 0;
            boolean mag = false;
            if (obj.tr.length > 0) {
                if (!is_white) {
                    this.setForeground(Color.white);
                } else {
                    this.setForeground(Color.black);
                }

                // g.drawString(" "+obj.tr[0].getSampleInterval()+" "+stepV+" "+stepT, 50, 50);
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(Color.white);
                for (int j = 0; j < obj.tr.length; j++) {
                    double maxV;
                    if (this.avgMAX) {
                        maxV = obj.tr[j].getMediaAbs();
                    } else {
                        maxV = obj.tr[j].getMaxValue() - obj.tr[j].media;
                    }

                    double stepT = (double) (this.getHeight()) / (double) (obj.tr[0].length);
                    double stepCh = (this.getWidth() - 2 * margine_X) / obj.tr.length;
                    double stepV = 0.5 * (double) stepCh / (double) maxV;
                    if (!is_white) {
                        g2.setColor(Color.lightGray);
                    } else {
                        g2.setColor(Color.DARK_GRAY);
                    }
                    g2.drawLine((int) ((((j) * stepCh) + stepCh / 2)) + margine_X, 0,
                            (int) ((((j) * stepCh) + stepCh / 2)) + margine_X, this.getHeight());

                    int x2 = 0;
                    int y2 = 0;
                    int x1 = 0;
                    if (!this.avgMAX) {
                        maxV = maxV / this.scaleX;
                    }
                    ZoomTraceUtil[] zu = obj.TraceGroup.get(obj.trace_index).zoomTr;
                    for (int i = 0; i < obj.tr[0].length - 1; i++) {
                        // g2.drawRect((int) ((((j) * stepCh)+ stepCh / 2) + stepV *
                        // obj.tr[j].value[i]), (int) (i * stepT), 1, 1);

                        if (Math.abs(obj.tr[j].value[i] * zu[j].zoom_factor) < maxV) {
                            x1 = (int) ((((j) * stepCh) + stepCh / 2)
                                    + this.scaleX * stepV * (obj.tr[j].value[i] - obj.tr[j].media) * zu[j].zoom_factor);
                        } else {
                            int s = (int) (Math.abs(obj.tr[j].value[i]) / obj.tr[j].value[i]);
                            x1 = (int) ((((j) * stepCh) + stepCh / 2)
                                    + s * this.scaleX * stepV * (maxV - obj.tr[j].media));
                        }

                        int y1 = (int) (i * stepT);
                        if (Math.abs(obj.tr[j].value[i + 1] * zu[j].zoom_factor) < maxV) {
                            x2 = (int) ((((j) * stepCh) + stepCh / 2) + this.scaleX * stepV
                                    * (obj.tr[j].value[i + 1] - obj.tr[j].media) * zu[j].zoom_factor);
                            // System.out.println(x2+" "+(((j) * stepCh) + stepCh / 2)+" "+this.scaleX *
                            // stepV * (obj.tr[j].value[i + 1] - obj.tr[j].media) * zu[j].zoom_factor+"
                            // "+j+" "+obj.tr[j].value[i + 1]+" "+i);
                        } else {
                            int s = (int) (Math.abs(obj.tr[j].value[i]) / obj.tr[j].value[i]);

                            x2 = (int) ((((j) * stepCh) + stepCh / 2)
                                    + s * this.scaleX * stepV * (maxV - obj.tr[j].media));

                            // System.out.println("Via 2 "+x2+" "+(((j) * stepCh) + stepCh / 2)+" "+s *
                            // this.scaleX * stepV * (maxV - obj.tr[j].media)+" "+j+" "+obj.tr[j].value[i +
                            // 1]+" "+i);
                        }

                        // x2 = (int) ((((j) * stepCh) + stepCh / 2) + this.scaleX * stepV *
                        // (obj.tr[j].value[i + 1] - obj.tr[j].media));
                        y2 = (int) ((i + 1) * stepT);
                        if (!is_white) {
                            g2.setColor(Color.white);

                        } else {
                            g2.setColor(Color.black);
                        }

                        // System.out.println(x2);
                        g2.drawLine(x1 + margine_X, y1, x2 + margine_X, y2);

                        if (obj.tr[j].value[i + 1] - obj.tr[j].media > 0) {

                            if (mag == false) {
                                // Graphics2D g2 = (Graphics2D) g.create();
                                // Path2D p2 = new Path2D.Double();
                                p = new Path2D.Double();

                                if (x2 != x1) {
                                    double m = (y2 * 1.0 - y1 * 1.0) / (x2 - x1);
                                    double q = ((x2 * y1) - (x1 * y2)) / (x2 - x1);
                                    double x = (((j) * stepCh) + stepCh / 2);
                                    int y = (int) (m * x + q);

                                    // p = new Path2D.Double();
                                    p.moveTo(x + margine_X, y);
                                    if (y < y1 && y < y2)
                                        ;

                                    x1i = (int) x;
                                    y1i = y;
                                } else {
                                    p.moveTo(x1 + margine_X, y1);
                                    x1i = (int) x1;
                                    y1i = y1;
                                }

                                mag = true;
                            } else {
                                p.lineTo(x1 + margine_X, y1); // curveTo(x1,y1, ystp * proj.Zg1[(proj.Zg1.length - 3)] +
                                                              // (margUp * ymax) + 30, (xshf + (nchanel *
                                                              // (proj.Zg1.length - 3))), ystp *
                                                              // proj.Zg1[(proj.Zg1.length - 3)] + (margUp * ymax) +
                                                              // 30);

                            }

                            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                            /*
                             * if (i % 1.5 == 0) {
                             *
                             * g2.setColor(Color.GRAY); g2.drawLine((int) ((((j)
                             * * stepCh) + stepCh / 2)), y2, x2, y2); }
                             */
                        } else {
                            if (mag) {

                                if (x2 != x1) {
                                    double m = (y2 * 1.0 - y1 * 1.0) / (x2 - x1);
                                    double q = ((x2 * y1) - (x1 * y2)) / (x2 - x1);
                                    double x = (((j) * stepCh) + stepCh / 2);
                                    int y = (int) (m * x + q);

                                    // p = new Path2D.Double();
                                    p.lineTo(x + margine_X, y);
                                }

                                p.lineTo(x1i + margine_X, y1i);

                                BufferedImage bi = new BufferedImage(5, 5,
                                        BufferedImage.TYPE_INT_RGB);
                                Graphics2D big = bi.createGraphics();

                                if (!is_white) {
                                    big.setColor(Color.LIGHT_GRAY);
                                } else {
                                    big.setColor(Color.darkGray);
                                }

                                big.fillRect(0, 0, 5, 5);
                                // big.setColor(Color.lightGray);
                                // big.fillOval(0, 0, 5, 5);
                                Rectangle r = new Rectangle(0, 0, 5, 5);
                                g2.setPaint(new TexturePaint(bi, r));

                                g2.draw(p);
                                g2.fill(p);

                                mag = !mag;
                            }
                        }

                    }

                    if (mag) {

                        double x = (((j) * stepCh) + stepCh / 2);

                        // p = new Path2D.Double();
                        p.lineTo(x2 + margine_X, y2);
                        p.lineTo(((j) * stepCh) + stepCh / 2
                                + margine_X, y2);

                        p.lineTo(x1i + margine_X, y1i);

                        BufferedImage bi = new BufferedImage(5, 5,
                                BufferedImage.TYPE_INT_RGB);
                        Graphics2D big = bi.createGraphics();

                        if (!is_white) {
                            big.setColor(Color.LIGHT_GRAY);
                        } else {
                            big.setColor(Color.darkGray);
                        }

                        big.fillRect(0, 0, 5, 5);
                        // big.setColor(Color.lightGray);
                        // big.fillOval(0, 0, 5, 5);
                        Rectangle r = new Rectangle(0, 0, 5, 5);
                        g2.setPaint(new TexturePaint(bi, r));

                        g2.draw(p);
                        g2.fill(p);

                        mag = !mag;
                    }

                }
            }
        } catch (Exception e) {
        }

        // throw new UnsupportedOperationException("Not yet implemented");
    }

    public void resized(int w, int h) {

        w = this.getParent().getWidth();
        h = this.getParent().getHeight();

        this.setPreferredSize(new Dimension((int) (w * scaleX), h * scaleY));
        rView.setPreferredHeight(h * scaleY);
        cView.setPreferredWidth((int) (w * scaleX));
        this.setSize(new Dimension((int) (w * scaleX), h * scaleY));
        this.invalidate();
    }

    public void undo() {
        obj.tr[undoCh].setPick(undoFB);
        obj.fb.setFB(undoCh, undoFB * 1000);
        // obj.TraceGroup.remove(obj.trace_index);
        obj.TraceGroup.set(obj.trace_index, obj.fb);
        obj.proj.stesa = obj.TraceGroup;
    }

    // exec autopicking of first break
    /**
     * Automatically picks the first arrival in seismic trace data using the STA/LTA method.
     *
     * @param LTA the length of the long-term average window
     * @param STA the length of the short-term average window
     * @param thrs the threshold for the STA/LTA ratio to identify a pick
     *
     * The method processes each trace in the `obj.tr` array. It calculates the short-term average (STA),
     * long-term average (LTA), and their ratio (STA/LTA) for each data point in the trace. When the STA/LTA
     * ratio exceeds the specified threshold, it identifies the index as the first arrival and sets the pick
     * time accordingly.
     *
     * The method performs the following steps:
     * 1. Initializes the STA, LTA, and STA/LTA ratio arrays.
     * 2. Calculates the STA for each data point.
     * 3. Calculates the LTA for each data point.
     * 4. Calculates the STA/LTA ratio for each data point.
     * 5. Identifies the first arrival based on the STA/LTA ratio exceeding the threshold.
     * 6. Sets the pick time and updates the relevant objects.
     *
     * The method also repaints and invalidates the current view after processing all traces.
     */
    public void autoPick_2(int LTA,int STA, double thrs) {
        int staWindow = STA; // finestra temporale breve
        int ltaWindow = LTA; // finestra temporale lunga
        double threshold = thrs; // soglia per il rapporto STA/LTA

        for (int i = 0; i < obj.tr.length; i++) {
            int len = obj.tr[i].value.length;
            double[] data = obj.tr[i].value;
            double[] sta = new double[len];
            double[] lta = new double[len];
            double[] staLtaRatio = new double[len];

            // Calcolare STA
            for (int j = staWindow; j < len; j++) {
                double sum = 0.0;
                for (int k = j - staWindow; k < j; k++) {
                    sum += Math.abs(data[k]);
                }
                sta[j] = sum / staWindow;
            }

            // Calcolare LTA
            for (int j = ltaWindow; j < len; j++) {
                double sum = 0.0;
                for (int k = j - ltaWindow; k < j; k++) {
                    sum += Math.abs(data[k]);
                }
                lta[j] = sum / ltaWindow;
            }

            // Calcolare il rapporto STA/LTA
            for (int j = ltaWindow; j < len; j++) {
                if (lta[j] != 0) {
                    staLtaRatio[j] = sta[j] / lta[j];
                } else {
                    staLtaRatio[j] = 0;
                }
            }

            // Identificare i primi arrivi
            for (int j = ltaWindow; j < len; j++) {
                if (staLtaRatio[j] > threshold) 
                {
                    System.out.println("Primo arrivo rilevato a indice: " + j);
                    obj.tr[i].setPick(j * obj.tr[i].sampleInterval);
                    obj.fb.setFB(i, j * obj.tr[i].sampleInterval * 1000);
                    j = len - 1;
                    obj.TraceGroup.set(obj.trace_index, obj.fb);
                    obj.proj.stesa = obj.TraceGroup;
                    break;
                }
            }
        }
        this.repaint();
        this.invalidate();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    public void AutoPick() {

        // double[] mobav=new double[len];
        for (int i = 0; i < obj.tr.length; i++) {
            int len = obj.tr[i].value.length;

            double[] mobav = new double[len];

            for (int j = 10; j < len - 10; j++) {
                int count = 0;
                for (int k = -10; k <= 9; k++) {
                    mobav[j] = mobav[j] + obj.tr[i].value[j + k];
                    count++;
                }
                mobav[j] = mobav[j] / count;
            }
            for (int j = 10; j < len - 10; j++) {
                int count = 0;
                double dV = 0;
                boolean bV = true;
                if (obj.tr[i].value[j] > 0) {
                    for (int k = -3; k <= 3; k++) {
                        dV = dV + (mobav[j + k + 1 - 5] - mobav[j + k - 5]);
                        count++;
                        bV = bV && ((mobav[j + (k + 5)] - mobav[j + (k + 5) - 1]) > 5);
                    }

                    dV = dV / count;

                    if (dV > 4 && bV) {
                        if (mobav[j] + (10 * 1.4 * dV) < mobav[j + 10]) {

                            obj.tr[i].setPick(j * obj.tr[i].sampleInterval);
                            obj.fb.setFB(i, j * obj.tr[i].sampleInterval * 1000);
                            j = len - 1;
                            obj.TraceGroup.set(obj.trace_index, obj.fb);
                            obj.proj.stesa = obj.TraceGroup;
                        }
                    }
                }
            }

        }
        this.repaint();
        this.invalidate();

    }

    public void setLock(boolean selected) {
        unlocked = selected; // To change body of generated methods, choose Tools | Templates.
    }

    FirstBrakeList ArrFB;
    Indagine proj;

    private void draw_all_Pick(Graphics g) {
        try {
            float[] dash1 = { 5.0f };
            final BasicStroke dashed = new BasicStroke(0.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash1,
                    0.0f);
            if (obj.tr.length > 0) {

                Graphics2D g2 = (Graphics2D) g;
                g2.setStroke(dashed);
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

                // double maxV = obj.tr[i].getMaxValue();
                double stepT = (double) (this.getHeight()) / (double) (obj.tr[0].length);
                double stepCh = (this.getWidth() - 2 * margine_X) / obj.tr.length;
                // double stepV = (double) stepCh / (double) maxV;

                for (int i = 0; i < proj.stesa.size(); i++) {
                    if (i != obj.trace_index) {
                        FirstBrakeList fbl = (FirstBrakeList) proj.stesa.get(i);
                        Path2D p = new Path2D.Double();
                        p.moveTo(0 * stepCh + stepCh / 2 + margine_X,
                                (fbl.fb[0].time * stepT / obj.tr[0].sampleInterval) / 1000);
                        for (int j = 0; j < fbl.fb.length; j++) {

                            int x1 = (int) ((((j) * stepCh) + stepCh / 2) - stepCh / 3) + margine_X;
                            int y1 = (int) (fbl.fb[j].time * stepT / obj.tr[0].sampleInterval) / 1000;

                            int x2 = (int) ((((j) * stepCh) + stepCh / 2) + stepCh / 3) + margine_X;
                            int y2 = (int) (fbl.fb[j].time * stepT / obj.tr[0].sampleInterval) / 1000;
                            g2.setColor(Color.GREEN);

                            x1 = (int) (x1 + stepCh / 3);

                            if ((fbl.scoppio - fbl.spaz_in) / fbl.spaz > j
                                    && (fbl.scoppio - fbl.spaz_in) / fbl.spaz < j + 1) {

                                p.lineTo(x1, y1);
                                p.lineTo(((j + 0.5) * stepCh) + stepCh / 2 + margine_X, 0);
                            } else {
                                p.lineTo(x1, y1);
                            }
                            // g2.drawLine(x1, y1, x2, y2);
                            g2.drawRect((int) (x1 - 3), y1 - 3, 6, 6);
                            g2.draw(p);
                        }
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    void setRuler(Rule columnView, Rule rowView) {
        cView = columnView;
        rView = rowView;
        // throw new UnsupportedOperationException("Not supported yet."); //To change
        // body of generated methods, choose Tools | Templates.
    }

    public boolean getSelectionMode() {
        return selectionMode;
    }

    public void setSelectionMode(boolean selected) {

        this.selectionMode = selected;
        // throw new UnsupportedOperationException("Not supported yet."); //To change
        // body of generated methods, choose Tools | Templates.
    }

    private void drawMousePosition(Graphics offg, int n_canale) {
        double stepCh = (this.getWidth() - (2 * margine_X)) / obj.tr.length;
        Color prev_colot = offg.getColor();
        offg.setColor(Color.yellow);
        offg.drawRect((int) (n_canale * stepCh), 0, (int) stepCh, this.getHeight());
        offg.setColor(prev_colot);
        // throw new UnsupportedOperationException("Not supported yet."); //To change
        // body of generated methods, choose Tools | Templates.
    }

    private void drawSelected(Graphics offg) {
        double stepCh = (this.getWidth() - (2 * margine_X)) / obj.tr.length;
        Color prev_colot = offg.getColor();
        offg.setColor(new Color(255, 255, 0, 120));
        for (int i = 0; i < obj.TraceGroup.get(obj.trace_index).zoomTr.length; i++) {
            if (obj.TraceGroup.get(obj.trace_index).zoomTr[i].is_selected) {
                offg.fillRect((int) (i * stepCh), 0, (int) stepCh, this.getHeight());
            }

        }

        // throw new UnsupportedOperationException("Not supported yet."); //To change
        // body of generated methods, choose Tools | Templates.
    }

    private void drawDisabled(Graphics2D g2, int w, int h, int x1, int x2) {
        g2.setColor(Color.ORANGE);
        g2.drawLine(x1, 0, x2, h);
        g2.drawLine(x2, 0, x1, h);
        // To change body of generated methods, choose Tools | Templates.
    }
}
