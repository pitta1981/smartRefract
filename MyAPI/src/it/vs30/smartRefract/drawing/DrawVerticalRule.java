/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.vs30.smartRefract.drawing;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 *
 * @author PC
 */
public class DrawVerticalRule {
    private double time_step;
    private int lenght;
    private double sampleInterval;
    private int width;
    private int height;

    public DrawVerticalRule(){
    
    }
    
    public int getHeight() {
        return height;
    }

    public void setHeight(int h) {
        height = h;
    }

    public void setWidth(int w) {
        width = w;
    }

    public int getWidth() {
        return width;
    }
    
    public void setVertInterv(double stepT, int lungh, double sampleinterval) {
        this.time_step = stepT;
        this.lenght = lungh;
        this.sampleInterval = sampleinterval;
    }
    
    public void drawAssi(Graphics g) {
        try {

            Graphics2D g2 = (Graphics2D) g;
            final BasicStroke asse = new BasicStroke(0.0f);
            g2.setStroke(asse);


            /*    if (!is_white) {
             g2.setColor(Color.WHITE);
             } else {
             g2.setColor(Color.BLACK);
             }*/
            g2.setColor(Color.BLACK);
            int j = 0;
            //double maxV = obj.tr[j].getMaxValue();
            double stepT = (double) (this.getHeight()) / (double) (this.lenght);
            double max = sampleInterval * 1000 * this.lenght;
            //this.rView.setVertInterv(stepT*obj.tr[0].sampleInterval * 1000);
            //double stepCh = (this.getWidth() - 2 * margine_X) / obj.tr.length;
            //double stepV = (double) stepCh / (double) maxV;
            //int xshf = (int) (margine_X * 0.75);



            //  " tempo: " + (int) (evt.getY() * (1 / stepT)) * obj.tr[0].sampleInterval * 1000


            int majTic = 0, minTic = 0;


            //ystp = (ymax - (2 * 45) - 30) / max;
            // double ystp = stepT;

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


            //g2.setColor(Color.white);
  /*          if (is_white) {
                g2.setColor(Color.black);
            } else {
                g2.setColor(Color.white);
            }
*/

            g2.setFont(new Font("Dialog", Font.PLAIN, 12));
            // new Font("Dialog", Font.PLAIN, 12);

            FontMetrics fontMetrics = g2.getFontMetrics();


            int height = fontMetrics.getHeight();




            //FontMetrics fontMetrics = g2.getFontMetrics();


            //int height = fontMetrics.getHeight();


            for (int i = 0; i < max; i = i + minTic) {
                if (i % majTic == 0) {
                    int x1 = (int) 35;

                    int y1 = (int) (i * stepT / sampleInterval) / 1000;

                    int x2 = (int) 35 - 8;
                    int y2 = (int) (i * stepT / sampleInterval) / 1000;

                    g2.drawLine(x1, y1, x2, y2);


                    int width = fontMetrics.stringWidth("" + (i));

                    g2.drawString("" + (i), (int) x2 - width, y2 + (height / 3));

                } else {

                    int x1 = (int) 35;

                    int y1 = (int) (i * stepT / sampleInterval) / 1000;

                    int x2 = (int) 35 - 5;
                    int y2 = (int) (i * stepT / sampleInterval) / 1000;

                    g2.drawLine(x1, y1, x2, y2);

                    //   g2.drawLine((int) (xshf * 0.75), ymax - (int) (i * ystp + (margUp * ymax)), (int) (xshf * 0.65), ymax - (int) (i * ystp + (margUp * ymax)));
                    //   g2.drawLine((int) (xmax - xshf * 0.75), ymax - (int) (i * ystp + (margUp * ymax)), (int) (xmax - xshf * 0.65), ymax - (int) (i * ystp + (margUp * ymax)));
                }


            }
        } catch (Exception ex) {
        }

    }



}
