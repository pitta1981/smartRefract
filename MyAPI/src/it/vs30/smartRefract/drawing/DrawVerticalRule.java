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
 * DrawVerticalRule
 *
 * Utility class responsible for drawing a vertical ruler (time axis) on a
 * Graphics surface. The ruler draws major and minor tick marks and numeric
 * labels for major ticks. Time values are expected to be represented in
 * milliseconds when computing positions.
 *
 * Usage contract:
 * - setVertInterv(stepT, length, sampleInterval) must be called before drawing
 * - drawAssi(Graphics) renders the ruler along the left edge (x = 0) of the
 *   provided Graphics context
 *
 * Inputs / units:
 * - stepT (internal computation): derived pixel distance between samples on the
 *   vertical axis (pixels per sample index)
 * - sampleInterval: original sample interval in seconds (the code multiplies
 *   by 1000 to convert to milliseconds when computing `max`)
 * - max: total time span in milliseconds = sampleInterval * 1000 * lenght
 *
 * Behavior / outputs:
 * - draws a vertical main line from y=0 to y=getHeight()
 * - draws tick marks: long ticks for major intervals and short ticks for
 *   minor intervals. Major ticks receive numeric labels (time in ms)
 *
 * Edge cases:
 * - if `lenght` or `sampleInterval` are zero, the method will effectively draw
 *   only the main vertical line and skip ticks (guarded by loop conditions)
 * - this class does not perform clipping; drawing occurs relative to the
 *   provided Graphics context and its coordinate system
 */
public class DrawVerticalRule {
    // Pixel distance between successive samples on the vertical axis is derived
    // when drawing; stored here for API symmetry with the horizontal ruler.
    private double time_step;
    private int lenght;
    private double sampleInterval; // in seconds
    private int width;
    private int height;

    public DrawVerticalRule(){
    
    }

    /**
     * Get the current drawing height (pixels).
     */
    public int getHeight() {
        return height;
    }

    /**
     * Set the drawing height (pixels).
     * @param h height in pixels
     */
    public void setHeight(int h) {
        height = h;
    }

    /**
     * Set the drawing width (pixels).
     * @param w width in pixels
     */
    public void setWidth(int w) {
        width = w;
    }

    /**
     * Get the drawing width (pixels).
     */
    public int getWidth() {
        return width;
    }

    /**
     * Set vertical interval parameters used by the ruler.
     *
     * @param stepT unused here directly; kept for API compatibility (pixels per sample)
     * @param lungh number of samples (length)
     * @param sampleinterval sample interval in seconds
     */
    public void setVertInterv(double stepT, int lungh, double sampleinterval) {
        this.time_step = stepT;
        this.lenght = lungh;
        this.sampleInterval = sampleinterval;
    }

    /**
     * Draw the vertical ruler (time axis) on the provided Graphics context.
     *
     * Behavior details:
     * - Computes the total time span `max` in milliseconds: sampleInterval * 1000 * lenght
     * - Determines major and minor tick spacing (`majTic` and `minTic`) based on
     *   `max` to create readable intervals
     * - For each tick index `i`, computes the pixel Y-position and draws either
     *   a major tick (long + label) or a minor tick (short)
     *
     * @param g Graphics context where the ruler will be painted; the ruler draws
     *          along the left edge (x = 0)
     */
    public void drawAssi(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        final BasicStroke asse = new BasicStroke(0.0f);
        g2.setStroke(asse);


        /*    if (!is_white) {
         g2.setColor(Color.WHITE);
         } else {
         g2.setColor(Color.BLACK);
         }*/
        g2.setColor(Color.BLACK);
        // Pixel step between samples on Y (pixels per sample index)
        // Prefer the explicitly set time_step (api compatibility), otherwise
        // compute a fallback step based on height and sample count.
        double stepT;
        if (this.time_step > 0) {
            stepT = this.time_step;
        } else {
            stepT = (double) (this.getHeight()) / (double) (this.lenght);
        }
         // Total time span in milliseconds
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

        FontMetrics fontMetrics = g2.getFontMetrics();
        int fontHeight = fontMetrics.getHeight();

        // Disegna il righello verticale con le tacche
        for (int i = 0; i < max; i = i + minTic) {
            double yPos = (i / (sampleInterval * 1000)) * stepT;
            
            if (i % majTic == 0) {
                // Tacca maggiore
                g2.drawLine(0, (int) yPos, 10, (int) yPos);
                
                // Etichetta per la tacca maggiore (time in ms)
                String label = String.valueOf(i);
                int labelWidth = fontMetrics.stringWidth(label); // measured to avoid overflow
                int labelX = 12;
                // If the label would overflow the available width, shift it left
                if (labelX + labelWidth > this.getWidth()) {
                    labelX = Math.max(2, this.getWidth() - labelWidth - 2);
                }
                g2.drawString(label, labelX, (int) yPos + (fontHeight / 3));
            } else {
                // Tacca minore
                g2.drawLine(0, (int) yPos, 5, (int) yPos);
            }
        }
        
        // Disegna la linea verticale principale del righello
        g2.drawLine(0, 0, 0, this.getHeight());
    }
}

