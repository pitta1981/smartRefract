/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myorg.myapi;

import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
public class InputFileSU {

    public RandomAccessFile fileInput;
    public File f;
    public int number;
    public int pointer[];
    public int type = 0;
    private float somma=0;
    private float sommaabs=0;

    public InputFileSU(File file) throws java.util.zip.DataFormatException, FileNotFoundException {

        try {
            f = file;
            fileInput = new RandomAccessFile(file, "r");
            try {
                //check if really seg2
                fileInput.seek(114);
            } catch (IOException ex) {
                Logger.getLogger(InputFileSgy.class.getName()).log(Level.SEVERE, null, ex);
            }
            short size=fileInput.readShort();
            //int b1 = fileInput.readUnsignedByte();
            //int b2 = fileInput.readUnsignedByte();
            int b3 = 0;//fileInput.readByte();
            int b4 = 0;//fileInput.readByte();
            /*
             * if (((b2 << 8) + b1) != 0x3a55) { throw new
             * java.util.zip.DataFormatException();
            }
             */
            //int size =  ((b2 << 8) + b1);
            //System.out.println(" " + b1 + " " + b2 + " " + (short) ((b2 << 8) + b1));


            /*
             * fileInput.seek(3224); b1 = fileInput.readUnsignedShort();
             * System.out.println(b1); type=b1;
            fileInput.seek(3840);
             */
            System.out.println(fileInput.length() + " " + (fileInput.length()) / (240 + (size * 4)));
            /*
             * for (int i=0;i<1024;i++) { float b5=fileInput.readFloat();
             * System.out.println(b5);
            }
             */

            //find number of traces in file
            fileInput.seek(6);
            int b1 = fileInput.readUnsignedByte();
            int b2 = fileInput.readUnsignedByte();
            number = (int) (fileInput.length()) / (240 + (size * 4));
            pointer = new int[number];

            fileInput.seek(12);
            b1 = fileInput.readUnsignedByte();


            //find beginning of each trace
            fileInput.seek(32);
            for (int i = 0; i < number; i++) {
                /*
                 * b1 = fileInput.readUnsignedByte(); b2 =
                 * fileInput.readUnsignedByte(); b3 =
                 * fileInput.readUnsignedByte(); b4 = fileInput.readUnsignedByte();
                 */

                int res = 240 + (i * (240 + (size) * 4));
                System.out.println(i + " " + res);
                pointer[i] = res;
            }


        } catch (FileNotFoundException fnf) {
            System.err.println(fnf);
        } catch (EOFException eof) {
            System.out.println("End of File");
        } catch (IOException e) {
            System.out.println("IO error: " + e);
        }
    }

    Trace getTrace(int traceNum) {
        Trace p = new Trace(0);
        try {
            long beginPos = pointer[traceNum];
            long startHead=beginPos-240;
            //read size of trace header
            fileInput.seek(114);
            short size=fileInput.readShort();
            short sample=fileInput.readShort();
            fileInput.seek(114);
            int b1 = fileInput.readUnsignedByte();
            int b2 = fileInput.readUnsignedByte();
            
            int b3 = fileInput.readUnsignedByte();
            int b4 = fileInput.readUnsignedByte();
            fileInput.seek(startHead + 8);
            int sizeData = size;//(short) ((b2 << 8) + b1);

            p = new Trace(sizeData);
            p.setNum(traceNum);
            int samplei = sample;//(short) ((b4 << 8) + b3);

            p.sampleInterval = samplei / 1000000.0;
            p.length = sizeData;
            fileInput.seek(beginPos +72);
            
            p.shotLocation=fileInput.readInt();
            fileInput.seek(beginPos +36);
            p.phoneLocation=fileInput.readInt();
            int sizeHeader = (b2 << 8) + b1;
            sizeHeader = 240;
            //read size of data block
            //fileInput.seek(beginPos+8);
            // b1 = fileInput.readUnsignedByte();
            // b2 = fileInput.readUnsignedByte();

            //read header into a string contained in the Trace object
            fileInput.seek(startHead + 70);
            short multiplier=fileInput.readShort();
            p.shotLocation=fileInput.readInt();
            fileInput.seek(startHead + 80);
            
            p.phoneLocation=fileInput.readInt();
            /*
             * for(int i = 32; i < sizeHeader; i++){ b1 = fileInput.readByte();
             * char c = (char)b1; //System.out.println(c); p.sb.append(c);
            }
             */
            somma=0.0f;
            sommaabs=0.0f;
            //decode the header string
            // p.buildMetadata();
            type=5;
            //read in the data
            fileInput.seek(beginPos);
            for (int i = 0; i < sizeData - 1; i++) {
                //b1 = fileInput.readUnsignedByte();
                //b2 = fileInput.readUnsignedByte();
                //b3 = fileInput.readUnsignedByte();
                //b4 = fileInput.readUnsignedByte();
                if (type == 5) {
                    
                    int res = 0;
                    //int res = ((b3 << 16) | (b2 << 8) ) + b1;
                    //res = ((b3 << 16) | (b2 << 8) | (b4 << 24)) + b1;
                    //short res=(short)((b2 << 8)  |b1);
                    double b5 = fileInput.readFloat();//Float.intBitsToFloat(res);
                    
                    
                    //float b5 = fileInput.readFloat();
                    if (b5 > p.getMaxValue()) {
                        p.setMaxValue(b5);
                    }
                    somma=somma+(float)b5;
                    sommaabs=sommaabs+(float)Math.abs(b5);
                    p.set(i, b5);

                }

                if (type == 2) {
                    int b5 = fileInput.readInt();
                    if (b5 > p.getMaxValue()) {
                        p.setMaxValue(b5);
                    }
                    somma=somma+b5;
                    sommaabs=sommaabs+Math.abs(b5);
                    p.set(i, b5);
                    

                }
                if (type == 1) {
                    float b5 = fileInput.readFloat();
                    if (b5 > p.getMaxValue()) {
                        p.setMaxValue(b5);
                    }
                    somma=somma+b5;
                    sommaabs=sommaabs+Math.abs(b5);
                    p.set(i, b5);

                }


                //                System.out.println("b5 "+b5);
                int res = ((b3 << 16) | (b2 << 8)) + b1;
                //int res = ((b3 << 16) | (b2 << 8)  |(b4 << 24)) + b1;
                //short res=(short)((b2 << 8)  |b1);


                //double f = Float.intBitsToFloat(res);
                int f = res;
                //keep track of largest value for normalization later
                //System.out.println("#"+i+" "+b5 +" "+p.get(i));;
            }

        } catch (Exception e) {
            System.err.println(e);
        }
        // System.out.println("done reading trace");
         p.setmedia(somma,p.value.length);
         p.setMediaAbs(sommaabs,p.value.length);
        
        return p;
    }

    public TraceSet getTraceSet() {
        TraceSet s = new TraceSet(number);
        for (int i = 0; i < number; i++) {
            s.setTrace(i, getTrace(i));
        }
        return s;
    }

    public void close() {
        try {
            fileInput.close();
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}
