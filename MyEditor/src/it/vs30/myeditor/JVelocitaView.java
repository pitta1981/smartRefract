/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * JVelocitaView.java
 *
 * Created on 5-apr-2012, 22.37.55
 */
package it.vs30.myeditor;

import java.awt.*;
import org.myorg.myapi.FirstBrakeList;

/**
 *
 * @author user
 */
public class JVelocitaView extends javax.swing.JPanel {

    /**
     * Creates new form JVelocitaView
     */
    public JVelocitaView() {
        initComponents();
    }
    
    JVelocitaView(double[] V1, double[] V2, double[] V3, double spz, double spz_in) {
        initComponents();
        V1A = V1;
        V2A = V2;
        V3A = V3;
        spaz = spz;
        spaz_in = spaz_in;
        this.repaint();
    }
    
    public void paintComponent(Graphics g) {
        
        
        super.paintComponent(g);
        drawAssi(g);
        drawPunti(g);

        //disegnaSegnale(g);
        //disegnaPrimoA(g);

    }
    
    private void drawAssi(Graphics g) {
        //      FirstBrakeList ArrFB = proj.stesa.get(0);
        int ymax = this.getHeight();
        int xmax = this.getWidth();
        int nchanel = xmax / (V1A.length - 1);
        int ychn = (int) (ymax - (0 * nchanel)) / nchanel;
        int xshf = nchanel;
        g.setColor(Color.white);
        g.drawLine((int) (xshf * 0.75), (int) ((margUp * ymax)), (int) (xshf * 0.75), (int) (ymax - (margUp * ymax)));
        g.drawLine(xmax - (int) (xshf * 0.75), (int) ((margUp * ymax)), xmax - (int) (xshf * 0.75), (int) (ymax - (margUp * ymax)));
        g.drawLine((int) (xshf * 0.75), (int) (ymax - (margUp * ymax)), xmax - (int) (xshf * 0.75), (int) (ymax - (margUp * ymax)));
        g.drawLine((int) (xshf * 0.75), (int) ((margUp * ymax)), xmax - (int) (xshf * 0.75), (int) ((margUp * ymax)));
    }
    
    private void drawPunti(Graphics g) {
        double ystp = 0.0;
        // FirstBrakeList ArrFB = proj.stesa.get(0);
        int ymax = this.getHeight();
        //ymax=(int)(ymax- 2*(margUp * ymax));
        int xmax = this.getWidth();
        int nchanel = xmax / (V1A.length - 1);
        //int ychn = (int) (ymax - (0 * nchanel)) / nchanel;
        int xshf = nchanel;
        Graphics2D g2 = (Graphics2D) g.create();
        
        
        double max = -999;



        //     g.drawString("ZG 2", 20, 60);

        for (int i = 0; i < V1A.length - 2; i++) {
            if (V1A[i] > max) {
                max = V1A[i];
            }
            if (V2A != null) {
                if (V2A[i] > max) {
                    max = V2A[i];
                }
            }
            if (V3A != null) {
                if (V3A[i] > max) {
                    max = V3A[i];
                }
            }
        }
        
        g2.setColor(Color.white);
        ystp = (ymax - (2 * margUp * ymax) - 30) / max;
        //  double[][] data = new double[tV1.length - 2][2];
        //  double[][] data2 = new double[tV1.length - 2][2];

        for (int i = 0; i < V1A.length - 2; i++) {
            g2.setColor(Color.green);
            g2.drawRect(i * nchanel + xshf - 2, (int) (ymax - (V1A[i] * ystp)-(margUp * ymax)) - 15 + 2, 4, 4);
            if (V2A != null) {
                g2.setColor(Color.CYAN);
                g2.drawRect(i * nchanel + xshf - 2, (int) (ymax - (V2A[i] * ystp)-(margUp * ymax)) - 15 + 2, 4, 4);
            }
            
            if (V3A != null) {
                g2.setColor(Color.orange);
                g2.drawRect(i * nchanel + xshf - 2, (int) (ymax - (V3A[i] * ystp)) - 15 + 2, 4, 4);
            }
            
        }
        drawtick(g, max, xmax, ymax);
        
    }
    
