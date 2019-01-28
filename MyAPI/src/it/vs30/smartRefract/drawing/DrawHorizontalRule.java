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
import org.myorg.myapi.DrawingAPI;

/**
 *
 * @author PC
 */
public class DrawHorizontalRule {

    private double step_geofoni;
    private double pos_0;
    private double inter_geof;
    private int lenght;

    public DrawHorizontalRule() {

    }

    public void setHorPar(double stepCh, double spaz_in, double spaz, int len) {
        step_geofoni = stepCh;
        pos_0 = spaz_in;
        inter_geof = spaz;
        lenght = len;
    }
    
    public void drawX(Graphics g) {
        try {
            DrawingAPI dapi = new DrawingAPI();
            Graphics2D g2 = (Graphics2D) g;
            final BasicStroke asse = new BasicStroke(0.0f);
            g2.setStroke(asse);
       /*     if (is_white) {
                g2.setColor(Color.black);
            } else {
                g2.setColor(Color.white);
            }*/
            g2.setColor(Color.BLACK);
            //g.drawLine(0, 10, this.getWidth(), 10);
            double lung_meter = (this.lenght - 1) * this.inter_geof;
            double m_X_pixel = ((this.lenght - 1) * this.step_geofoni) / lung_meter;
            int minTic = 0;
            int majTic = 0;

            if (lung_meter / 10.0 < 1.5) {
                majTic = 2;
                minTic = 1;

            } else if (lung_meter / 10.0 < 2.5) {
                majTic = 3;
                minTic = 1;
            } else if (lung_meter / 10.0 < 5) {
                majTic = 5;
                minTic = 1;
            } else if (lung_meter / 10.0 < 10) {
                majTic = 10;
                minTic = 2;
            } else if (lung_meter / 10.0 < 30) {
                majTic = 20;
                minTic = 5;
            } else if (lung_meter / 10.0 < 50) {
                majTic = 25;
                minTic = 5;
            } else if (lung_meter / 10.0 < 100) {
                majTic = 30;
                minTic = 10;
            } else if (lung_meter / 10.0 >= 100) {
                majTic = 50;
                minTic = 25;
            }


            if (this.step_geofoni > 0) {
                /*   for (int i=0;i<lung_meter;i=i+minTic){
                 if(i%majTic==0){
                 int x0=(int)(i*m_X_pixel+(this.step_geofoni/2));
                 g2.drawLine((int) x0, 10, (int) x0, 20);
                    
                 }
                 else{
                 int x0=(int)(i*m_X_pixel+(this.step_geofoni/2));
                 g2.drawLine((int) x0, 10, (int) x0, 15);
                 }
                    
                 }*/

                g2.setFont(new Font("Lucida", Font.PLAIN, 11));
                FontMetrics fontMetrics = g2.getFontMetrics();


                int font_height = fontMetrics.getHeight();
                
                
                for (int i = 0; i < this.lenght; i++) {
                    int x0 = (int) (this.step_geofoni / 2 + i * this.step_geofoni);

                    int font_width = fontMetrics.stringWidth("G"+(i+1));
                    if (this.step_geofoni < 40) {
                        if (i % 3 == 0) {
                            g2.drawString("G" + (i + 1), x0-(font_width/2), 15);
                        }
                    } else {
                        g2.drawString("G" + (i + 1), x0-(font_width/2), 15);

                    }



                    dapi.drawGeo(g, (int) x0, 20);
                }
            }
        } catch (Exception ex) {
        }


    }

    
}
