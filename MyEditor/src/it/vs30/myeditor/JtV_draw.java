/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * JtV_draw.java
 *
 * Created on 2-feb-2012, 19.52.44
 */
package it.vs30.myeditor;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import org.myorg.myapi.FirstBrakeList;

/**
 *
 * @author user
 */
public class JtV_draw extends javax.swing.JPanel {

    private double margUp = 0.07;
    double[] tV1;
    double[][] tV1A;// = new double[][];
    double[][] tV2A = new double[6][];
    int sel1 = 0;
    int sel2 = 0;
    double[] tV2;
    double spaz;
    int[] bp;
    int act_bp = 1;
    Color[] colorList = {Color.green, Color.CYAN, Color.ORANGE, Color.yellow, Color.BLUE, Color.RED, new Color(0.0f, 0.4f, 0.4f)};

    /**
     * Creates new form JtV_draw
     */
    public JtV_draw() {
        initComponents();
        this.setBackground(Color.BLACK);
    }

    public JtV_draw(double p_spaz) {
        initComponents();
        this.setBackground(Color.BLACK);
    }

    public JtV_draw(double[] p_tV, double[] p_tV2, double p_spaz) {
        initComponents();
        tV1 = p_tV;
        tV2 = p_tV2;
        spaz = p_spaz;
        this.setBackground(Color.BLACK);
        bp = new int[(int) (tV1.length / 3.0)];
        for (int i = 0; i < bp.length; i++) {
            //  g2.drawRect(i*nchanel+xshf-2, (int)(ymax-(tV[i]*ystp))-30+2, 4, 4);
            bp[i] = -999;
        }
        bp[0] = 0;


    }

    @Override
    public void paintComponent(Graphics g) {


        super.paintComponent(g);
        try {
            drawAssi(g);
            drawPunti(g);
        } catch (Exception ex) {
            System.out.println("Eccezione disegno Tv: " + ex.getMessage());
        }
        //disegnaSegnale(g);
        //disegnaPrimoA(g);

    }

    private void drawAssi(Graphics g) {
        //      FirstBrakeList ArrFB = proj.stesa.get(0);
        int ymax = this.getHeight();
        int xmax = this.getWidth();
        int nchanel = 0;

        if (tV1 != null) {
            nchanel = xmax / (tV1.length - 1);
        } else {
            nchanel = xmax / (tV2.length - 1);
        }

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
        int xmax = this.getWidth();

        int nchanel = 0;

        if (tV1 != null) {
            nchanel = xmax / (tV1.length - 1);
        } else {
            nchanel = xmax / (tV2.length - 1);
        }


        int ychn = (int) (ymax - (0 * nchanel)) / nchanel;
        int xshf = nchanel;
        Graphics2D g2 = (Graphics2D) g.create();


        double max = -999;



        //     g.drawString("ZG 2", 20, 60);
        for (int j = 0; j <( tV1A != null ? tV1A.length  : tV2A.length) ; j++) {
            for (int i = 0; i < (tV1 != null ? tV1.length  : tV2.length ); i++) {
                if (tV1 != null) {
                    if (tV1[i] > max) {
                        max = tV1[i];
                    }
                    if (tV1A[j][i]+j*15 > max) {
                        max = tV1A[j][i]+j*15;
                    }
                }

                if (tV2 != null) {
                    if (tV2[i] > max) {
                        max = tV2[i];
                    }
                    if (tV2A[j][i]+j*15 > max) {
                        max = tV2A[j][i]+j*15;
                    }


                }
            }
        }

        g2.setColor(Color.white);
        ystp = (ymax - (2.3 * margUp * ymax) - 30) / max;
        int len = (tV1 != null ? tV1.length : tV2.length);
        double[][] data = new double[len - 2][2];
        double[][] data2 = new double[len - 2][2];
        for (int j = 0; j < (tV1 != null ? tV1A.length  : tV2A.length ); j++) {

            if (tV1 != null) {


                for (int i = 0 + (j); i < len - 2 - j; i++) {
                    g2.setColor(colorList[j]);
                    g2.drawRect(i * nchanel + xshf - 2, (int) (ymax - ((tV1A[j][i]+j*15) * ystp)) - 30 - 2, 4, 4);
                    data[i][0] = i * this.spaz;
                    data[i][1] = tV1A[sel1][i]+sel1*15;
                }
                //     g2.drawRect(i * nchanel + xshf - 2, (int) (ymax - (tV1[i] * ystp)) - 30 + 2, 4, 4);

            }


            if (tV2 != null) {

                //       g2.drawRect(i * nchanel + xshf - 2, (int) (ymax - (tV2[i] * ystp)) - 30 + 2, 4, 4);
                for (int i = 0 + j; i < len - 2 - j; i++) {

                    g2.setColor(colorList[j]);
                    g2.drawRect(i * nchanel + xshf - 2, (int) (ymax - ((tV2A[j][i]+j*15) * ystp)) - 30 - 2, 4, 4);
                    data2[i][0] = i * this.spaz;
                    data2[i][1] = tV2A[sel2][i]+sel2*15;
                }





            }


        }
        // Regressione non calcolabile (meno di due punti, o punti allineati verticalmente):
        // la retta va semplicemente omessa, non deve interrompere il disegno del pannello.
        if (tV1 != null) {
            try {
                double[] result = FirstBrakeList.getOLSRegression(data);

                g2.drawLine(xshf, (int) ((ymax - (result[1] * data[0][0] + result[0]) * ystp) - 30), xmax - xshf, (int) (ymax - ((result[1] * data[tV1.length - 3][0] + result[0]) * ystp) - 30));
            } catch (IllegalArgumentException ex) {
            }
        }
        if (tV2 != null) {
            try {
                double[] result2 = FirstBrakeList.getOLSRegression(data2);
                g2.drawLine(xshf, (int) ((ymax - (result2[1] * data2[0][0] + result2[0]) * ystp) - 30), xmax - xshf, (int) (ymax - ((result2[1] * data2[tV2.length - 3][0] + result2[0]) * ystp) - 30));
            } catch (IllegalArgumentException ex) {
            }
        }
        g2.setColor(Color.yellow);
        for (int j = 1; j < bp.length; j++) {
            if (bp[j] > 0) {

                data = new double[bp[j] - bp[j - 1] + 1][2];
                for (int i = 0; i < bp[j] - bp[j - 1] + 1; i++) {

                    data[i][0] = i * this.spaz;
                    data[i][1] = tV1[bp[j - 1] + i];

                }
                try {
                    double[] result = FirstBrakeList.getOLSRegression(data);

                    g2.drawLine(bp[j - 1] * nchanel + xshf, (int) ((ymax - (result[1] * data[0][0] + result[0]) * ystp) - 30), (bp[j]) * nchanel + xshf, (int) (ymax - ((result[1] * data[data.length - 1][0] + result[0]) * ystp) - 30));
                } catch (Exception ex) {
                }
            }
        }



    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                formMouseReleased(evt);
            }
        });
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                formMouseDragged(evt);
            }
        });

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

