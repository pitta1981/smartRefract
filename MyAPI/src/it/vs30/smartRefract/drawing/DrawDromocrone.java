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
import java.awt.geom.Path2D;
import org.myorg.myapi.APIObject;
import org.myorg.myapi.FirstBrakeList;
import org.myorg.myapi.Indagine;

/**
 *
 * @author PC
 */
public class DrawDromocrone {
    public Indagine proj;
    public APIObject obj=null;
    private int indj=0;
    public FirstBrakeList ArrFB=null;
    private double margUp = 0.07;
    boolean ritorno = true;
    boolean andata = true;
    boolean m_print=true;
    
    public DrawDromocrone(Indagine prj, APIObject api){
        this.proj=prj;
        this.obj=api;
        
    }
    
    public void setProj(Indagine prj) {
        this.proj = prj;
    }

    public void drawdr_v1(Graphics g, double max, int w, int h) {
        int ymax = h;
        int xmax = w;
        ArrFB = proj.stesa.get(indj);
        int nchanel = xmax / (ArrFB.fb.length +1);
        int ychn = (ymax - (5 * nchanel)) / nchanel;
        int xshf = nchanel;
        int in0 = 0, fi0 = 0, in1 = 0, fi1 = 0;
        int yshf = (int) (margUp * ymax);
        int ystp = (int) ((ymax - ((2 * margUp) * ymax)) / max);

        for (int j = 0; j < proj.stesa.size(); j++) {





            ArrFB = proj.stesa.get(j);

//  /*
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(1));
            Path2D p = new Path2D.Double();
            //p.moveTo((0 * nchanel) + xshf, ymax - (int) (ArrFB.fb[0].time * ystp) - yshf);
            boolean first = true;
            int layer = ArrFB.fb[0].layer;
            for (int i = 0; i < ArrFB.fb.length; i++) {

                if (first) {
                    p.moveTo((i * nchanel) + xshf, ymax - (int) (ArrFB.fb[i].time * ystp) - yshf);
                    first = false;
                }
                if (layer == ArrFB.fb[i].layer) {
                    switch (layer) {
                        case 3:
                            g2.setColor(Color.RED);
                            break;
                        case 2:
                            g2.setColor(Color.CYAN);
                            break;
                        case 1:
                            g2.setColor(Color.GREEN);
                            break;

                        default:
                        //g2.setColor(Color.YELLOW);
                    }
                    p.lineTo((i * nchanel) + xshf, ymax - (int) (ArrFB.fb[i].time * ystp) - yshf);
                    g2.draw(p);
                    p = new Path2D.Double();
                    p.moveTo((i * nchanel) + xshf, ymax - (int) (ArrFB.fb[i].time * ystp) - yshf);

                } else {
                    g2.setColor(Color.GRAY);
                    p.lineTo((i * nchanel) + xshf, ymax - (int) (ArrFB.fb[i].time * ystp) - yshf);
                    layer = ArrFB.fb[i].layer;
                    g2.draw(p);
                    p = new Path2D.Double();
                    p.moveTo((i * nchanel) + xshf, ymax - (int) (ArrFB.fb[i].time * ystp) - yshf);


                }


                double is = (ArrFB.scoppio - ArrFB.spaz_in) / ArrFB.spaz;
                try {

                    if (i < ArrFB.fb.length - 2) {
                        if (ArrFB.scoppio > (ArrFB.fb[i].posx) && ArrFB.scoppio < (ArrFB.fb[i + 1].posx)) {
                            p.moveTo((i * nchanel) + xshf, ymax - (int) (ArrFB.fb[i].time * ystp) - yshf);
                            p.lineTo((is * nchanel) + xshf, ymax - (int) (0 * ystp) - yshf);
                            p.lineTo(((i + 1) * nchanel) + xshf, ymax - (int) (ArrFB.fb[i + 1].time * ystp) - yshf);

                        }
                    } else {
                    }



                } catch (Exception ex) {
                    System.err.println(ex.getMessage());
                }


            }


        }
    }

    public void drawdr(Graphics g, double max, int w, int h) {
        int ymax = h;
        int xmax = w;
        ArrFB = proj.stesa.get(indj);
        int nchanel = xmax / (ArrFB.fb.length +1);
        int ychn = (ymax - (5 * nchanel)) / nchanel;
        int xshf = nchanel;
        int in0 = 0, fi0 = 0, in1 = 0, fi1 = 0;
        int yshf = (int) (margUp * ymax);
        int ystp = (int) ((ymax - ((2 * margUp) * ymax)) / max);



        for (int j = 0; j < proj.stesa.size(); j++) {





            ArrFB = proj.stesa.get(j);

//  /*
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(1));
            Path2D p = new Path2D.Double();
            //p.moveTo((0 * nchanel) + xshf, ymax - (int) (ArrFB.fb[0].time * ystp) - yshf);
            boolean first = true;
            int layer = ArrFB.fb[0].layer;
            for (int i = 0; i < ArrFB.fb.length; i++) {
                if (ArrFB.fb[i].time > -0.1) {
                    if (ArrFB.scoppio < ((i) * ArrFB.spaz) + ArrFB.spaz_in) {
                        //System.out.println("@ G"+i+" spaz "+(((i-1)*ArrFB.spaz)+ArrFB.spaz_in)+" in "+ArrFB.spaz_in+" scop "+ArrFB.scoppio);
                        //setForeground(Color.GREEN);
                        if (andata) {
                            if (first) {
                                p.moveTo((i * nchanel) + xshf, ymax - (int) (ArrFB.fb[i].time * ystp) - yshf);
                                first = false;
                            }
                            g.setColor(Color.ORANGE);
                            if (ArrFB.fb[i].layer == 1) {
                                g2.setColor(Color.GREEN);

                                if (layer == ArrFB.fb[i].layer) {
                                    p.lineTo((i * nchanel) + xshf, ymax - (int) (ArrFB.fb[i].time * ystp) - yshf);
                                } else {
                                    p.lineTo((i * nchanel) + xshf, ymax - (int) (ArrFB.fb[i].time * ystp) - yshf);
                                    switch (layer) {
                                        case 3:
                                            g2.setColor(Color.RED);
                                            break;
                                        case 2:
                                            g2.setColor(Color.CYAN);
                                            break;
                                        case 1:
                                            g2.setColor(Color.GREEN);
                                            break;

                                        default:
                                        //g2.setColor(Color.YELLOW);
                                    }

                                    layer = ArrFB.fb[i].layer;
                                    g2.draw(p);
                                    p = new Path2D.Double();
                                    p.moveTo((i * nchanel) + xshf, ymax - (int) (ArrFB.fb[i].time * ystp) - yshf);

                                }

                            }
                            if (ArrFB.fb[i].layer == 2) {
                                g2.setColor(Color.BLUE);
                                if (layer == ArrFB.fb[i].layer) {
                                    p.lineTo((i * nchanel) + xshf, ymax - (int) (ArrFB.fb[i].time * ystp) - yshf);
                                } else {
                                    p.lineTo((i * nchanel) + xshf, ymax - (int) (ArrFB.fb[i].time * ystp) - yshf);

                                    switch (layer) {
                                        case 3:
                                            g2.setColor(Color.RED);
                                            break;
                                        case 2:
                                            g2.setColor(Color.CYAN);
                                            break;
                                        case 1:
                                            g2.setColor(Color.GREEN);
                                            break;
                                        default:
                                        //g2.setColor(Color.YELLOW);
                                    }
                                    layer = ArrFB.fb[i].layer;
                                    g2.draw(p);
                                    p = new Path2D.Double();
                                    p.moveTo((i * nchanel) + xshf, ymax - (int) (ArrFB.fb[i].time * ystp) - yshf);

                                }

                            }
                            if (ArrFB.fb[i].layer == 3) {

                                if (layer == ArrFB.fb[i].layer) {
                                    g2.setColor(Color.RED);
                                    p.lineTo((i * nchanel) + xshf, ymax - (int) (ArrFB.fb[i].time * ystp) - yshf);
                                } else {
                                    p.lineTo((i * nchanel) + xshf, ymax - (int) (ArrFB.fb[i].time * ystp) - yshf);

                                    switch (layer) {
                                        case 3:
                                            g2.setColor(Color.RED);
                                            break;
                                        case 2:
                                            g2.setColor(Color.CYAN);
                                            break;
                                        case 1:
                                            g2.setColor(Color.GREEN);
                                            break;
                                        default:
                                        //g2.setColor(Color.YELLOW);
                                    }
                                    layer = ArrFB.fb[i].layer;
                                    g2.draw(p);
                                    p = new Path2D.Double();
                                    p.moveTo((i * nchanel) + xshf, ymax - (int) (ArrFB.fb[i].time * ystp) - yshf);

                                }
                            }

                            //this.drawTriang(g, (i * nchanel) + xshf, ymax - (int) (ArrFB.fb[i].time * ystp) - yshf);
                        }

                    } else {
                        //System.out.println("@ C"+i+" spaz "+(((i-1)*ArrFB.spaz)+ArrFB.spaz_in)+" in "+ArrFB.spaz_in+" scop "+ArrFB.scoppio);
                        //setForeground(Color.CYAN);

                        if (ritorno) {

                            if (first) {
                                p.moveTo((i * nchanel) + xshf, ymax - (int) (ArrFB.fb[i].time * ystp) - yshf);
                                first = false;
                            }

                            g.setColor(Color.RED);
                            if (ArrFB.fb[i].layer == 1) {
                                g2.setColor(Color.PINK);
                                if (layer == ArrFB.fb[i].layer) {
                                    p.lineTo((i * nchanel) + xshf, ymax - (int) (ArrFB.fb[i].time * ystp) - yshf);
                                } else {
                                    p.lineTo((i * nchanel) + xshf, ymax - (int) (ArrFB.fb[i].time * ystp) - yshf);

                                    switch (layer) {
                                        case 3:
                                            g2.setColor(Color.RED);
                                            break;
                                        case 2:
                                            g2.setColor(Color.CYAN);
                                            break;
                                        case 1:
                                            g2.setColor(Color.GREEN);
                                            break;
                                        default:
                                        //g2.setColor(Color.YELLOW);
                                    }
                                    layer = ArrFB.fb[i].layer;
                                    g2.draw(p);
                                    p = new Path2D.Double();
                                    p.moveTo((i * nchanel) + xshf, ymax - (int) (ArrFB.fb[i].time * ystp) - yshf);

                                }

                            }
                            if (ArrFB.fb[i].layer == 2) {

                                if (layer == ArrFB.fb[i].layer) {
                                    g2.setColor(Color.CYAN);
                                    p.lineTo((i * nchanel) + xshf, ymax - (int) (ArrFB.fb[i].time * ystp) - yshf);
                                } else {
                                    p.lineTo((i * nchanel) + xshf, ymax - (int) (ArrFB.fb[i].time * ystp) - yshf);

                                    switch (layer) {
                                        case 3:
                                            g2.setColor(Color.RED);
                                            break;
                                        case 2:
                                            g2.setColor(Color.CYAN);
                                            break;
                                        case 1:
                                            g2.setColor(Color.GREEN);
                                            break;
                                        default:
                                        //g2.setColor(Color.YELLOW);
                                    }
                                    g2.draw(p);
                                    layer = ArrFB.fb[i].layer;
                                    p = new Path2D.Double();
                                    p.moveTo((i * nchanel) + xshf, ymax - (int) (ArrFB.fb[i].time * ystp) - yshf);

                                }

                            }
                            if (ArrFB.fb[i].layer == 3) {

                                if (layer == ArrFB.fb[i].layer) {
                                    g2.setColor(Color.RED);
                                    p.lineTo((i * nchanel) + xshf, ymax - (int) (ArrFB.fb[i].time * ystp) - yshf);
                                } else {
                                    p.lineTo((i * nchanel) + xshf, ymax - (int) (ArrFB.fb[i].time * ystp) - yshf);

                                    switch (layer) {
                                        case 3:
                                            g2.setColor(Color.RED);
                                            break;
                                        case 2:
                                            g2.setColor(Color.CYAN);
                                            break;
                                        case 1:
                                            g2.setColor(Color.GREEN);
                                            break;
                                        default:
                                        //g2.setColor(Color.YELLOW);
                                    }
                                    layer = ArrFB.fb[i].layer;
                                    g2.draw(p);
                                    p = new Path2D.Double();
                                    p.moveTo((i * nchanel) + xshf, ymax - (int) (ArrFB.fb[i].time * ystp) - yshf);

                                }

                            }


                            //this.drawCircle(g, (i * nchanel) + xshf, ymax - (int) (ArrFB.fb[i].time * ystp) - yshf);
                        }
                    }

                    double is = (ArrFB.scoppio - ArrFB.spaz_in) / ArrFB.spaz;
                    try {

                        if (i < ArrFB.fb.length - 2) {
                            if (ArrFB.scoppio > (ArrFB.fb[i].posx) && ArrFB.scoppio < (ArrFB.fb[i + 1].posx)) {
                                p.moveTo((i * nchanel) + xshf, ymax - (int) (ArrFB.fb[i].time * ystp) - yshf);
                                p.lineTo((is * nchanel) + xshf, ymax - (int) (0 * ystp) - yshf);
                                p.lineTo(((i + 1) * nchanel) + xshf, ymax - (int) (ArrFB.fb[i + 1].time * ystp) - yshf);

                            }
                        } else {
                        }



                    } catch (Exception ex) {
                        System.err.println(ex.getMessage());
                    }


                    //g2.drawLine((i * nchanel) - 5+xshf, ymax - (int) (ArrFB.fb[i].time * ystp)-yshf, (i * nchanel) + 5+xshf, ymax - (int) (ArrFB.fb[i].time * ystp)-yshf);
                    //System.out.println(" @ "+i+" "+Double.toString(ArrFB.fb[i].time*4000)+" "+Double.toString(ymax-(int)(ArrFB.fb[i].time*ystp))+" "+ymax);
                    //g.drawLine(i*20-5,(int)((ArrFB.fb[i].time*20000/8000*(xmax-xshf))+xshf), (int)((ArrFB.fb[i].time*20000/8000*(xmax-xshf))+xshf), (ychn_max/2)+(ArrFB.fb[i].chan-1)*ychn_max-20);

                }
                switch (layer) {
                    case 3:
                        g2.setColor(Color.RED);
                        break;
                    case 2:
                        g2.setColor(Color.CYAN);
                        break;
                    case 1:
                        g2.setColor(Color.GREEN);
                        break;

                    default:
                    //g2.setColor(Color.YELLOW);
                }

                g2.draw(p);

            }


//*/





            FirstBrakeList fbl = proj.stesa.get(j);

            for (int i = 0; i < fbl.fb.length; i++) {
                if (ArrFB.fb[i].time > -0.1) {
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
                /*       if (andata) {
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
                 */
            }
        }
    }

    private void drawtick(Graphics g, double max, int w, int h) {
        double ystp = 0.0;
        FirstBrakeList ArrFB = proj.stesa.get(0);
        int ymax = h;
        int xmax = w;
        int nchanel = xmax / (ArrFB.fb.length +1);
        int ychn = (int) (ymax - (0 * nchanel)) / nchanel;
        int xshf = nchanel;
        int majTic = 0, minTic = 0;
        Graphics2D g2 = (Graphics2D) g.create();

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
        if(this.m_print)g2.setColor(Color.BLACK);

        g2.setFont(new Font("Dialog", Font.PLAIN, (int) (12 * (w / 1000.0))));
        new Font("Dialog", Font.PLAIN, 12);

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


        for (int i = 0; i < ArrFB.fb.length; i++) {

            g2.drawLine((int) (xshf + (nchanel * i)), (int) ((margUp * ymax)), (int) (xshf + (nchanel * i)), (int) ((margUp * ymax) * 0.85));
            int width = fontMetrics.stringWidth("" + String.format("%.2g%n", (i * ArrFB.spaz + ArrFB.spaz_in)));

            g2.drawString("" + String.format("%.2g%n", i * ArrFB.spaz + ArrFB.spaz_in), (int) (xshf + (nchanel * i) - (width / 2)), (int) ((margUp * ymax) * 0.83));
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
    
    
    public void drawDr(Graphics g, int w, int h, boolean print) {
        //g.drawLine(24, 0, 200, 200);
        this.m_print=print;
        ArrFB = proj.stesa.get(indj);
        int ymax = h;//this.getHeight();
        int xmax = w;

        //int ymax = this.getHeight();
        //int xmax = this.getWidth();
        int nchanel = xmax / (ArrFB.fb.length +1 );
        int ychn = (int) (ymax - (0 * nchanel)) / nchanel;
        int xshf = nchanel;
        if (obj.is_white) {
           // this.setBackground(Color.white);
            g.setColor(Color.black);
        } else {
          //  this.setBackground(Color.black);

            g.setColor(Color.white);
        }
        if(print)g.setColor(Color.BLACK);

        g.drawLine((int) (xshf * 0.75), (int) ((margUp * ymax)), (int) (xshf * 0.75), (int) (ymax - (margUp * ymax)));
        g.drawLine(xmax - (int) (xshf * 0.75), (int) ((margUp * ymax)), xmax - (int) (xshf * 0.75), (int) (ymax - (margUp * ymax)));
        g.drawLine((int) (xshf * 0.75), (int) (ymax - (margUp * ymax)), xmax - (int) (xshf * 0.75), (int) (ymax - (margUp * ymax)));
        g.drawLine((int) (xshf * 0.75), (int) ((margUp * ymax)), xmax - (int) (xshf * 0.75), (int) ((margUp * ymax)));

        int yshf = (int) (margUp * ymax);

        //ymax=ymax-(int)(2*margUp*ymax);


        double max = 0.00001;
        for (int j = 0; j < proj.stesa.size(); j++) {
            FirstBrakeList fbl = proj.stesa.get(j);
            for (int i = 0; i < fbl.fb.length; i++) {
                if (max < (fbl.fb[i].time)) {
                    max = (int) (fbl.fb[i].time);
                }
            }
        }
        max = 1.05 * max;
        drawdr(g, max, w, h);
        drawdr_v1(g, max, w, h);

        drawtick(g, max, w, h);
        ArrFB = proj.stesa.get(indj);
        int ystp = (int) ((ymax - (2 * margUp * ymax)) / max);
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(2));
        Path2D p = new Path2D.Double();
        p.moveTo((0 * nchanel) + xshf, ymax - (int) (ArrFB.fb[0].time * ystp) - yshf);
        int layer = ArrFB.fb[0].layer;
        boolean first=true;
        if (!print) {
            for (int i = 0; i < ArrFB.fb.length; i++) {
                if (ArrFB.fb[i].time > -0.1) {
                
                    if (first) {
                    p.moveTo((i * nchanel) + xshf, ymax - (int) (ArrFB.fb[i].time * ystp) - yshf);
                    first = false;
                }
                if (layer == ArrFB.fb[i].layer) {
                    switch (layer) {
                        case 3:
                            g2.setColor(Color.RED);
                            break;
                        case 2:
                            g2.setColor(Color.CYAN);
                            break;
                        case 1:
                            g2.setColor(Color.GREEN);
                            break;

                        default:
                        //g2.setColor(Color.YELLOW);
                    }
                    p.lineTo((i * nchanel) + xshf, ymax - (int) (ArrFB.fb[i].time * ystp) - yshf);
                    g2.draw(p);
                    p = new Path2D.Double();
                    p.moveTo((i * nchanel) + xshf, ymax - (int) (ArrFB.fb[i].time * ystp) - yshf);

                } else {
                    g2.setColor(Color.GRAY);
                    p.lineTo((i * nchanel) + xshf, ymax - (int) (ArrFB.fb[i].time * ystp) - yshf);
                    layer = ArrFB.fb[i].layer;
                    g2.draw(p);
                    p = new Path2D.Double();
                    p.moveTo((i * nchanel) + xshf, ymax - (int) (ArrFB.fb[i].time * ystp) - yshf);


                }


                double is = (ArrFB.scoppio - ArrFB.spaz_in) / ArrFB.spaz;
                try {

                    if (i < ArrFB.fb.length - 2) {
                        if (ArrFB.scoppio > (ArrFB.fb[i].posx) && ArrFB.scoppio < (ArrFB.fb[i + 1].posx)) {
                            p.moveTo((i * nchanel) + xshf, ymax - (int) (ArrFB.fb[i].time * ystp) - yshf);
                            p.lineTo((is * nchanel) + xshf, ymax - (int) (0 * ystp) - yshf);
                            p.lineTo(((i + 1) * nchanel) + xshf, ymax - (int) (ArrFB.fb[i + 1].time * ystp) - yshf);

                        }
                    } else {
                    }



                } catch (Exception ex) {
                    System.err.println(ex.getMessage());
                }

                                        
                    
                    switch (layer) {
                        case 3:
                            g2.setColor(Color.RED);
                            break;
                        case 2:
                            g2.setColor(Color.CYAN);
                            break;
                        case 1:
                            g2.setColor(Color.GREEN);
                            break;

                        default:
                        //g2.setColor(Color.YELLOW);
                    }




                    g2.draw(p);
                    int in0 = 0, fi0 = 0;
                    g2 = (Graphics2D) g;
                    g2.setStroke(new BasicStroke(3));

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
                                g2.setColor(new Color(0.3f, 0.9f, 0.2f));
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
            }
        }
        //Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(1));




        //  System.out.println(" -- n fb : "+ ArrFB.fb.length);
    }
    
}