    public void drawtick(Graphics g, double max, int w, int h) {
        double ystp = 0.0;
        //   FirstBrakeList ArrFB = proj.stesa.get(0);
        int ymax = h;//this.getHeight();
        int xmax = w;//this.getWidth();
        int nchanel = xmax / V1A.length;
        int ychn = (int) (ymax - (0 * nchanel)) / nchanel;
        int xshf = nchanel;
        int majTic = 2, minTic = 1;
        Graphics2D g2 = (Graphics2D) g.create();
        
        ystp = (ymax - (2.3 * margUp * ymax) - 30) / max;
        
        if (max / 10.0 < 1) {
            majTic = 2;
            minTic = 1;
            
        } else if (max / 10.0 < 2) {
            majTic = 3;
            minTic = 1;
        } else if (max / 10.0 < 3) {
            majTic = 5;
            minTic = 2;
        }
        
        if (!false) {
            g2.setColor(Color.white);
        } else {
            g2.setColor(Color.black);
        }

        //  g2.setColor(Color.white);

        FontMetrics fontMetrics = g2.getFontMetrics();
        
        
        int height = fontMetrics.getHeight();
        
        
        for (int i = 0; i < max; i = i + minTic) {
            if (i % majTic == 0) {
                g2.drawLine((int) (xshf * 0.75), ymax - (int) (i * ystp + (margUp * ymax)), (int) (xshf * 0.6), ymax - (int) (i * ystp + (margUp * ymax)));
                int width = fontMetrics.stringWidth("" + (i));
                
                g2.drawString("" + (i), (int) (xshf * 0.55) - width, ymax - (int) (i * ystp + (margUp * ymax) - (height / 3)));
                g2.drawLine((int) (xmax - xshf * 0.75), ymax - (int) (i * ystp + (margUp * ymax)), (int) (xmax - xshf * 0.6), ymax - (int) (i * ystp + (margUp * ymax)));
                g2.setColor(Color.GRAY);
                float dash1[] = {10.0f};
                BasicStroke dashed = new BasicStroke(1.0f,
                        BasicStroke.CAP_BUTT,
                        BasicStroke.JOIN_MITER,
                        10.0f, dash1, 0.0f);
                
                g2.setStroke(dashed);
                
                g2.drawLine((int) (xshf * 0.75), ymax - (int) (i * ystp + (margUp * ymax)), (int) xmax - (int) (xshf * 0.75), ymax - (int) (i * ystp + (margUp * ymax)));
                
                
                
            } else {
                g2.drawLine((int) (xshf * 0.75), ymax - (int) (i * ystp + (margUp * ymax)), (int) (xshf * 0.65), ymax - (int) (i * ystp + (margUp * ymax)));
                g2.drawLine((int) (xmax - xshf * 0.75), ymax - (int) (i * ystp + (margUp * ymax)), (int) (xmax - xshf * 0.65), ymax - (int) (i * ystp + (margUp * ymax)));
            }
        }
        
        
        for (int i = 0; i < V1A.length - 1; i++) {
            
            g2.drawLine((int) (xshf + (nchanel * i)), (int) ((margUp * ymax)), (int) (xshf + (nchanel * i)), (int) ((margUp * ymax) * 0.85));
            if (i % 2 == 0) {
                g2.setColor(Color.GRAY);
                float dash1[] = {10.0f};
                BasicStroke dashed = new BasicStroke(1.0f,
                        BasicStroke.CAP_BUTT,
                        BasicStroke.JOIN_MITER,
                        10.0f, dash1, 0.0f);
                
                g2.setStroke(dashed);
                g2.drawLine((int) (xshf + (nchanel * i)), (int) ((margUp * ymax)), (int) (xshf + (nchanel * i)), (int) (ymax - (margUp * ymax)));
                
            }
            
            
            int width = fontMetrics.stringWidth("" + (i * spaz + spaz_in));
            
            g2.drawString("" + (i * spaz + spaz_in), (int) (xshf + (nchanel * i) - (width / 2)), (int) ((margUp * ymax) * 0.83));
        }
        
        
        
        
        
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    double[] V1A, V2A, V3A;
    private double margUp = 0.07;
    double spaz, spaz_in;

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