private void formMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseDragged
// TODO add your handling code here:

    Graphics g = this.getGraphics();
    int xmax = this.getWidth();
    int nchanel = xmax / (tV1.length - 1);

    BufferedImage im = (BufferedImage) this.createImage(this.getWidth(), this.getHeight());

    Graphics offg = im.getGraphics();
    super.paintComponent(offg);
    drawAssi(offg);
    drawPunti(offg);

    offg.setColor(Color.GREEN);
    int bp1 = (int) (evt.getX() / nchanel);
    offg.drawString(evt.getX() + " " + evt.getY() + " " + bp1, 5, 15);

    drawDrag(offg, evt.getX(), evt.getY());

    g.drawImage(im, 0, 0, this);


}//GEN-LAST:event_formMouseDragged

private void formMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseReleased
    double ystp = 0.0;
    // FirstBrakeList ArrFB = proj.stesa.get(0);
    int ymax = this.getHeight();
    int xmax = this.getWidth();
    int nchanel = xmax / (tV1.length - 1);
    //   int ychn = (int) (ymax - (0 * nchanel)) / nchanel;
    //   int xshf = nchanel;
    //Graphics2D g2 = (Graphics2D) offg.create();
    bp[0] = 0;
    bp[act_bp] = (int) (evt.getX() / nchanel) - 1;
    act_bp++;

}//GEN-LAST:event_formMouseReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    private void drawDrag(Graphics offg, int x, int y) {

        double ystp = 0.0;
        // FirstBrakeList ArrFB = proj.stesa.get(0);
        int ymax = this.getHeight();
        int xmax = this.getWidth();
        int nchanel = xmax / (tV1.length - 1);
        int ychn =  (ymax - (0 * nchanel)) / nchanel;
        int xshf = nchanel;
        Graphics2D g2 = (Graphics2D) offg.create();
        int bp1 = (x / nchanel) - 1;
        double[][] data = new double[bp1 + 1][2];

        double max = 0.0;
        for (int i = 0; i < tV1.length - 2; i++) {
            if (tV1[i] > max) {
                max = tV1[i];
            }
        }

        //g2.setColor(Color.white);
        ystp = (ymax - (2.3 * margUp * ymax) - 30) / max;


        for (int i = 0; i < bp1 + 1; i++) {
            //  g2.drawRect(i*nchanel+xshf-2, (int)(ymax-(tV[i]*ystp))-30+2, 4, 4);
            data[i][0] = i * this.spaz;
            data[i][1] = tV1[i];

        }
        try {
            double[] result = FirstBrakeList.getOLSRegression(data);

            g2.drawLine(xshf, (int) ((ymax - (result[1] * data[0][0] + result[0]) * ystp) - 30), bp1 * nchanel + xshf, (int) (ymax - ((result[1] * data[bp1][0] + result[0]) * ystp) - 30));
        } catch (Exception ex) {
        }
        g2.setColor(Color.yellow);
        for (int j = 1; j < bp.length; j++) {
            if (bp[j] > 0) {

                data = new double[bp[j] - bp[j - 1] + 1][2];
                for (int i = 0; i < bp[j] - bp[j - 1] + 1; i++) {

                    data[i][0] = i * this.spaz;
                    data[i][1] = tV1[bp[j - 1] + i];

                }
                try {
                    double[] result = FirstBrakeList.getOLSRegression(data);

                    g2.drawLine(bp[j - 1] * nchanel + xshf, (int) ((ymax - (result[1] * data[0][0] + result[0]) * ystp) - 30), (bp[j]) * nchanel + xshf, (int) (ymax - ((result[1] * data[data.length - 1][0] + result[0]) * ystp) - 30));
                } catch (Exception ex) {
                }
            }
        }
    }
    /*  public void drawtick(Graphics g, double max, int w, int h) {
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
        
        
        
        
        
     }*/
}
