/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.vs30.myeditor;

/**
 *
 * @author PC
 */

/*
 * Copyright (c) 2009, 2014, Simone Pittaluga and/or its affiliates. All rights reserved.
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
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;

import org.myorg.myapi.DrawingAPI;


/* Rule.java is used by ScrollDemo.java. */
public class Rule extends JComponent {

    public static final int INCH = Toolkit.getDefaultToolkit().getScreenResolution();
    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;
    public static final int SIZE = 35;
    public int orientation;
    public boolean isMetric;
    private int increment;
    private int units;
    private double time_step;
    private int lenght;
    private double sampleInterval;
    private boolean is_white;
    private double step_geofoni;
    private double inter_geof;
    private double pos_0;

    public Rule(int o, boolean m) {
        orientation = o;
        isMetric = m;
        setIncrementAndUnits();
    }

    public void setIsMetric(boolean isMetric) {
        this.isMetric = isMetric;
        setIncrementAndUnits();
        repaint();
    }

    private void setIncrementAndUnits() {
        if (isMetric) {
            units = (int) ((double) INCH /  2.54); // dots per centimeter
            increment = units;
        } else {
            units = INCH;
            increment = units / 2;
        }
    }

    public boolean isMetric() {
        return this.isMetric;
    }

    public int getIncrement() {
        return increment;
    }

    public void setPreferredHeight(int ph) {
        setPreferredSize(new Dimension(SIZE, ph));
    }

    public void setPreferredWidth(int pw) {
        setPreferredSize(new Dimension(pw, SIZE));
    }

    protected void paintComponent(Graphics g) {
        Rectangle drawHere = g.getClipBounds();

        BufferedImage im = (BufferedImage) this.createImage(this.getWidth(), this.getHeight());

        Graphics offg = im.getGraphics();

        // Fill clipping area with dirty brown/orange.
        if (this.orientation == VERTICAL) {
//            g.setColor(new Color(230, 125, 20));
            if (is_white) {
                offg.setColor(Color.WHITE);
            } else {
                offg.setColor(Color.BLACK);
            }
        } else {
            //   offg.setColor(new Color(20, 220, 125));
            if (is_white) {
                offg.setColor(Color.WHITE);
            } else {
                offg.setColor(Color.BLACK);
            }
        }
        this.getHeight();
        offg.fillRect(drawHere.x, drawHere.y, drawHere.width, drawHere.height);

        // Do the ruler labels in a small font that's black.
        offg.setFont(new Font("SansSerif", Font.PLAIN, 10));
        offg.setColor(Color.black);

        // Some vars we need.
        int end = 0;
        int start = 0;
        int tickLength = 0;
        String text = null;

        // Use clipping bounds to calculate first and last tick locations.
        if (orientation == HORIZONTAL) {
            start = (drawHere.x / increment) * increment;
            end = (((drawHere.x + drawHere.width) / increment) + 1)
                    * increment;
        } else {
//            start = (drawHere.y / increment) * increment;
            start = (int) ((drawHere.y / time_step) * time_step);
            end = (int) ((((drawHere.y + drawHere.height) / time_step) + 1)
                    * time_step);
//            end = (((drawHere.y + drawHere.height) / increment) + 1)
//                    * increment;
        }

        // Make a special case of 0 to display the number
        // within the rule and draw a units label.
        if (start == 0) {
            text = Integer.toString(0) + (isMetric ? " cm" : " in");
            tickLength = 10;
            if (orientation == HORIZONTAL) {
                //            offg.drawLine(0, SIZE - 1, 0, SIZE - tickLength - 1);
                //            offg.drawString(text, 2, 21);
            } else {
//                g.drawLine(SIZE - 1, 0, SIZE - tickLength - 1, 0);
//                g.drawString(text, 9, 10);
            }
            text = null;
            start = increment;
        }

        // ticks and labels
        for (int i = start; i < end; i += increment) {
            if (i % units == 0) {
                tickLength = 10;
                text = Integer.toString(i / units);
            } else {
                tickLength = 7;
                text = null;
            }

            if (tickLength != 0) {
                if (orientation == HORIZONTAL) {
                    //            offg.drawLine(i, SIZE - 1, i, SIZE - tickLength - 1);
                    if (text != null) {
                        //                  offg.drawString(text, i - 3, 21);
                    }
                } else {
//                    g.drawLine(SIZE - 1, i, SIZE - tickLength - 1, i);
//                    if (text != null) {
//                        g.drawString(text, 9, i + 3);
//                    }
                }
            }
        }
        if (orientation != HORIZONTAL) {
            drawAssi(offg);
        } else {
            drawX(offg);
        }
        g.drawImage(im, 0, 0, this);
    }

    void setVertInterv(double stepT, int lungh, double sampleinterval) {
        this.time_step = stepT;
        this.lenght = lungh;
        this.sampleInterval = sampleinterval;
    }

    private void drawAssi(Graphics g) {
        try {

            Graphics2D g2 = (Graphics2D) g;
            final BasicStroke asse = new BasicStroke(0.0f);
            g2.setStroke(asse);


            /*    if (!is_white) {
             g2.setColor(Color.WHITE);
             } else {
             g2.setColor(Color.BLACK);
             }*/
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
            if (is_white) {
                g2.setColor(Color.black);
            } else {
                g2.setColor(Color.white);
            }


            g2.setFont(new Font("Dialog", Font.PLAIN, 12));
            // new Font("Dialog", Font.PLAIN, 12);

            FontMetrics fontMetrics = g2.getFontMetrics();


            int height = fontMetrics.getHeight();




            //FontMetrics fontMetrics = g2.getFontMetrics();


            //int height = fontMetrics.getHeight();


            for (int i = 0; i < max; i = i + minTic) {
                if (i % majTic == 0) {
                    int x1 =  35;

                    int y1 = (int) (i * stepT / sampleInterval) / 1000;

                    int x2 =  35 - 8;
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

    void isWhite(boolean _white) {
        is_white = _white;
    }

    void setHorPar(double stepCh) {
        step_geofoni = stepCh;

    }

    private void drawX(Graphics g) {
        try {
            DrawingAPI dapi = new DrawingAPI();
            Graphics2D g2 = (Graphics2D) g;
            final BasicStroke asse = new BasicStroke(0.0f);
            g2.setStroke(asse);
            if (is_white) {
                g2.setColor(Color.black);
            } else {
                g2.setColor(Color.white);
            }
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

    void setHorPar(double stepCh, double spaz_in, double spaz, int len) {
        step_geofoni = stepCh;
        pos_0 = spaz_in;
        inter_geof = spaz;
        lenght = len;
    }
}