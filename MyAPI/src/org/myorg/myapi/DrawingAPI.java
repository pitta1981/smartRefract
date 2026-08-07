/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myorg.myapi;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.TexturePaint;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;

/**
 *
 * @author user
 */
public class DrawingAPI {

    public Indagine proj;
    public APIObject obj = null;
    private double margUp = 0.07;
    int VAR = 0;
    boolean ritorno = true;
    boolean andata = true;
    FirstBrakeList ArrFB;
    public int indi = 0, indj = 0;
    public boolean is_white = false;
    public int xshf;
    private double maxv;
    public BufferedImage bIm2, bIm1;
    public BufferedImage bIm3;
    boolean[][][] cell;
    private BufferedImage[] im = new BufferedImage[20];

    public double scaleX = 1.0;
    protected int height;
    protected int width;
    public int margine_alto = 35;
    public int margine_sx = 40;
    public int margine_dx = 40;

    

    public DrawingAPI() {
    
    }

    public DrawingAPI(Indagine par_proj, APIObject par_obj) {
        this.proj = par_proj;
        this.obj = par_obj;
    }

    // drawpick
    
    public void drawSeism(Graphics g, Trace[] tr) {
        int margine_X = 0;

        try {

            Path2D p = new Path2D.Double();
            int x1i = 0, y1i = 0;
            boolean mag = false;
            if (tr.length > 0) {
                /*      if (!is_white) {
                 this.setForeground(Color.white);
                 } else {
                 this.setForeground(Color.black);
                 }*/

                //g.drawString(" "+obj.tr[0].getSampleInterval()+" "+stepV+" "+stepT, 50, 50);
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(Color.black);
                    double stepT;
                    stepT = (double) (this.getHeight()) / (double) (tr[0].length);
                    double stepCh = (this.getWidth() - 2 * margine_X) / tr.length;
                   // double stepV = 0.5 * (double) stepCh / (double) maxV;
                
                for (int j = 0; j < tr.length; j++) {
                    double maxV;
                    /* if (avgMAX) {
                     maxV = obj.tr[j].getMediaAbs();
                     } else {*/
                    maxV = tr[j].getMaxValue() - tr[j].media;
                    //}

                  //  double stepT;
                  //  stepT = (double) (this.getHeight()) / (double) (tr[0].length);
                  //  double stepCh = (this.getWidth() - 2 * margine_X) / tr.length;
                    double stepV = 0.5 * (double) stepCh / (double) maxV;
                    if (!is_white) {
                        g2.setColor(Color.lightGray);
                    } else {
                        g2.setColor(Color.DARK_GRAY);
                    }
                    g2.drawLine((int) ((((j) * stepCh) + stepCh / 2)) + margine_X, 0, (int) ((((j) * stepCh) + stepCh / 2)) + margine_X, this.getHeight());

                    int x2 = 0;
                    int y2 = 0;
                    int x1 = 0;
                    //if (!this.avgMAX) {
                    maxV = maxV / this.scaleX;
                    // }

                    for (int i = 0; i < tr[0].length - 1; i++) {
                        //g2.drawRect((int) ((((j) * stepCh)+ stepCh / 2) + stepV * tr[j].value[i]), (int) (i * stepT), 1, 1);

                        if (Math.abs(tr[j].value[i]) < maxV) {
                            x1 = (int) ((((j) * stepCh) + stepCh / 2) + this.scaleX * stepV * (tr[j].value[i] - tr[j].media));
                        } else {
                            int s = (int) (Math.abs(tr[j].value[i]) / tr[j].value[i]);
                            x1 = (int) ((((j) * stepCh) + stepCh / 2) + s * this.scaleX * stepV * (maxV - tr[j].media));
                        }

                        int y1 = (int) (i * stepT);
                        if (Math.abs(tr[j].value[i + 1]) < maxV) {
                            x2 = (int) ((((j) * stepCh) + stepCh / 2) + this.scaleX * stepV * (tr[j].value[i + 1] - tr[j].media));

                        } else {
                            int s = (int) (Math.abs(tr[j].value[i]) / tr[j].value[i]);

                            x2 = (int) ((((j) * stepCh) + stepCh / 2) + s * this.scaleX * stepV * (maxV - tr[j].media));

                        }

                        //  x2 = (int) ((((j) * stepCh) + stepCh / 2) + this.scaleX * stepV * (tr[j].value[i + 1] - tr[j].media));
                        y2 = (int) ((i + 1) * stepT);
                        if (!is_white) {
                            g2.setColor(Color.white);

                        } else {
                            g2.setColor(Color.black);
                        }
                        g2.setColor(Color.black);
                        g2.drawLine(x1 + margine_X, y1, x2 + margine_X, y2);

                        if (tr[j].value[i + 1] - tr[j].media > 0) {

                            if (mag == false) {
                                //  Graphics2D g2 = (Graphics2D) g.create();
                                //     Path2D p2 = new Path2D.Double();
                                p = new Path2D.Double();

                                if (x2 != x1) {
                                    double m = (y2 * 1.0 - y1 * 1.0) / (x2 - x1);
                                    double q = ((x2 * y1) - (x1 * y2)) / (x2 - x1);
                                    double x = (((j) * stepCh) + stepCh / 2);
                                    int y = (int) (m * x + q);

                                    // p = new Path2D.Double();
                                    p.moveTo(x + margine_X, y);
                                    if (y < y1 && y < y2);

                                    x1i = (int) x;
                                    y1i = y;
                                } else {
                                    p.moveTo(x1 + margine_X, y1);
                                    x1i = (int) x1;
                                    y1i = y1;
                                }

                                mag = true;
                            } else {
                                p.lineTo(x1 + margine_X, y1); //curveTo(x1,y1, ystp * proj.Zg1[(proj.Zg1.length - 3)] + (margUp * ymax) + 30, (xshf + (nchanel * (proj.Zg1.length - 3))), ystp * proj.Zg1[(proj.Zg1.length - 3)] + (margUp * ymax) + 30);

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
                                //big.setColor(Color.lightGray);
                                //big.fillOval(0, 0, 5, 5);
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
                        //big.setColor(Color.lightGray);
                        //big.fillOval(0, 0, 5, 5);
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

        //throw new UnsupportedOperationException("Not yet implemented");
    }

    public void drawdr(Graphics g, double max, int w, int h) {
        int ymax = h;
        int xmax = w;
        ArrFB = proj.stesa.get(indj);
        int nchanel = xmax / (ArrFB.fb.length + 2);
        int ychn = (ymax - (5 * nchanel)) / nchanel;
        xshf = nchanel;
        int in0 = 0, fi0 = 0, in1 = 0, fi1 = 0;
        int yshf = (int) (margUp * ymax);
        int ystp = (int) ((ymax - ((2 * margUp) * ymax)) / max);

        for (int j = 0; j < proj.stesa.size(); j++) {
            FirstBrakeList fbl = proj.stesa.get(j);

            for (int i = 0; i < fbl.fb.length; i++) {
                if (fbl.scoppio < ((i) * fbl.spaz) + fbl.spaz_in) {

                    g.setColor(Color.DARK_GRAY);
                    if (andata) {
                        this.drawTriang(g, (i * nchanel) + xshf, ymax - (int) (fbl.fb[i].time * ystp) - yshf);
                    }

                } else {

                    g.setColor(Color.LIGHT_GRAY);
                    if (ritorno) {

                        this.drawCircle(g, (i * nchanel) + xshf, ymax - (int) (fbl.fb[i].time * ystp) - yshf);
                    }
                }

                // g.drawLine((i * nchanel) - 5 + xshf, ymax - (int) (fbl.fb[i].time * ystp) - yshf, (i * nchanel) + 5 + xshf, ymax - (int) (fbl.fb[i].time * ystp) - yshf);
            }
            try {
                if (andata) {
                    if (!fbl.strato1.equals("0-0")) {
                        String[] split = fbl.strato1.split("-");
                        in0 = Integer.parseInt(split[0]);
                        fi0 = Integer.parseInt(split[1]);

                        g.setColor(new Color(0.0f, 0.5f, 0.0f));

                        g.drawLine((in0 * nchanel) + xshf, ymax - (int) ((fbl.dromo[0].b * ((in0) * fbl.spaz + fbl.spaz_in) + fbl.dromo[0].a) * ystp) - yshf, (fi0 * nchanel) + xshf, ymax - (int) ((fbl.dromo[0].b * ((fi0) * fbl.spaz + fbl.spaz_in) + fbl.dromo[0].a) * ystp) - yshf);
                    }
                    if (!fbl.strato2.equals("0-0")) {
                        String[] split = fbl.strato2.split("-");
                        in0 = Integer.parseInt(split[0]);
                        fi0 = Integer.parseInt(split[1]);
                        g.setColor(new Color(0.2f, 0.5f, 0.5f));
                        g.drawLine((in0 * nchanel) + xshf, ymax - (int) ((fbl.dromo[1].b * ((in0) * fbl.spaz + fbl.spaz_in) + fbl.dromo[1].a) * ystp) - yshf, (fi0 * nchanel) + xshf, ymax - (int) ((fbl.dromo[1].b * ((fi0) * fbl.spaz + fbl.spaz_in) + fbl.dromo[1].a) * ystp) - yshf);
                    }
                    if (!fbl.strato3.equals("0-0")) {
                        String[] split = fbl.strato3.split("-");
                        in0 = Integer.parseInt(split[0]);
                        fi0 = Integer.parseInt(split[1]);
                        g.setColor(new Color(0.5f, 0.4f, 0.1f));
                        g.drawLine((in0 * nchanel) + xshf, ymax - (int) ((fbl.dromo[2].b * ((in0) * fbl.spaz + fbl.spaz_in) + fbl.dromo[2].a) * ystp) - yshf, (fi0 * nchanel) + xshf, ymax - (int) ((fbl.dromo[2].b * ((fi0) * fbl.spaz + fbl.spaz_in) + fbl.dromo[2].a) * ystp) - yshf);
                    }
                }
            } catch (Exception e1) {
            }
            try {
                if (ritorno) {

                    if (!fbl.strato1R.equals("0-0")) {
                        String[] split = fbl.strato1R.split("-");
                        in0 = Integer.parseInt(split[0]);
                        fi0 = Integer.parseInt(split[1]);
                        g.setColor(new Color(0.1f, 0.6f, 0.1f));
                        g.drawLine((in0 * nchanel) + xshf, ymax - (int) ((fbl.dromoR[0].b * ((in0) * fbl.spaz + fbl.spaz_in) + fbl.dromoR[0].a) * ystp) - yshf, (fi0 * nchanel) + xshf, ymax - (int) ((fbl.dromoR[0].b * ((fi0) * fbl.spaz + fbl.spaz_in) + fbl.dromoR[0].a) * ystp) - yshf);
                    }
                    if (!fbl.strato2R.equals("0-0")) {
                        String[] split = fbl.strato2R.split("-");
                        in0 = Integer.parseInt(split[0]);
                        fi0 = Integer.parseInt(split[1]);
                        g.setColor(new Color(0.4f, 0.65f, 0.65f));
                        g.drawLine((in0 * nchanel) + xshf, ymax - (int) ((fbl.dromoR[1].b * ((in0) * fbl.spaz + fbl.spaz_in) + fbl.dromoR[1].a) * ystp) - yshf, (fi0 * nchanel) + xshf, ymax - (int) ((fbl.dromoR[1].b * ((fi0) * fbl.spaz + fbl.spaz_in) + fbl.dromoR[1].a) * ystp) - yshf);
                    }

                    if (!fbl.strato3R.equals("0-0")) {
                        String[] split = fbl.strato3R.split("-");
                        in0 = Integer.parseInt(split[0]);
                        fi0 = Integer.parseInt(split[1]);
                        g.setColor(new Color(0.5f, 0.4f, 0.2f));
                        g.drawLine((in0 * nchanel) + xshf, ymax - (int) ((fbl.dromoR[2].b * ((in0) * fbl.spaz + fbl.spaz_in) + fbl.dromoR[2].a) * ystp) - yshf, (fi0 * nchanel) + xshf, ymax - (int) ((fbl.dromoR[2].b * ((fi0) * fbl.spaz + fbl.spaz_in) + fbl.dromoR[2].a) * ystp) - yshf);
                        //System.out.println("Line "+(in0*nchanel)+" " +(ymax-(int)(fbl.dromo[2].b*((in0-1)*fbl.spaz+fbl.spaz_in)+fbl.dromo[2].a)*ystp)+" "+(fi0*nchanel)+" "+(ymax-(int) (fbl.dromo[2].b*((fi0-1)*fbl.spaz+fbl.spaz_in)+fbl.dromo[2].a)*ystp)+" "+fbl.strato3+" "+fbl.dromo[2].b+" "+fbl.dromo[2].a);
                    }
                }
            } catch (Exception e1) {
            }

        }
    }

    public void drawDr(Graphics g, int w, int h, boolean print) {
        //g.drawLine(24, 0, 200, 200);
        ArrFB = proj.stesa.get(indj);
        int ymax = h;//this.getHeight();
        int xmax = w;

        //int ymax = this.getHeight();
        //int xmax = this.getWidth();
        int nchanel = xmax / (ArrFB.fb.length + 2);
        int ychn = (int) (ymax - (0 * nchanel)) / nchanel;
        int xshf = nchanel;
        if (obj.is_white) {
            // this.setBackground(Color.white);
            g.setColor(Color.black);
        } else {
            //  this.setBackground(Color.black);

            g.setColor(Color.white);
        }

        g.drawLine((int) (xshf * 0.75), (int) ((margUp * ymax)), (int) (xshf * 0.75), (int) (ymax - (margUp * ymax)));
        g.drawLine(xmax - (int) (xshf * 0.75), (int) ((margUp * ymax)), xmax - (int) (xshf * 0.75), (int) (ymax - (margUp * ymax)));
        g.drawLine((int) (xshf * 0.75), (int) (ymax - (margUp * ymax)), xmax - (int) (xshf * 0.75), (int) (ymax - (margUp * ymax)));
        g.drawLine((int) (xshf * 0.75), (int) ((margUp * ymax)), xmax - (int) (xshf * 0.75), (int) ((margUp * ymax)));

        int yshf = (int) (margUp * ymax);

        //ymax=ymax-(int)(2*margUp*ymax);
        double max = 0.00001;
        for (int j = 0; j < proj.stesa.size(); j++) {
            FirstBrakeList fbl = proj.stesa.get(j);
            for (int i = 0; i < fbl.fb.length - 1; i++) {
                if (max < (fbl.fb[i].time)) {
                    max = (int) (fbl.fb[i].time);
                }
            }
        }
        max = 1.05 * max;
        drawdr(g, max, w, h);
        drawtick(g, max, w, h);

        int ystp = (int) ((ymax - (2 * margUp * ymax)) / max);
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(2));
        //  Path2D p = new Path2D.Double();
        //  p.moveTo(0, 0);

        for (int i = 0; i < ArrFB.fb.length; i++) {
            if (ArrFB.scoppio < ((i) * ArrFB.spaz) + ArrFB.spaz_in) {
                //System.out.println("@ G"+i+" spaz "+(((i-1)*ArrFB.spaz)+ArrFB.spaz_in)+" in "+ArrFB.spaz_in+" scop "+ArrFB.scoppio);
                //setForeground(Color.GREEN);

                g.setColor(Color.ORANGE);
                if (ArrFB.fb[i].layer == 1) {
                    g.setColor(Color.CYAN);
                }
                if (ArrFB.fb[i].layer == 2) {
                    g.setColor(Color.BLUE);
                }
                if (andata) {
                    this.drawTriang(g, (i * nchanel) + xshf, ymax - (int) (ArrFB.fb[i].time * ystp) - yshf);
                }

            } else {
                //System.out.println("@ C"+i+" spaz "+(((i-1)*ArrFB.spaz)+ArrFB.spaz_in)+" in "+ArrFB.spaz_in+" scop "+ArrFB.scoppio);
                //setForeground(Color.CYAN);
                g.setColor(Color.RED);
                if (ArrFB.fb[i].layer == 1) {
                    g.setColor(Color.PINK);
                }
                if (ArrFB.fb[i].layer == 2) {
                    g.setColor(Color.MAGENTA);
                }
                if (ritorno) {

                    this.drawCircle(g, (i * nchanel) + xshf, ymax - (int) (ArrFB.fb[i].time * ystp) - yshf);
                }
            }

            /*  int x1 = (int) ((((j) * stepCh) + stepCh / 2) - stepCh / 3) + margine_X;
             int y1 = (int) (fbl.fb[j].time * stepT / obj.tr[0].sampleInterval) / 1000;
            
             int x2 = (int) ((((j) * stepCh) + stepCh / 2) + stepCh / 3) + margine_X;
             int y2 = (int) (fbl.fb[j].time * stepT / obj.tr[0].sampleInterval) / 1000;
             */
            //        p.lineTo((i * nchanel) + xshf, ymax - (int) (ArrFB.fb[i].time * ystp) - yshf);
            //g2.drawLine((i * nchanel) - 5+xshf, ymax - (int) (ArrFB.fb[i].time * ystp)-yshf, (i * nchanel) + 5+xshf, ymax - (int) (ArrFB.fb[i].time * ystp)-yshf);
            //System.out.println(" @ "+i+" "+Double.toString(ArrFB.fb[i].time*4000)+" "+Double.toString(ymax-(int)(ArrFB.fb[i].time*ystp))+" "+ymax);
            //g.drawLine(i*20-5,(int)((ArrFB.fb[i].time*20000/8000*(xmax-xshf))+xshf), (int)((ArrFB.fb[i].time*20000/8000*(xmax-xshf))+xshf), (ychn_max/2)+(ArrFB.fb[i].chan-1)*ychn_max-20);
        }

        //  g2.draw(p);
        int in0 = 0, fi0 = 0;
        g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(3));
        if (!print) {
            try {

                if (andata) {
                    if (!ArrFB.strato1.equals("0-0")) {
                        String[] split = ArrFB.strato1.split("-");
                        in0 = Integer.parseInt(split[0]);
                        fi0 = Integer.parseInt(split[1]);
                        g2.setColor(new Color(0.2f, 0.9f, 0.2f));

                        g2.drawLine((in0 * nchanel) + xshf, ymax - (int) ((ArrFB.dromo[0].b * ((in0) * ArrFB.spaz + ArrFB.spaz_in) + ArrFB.dromo[0].a) * ystp) - yshf, (fi0 * nchanel) + xshf, ymax - (int) ((ArrFB.dromo[0].b * ((fi0) * ArrFB.spaz + ArrFB.spaz_in) + ArrFB.dromo[0].a) * ystp) - yshf);
                    }
                    if (!ArrFB.strato2.equals("0-0")) {
                        String[] split = ArrFB.strato2.split("-");
                        in0 = Integer.parseInt(split[0]);
                        fi0 = Integer.parseInt(split[1]);
                        g2.setColor(new Color(0.6f, 0.9f, 0.9f));

                        g2.drawLine((in0 * nchanel) + xshf, ymax - (int) ((ArrFB.dromo[1].b * ((in0) * ArrFB.spaz + ArrFB.spaz_in) + ArrFB.dromo[1].a) * ystp) - yshf, (fi0 * nchanel) + xshf, ymax - (int) ((ArrFB.dromo[1].b * ((fi0) * ArrFB.spaz + ArrFB.spaz_in) + ArrFB.dromo[1].a) * ystp) - yshf);
                    }

                    if (!ArrFB.strato3.equals("0-0")) {
                        String[] split = ArrFB.strato3.split("-");
                        in0 = Integer.parseInt(split[0]);
                        fi0 = Integer.parseInt(split[1]);
                        g2.setColor(new Color(0.9f, 0.8f, 0.5f));
                        g2.drawLine((in0 * nchanel) + xshf, ymax - (int) ((ArrFB.dromo[2].b * ((in0) * ArrFB.spaz + ArrFB.spaz_in) + ArrFB.dromo[2].a) * ystp) - yshf, (fi0 * nchanel) + xshf, ymax - (int) ((ArrFB.dromo[2].b * ((fi0) * ArrFB.spaz + ArrFB.spaz_in) + ArrFB.dromo[2].a) * ystp) - yshf);
                        //System.out.println("Line "+(in0*nchanel)+" " +(ymax-(int)(ArrFB.dromo[2].b*((in0-1)*ArrFB.spaz+ArrFB.spaz_in)+ArrFB.dromo[2].a)*ystp)+" "+(fi0*nchanel)+" "+(ymax-(int) (ArrFB.dromo[2].b*((fi0-1)*ArrFB.spaz+ArrFB.spaz_in)+ArrFB.dromo[2].a)*ystp)+" "+ArrFB.strato3+" "+ArrFB.dromo[2].b+" "+ArrFB.dromo[2].a);
                    }
                }
            } catch (Exception e1) {
            }

            try {
                if (ritorno) {

                    if (!ArrFB.strato1R.equals("0-0")) {
                        String[] split = ArrFB.strato1R.split("-");
                        in0 = Integer.parseInt(split[0]);
                        fi0 = Integer.parseInt(split[1]);
                        g2.setColor(new Color(0.5f, 0.9f, 0.2f));
                        g2.drawLine((in0 * nchanel) + xshf, ymax - (int) ((ArrFB.dromoR[0].b * ((in0) * ArrFB.spaz + ArrFB.spaz_in) + ArrFB.dromoR[0].a) * ystp) - yshf, (fi0 * nchanel) + xshf, ymax - (int) ((ArrFB.dromoR[0].b * ((fi0) * ArrFB.spaz + ArrFB.spaz_in) + ArrFB.dromoR[0].a) * ystp) - yshf);
                    }
                    if (!ArrFB.strato2R.equals("0-0")) {
                        String[] split = ArrFB.strato2R.split("-");
                        in0 = Integer.parseInt(split[0]);
                        fi0 = Integer.parseInt(split[1]);
                        g2.setColor(new Color(0.6f, 0.9f, 0.9f));
                        g2.drawLine((in0 * nchanel) + xshf, ymax - (int) ((ArrFB.dromoR[1].b * ((in0) * ArrFB.spaz + ArrFB.spaz_in) + ArrFB.dromoR[1].a) * ystp) - yshf, (fi0 * nchanel) + xshf, ymax - (int) ((ArrFB.dromoR[1].b * ((fi0) * ArrFB.spaz + ArrFB.spaz_in) + ArrFB.dromoR[1].a) * ystp) - yshf);
                    }

                    if (!ArrFB.strato3R.equals("0-0")) {
                        String[] split = ArrFB.strato3R.split("-");
                        in0 = Integer.parseInt(split[0]);
                        fi0 = Integer.parseInt(split[1]);
                        g2.setColor(new Color(0.9f, 0.8f, 0.5f));
                        g2.drawLine((in0 * nchanel) + xshf, ymax - (int) ((ArrFB.dromoR[2].b * ((in0) * ArrFB.spaz + ArrFB.spaz_in) + ArrFB.dromoR[2].a) * ystp) - yshf, (fi0 * nchanel) + xshf, ymax - (int) ((ArrFB.dromoR[2].b * ((fi0) * ArrFB.spaz + ArrFB.spaz_in) + ArrFB.dromoR[2].a) * ystp) - yshf);
                        //System.out.println("Line "+(in0*nchanel)+" " +(ymax-(int)(ArrFB.dromo[2].b*((in0-1)*ArrFB.spaz+ArrFB.spaz_in)+ArrFB.dromo[2].a)*ystp)+" "+(fi0*nchanel)+" "+(ymax-(int) (ArrFB.dromo[2].b*((fi0-1)*ArrFB.spaz+ArrFB.spaz_in)+ArrFB.dromo[2].a)*ystp)+" "+ArrFB.strato3+" "+ArrFB.dromo[2].b+" "+ArrFB.dromo[2].a);
                    }
                }

            } catch (Exception e1) {
            }
        }
        //Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(1));

        //  System.out.println(" -- n fb : "+ ArrFB.fb.length);
    }

    private void draw_all_Dromo(Graphics g, int width, int height) {
        try {
            float[] dash1 = {5.0f};
            final BasicStroke dashed = new BasicStroke(0.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash1, 0.0f);
            if (obj.tr.length > 0) {

                Graphics2D g2 = (Graphics2D) g;
                g2.setStroke(dashed);
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

                //double maxV = obj.tr[i].getMaxValue();
                double stepT = (double) (height) / (double) (obj.tr[0].length);
                double stepCh = (width - 2 * margine_dx) / obj.tr.length;
                //    double stepV = (double) stepCh / (double) maxV;

                for (int i = 0; i < proj.stesa.size(); i++) {
                    if (i != obj.trace_index) {
                        FirstBrakeList fbl = proj.stesa.get(i);
                        Path2D p = new Path2D.Double();
                        p.moveTo(0 * stepCh + stepCh / 2 + margine_dx, (fbl.fb[0].time * stepT / obj.tr[0].sampleInterval) / 1000);
                        for (int j = 0; j < fbl.fb.length; j++) {

                            int x1 = (int) ((((j) * stepCh) + stepCh / 2) - stepCh / 3) + margine_dx;
                            int y1 = (int) (fbl.fb[j].time * stepT / obj.tr[0].sampleInterval) / 1000;

                            int x2 = (int) ((((j) * stepCh) + stepCh / 2) + stepCh / 3) + margine_dx;
                            int y2 = (int) (fbl.fb[j].time * stepT / obj.tr[0].sampleInterval) / 1000;
                            g2.setColor(Color.GREEN);

                            x1 = (int) (x1 + stepCh / 3);

                            if ((fbl.scoppio - fbl.spaz_in) / fbl.spaz > j && (fbl.scoppio - fbl.spaz_in) / fbl.spaz < j + 1) {

                                p.lineTo(x1, y1);
                                p.lineTo(((j + 0.5) * stepCh) + stepCh / 2 + margine_dx, 0);
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

    private void drawTriang(Graphics g, int x, int y) {

        g.drawLine(x - 4, y - 4, x + 4, y - 4);
        g.drawLine(x - 4, y - 4, x, y + 4);
        g.drawLine(x + 4, y - 4, x, y + 4);

    }

    private void drawCircle(Graphics g, int x, int y) {

        g.drawOval(x - 4, y - 4, 8, 8);
    }

    private void drawDrag(Graphics g, int x, int y, int mx, int my) {
        int ymax = my;//this.getHeight();
        int xmax = mx;//this.getWidth();

        ArrFB = proj.stesa.get(indj);
        int nchanel = xmax / ArrFB.fb.length;
        int ychn = (ymax - (5 * nchanel)) / nchanel;
        xshf = nchanel;
        //g.setColor(Color.yellow);

        if (is_white) {
            g.setColor(Color.magenta);
        } else {
            g.setColor(Color.ORANGE);
        }

        double max = 0.00001;
        for (int j = 0; j < proj.stesa.size(); j++) {
            FirstBrakeList fbl = proj.stesa.get(j);
            for (int i = 0; i < fbl.fb.length; i++) {
                if (max < fbl.fb[i].time) {
                    max = (int) fbl.fb[i].time;
                }
            }
        }
        max = 1.05 * max;

        double dist = 99999.9;
        try {
            int ystp = (int) ((ymax - (2 * margUp * ymax)) / max);
            int yshf = (int) (margUp * ymax);
            int fi = indi;
            int j = indj;
            int in = indi;
            FirstBrakeList fbl = proj.stesa.get(j);
            double scoppio = ((fbl.scoppio - fbl.spaz_in) / fbl.spaz);
            for (int i = 0; i < fbl.fb.length; i++) {
                int xx = (i * nchanel) + xshf;
                int yy = ymax - (int) (fbl.fb[i].time * ystp) - yshf;
                if (ArrFB.scoppio < ((i) * ArrFB.spaz) + ArrFB.spaz_in) {

                    this.drawTriang(g, (i * nchanel) + xshf, ymax - (int) (ArrFB.fb[i].time * ystp) - yshf);
                } else {
                    this.drawCircle(g, (i * nchanel) + xshf, ymax - (int) (ArrFB.fb[i].time * ystp) - yshf);
                }

                //g.drawLine((i * nchanel) - 5 + xshf, ymax - (int) (fbl.fb[i].time * ystp) - yshf, (i * nchanel) + 5 + xshf, ymax - (int) (fbl.fb[i].time * ystp) - yshf);
                double dst = Math.sqrt((Math.pow(xx - x, 2)) + Math.pow(yy - y, 2));
                if (dist > dst) {
                    dist = dst;
                    if ((i - scoppio) * (in - scoppio) >= 0) {
                        fi = i;
                    }
                    indj = j;

                }
            }

            double[][] data = FirstBrakeList.getList(fbl.fb, in, fi, ((fbl.scoppio - fbl.spaz_in) / fbl.spaz), fbl.scoppio);
            double[] result = FirstBrakeList.getOLSRegression(data);

            double b = result[1];  //1 / (fbl.varianza(in, fi) / fbl.covarianza(in, fi));
            double a = result[0]; //fbl.media(in, fi) - b * fbl.mediax(in, fi);

            fbl = proj.stesa.get(indj);
            int yy = ymax - (int) (fbl.fb[fi].time * ystp) - yshf;
            int xx = (fi * nchanel);
            g.setFont(new Font("Dialog", Font.PLAIN, (int) (14)));
            DecimalFormat df = new DecimalFormat("#.###");

            g.drawString("Velocità : " + df.format(1 / b) + " km/s", 30, 50);

            //    System.out.println("=Dragged "+fbl.fb[in].time+" "+(b * ((in) * fbl.spaz + fbl.spaz_in) + a)+" "+(ymax - (int) (fbl.fb[in].time * ystp))+" "+(ymax - (int) (b * ((in) * fbl.spaz + fbl.spaz_in) + a) * ystp));
            g.drawOval(xx - 15 + xshf, yy - 15, 30, 30);
            g.drawLine((in * nchanel) + xshf, ymax - (int) ((b * ((in) * fbl.spaz + fbl.spaz_in) + a) * ystp) - yshf, (fi * nchanel) + xshf, ymax - (int) ((b * ((fi) * fbl.spaz + fbl.spaz_in) + a) * ystp) - yshf);
        } catch (Exception ex) {
        }

    }

    private void drawtick(Graphics g, double max, int w, int h) {
        double ystp = 0.0;
        FirstBrakeList ArrFB = proj.stesa.get(0);
        int ymax = h;
        int xmax = w;
        int nchanel = xmax / (ArrFB.fb.length + 2);
        int ychn = (int) (ymax - (0 * nchanel)) / nchanel;
        int xshf = nchanel;
        int majTic = 0, minTic = 0;
        Graphics2D g2 = (Graphics2D) g.create();

        //ystp = (ymax - (2 * 45) - 30) / max;
        ystp = (ymax - (2 * margUp * ymax)) / max;

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
            minTic = 5;
        } else if (max / 10.0 < 30) {
            majTic = 20;
            minTic = 10;
        }

        //g2.setColor(Color.white);
        if (obj.is_white) {
            g2.setColor(Color.black);
        } else {
            g2.setColor(Color.white);
        }

        g2.setFont(new Font("Lucida", Font.PLAIN, (int) (12 * (w / 1000.0))));
        new Font("Lucida", Font.PLAIN, 12);

        FontMetrics fontMetrics = g2.getFontMetrics();

        int height = fontMetrics.getHeight();

        //FontMetrics fontMetrics = g2.getFontMetrics();
        //int height = fontMetrics.getHeight();
        for (int i = 0; i < max; i = i + minTic) {
            if (i % majTic == 0) {
                g2.drawLine((int) (xshf * 0.75), ymax - (int) (i * ystp + (margUp * ymax)), (int) (xshf * 0.6), ymax - (int) (i * ystp + (margUp * ymax)));
                int width = fontMetrics.stringWidth("" + (i));

                g2.drawString("" + (i), (int) (xshf * 0.55) - width, ymax - (int) (i * ystp + (margUp * ymax) - (height / 3)));
                g2.drawLine((int) (xmax - xshf * 0.75), ymax - (int) (i * ystp + (margUp * ymax)), (int) (xmax - xshf * 0.6), ymax - (int) (i * ystp + (margUp * ymax)));

            } else {
                g2.drawLine((int) (xshf * 0.75), ymax - (int) (i * ystp + (margUp * ymax)), (int) (xshf * 0.65), ymax - (int) (i * ystp + (margUp * ymax)));
                g2.drawLine((int) (xmax - xshf * 0.75), ymax - (int) (i * ystp + (margUp * ymax)), (int) (xmax - xshf * 0.65), ymax - (int) (i * ystp + (margUp * ymax)));
            }
        }

        for (int i = 0; i < ArrFB.fb.length - 1; i++) {

            g2.drawLine((int) (xshf + (nchanel * i)), (int) ((margUp * ymax)), (int) (xshf + (nchanel * i)), (int) ((margUp * ymax) * 0.85));
            int width = fontMetrics.stringWidth("" + String.format("%.2g%n", (i * ArrFB.spaz + ArrFB.spaz_in)));

            g2.drawString("" + String.format("%.2g%n", i * ArrFB.spaz + ArrFB.spaz_in), (int) (xshf + (nchanel * i) - (width / 2)), (int) ((margUp * ymax) * 0.83));
        }

    }

    public void drawAssi(Graphics g, int w, int h) {
        FirstBrakeList ArrFB = proj.stesa.get(0);
        int ymax = h;//this.getHeight();
        int xmax = w;//this.getWidth();
        int nchanel = xmax / ArrFB.fb.length;
        int ychn = (int) (ymax - (0 * nchanel)) / nchanel;
        xshf = nchanel;
        if (!is_white) {
            g.setColor(Color.white);
        } else {
            g.setColor(Color.black);
        }

        g.drawLine((int) (xshf * 0.75), (int) ((margUp * ymax)), (int) (xshf * 0.75), (int) (ymax - (margUp * ymax)));
        g.drawLine(xmax - (int) (xshf * 0.75), (int) ((margUp * ymax)), xmax - (int) (xshf * 0.75), (int) (ymax - (margUp * ymax)));
        g.drawLine((int) (xshf * 0.75), (int) (ymax - (margUp * ymax)), xmax - (int) (xshf * 0.75), (int) (ymax - (margUp * ymax)));
        g.drawLine((int) (xshf * 0.75), (int) ((margUp * ymax)), xmax - (int) (xshf * 0.75), (int) ((margUp * ymax)));
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

    public void drawSezAssi(Graphics g, int w, int h, double scala, double stepy) {
        FirstBrakeList ArrFB = proj.stesa.get(0);
        int ymax = h;//this.getHeight();
        int xmax = w;//this.getWidth();
        int nchanel = (int) (scala * ArrFB.spaz);
        int ychn = (int) (ymax - (0 * nchanel)) / nchanel;
        xshf = margine_sx;
        xmax = (nchanel * (ArrFB.ch - 1)) + 2 * xshf;
        if (!obj.is_white) {
            g.setColor(Color.white);
        } else {
            g.setColor(Color.black);
        }

        //g.drawLine((int) (xshf * 0.75), (int) ((margUp * ymax)), (int) (xshf * 0.75), (int) (ymax - (margUp * ymax)));
        g.drawLine((int) (xshf), (int) margine_alto, xmax + (int) (xshf), (int) margine_alto);
        g.drawLine(xmax + (int) (xshf), (int) margine_alto, xmax + (int) (xshf), (int) (ymax - margine_alto));
        g.drawLine((int) (xshf), (int) (ymax - (margine_alto)), xmax + (int) (xshf), (int) (ymax - margine_alto));
        g.drawLine((int) (xshf), (int) ((margine_alto)), (int) (xshf), (int) (ymax - (margine_alto)));
    }

    public void drawGeo(Graphics g, int x, int y) {
        /* if (!obj.is_white) {
         g.setColor(Color.YELLOW);
         } else {
         g.setColor(Color.BLUE);
         }*/
        g.drawLine(x - 5, y, x + 5, y);
        g.drawLine(x - 5, y, x, y + 10);
        g.drawLine(x + 5, y, x, y + 10);
        g.drawLine(x, y + 10, x, y + 20);
    }

    public void draw_sez_tick(Graphics g, double max, double min, int w, int h, double step) {

        double ystp = 0.0;
        FirstBrakeList ArrFB = proj.stesa.get(0);
        int ymax = h;//this.getHeight();
        int xmax = w;//this.getWidth();
        int nchanel = (int) (step * ArrFB.spaz);

        int xshf = margine_sx;
        int majTic = 0, minTic = 0;
        Graphics2D g2 = (Graphics2D) g.create();
        xmax = (nchanel * (ArrFB.ch - 1)) + 2 * margine_sx;
        double mxmn = min + max;

        ystp = (ymax - (2 * 45) - 30) / mxmn;
        // ystp = (ymax - (2.3 * margUp * ymax) - 30) / mxmn;

        if (mxmn / 10.0 < 1) {
            majTic = 2;
            minTic = 1;

        } else if (mxmn / 10.0 < 2) {
            majTic = 3;
            minTic = 1;
        } else if (mxmn / 10.0 < 3) {
            majTic = 5;
            minTic = 2;
        } else {
            majTic = 8;
            minTic = 4;
        }

        if (!obj.is_white) {
            g2.setColor(Color.white);
        } else {
            g2.setColor(Color.black);
        }

        //  g2.setColor(Color.white);
        g2.setFont(new Font("Dialog", Font.PLAIN, (int) (12 * (w / 1000.0))));
        //new Font("Dialog", Font.PLAIN, 12);

        FontMetrics fontMetrics = g2.getFontMetrics();

        int height = fontMetrics.getHeight();

        for (int j = (int) min; j > -max; j = j - minTic) {
            int i = -(j - (int) min);
            if (i % majTic == 0) {
                g2.drawLine((int) (xshf), (int) (i * ystp + (margUp * ymax) + 30), (int) (xshf - 10), (int) (i * ystp + (margUp * ymax) + 30));
                // int width = fontMetrics.stringWidth("" + (i));
                if ((int) (12 * (w / 1000.0)) > 9) {
                    // g2.drawString("" + j, (int) (xshf * 0.55) - width, (int) (i * ystp + (margUp * ymax) + 30 + (height / 3)));
                } else {
                    g2.setFont(new Font("Dialog", Font.PLAIN, (int) 10));
                }
                int width = fontMetrics.stringWidth("" + (i));

                g2.drawString("" + j, (int) (xshf * 0.55) - width, (int) (i * ystp + (margUp * ymax) + 30 + (height / 3)));

                g2.drawLine((int) (xmax + xshf), (int) (i * ystp + (margUp * ymax) + 30), (int) (xmax + xshf + 10), (int) (i * ystp + (margUp * ymax) + 30));

            } else {
                g2.drawLine((int) (xshf), (int) (i * ystp + (margUp * ymax) + 30), (int) (xshf) - 5, (int) (i * ystp + (margUp * ymax) + 30));
                g2.drawLine((int) (xmax + xshf), (int) (i * ystp + (margUp * ymax) + 30), (int) (xmax + xshf + 5), (int) (i * ystp + (margUp * ymax) + 30));
            }
        }

        for (int i = 0; i < ArrFB.fb.length; i++) {

            g2.drawLine((int) (2 * xshf + (nchanel * i)), (int) (margine_alto), (int) (2 * xshf + (nchanel * i)), (int) (margine_alto - 5));
            int width = fontMetrics.stringWidth("" + String.format("%.1f%n", (i * ArrFB.spaz + ArrFB.spaz_in)));

            if ((int) (12 * (w / 1000.0)) > 9) {
                //g2.drawString("" + String.format("%.2g%n", (i * ArrFB.spaz + ArrFB.spaz_in)), (int) (xshf + (nchanel * i) - (width / 2)), (int) ((margUp * ymax) * 0.83));
                // g2.drawString("" + String.format("%.1f%n", (i * ArrFB.spaz + ArrFB.spaz_in)), (int) (2 * xshf + (nchanel * i) - (width / 2)), (int) margine_alto - 7);
            } else {
                g2.setFont(new Font("Dialog", Font.PLAIN, (int) 10));
            }
            width = fontMetrics.stringWidth("" + String.format("%.1f%n", (i * ArrFB.spaz + ArrFB.spaz_in)));
            g2.drawString("" + String.format("%.1f%n", (i * ArrFB.spaz + ArrFB.spaz_in)), (int) (2 * xshf + (nchanel * i) - (width / 2)), (int) margine_alto - 7);

        }

    }

    public double[] maxmin() {
        FirstBrakeList ArrFB = proj.stesa.get(0);
        //     int ymax = h;//this.getHeight();
        //   int xmax = w;//this.getWidth();
        //   int nchanel = xmax / ArrFB.fb.length;
        //    int ychn = (int) (ymax - (0 * nchanel)) / nchanel;
        //    xshf = nchanel;
        //    Graphics2D g2 = (Graphics2D) g.create();
        /* for (int i = 0; i < ArrFB.fb.length - 1; i++) {
        
         drawGeo(g, (int) (xshf + (nchanel * i)), (int) ((margUp * ymax) + 15));
         }*/
        double max = -999;
        double min = -9999;
        FirstBrakeList fbl = proj.stesa.get(0);

        if (proj.Zg2.length > 1) {

            //     g.drawString("ZG 2", 20, 60);
            for (int i = 0; i < proj.Zg1.length - 1; i++) {
                if (proj.Zg1[i] - fbl.fb[i].z > max) {
                    max = proj.Zg1[i] - fbl.fb[i].z;
                }
            }
            if (proj.Zg2.length > 1) {
                for (int i = 0; i < proj.Zg2.length - 1; i++) {

                    if (proj.Zg2[i] - fbl.fb[i].z > max) {
                        max = proj.Zg2[i] - fbl.fb[i].z;
                    }
                }
            }

            for (int i = 0; i < proj.Zg1.length - 1; i++) {
                if (fbl.fb[i].z > min) {
                    min = fbl.fb[i].z;
                }
            }

        }

        if (proj.Zg1.length > 1) {
            //  g.drawString("ZG 1", 20, 30);

            for (int i = 0; i < proj.Zg1.length - 1; i++) {
                if (proj.Zg1[i] - fbl.fb[i].z > max) {
                    max = proj.Zg1[i] - fbl.fb[i].z;
                }
            }
            if (proj.Zg2.length > 1) {
                for (int i = 0; i < proj.Zg2.length - 1; i++) {

                    if (proj.Zg2[i] - fbl.fb[i].z > max) {
                        max = proj.Zg2[i] - fbl.fb[i].z;
                    }
                }
            }

            for (int i = 0; i < proj.Zg1.length - 1; i++) {
                if (fbl.fb[i].z > min) {
                    min = fbl.fb[i].z;
                }
            }
        }
        double[] ret = {max, min};
        return ret;
    }

    public void drawSezione(Graphics g, int w, int h, double step, double stepy) {
        double ystp = 0.0;
        FirstBrakeList ArrFB = proj.stesa.get(0);
        int ymax = h;//this.getHeight();
        int xmax = w;//this.getWidth();
        //int nchanel = xmax / ArrFB.fb.length;
        int nchanel = (int) (step * ArrFB.spaz);
        int ychn = (int) (ymax - (0 * nchanel)) / nchanel;
        xshf = 2 * margine_sx;
        Graphics2D g2 = (Graphics2D) g.create();
        Path2D p = new Path2D.Double();
        Path2D p2 = new Path2D.Double();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        //g.drawLine((int) (xshf * 0.75), (int) ((margUp * ymax) + 30), xmax - (int) (xshf * 0.75), (int) ((margUp * ymax) + 30));
        g.setColor(Color.YELLOW);
        /* for (int i = 0; i < ArrFB.fb.length - 1; i++) {
        
         drawGeo(g, (int) (xshf + (nchanel * i)), (int) ((margUp * ymax) + 15));
         }*/
        double max = -999;
        FirstBrakeList fbl = proj.stesa.get(0);

        if (proj.Zg2.length > 1) {
            //     g.drawString("ZG 2", 20, 60);

            for (int i = 0; i < proj.Zg1.length - 1; i++) {
                if (proj.Zg1[i] - fbl.fb[i].z > max) {
                    max = proj.Zg1[i] - fbl.fb[i].z;
                }
            }
            if (proj.Zg2.length > 1) {
                for (int i = 0; i < proj.Zg2.length - 1; i++) {

                    if (proj.Zg2[i] - fbl.fb[i].z > max) {
                        max = proj.Zg2[i] - fbl.fb[i].z;
                    }
                }
            }
            double min = -9999;
            for (int i = 0; i < proj.Zg1.length - 1; i++) {
                if (fbl.fb[i].z > min) {
                    min = fbl.fb[i].z;
                }
            }

            this.maxv = max;
            //      drawtick(g, max, min, w, h);

            //ystp = (ymax - (2.3 * margUp * ymax) - 30) / max;
            double mxmn = min + max;

            //  ystp = (ymax - (2.3 * margUp * ymax) - 30) / mxmn;
            // ystp = (ymax - (2 * 45) - 30) / mxmn;
            ystp = stepy;
            for (int i = 0; i < ArrFB.fb.length - 1; i++) {

                drawGeo(g, (int) (xshf + (nchanel * i)), (int) ((margUp * ymax) + 15 - ((ArrFB.fb[i].z - min) * ystp)));
            }

            BufferedImage bi = new BufferedImage(5, 5,
                    BufferedImage.TYPE_INT_RGB);
            Graphics2D big = bi.createGraphics();
            big.setColor(Color.CYAN);
            big.fillRect(0, 0, 5, 5);
            big.setColor(Color.DARK_GRAY);
            big.fillOval(0, 0, 4, 4);
            Rectangle r = new Rectangle(0, 0, 5, 5);

            if (bIm2 == null) {
                bIm2 = getFromDefautl(1);
                r = new Rectangle(0, 0, bIm2.getWidth(), bIm2.getHeight());

                //Rectangle r = new Rectangle(0, 0, 5, 5);
                g2.setPaint(new TexturePaint(bIm2, r));

                /*
                 r = new Rectangle(0, 0, 5, 5);
                 g2.setPaint(new TexturePaint(bi, r));*/
            } else {
                r = new Rectangle(0, 0, bIm2.getWidth(), bIm2.getHeight());
                g2.setPaint(new TexturePaint(bIm2, r));

            }
            //g2.setPaint(new TexturePaint(bi, r));

            p2 = new Path2D.Double();
            p2.moveTo((xshf + (nchanel * 0)), ystp * (proj.Zg2[0] - fbl.fb[0].z + min) + (margUp * ymax) + 30);
            for (int i = 0; i < proj.Zg1.length - 1; i++) {
                p2.curveTo((xshf + (nchanel * (i + 0.3))), ystp * (proj.Zg2[i] - fbl.fb[i].z + min) + (margUp * ymax) + 30, (xshf + (nchanel * (i - 0.3))), ystp * (proj.Zg2[i] - fbl.fb[i].z + min) + (margUp * ymax) + 30, (xshf + (nchanel * (i))), ystp * (proj.Zg2[i] - fbl.fb[i].z + min) + (margUp * ymax) + 30);
            }
            p2.curveTo((xshf + (nchanel * (proj.Zg2.length - 1))), ystp * (proj.Zg2[(proj.Zg2.length - 1)] - fbl.fb[(proj.Zg1.length - 1)].z + min) + (margUp * ymax) + 30, (xshf + (nchanel * ((proj.Zg2.length - 1) - 0.5))), ystp * (proj.Zg2[(proj.Zg2.length - 1)] - fbl.fb[(proj.Zg1.length - 1)].z + min) + (margUp * ymax) + 30, (xshf + (nchanel * (proj.Zg2.length - 1))), ystp * (proj.Zg2[(proj.Zg2.length - 1)] - fbl.fb[(proj.Zg1.length - 1)].z + min) + (margUp * ymax) + 30);

            p2.lineTo((xshf + (nchanel * (proj.Zg2.length - 1))), (-(fbl.fb[(proj.Zg1.length - 1)].z - min) * ystp) + (margUp * ymax) + 30);

            for (int i = proj.Zg2.length - 1; i > 0; i--) {
                p2.lineTo((xshf + (nchanel * i)), (-(fbl.fb[i].z - min) * ystp) + (margUp * ymax) + 30);

            }

            p2.lineTo((xshf + (nchanel * 0)), (-(fbl.fb[0].z - min) * ystp) + (margUp * ymax) + 30);
            p2.lineTo((xshf + (nchanel * 0)), ystp * (proj.Zg2[0] - fbl.fb[0].z + min) + (margUp * ymax) + 30);
            g2.draw(p2);
            g2.fill(p2);

        }

        if (proj.Zg1.length > 1) {
            //  g.drawString("ZG 1", 20, 30);

            for (int i = 0; i < proj.Zg1.length - 1; i++) {
                if (proj.Zg1[i] - fbl.fb[i].z > max) {
                    max = proj.Zg1[i] - fbl.fb[i].z;
                }
            }
            if (proj.Zg2.length > 1) {
                for (int i = 0; i < proj.Zg2.length - 1; i++) {

                    if (proj.Zg2[i] - fbl.fb[i].z > max) {
                        max = proj.Zg2[i] - fbl.fb[i].z;
                    }
                }
            }

            double min = -9999;
            for (int i = 0; i < proj.Zg1.length - 1; i++) {
                if (fbl.fb[i].z > min) {
                    min = fbl.fb[i].z;
                }
            }

            draw_sez_tick(g, max, min, w, h, step);
            //ystp = (ymax - (2.3 * margUp * ymax) - 30) / max;

            double mxmn = min + max;

            //ystp = (ymax - (2.3 * margUp * ymax) - 30) / mxmn;
            //ystp = (ymax - (2 * 45) - 30) / mxmn;
            ystp = stepy;
            for (int i = 0; i < ArrFB.fb.length - 1; i++) {
                //           drawGeo(g, (int) (xshf + (nchanel * i)), (int) ((margUp * ymax) + 15 - ((ArrFB.fb[i].z - min) * ystp)));
            }

            BufferedImage bi = new BufferedImage(5, 5,
                    BufferedImage.TYPE_INT_RGB);
            Graphics2D big = bi.createGraphics();
            big.setColor(Color.GREEN);
            big.fillRect(0, 0, 5, 5);
            big.setColor(Color.lightGray);
            big.fillOval(0, 0, 5, 5);
            if (bIm1 == null) {
                bIm1 = getFromDefautl(0);
                Rectangle r = new Rectangle(0, 0, bIm1.getWidth(), bIm1.getHeight());

                //Rectangle r = new Rectangle(0, 0, 5, 5);
                g2.setPaint(new TexturePaint(bIm1, r));
            } else {
                Rectangle r = new Rectangle(0, 0, bIm1.getWidth(), bIm1.getHeight());
                g2.setPaint(new TexturePaint(bIm1, r));

            }

            p = new Path2D.Double();
            p.moveTo((xshf + (nchanel * 0)), ystp * (proj.Zg1[0] - fbl.fb[0].z + min) + (margUp * ymax) + 30);
            for (int i = 0; i < proj.Zg1.length - 1; i++) {
                p.curveTo((xshf + (nchanel * (i + 0.3))), ystp * (proj.Zg1[i] - fbl.fb[i].z + min) + (margUp * ymax) + 30, (xshf + (nchanel * (i - 0.3))), ystp * (proj.Zg1[i] - fbl.fb[i].z + min) + (margUp * ymax) + 30, (xshf + (nchanel * (i))), ystp * (proj.Zg1[i] - fbl.fb[i].z + min) + (margUp * ymax) + 30);
            }
            p.curveTo((xshf + (nchanel * (proj.Zg1.length - 1))), ystp * (proj.Zg1[(proj.Zg1.length - 1)] - fbl.fb[proj.Zg1.length - 3].z + min) + (margUp * ymax) + 30, (xshf + (nchanel * ((proj.Zg1.length - 1) - 0.5))), ystp * (proj.Zg1[(proj.Zg1.length - 1)] - fbl.fb[(proj.Zg1.length - 1)].z + min) + (margUp * ymax) + 30, (xshf + (nchanel * (proj.Zg1.length - 1))), ystp * (proj.Zg1[(proj.Zg1.length - 1)] - fbl.fb[(proj.Zg1.length - 1)].z + min) + (margUp * ymax) + 30);

            p.lineTo((xshf + (nchanel * (proj.Zg1.length - 1))), (-(fbl.fb[(proj.Zg1.length - 1)].z - min) * ystp) + (margUp * ymax) + 30);

            for (int i = proj.Zg1.length - 3; i >= 0; i--) {
                p.lineTo((xshf + (nchanel * i)), (-(fbl.fb[i].z - min) * ystp) + (margUp * ymax) + 30);

            }

            p.lineTo((xshf + (nchanel * 0)), (-(fbl.fb[(proj.Zg1.length - 1)].z - min) * ystp) + (margUp * ymax) + 30);
            p.lineTo((xshf + (nchanel * 0)), ystp * (proj.Zg1[0] - fbl.fb[0].z + min) + (margUp * ymax) + 30);

            g2.draw(p);

            g2.fill(p);
            drawLeg(g2, proj.V1, 1, w, h);

            bi = new BufferedImage(5, 5,
                    BufferedImage.TYPE_INT_RGB);
            big = bi.createGraphics();
            big.setColor(Color.CYAN);
            big.fillRect(0, 0, 5, 5);
            big.setColor(Color.DARK_GRAY);
            big.fillOval(1, 1, 3, 3);
            Rectangle r = new Rectangle(0, 0, 5, 5);
            if (bIm2 == null) {
                bIm2 = getFromDefautl(1);
                r = new Rectangle(0, 0, bIm2.getWidth(), bIm2.getHeight());

                //Rectangle r = new Rectangle(0, 0, 5, 5);
                g2.setPaint(new TexturePaint(bIm2, r));

            } else {
                r = new Rectangle(0, 0, bIm2.getWidth(), bIm2.getHeight());
                g2.setPaint(new TexturePaint(bIm2, r));

            }

            //g2.setPaint(new TexturePaint(bi, r));
            drawLeg(g2, proj.V2, 2, w, h);

            if (proj.Zg2.length > 1) {
                p2 = new Path2D.Double();
                p2.moveTo((xshf + (nchanel * 0)), ystp * (proj.Zg2[0] - fbl.fb[0].z + min) + (margUp * ymax) + 30);
                for (int i = 0; i < proj.Zg1.length - 3; i++) {
                    p2.curveTo((xshf + (nchanel * (i + 0.3))), ystp * (proj.Zg2[i] - fbl.fb[i].z + min) + (margUp * ymax) + 30, (xshf + (nchanel * (i - 0.3))), ystp * (proj.Zg2[i] - fbl.fb[i].z + min) + (margUp * ymax) + 30, (xshf + (nchanel * (i))), ystp * (proj.Zg2[i] - fbl.fb[i].z + min) + (margUp * ymax) + 30);
                }

                p2.curveTo((xshf + (nchanel * (proj.Zg2.length - 1))), ystp * ((proj.Zg2[(proj.Zg1.length - 1)] - fbl.fb[(proj.Zg1.length - 1)].z + min)) + (margUp * ymax) + 30, (xshf + (nchanel * ((proj.Zg2.length - 1) - 0.5))), ystp * (proj.Zg2[(proj.Zg2.length - 1)] - fbl.fb[(proj.Zg1.length - 1)].z + min) + (margUp * ymax) + 30, (xshf + (nchanel * (proj.Zg2.length - 1))), ystp * (proj.Zg2[(proj.Zg2.length - 1)] - fbl.fb[(proj.Zg1.length - 1)].z + min) + (margUp * ymax) + 30);

                p2.lineTo((xshf + (nchanel * (proj.Zg2.length - 1))), (ymax - (margine_alto * 1.1)));

                p2.lineTo((xshf + (nchanel * 0)), (ymax - (margine_alto * 1.1)));
                p2.lineTo((xshf + (nchanel * 0)), ystp * proj.Zg2[0] + (margUp * ymax) + 30);

                bi = new BufferedImage(5, 5,
                        BufferedImage.TYPE_INT_RGB);
                big = bi.createGraphics();
                big.setColor(Color.ORANGE);
                big.fillRect(0, 0, 5, 5);
                big.setColor(Color.yellow);
                big.fillOval(1, 1, 3, 3);
                r = new Rectangle(0, 0, 5, 5);

                if (bIm3 == null) {

                    bIm3 = getFromDefautl(2);
                    r = new Rectangle(0, 0, bIm3.getWidth(), bIm3.getHeight());

                    //Rectangle r = new Rectangle(0, 0, 5, 5);
                    g2.setPaint(new TexturePaint(bIm3, r));

                } else {
                    r = new Rectangle(0, 0, bIm3.getWidth(), bIm3.getHeight());
                    g2.setPaint(new TexturePaint(bIm3, r));

                }

                //g2.setPaint(new TexturePaint(bi, r));
                g2.draw(p2);

                g2.fill(p2);
                drawLeg(g2, proj.V3, 3, w, h);

            } else {

                p2 = new Path2D.Double();
                p2.moveTo((xshf + (nchanel * 0)), ystp * (proj.Zg1[0] - fbl.fb[0].z + min) + (margUp * ymax) + 30);
                for (int i = 0; i < proj.Zg1.length - 3; i++) {
                    p2.curveTo((xshf + (nchanel * (i + 0.3))), ystp * (proj.Zg1[i] - fbl.fb[i].z + min) + (margUp * ymax) + 30, (xshf + (nchanel * (i - 0.3))), ystp * (proj.Zg1[i] - fbl.fb[i].z + min) + (margUp * ymax) + 30, (xshf + (nchanel * (i))), ystp * (proj.Zg1[i] - fbl.fb[i].z + min) + (margUp * ymax) + 30);
                }

                p2.curveTo((xshf + (nchanel * (proj.Zg1.length - 1))), ystp * (proj.Zg1[(proj.Zg1.length - 1)] - fbl.fb[(proj.Zg1.length - 1)].z + min) + (margUp * ymax) + 30, (xshf + (nchanel * ((proj.Zg1.length - 1) - 0.5))), ystp * (proj.Zg1[(proj.Zg1.length - 1)] - fbl.fb[(proj.Zg1.length - 1)].z + min) + (margUp * ymax) + 30, (xshf + (nchanel * (proj.Zg1.length - 1))), ystp * (proj.Zg1[(proj.Zg1.length - 1)] - fbl.fb[(proj.Zg1.length - 1)].z + min) + (margUp * ymax) + 30);

                p2.lineTo((xshf + (nchanel * (proj.Zg1.length - 1))), (ymax - (margine_alto * 1.1)));
                p2.lineTo((xshf + (nchanel * 0)), (ymax - (margine_alto * 1.1)));
                p2.lineTo((xshf + (nchanel * 0)), ystp * (proj.Zg1[0] - fbl.fb[0].z + min) + (margUp * ymax) + 30);

                bi = new BufferedImage(5, 5,
                        BufferedImage.TYPE_INT_RGB);
                big = bi.createGraphics();
                big.setColor(Color.CYAN);
                big.fillRect(0, 0, 5, 5);
                big.setColor(Color.DARK_GRAY);
                big.fillOval(1, 1, 3, 3);
                //r = new Rectangle(0, 0, 5, 5);

                if (bIm2 == null) {

                    bIm2 = getFromDefautl(1);
                    r = new Rectangle(0, 0, bIm2.getWidth(), bIm2.getHeight());

                    //Rectangle r = new Rectangle(0, 0, 5, 5);
                    g2.setPaint(new TexturePaint(bIm2, r));

                    //r = new Rectangle(0, 0, 5, 5);
                    //g2.setPaint(new TexturePaint(bi, r));
                } else {
                    r = new Rectangle(0, 0, bIm2.getWidth(), bIm2.getHeight());
                    g2.setPaint(new TexturePaint(bIm2, r));

                }

                //g2.setPaint(new TexturePaint(bi, r));
                g2.draw(p2);

                g2.fill(p2);

                drawLeg(g2, proj.V2, 2, w, h);

            }

        }

    }

    public void drawSezione2(Graphics g, int w, int h, double step, double stepy) {
        double ystp = 0.0;
        FirstBrakeList ArrFB = proj.stesa.get(0);
        int ymax = h;//this.getHeight();
        int xmax = w;//this.getWidth();
        //int nchanel = xmax / ArrFB.fb.length;
        int nchanel = (int) (step * ArrFB.spaz);
        int ychn = (int) (ymax - (0 * nchanel)) / nchanel;
        xshf = 2 * margine_sx;
        Graphics2D g2 = (Graphics2D) g.create();
        Path2D p = new Path2D.Double();
        Path2D p2 = new Path2D.Double();
        this.loadFromDefautl(0);

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        //g.drawLine((int) (xshf * 0.75), (int) ((margUp * ymax) + 30), xmax - (int) (xshf * 0.75), (int) ((margUp * ymax) + 30));
        g.setColor(Color.YELLOW);
        /* for (int i = 0; i < ArrFB.fb.length - 1; i++) {
        
         drawGeo(g, (int) (xshf + (nchanel * i)), (int) ((margUp * ymax) + 15));
         }*/
        double max = -999;
        FirstBrakeList fbl = proj.stesa.get(0);

        if (proj.Zg2.length > 1) {
            //     g.drawString("ZG 2", 20, 60);

            for (int i = 0; i < proj.Zg1.length; i++) {
                if (proj.Zg1[i] - fbl.fb[i].z > max) {
                    max = proj.Zg1[i] - fbl.fb[i].z;
                }
            }
            if (proj.Zg2.length > 1) {
                for (int i = 0; i < proj.Zg2.length; i++) {

                    if (proj.Zg2[i] - fbl.fb[i].z > max) {
                        max = proj.Zg2[i] - fbl.fb[i].z;
                    }
                }
            }
            double min = -9999;
            for (int i = 0; i < proj.Zg1.length; i++) {
                if (fbl.fb[i].z > min) {
                    min = fbl.fb[i].z;
                }
            }

            this.maxv = max;
            //      drawtick(g, max, min, w, h);

            //ystp = (ymax - (2.3 * margUp * ymax) - 30) / max;
            double mxmn = min + max;

            //  ystp = (ymax - (2.3 * margUp * ymax) - 30) / mxmn;
            // ystp = (ymax - (2 * 45) - 30) / mxmn;
            ystp = stepy;
            for (int i = 0; i < ArrFB.fb.length; i++) {

                drawGeo(g, (int) (xshf + (nchanel * i)), (int) ((margUp * ymax) + 15 - ((ArrFB.fb[i].z - min) * ystp)));
            }

            BufferedImage bi = new BufferedImage(5, 5,
                    BufferedImage.TYPE_INT_RGB);
            Graphics2D big = bi.createGraphics();
            big.setColor(Color.CYAN);
            big.fillRect(0, 0, 5, 5);
            big.setColor(Color.DARK_GRAY);
            big.fillOval(0, 0, 4, 4);
            Rectangle r;// = new Rectangle(0, 0, 5, 5);

            if (bIm2 == null) {
                bIm2 = getFromDefautl(1);
                r = new Rectangle(0, 0, bIm2.getWidth(), bIm2.getHeight());
                g2.setPaint(new TexturePaint(bIm2, r));

            } else {
                r = new Rectangle(0, 0, bIm2.getWidth(), bIm2.getHeight());
                g2.setPaint(new TexturePaint(bIm2, r));

            }
            //g2.setPaint(new TexturePaint(bi, r));

            p2 = new Path2D.Double();
            p2.moveTo((xshf + (nchanel * 0)), ystp * (proj.Zg2[0] - fbl.fb[0].z + min) + (margUp * ymax) + 30);
            for (int i = 0; i < proj.Zg1.length - 1; i++) {
                p2.curveTo((xshf + (nchanel * (i - 0.5))), ystp * (proj.Zg2[i] - fbl.fb[i].z + min) + (margUp * ymax) + 30, (xshf + (nchanel * (i - 0.3))), ystp * (proj.Zg2[i] - fbl.fb[i].z + min) + (margUp * ymax) + 30, (xshf + (nchanel * (i))), ystp * (proj.Zg2[i] - fbl.fb[i].z + min) + (margUp * ymax) + 30);
            }
            p2.curveTo((xshf + (nchanel * (proj.Zg2.length - 1))), ystp * (proj.Zg2[(proj.Zg2.length - 1)] - fbl.fb[(proj.Zg1.length - 1)].z + min) + (margUp * ymax) + 30, (xshf + (nchanel * ((proj.Zg2.length - 1) - 0.5))), ystp * (proj.Zg2[(proj.Zg2.length - 1)] - fbl.fb[(proj.Zg1.length - 1)].z + min) + (margUp * ymax) + 30, (xshf + (nchanel * (proj.Zg2.length - 1))), ystp * (proj.Zg2[(proj.Zg2.length - 1)] - fbl.fb[(proj.Zg1.length - 1)].z + min) + (margUp * ymax) + 30);

            p2.lineTo((xshf + (nchanel * (proj.Zg2.length - 1))), (-(fbl.fb[(proj.Zg1.length - 1)].z - min) * ystp) + (margUp * ymax) + 30);

            for (int i = proj.Zg2.length - 1; i > 0; i--) {
                p2.lineTo((xshf + (nchanel * i)), (-(fbl.fb[i].z - min) * ystp) + (margUp * ymax) + 30);

            }

            p2.lineTo((xshf + (nchanel * 0)), (-(fbl.fb[0].z - min) * ystp) + (margUp * ymax) + 30);
            p2.lineTo((xshf + (nchanel * 0)), ystp * (proj.Zg2[0] - fbl.fb[0].z + min) + (margUp * ymax) + 30);
            g2.draw(p2);
            g2.fill(p2);

        }

        if (proj.Zg1.length > 1) {
            //  g.drawString("ZG 1", 20, 30);

            for (int i = 0; i < proj.Zg1.length; i++) {
                if (proj.Zg1[i] - fbl.fb[i].z > max) {
                    max = proj.Zg1[i] - fbl.fb[i].z;
                }
            }
            if (proj.Zg2.length > 1) {
                for (int i = 0; i < proj.Zg2.length; i++) {

                    if (proj.Zg2[i] - fbl.fb[i].z > max) {
                        max = proj.Zg2[i] - fbl.fb[i].z;
                    }
                }
            }

            double min = -9999;
            for (int i = 0; i < proj.Zg1.length; i++) {
                if (fbl.fb[i].z > min) {
                    min = fbl.fb[i].z;
                }
            }

            draw_sez_tick(g, max, min, w, h, step);
            //ystp = (ymax - (2.3 * margUp * ymax) - 30) / max;

            double mxmn = min + max;

            //ystp = (ymax - (2.3 * margUp * ymax) - 30) / mxmn;
            //ystp = (ymax - (2 * 45) - 30) / mxmn;
            ystp = stepy;
            for (int i = 0; i < ArrFB.fb.length; i++) {
                //           drawGeo(g, (int) (xshf + (nchanel * i)), (int) ((margUp * ymax) + 15 - ((ArrFB.fb[i].z - min) * ystp)));
            }

            BufferedImage bi = new BufferedImage(5, 5,
                    BufferedImage.TYPE_INT_RGB);
            Graphics2D big = bi.createGraphics();
            big.setColor(Color.GREEN);
            big.fillRect(0, 0, 5, 5);
            big.setColor(Color.lightGray);
            big.fillOval(0, 0, 5, 5);
            if (bIm1 == null) {
                bIm1 = getFromDefautl(0);
                Rectangle r = new Rectangle(0, 0, bIm1.getWidth(), bIm1.getHeight());

                // Rectangle r = new Rectangle(0, 0, 5, 5);
                g2.setPaint(new TexturePaint(bIm1, r));
            } else {
                Rectangle r = new Rectangle(0, 0, bIm1.getWidth(), bIm1.getHeight());
                g2.setPaint(new TexturePaint(bIm1, r));

            }

            p = new Path2D.Double();
            p.moveTo((xshf + (nchanel * 0)), ystp * (proj.Zg1[0] - fbl.fb[0].z + min) + (margUp * ymax) + 30);
            for (int i = 0; i < proj.Zg1.length - 1; i++) {
                p.curveTo((xshf + (nchanel * (i - 0.5))), ystp * (proj.Zg1[i] - fbl.fb[i].z + min) + (margUp * ymax) + 30, (xshf + (nchanel * (i - 0.3))), ystp * (proj.Zg1[i] - fbl.fb[i].z + min) + (margUp * ymax) + 30, (xshf + (nchanel * (i))), ystp * (proj.Zg1[i] - fbl.fb[i].z + min) + (margUp * ymax) + 30);
            }
            p.curveTo((xshf + (nchanel * (proj.Zg1.length - 1))), ystp * (proj.Zg1[(proj.Zg1.length - 1)] - fbl.fb[proj.Zg1.length - 1].z + min) + (margUp * ymax) + 30, (xshf + (nchanel * ((proj.Zg1.length - 1) - 0.5))), ystp * (proj.Zg1[(proj.Zg1.length - 1)] - fbl.fb[(proj.Zg1.length - 1)].z + min) + (margUp * ymax) + 30, (xshf + (nchanel * (proj.Zg1.length - 1))), ystp * (proj.Zg1[(proj.Zg1.length - 1)] - fbl.fb[(proj.Zg1.length - 1)].z + min) + (margUp * ymax) + 30);

            p.lineTo((xshf + (nchanel * (proj.Zg1.length - 1))), (-(fbl.fb[(proj.Zg1.length - 1)].z - min) * ystp) + (margUp * ymax) + 30);

            for (int i = proj.Zg1.length - 1; i >= 0; i--) {
                p.lineTo((xshf + (nchanel * i)), (-(fbl.fb[i].z - min) * ystp) + (margUp * ymax) + 30);

            }

            p.lineTo((xshf + (nchanel * 0)), (-(fbl.fb[(proj.Zg1.length - 1)].z - min) * ystp) + (margUp * ymax) + 30);
            p.lineTo((xshf + (nchanel * 0)), ystp * (proj.Zg1[0] - fbl.fb[0].z + min) + (margUp * ymax) + 30);

            g2.draw(p);

            g2.fill(p);
            drawLeg(g2, proj.V1, 1, w, h);

            bi = new BufferedImage(5, 5,
                    BufferedImage.TYPE_INT_RGB);
            big = bi.createGraphics();
            big.setColor(Color.CYAN);
            big.fillRect(0, 0, 5, 5);
            big.setColor(Color.DARK_GRAY);
            big.fillOval(1, 1, 3, 3);
            Rectangle r = new Rectangle(0, 0, 5, 5);
            if (bIm2 == null) {
                bIm2 = getFromDefautl(1);
                r = new Rectangle(0, 0, bIm2.getWidth(), bIm2.getHeight());
                g2.setPaint(new TexturePaint(bIm2, r));
                /*
                 r = new Rectangle(0, 0, 5, 5);
                 g2.setPaint(new TexturePaint(bi, r));*/
            } else {
                r = new Rectangle(0, 0, bIm2.getWidth(), bIm2.getHeight());
                g2.setPaint(new TexturePaint(bIm2, r));

            }

            //g2.setPaint(new TexturePaint(bi, r));
            drawLeg(g2, proj.V2, 2, w, h);

            if (proj.Zg2.length > 1) {
                p2 = new Path2D.Double();
                p2.moveTo((xshf + (nchanel * 0)), ystp * (proj.Zg2[0] - fbl.fb[0].z + min) + (margUp * ymax) + 30);
                for (int i = 0; i < proj.Zg1.length - 1; i++) {//3
                    p2.curveTo((xshf + (nchanel * (i - 0.5))), ystp * (proj.Zg2[i] - fbl.fb[i].z + min) + (margUp * ymax) + 30, (xshf + (nchanel * (i - 0.3))), ystp * (proj.Zg2[i] - fbl.fb[i].z + min) + (margUp * ymax) + 30, (xshf + (nchanel * (i))), ystp * (proj.Zg2[i] - fbl.fb[i].z + min) + (margUp * ymax) + 30);
                }

                p2.curveTo((xshf + (nchanel * (proj.Zg2.length - 1))), ystp * ((proj.Zg2[(proj.Zg1.length - 1)] - fbl.fb[(proj.Zg1.length - 1)].z + min)) + (margUp * ymax) + 30, (xshf + (nchanel * ((proj.Zg2.length - 1) - 0.5))), ystp * (proj.Zg2[(proj.Zg2.length - 1)] - fbl.fb[(proj.Zg1.length - 1)].z + min) + (margUp * ymax) + 30, (xshf + (nchanel * (proj.Zg2.length - 1))), ystp * (proj.Zg2[(proj.Zg2.length - 1)] - fbl.fb[(proj.Zg1.length - 1)].z + min) + (margUp * ymax) + 30);

                p2.lineTo((xshf + (nchanel * (proj.Zg2.length - 1))), (ymax - (margine_alto * 1.1)));

                p2.lineTo((xshf + (nchanel * 0)), (ymax - (margine_alto * 1.1)));
                p2.lineTo((xshf + (nchanel * 0)), ystp * proj.Zg2[0] + (margUp * ymax) + 30);

                bi = new BufferedImage(5, 5,
                        BufferedImage.TYPE_INT_RGB);
                big = bi.createGraphics();
                big.setColor(Color.ORANGE);
                big.fillRect(0, 0, 5, 5);
                big.setColor(Color.yellow);
                big.fillOval(1, 1, 3, 3);
                r = new Rectangle(0, 0, 5, 5);

                if (bIm3 == null) {

                    bIm3 = getFromDefautl(2);
                    r = new Rectangle(0, 0, bIm3.getWidth(), bIm3.getHeight());

                    //Rectangle r = new Rectangle(0, 0, 5, 5);
                    g2.setPaint(new TexturePaint(bIm3, r));

                    // r = new Rectangle(0, 0, 5, 5);
//                     g2.setPaint(new TexturePaint(bi, r));
                } else {
                    r = new Rectangle(0, 0, bIm3.getWidth(), bIm3.getHeight());
                    g2.setPaint(new TexturePaint(bIm3, r));

                }

                //g2.setPaint(new TexturePaint(bi, r));
                g2.draw(p2);

                g2.fill(p2);
                drawLeg(g2, proj.V3, 3, w, h);

            } else {

                p2 = new Path2D.Double();
                p2.moveTo((xshf + (nchanel * 0)), ystp * (proj.Zg1[0] - fbl.fb[0].z + min) + (margUp * ymax) + 30);
                for (int i = 0; i < proj.Zg1.length - 1; i++) {
                    p2.curveTo((xshf + (nchanel * (i - 0.5))), ystp * (proj.Zg1[i] - fbl.fb[i].z + min) + (margUp * ymax) + 30, (xshf + (nchanel * (i - 0.3))), ystp * (proj.Zg1[i] - fbl.fb[i].z + min) + (margUp * ymax) + 30, (xshf + (nchanel * (i))), ystp * (proj.Zg1[i] - fbl.fb[i].z + min) + (margUp * ymax) + 30);
                }

                p2.curveTo((xshf + (nchanel * (proj.Zg1.length - 1))), ystp * (proj.Zg1[(proj.Zg1.length - 1)] - fbl.fb[(proj.Zg1.length - 1)].z + min) + (margUp * ymax) + 30, (xshf + (nchanel * ((proj.Zg1.length - 1) - 0.5))), ystp * (proj.Zg1[(proj.Zg1.length - 1)] - fbl.fb[(proj.Zg1.length - 1)].z + min) + (margUp * ymax) + 30, (xshf + (nchanel * (proj.Zg1.length - 1))), ystp * (proj.Zg1[(proj.Zg1.length - 1)] - fbl.fb[(proj.Zg1.length - 1)].z + min) + (margUp * ymax) + 30);

                p2.lineTo((xshf + (nchanel * (proj.Zg1.length - 1))), (ymax - (margine_alto * 1.1)));
                p2.lineTo((xshf + (nchanel * 0)), (ymax - (margine_alto * 1.1)));
                p2.lineTo((xshf + (nchanel * 0)), ystp * (proj.Zg1[0] - fbl.fb[0].z + min) + (margUp * ymax) + 30);

                bi = new BufferedImage(5, 5,
                        BufferedImage.TYPE_INT_RGB);
                big = bi.createGraphics();
                big.setColor(Color.CYAN);
                big.fillRect(0, 0, 5, 5);
                big.setColor(Color.DARK_GRAY);
                big.fillOval(1, 1, 3, 3);
                r = new Rectangle(0, 0, 5, 5);

                if (bIm2 == null) {
                    bIm2 = getFromDefautl(1);
                    r = new Rectangle(0, 0, bIm2.getWidth(), bIm2.getHeight());
                    g2.setPaint(new TexturePaint(bIm2, r));

                    // r = new Rectangle(0, 0, 5, 5);
                    // g2.setPaint(new TexturePaint(bi, r));
                } else {
                    r = new Rectangle(0, 0, bIm2.getWidth(), bIm2.getHeight());
                    g2.setPaint(new TexturePaint(bIm2, r));

                }

                //g2.setPaint(new TexturePaint(bi, r));
                g2.draw(p2);

                g2.fill(p2);

                drawLeg(g2, proj.V2, 2, w, h);

            }

        }

    }

    private void drawLeg(Graphics2D g2, double V1, int i, int w, int h) {
        double ystp = 0.0;
        FirstBrakeList ArrFB = proj.stesa.get(0);
        int ymax = h;//this.getHeight();
        int xmax = w;//this.getWidth();
        int nchanel = xmax / ArrFB.fb.length;
        int ychn = (int) (ymax - (0 * nchanel)) / nchanel;
        int xshf = nchanel;
        int majTic = 0, minTic = 0;
        Path2D p = new Path2D.Double();
        double posx = xshf + ((i - 1) * (xmax / 3));
        p.moveTo(posx, ymax - (margine_alto) + 5);
        p.lineTo(posx + 20, ymax - (margine_alto) + 5);
        p.lineTo(posx + 20, ymax - 5);
        p.lineTo(posx, ymax - 5);
        g2.draw(p);

        g2.fill(p);

        FontMetrics fontMetrics = g2.getFontMetrics();
        g2.setFont(new Font("Dialog", Font.PLAIN, (int) (12 * (w / 1000.0))));

        int height = fontMetrics.getHeight();
        // g2.setColor(Color.white);
        if (!obj.is_white) {
            g2.setColor(Color.white);
        } else {
            g2.setColor(Color.black);
        }

        g2.drawString("" + (int) (V1 * 1000) + " m/s", (float) posx + 30, ymax - 6 - (height / 3));

    }

    private boolean[][] loadBrush(String path) {
        boolean[][] brush = new boolean[15][15];
        try (InputStreamReader isr = new InputStreamReader(getClass().getClassLoader().getResourceAsStream(path));
             BufferedReader br = new BufferedReader(isr)) {

            String linea = br.readLine();
            // String[] module = new String[20];
            while (linea != null) {
                //stile[i] = linea;

                //linea = br.readLine();
                for (int k = 0; k < 15; k++) {
                    for (int j = 0; j < 15; j++) {
                        brush[k][j] = (linea.charAt(j + (15 * k)) == '0' ? false : true);
                    }
                }
                linea = br.readLine();

            }
        } catch (Exception ex) {
        }

        return brush;
    }

    private boolean loadFromDefautl(int y) {
        cell = new boolean[4][15][15];
        int i = 0;

        cell[i] = this.loadBrush("/org/myorg/myapi/Resources/topsoil.brush");
        im[i] = new BufferedImage(14, 14, BufferedImage.TYPE_INT_RGB);//(BufferedImage) this.createImage(14, 14);
        Graphics offg = im[i].getGraphics();
        Graphics2D g2 = (Graphics2D) offg.create();
        g2.setBackground(new Color(10, 210, 135));
        g2.clearRect(0, 0, 14, 14);
        g2.setColor(new Color(5, 105, 45));
        for (int j = 0; j < 15; j++) {
            for (int k = 0; k < 15; k++) {
                if (cell[i][j][k]) {
                    g2.fillRect(j, k, 1, 1);

                }
            }
        }
        i = 1;
        cell[i] = this.loadBrush("/org/myorg/myapi/Resources/weathered.brush");
        im[i] = new BufferedImage(14, 14, BufferedImage.TYPE_INT_RGB);//(BufferedImage) this.createImage(14, 14);
        offg = im[i].getGraphics();
        g2 = (Graphics2D) offg.create();
        g2.setBackground(new Color(0, 188, 210));
        g2.clearRect(0, 0, 14, 14);
        g2.setColor(new Color(42, 25, 125));
        for (int j = 0; j < 15; j++) {
            for (int k = 0; k < 15; k++) {
                if (cell[i][j][k]) {
                    g2.fillRect(j, k, 1, 1);

                }
            }
        }
        i = 2;
        cell[i] = this.loadBrush("/org/myorg/myapi/Resources/bedrock.brush");
        im[i] = new BufferedImage(14, 14, BufferedImage.TYPE_INT_RGB);//(BufferedImage) this.createImage(14, 14);
        offg = im[i].getGraphics();
        g2 = (Graphics2D) offg.create();
        g2.setBackground(new Color(175, 188, 70));
        g2.clearRect(0, 0, 14, 14);
        g2.setColor(new Color(110, 110, 35));
        for (int j = 0; j < 15; j++) {
            for (int k = 0; k < 15; k++) {
                if (cell[i][j][k]) {
                    g2.fillRect(j, k, 1, 1);

                }
            }
        }

        return true;
    }

    private BufferedImage getFromDefautl(int i) {
        return im[i]; //To change body of generated methods, choose Tools | Templates.
    }

    public static class sezione {

        public sezione() {
        }

        public static void draw() {
        }
    }
}
